package com.internship.training;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamsChallenge {

    private List<Series> series;

    public void executeChallenge() {
        getEpisodesTotalCount();
        getAverageEpisodesCount();
        getMaxEpisodeCount();
        getBest10SeriesByRating();
        getAllGenres();
        getSeriesByStudioShaft();
        getMostEpisodesSeries();
        getBestStudio();
        getBestGenre();
        getWorstGenre();
        getTop5MostCommmonEpisodeCount();
        getAverageRatingOfCommedySeries();
        getMostCommonGenreWhereSugitaTomokazuActs();
        getBestActor();
        getBestShounenStudio();
    }

    public StreamsChallenge(){
        init();
    }

    private void init(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("series.json").getFile());
            List<Series> importedSeries = mapper.readValue(file,new TypeReference<List<Series>>(){});
            this.series = importedSeries.stream().filter(x->x.getRating()>0).collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getEpisodesTotalCount(){
        System.out.println("------------------------------------------------------------");
        int count = this.series.stream()
                .mapToInt(Series::getEpisodes)
                .sum();

        System.out.println(String.format("Total episodes: %d", count));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageEpisodesCount(){
        System.out.println("------------------------------------------------------------");
        double average = this.series.stream()
                .mapToInt(Series::getEpisodes)
                .average()
                .orElse(0);

        System.out.println(String.format("Average number of episodes: %f", average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMaxEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        int count = this.series.stream()
                .mapToInt(Series::getEpisodes)
                .max()
                .orElse(0);

        System.out.println(String.format("Max number of episodes: %d",count));
        System.out.println("------------------------------------------------------------");

    }

    private void getBest10SeriesByRating(){
        System.out.println("------------------------------------------------------------");
        System.out.println("These are the top 10 series:");
        this.series.stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Series::getRating)))
                .limit(10)
                .map(Series::getName)
                .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getAllGenres(){
        System.out.println("------------------------------------------------------------");
        System.out.println("These are all the genres found:");

        this.series.stream()
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .sorted()
                .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getSeriesByStudioShaft(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Series by Studio Shaft:");
        this.series.stream()
                .filter(item -> item.getStudios().contains("Shaft"))
                .map(Series::getName)
                .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getMostEpisodesSeries(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Show with most episodes:");
        this.series.stream()
                .max(Comparator.comparing(Series::getEpisodes))
                .ifPresent(item -> {
                    System.out.println("Name:\t" + item.getName());
                    System.out.println("Episodes:\t" + item.getEpisodes());
                });

        System.out.println("------------------------------------------------------------");
    }

    private void getBestStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Studio:");
        this.series.stream()
                .map(Series::getStudios)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),
                        studio -> this.series.stream()
                            .filter(item -> item.getStudios().contains(studio))
                            .mapToDouble(Series::getRating)
                            .average()
                            .orElse(0)
                ))
                .entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .ifPresent(item -> {
                    System.out.println("Name:\t" + item.getKey());
                    System.out.println("Average:\t" + item.getValue());
                });

        System.out.println("------------------------------------------------------------");
    }

    private void getBestGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best genre:");
        this.series.stream()
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),
                        genre -> this.series.stream()
                                .filter(x -> x.getGenres().contains(genre))
                                .mapToDouble(Series::getRating)
                                .average()
                                .orElse(0)
                ))
                .entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .ifPresent(item -> {
                    System.out.println("Name:\t" + item.getKey());
                    System.out.format("Average:\t %.3f\n", item.getValue());
                });

        System.out.println("------------------------------------------------------------");
    }

    private void getWorstGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Worst genre:");
        this.series.stream()
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),
                        genre -> this.series.stream()
                                .filter(item -> item.getGenres().contains(genre))
                                .mapToDouble(Series::getRating)
                                .average()
                                .orElse(0)
                ))
                .entrySet().stream()
                .min(Comparator.comparing(Map.Entry::getValue))
                .ifPresent(item -> {
                    System.out.println("Name:\t" + item.getKey());
                    System.out.format("Average:\t %.3f\n", item.getValue());
                });

        System.out.println("------------------------------------------------------------");
    }

    private void getTop5MostCommmonEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Top 5 episode count:");
        this.series.stream()
                .map(Series::getEpisodes)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Map.Entry::getValue)))
                .limit(5)
                .forEach(item -> System.out.println(item.getValue() + " shows have "+ item.getKey() + " episodes"));

        System.out.println("------------------------------------------------------------");
    }

    private void getAverageRatingOfCommedySeries(){
        System.out.println("------------------------------------------------------------");
        double average = this.series.stream()
                .filter(item -> item.getGenres().contains("Comedy"))
                .mapToDouble(Series::getRating)
                .average()
                .orElse(0);

        System.out.println(String.format("Average rating of comedy series: %.3f", average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMostCommonGenreWhereSugitaTomokazuActs(){
        System.out.println("------------------------------------------------------------");
        System.out.print("Sugita Tomokazu most common genre: \t");
        this.series.stream()
                .filter(item -> item.getMainCast().contains("Sugita,Tomokazu"))
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .ifPresent(item -> System.out.println(item.getKey()));

        System.out.println("------------------------------------------------------------");
    }

    private void getBestActor(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Actor:");
        this.series.stream()
                .map(Series::getMainCast)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),
                        mainCast -> this.series.stream()
                                .filter(item -> item.getMainCast().contains(mainCast))
                                .mapToDouble(Series::getRating)
                                .average()
                                .orElse(0)
                ))
                .entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .ifPresent(item -> {
                    System.out.println("Name:\t" + item.getKey());
                    System.out.format("Average:\t %.3f\n", item.getValue());
                });

        System.out.println("------------------------------------------------------------");

    }

    private void getBestShounenStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Shounen Studio:");
        this.series.stream()
                .filter(item -> item.getGenres().contains("Shounen"))
                .map(Series::getStudios)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),
                        studio -> this.series.stream()
                            .filter(x -> x.getStudios().contains(studio))
                            .mapToDouble(Series::getRating)
                            .average()
                            .orElse(0)
                ))
                .entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .ifPresent(item -> {
                    System.out.println("Name:\t" + item.getKey());
                    System.out.format("Average:\t %.3f\n", item.getValue());
                });

        System.out.println("------------------------------------------------------------");
    }
}
