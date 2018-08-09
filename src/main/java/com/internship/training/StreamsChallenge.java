package com.internship.training;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StreamsChallenge {

    private List<Series> series;

    public StreamsChallenge() {
        init();
    }

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

    private void init() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("series.json").getFile());
            List<Series> importedSeries = mapper.readValue(file, new TypeReference<List<Series>>() {
            });
            this.series = importedSeries.stream().filter(x -> x.getRating() > 0).collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getEpisodesTotalCount() {
        System.out.println("------------------------------------------------------------");
        //add all episodes of all series, put the result in count
        int count = series.stream()
                .mapToInt(Series::getEpisodes)
                .reduce(0, (x, y) -> x + y);

        System.out.println(String.format("Total episodes: %d", count));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageEpisodesCount() {
        System.out.println("------------------------------------------------------------");
        //get the average number of episodes, put the result in average

        double average = series.stream()
                .mapToInt(Series::getEpisodes)
                .average().orElse(-1);

        System.out.println(String.format("Average number of episodes: %f", average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMaxEpisodeCount() {
        System.out.println("------------------------------------------------------------");

        //get the maximun number of episodes, put the result in count

        int count = series.stream()
                .mapToInt(Series::getEpisodes)
                .max().orElse(-1);

        System.out.println(String.format("Max number of episodes: %d", count));
        System.out.println("------------------------------------------------------------");

    }

    private void getBest10SeriesByRating() {
        System.out.println("------------------------------------------------------------");
        System.out.println("These are the top 10 series:");

        //print the name of the top 10 series - one by line

        series.stream()
                .sorted(Comparator.comparing(Series::getRating).reversed())
                .limit(10)
                .forEach(p -> System.out.println(p.getName()));

        System.out.println("------------------------------------------------------------");
    }

    private void getAllGenres() {
        System.out.println("------------------------------------------------------------");
        System.out.println("These are all the genres found:");

        //print all the genres of the series sorted alphabetically, they can not be repeated - one by line

        series.stream()
                .flatMap(c -> c.getGenres().stream())
                .collect(Collectors.toList())
                .stream()
                .distinct()
                .sorted()
                .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getSeriesByStudioShaft() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Series by Studio Shaft:");

        //print the name of all Studio 'Shaft' series - one by line

        series.stream()
                .filter(i -> i.getStudios().contains("Shaft"))
                .forEach(i -> System.out.println(i.getName()));

        System.out.println("------------------------------------------------------------");
    }

    private void getMostEpisodesSeries() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Show with most episodes:");

        //print the name and the episode count of the show with the most episodes

        series.stream()
                .sorted(Comparator.comparing(Series::getEpisodes).reversed())
                .limit(1)
                .forEach(i -> System.out.println(i.getName() + " having " + i.getEpisodes() + " episodes"));

        System.out.println("------------------------------------------------------------");
    }

    private void getBestStudio() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Studio:");

        //print the name and the average rating of the best Studio

        Map<String, Double> map = new HashMap<>();
        series.stream().map(Series::getStudios)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(i ->
                        map.put(i, series.stream()
                                .filter(s -> s.getStudios().contains(i))
                                .mapToDouble(Series::getRating).average().orElse(-1))
                );
        map.entrySet()
                .stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getBestGenre() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best genre:");

        //print the name and the average rating of the best Genre

        Map<String, Double> map = new HashMap<>();
        series.stream().map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(i ->
                        map.put(i, series.stream().filter(s -> s.getGenres().contains(i))
                                .mapToDouble(Series::getRating).average().orElse(-1))
                );
        map.entrySet()
                .stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getWorstGenre() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Worst genre:");
        //print the name and the average rating of the worst Genre

        Map<String, Double> map = new HashMap<>();
        series.stream().map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(i -> map.put(i, series.stream().filter(s -> s.getGenres().contains(i))
                        .mapToDouble(Series::getRating).average().orElse(-1))
                );
        map.entrySet()
                .stream()
                .min(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getTop5MostCommmonEpisodeCount() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Top 5 episode count:");

        //prints the count and value of the of the top 5 most common episode count  - example:  100 shows have 25 episodes

        series.stream()
                .collect(Collectors.groupingBy(Series::getEpisodes, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(a -> System.out.println(String.format("%d shows have %d episodes", a.getValue(), a.getKey())));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageRatingOfCommedySeries() {
        System.out.println("------------------------------------------------------------");

        //get the average rating of the 'Comedy' shows, put the result in average

        double average = series.stream()
                .filter(p -> p.getGenres().contains("Comedy"))
                .mapToDouble(Series::getRating)
                .average().orElse(-1);
        System.out.println("Average rating of comedy series: " + average);
        System.out.println("------------------------------------------------------------");
    }

    private void getMostCommonGenreWhereSugitaTomokazuActs() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Sugita Tomokazu most common genre:");

        //print the most common genre where 'Sugita,Tomokazu' acts - the most common genre of the shows where 'Sugita,Tomokazu' is in mainCast

        Map<String, Integer> map = new HashMap<>();
        series.stream()
                .filter(p -> p.getMainCast().contains("Sugita,Tomokazu"))
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(i ->
                        map.put(i, (int) series.stream()
                                .filter(s -> s.getGenres().contains(i))
                                .count()));

        map.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .ifPresent(System.out::println);
        System.out.println("------------------------------------------------------------");
    }

    private void getBestActor() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Actor:");
        //print the name and the average rating of the best Actor

        Map<String, Double> map = new HashMap<>();
        series.stream()
                .map(Series::getMainCast)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(i ->
                        map.put(i, series.stream().filter(s -> s.getMainCast().contains(i))
                                .mapToDouble(Series::getRating).average().orElse(-1)));

        map.entrySet()
                .stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(System.out::println);

        System.out.println("------------------------------------------------------------");

    }

    private void getBestShounenStudio() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Shounen Studio:");
        //print the name and the average rating (of the shounen series) of the best Shounen Studio - the studio with the best 'Shounen' (genre) series

        Map<String, Double> map = new HashMap<>();
        series.stream()
                .filter(i -> i.getGenres().contains("Shounen"))
                .map(Series::getStudios)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(i ->
                        map.put(i, series.stream().filter(s -> s.getStudios().contains(i))
                                .mapToDouble(Series::getRating).average().orElse(-1)));

        map.entrySet()
                .stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(System.out::println);

        System.out.println("------------------------------------------------------------");
    }
}
