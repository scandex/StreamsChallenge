package com.internship.training;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
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

    public StreamsChallenge() {
        init();
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

    private void getEpisodesTotalCount() {
        System.out.println("------------------------------------------------------------");
        int count = 0;
        count = series.stream().mapToInt(Series::getEpisodes).sum();

        System.out.println(String.format("Total episodes: %d", count));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageEpisodesCount() {
        System.out.println("------------------------------------------------------------");
        double average = 0;
        average = series.stream().mapToInt(Series::getEpisodes).average().getAsDouble();

        System.out.println(String.format("Average number of episodes: %f", average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMaxEpisodeCount() {
        System.out.println("------------------------------------------------------------");
        int count = 0;
        count = series.stream().mapToInt(Series::getEpisodes).max().getAsInt();

        System.out.println(String.format("Max number of episodes: %d", count));
        System.out.println("------------------------------------------------------------");

    }

    private void getBest10SeriesByRating() {
        System.out.println("------------------------------------------------------------");
        System.out.println("These are the top 10 series:");
        series.stream().sorted(Comparator.comparing(Series::getRating).reversed()).limit(10).forEach(best -> System.out.println(best.getName()));
        System.out.println("------------------------------------------------------------");
    }

    private void getAllGenres() {
        System.out.println("------------------------------------------------------------");
        System.out.println("These are all the genres found:");
        series.stream().map(Series::getGenres)
                .flatMap(Collection::stream).sorted().distinct().forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getSeriesByStudioShaft() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Series by Studio Shaft:");
        series.stream().filter(s -> s.getStudios().contains("Shaft")).forEach(s -> System.out.println(s.getName()));

        System.out.println("------------------------------------------------------------");
    }

    private void getMostEpisodesSeries() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Show with most episodes:");
        series.stream().sorted(Comparator.comparing(Series::getEpisodes).reversed()).limit(1)
                .forEach(best -> System.out.println(best.getName() + " with " + best.getEpisodes() + " episodes"));
        System.out.println("------------------------------------------------------------");
    }

    private void getBestStudio() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Studio:");
        Map<String, Double> stu = new HashMap<>();
        series.stream().flatMap(series1 -> series1.getStudios().stream()).distinct()
                .forEach(studio -> stu.put(studio, series.stream()
                        .filter(series2 -> series2.getStudios().contains(studio))
                        .collect(Collectors.averagingDouble(Series::getRating))
                ));
        stu.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + String.format(" with an average rating of: %.2f", map.getValue())));

        System.out.println("------------------------------------------------------------");
    }

    private void getBestGenre() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best genre:");
        Map<String, Double> gen = new HashMap<>();
        series.stream().flatMap(series1 -> series1.getGenres().stream()).distinct()
                .forEach(genre -> gen.put(genre, series.stream()
                        .filter(series2 -> series2.getGenres().contains(genre))
                        .collect(Collectors.averagingDouble(Series::getRating))
                ));
        gen.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + String.format(" with an average rating of: %.2f", map.getValue())));
        System.out.println("------------------------------------------------------------");
    }

    private void getWorstGenre() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Worst genre:");
        Map<String, Double> gen = new HashMap<>();
        series.stream().flatMap(series1 -> series1.getGenres().stream()).distinct()
                .forEach(genre -> gen.put(genre, series.stream()
                        .filter(series2 -> series2.getGenres().contains(genre))
                        .collect(Collectors.averagingDouble(Series::getRating))
                ));
        gen.entrySet().stream().min(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + String.format(" with an average rating of: %.2f", map.getValue())));
        System.out.println("------------------------------------------------------------");
    }

    private void getTop5MostCommmonEpisodeCount() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Top 5 episode count:");
        Map<Integer, Long> ep = new HashMap<>();
        series.stream().map(Series::getEpisodes).distinct().forEach(epis -> ep.put(epis, series.stream().filter(series1 -> series1.getEpisodes() == epis).count()));
        ep.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(5)
                .forEach(map -> System.out.println(map.getKey() + String.format(" episodes with a count of: " + map.getValue())));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageRatingOfCommedySeries() {
        System.out.println("------------------------------------------------------------");
        double avgCommedy = series.stream().filter(series2 -> series2.getGenres().contains("Comedy"))
                        .collect(Collectors.averagingDouble(Series::getRating));
        System.out.println(String.format("The commedy genre have an average rating of: %.2f", avgCommedy));
        System.out.println("------------------------------------------------------------");
    }

    private void getMostCommonGenreWhereSugitaTomokazuActs() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Sugita Tomokazu most common genre:");
        Map<String, Long> gen = new HashMap<>();
        series.stream().filter(ser -> ser.getMainCast().contains("Sugita,Tomokazu")).flatMap(series1 -> series1.getGenres().stream()).distinct()
                .forEach(genre -> gen.put(genre, series.stream()
                        .filter(series2 -> series2.getGenres().contains(genre)).count()
                ));
        gen.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + " with a count of: " + map.getValue()+ " appears"));
        System.out.println("------------------------------------------------------------");
    }

    private void getBestActor() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Actor:");
        Map<String, Double> cas = new HashMap<>();
        series.stream().flatMap(series1 -> series1.getMainCast().stream()).distinct()
                .forEach(actor -> cas.put(actor, series.stream()
                        .filter(series2 -> series2.getMainCast().contains(actor))
                        .collect(Collectors.averagingDouble(Series::getRating))
                ));
        cas.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + String.format(" with an average rating of: %.2f", map.getValue())));
        System.out.println("------------------------------------------------------------");

    }

    private void getBestShounenStudio() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Shounen Studio:");
        Map<String, Double> stu = new HashMap<>();
        series.stream().filter(s -> s.getGenres().contains("Shounen")).map(Series::getStudios)
                .flatMap(Collection::stream).forEach(st->stu.put(st, series.stream()
                .filter(series1 -> series1.getStudios().contains(st)).collect(Collectors.averagingDouble(Series::getRating))));
        stu.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + String.format(" with an average rating of: %.2f", map.getValue())));
        System.out.println("------------------------------------------------------------");

    }
}
