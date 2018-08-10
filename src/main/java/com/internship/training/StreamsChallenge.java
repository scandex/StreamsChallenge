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
        getTop5MostCommonEpisodeCount();
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
        int count = series.stream().mapToInt(Series::getEpisodes).sum();
        //TODO add all episodes of all series, put the result in count
        System.out.println(String.format("Total episodes: %d", count));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageEpisodesCount() {
        System.out.println("------------------------------------------------------------");
        double average = series.stream().collect(Collectors.averagingDouble(Series::getEpisodes));
        //TODO get the average number of episodes, put the result in average
        System.out.println(String.format("Average number of episodes: %.3f", average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMaxEpisodeCount() {
        System.out.println("------------------------------------------------------------");
        int count = series.stream().map(Series::getEpisodes).reduce(0, Integer::max);
        //TODO get the maximun number of episodes, put the result in count
        System.out.println(String.format("Max number of episodes: %d", count));
        System.out.println("------------------------------------------------------------");

    }

    private void getBest10SeriesByRating() {
        System.out.println("------------------------------------------------------------");
        System.out.println("These are the top 10 series:");
        //TODO print the name of the top 10 series - one by line
        series.stream().sorted(Comparator.comparing(Series::getRating).reversed())
                .limit(10)
                .forEach(Series -> System.out.println(Series.getName()));
        System.out.println("------------------------------------------------------------");
    }

    private void getAllGenres() {
        System.out.println("------------------------------------------------------------");
        System.out.println("These are all the genres found:");
        //TODO print all the genres of the series sorted alphabetically, they can not be repeated - one by line
        series.stream().map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .sorted()
                .forEach(System.out::println);
        System.out.println("------------------------------------------------------------");
    }

    private void getSeriesByStudioShaft() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Series by Studio Shaft:");
        //TODO print the name of all Studio 'Shaft' series - one by line
        series.stream().filter(s -> s.getStudios().contains("Shaft"))
                .forEach(Series -> System.out.println(Series.getName()));
        System.out.println("------------------------------------------------------------");
    }

    private void getMostEpisodesSeries() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Show with most episodes:");
        //TODO print the name and the episode count of the show with the most episodes
        series.stream().sorted(Comparator.comparing(Series::getEpisodes).reversed())
                .limit(1)
                .forEach(Series -> System.out.println(Series.getName() + ": " + Series.getEpisodes() + " episodes"));
        System.out.println("------------------------------------------------------------");
    }

    private void getBestStudio() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Studio:");
        //TODO print the name and the average rating of the best Studio}
        series.stream().flatMap(s -> s.getStudios().stream()).distinct().collect(Collectors.toList()).stream()
                .collect(Collectors.toMap(studio -> studio, studio ->
                        series.stream().filter(s1 -> s1.getStudios().contains(studio))
                                .collect(Collectors.averagingDouble(Series::getRating)), (a, b) -> a)
                ).entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + String.format(": %.3f", map.getValue()) + " (average Rating)")
                );
        System.out.println("------------------------------------------------------------");
    }

    private void getBestGenre() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best genre:");
        //TODO print the name and the average rating of the best Genre
        series.stream().flatMap(s -> s.getGenres().stream()).distinct().collect(Collectors.toList()).stream()
                .collect(Collectors.toMap(genre -> genre, genre ->
                        series.stream().filter(s1 -> s1.getGenres().contains(genre))
                                .collect(Collectors.averagingDouble(Series::getRating)), (a, b) -> a)
                ).entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + String.format(": %.3f", map.getValue()) + " (average Rating)")
                );
        System.out.println("------------------------------------------------------------");
    }

    private void getWorstGenre() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Worst genre:");
        //TODO print the name and the average rating of the worst Genre
        series.stream().flatMap(s -> s.getGenres().stream()).distinct().collect(Collectors.toList()).stream()
                .collect(Collectors.toMap(genre -> genre, genre ->
                        series.stream().filter(s1 -> s1.getGenres().contains(genre))
                                .collect(Collectors.averagingDouble(Series::getRating)), (a, b) -> a)
                ).entrySet().stream().min(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + String.format(": %.3f", map.getValue()) + " (average Rating)")
                );
        System.out.println("------------------------------------------------------------");
    }

    private void getTop5MostCommonEpisodeCount() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Top 5 episode count:");
        //TODO print the count and value of the top 5 most common episode count  - example:  100 shows have 25 episodes
        series.stream().map(Series::getEpisodes).distinct().collect(Collectors.toList()).stream()
                .collect(Collectors.toMap(episode -> episode, episode ->
                        series.stream().filter(s1 -> s1.getEpisodes() == episode)
                                .count(), (a, b) -> a)
                ).entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(5)
                .forEach(map -> System.out.println(map.getValue() + " shows have " + map.getKey() + " episodes"));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageRatingOfCommedySeries() {
        System.out.println("------------------------------------------------------------");
        double average = series.stream().filter(series1 -> series1.getGenres().contains("Comedy"))
                .collect(Collectors.averagingDouble(Series::getRating));
        //TODO get the average rating of the 'Comedy' shows, put the result in average
        System.out.println(String.format("Average rating of comedy series: %.3f", average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMostCommonGenreWhereSugitaTomokazuActs() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Sugita Tomokazu most common genre:");
        //TODO print the most common genre where 'Sugita,Tomokazu' acts - the most common genre of the shows where 'Sugita,Tomokazu' is in mainCast
        series.stream().filter(s1->s1.getMainCast().contains("Sugita,Tomokazu")).flatMap(s1->s1.getGenres().stream()).distinct().collect(Collectors.toList()).stream()
                .collect(Collectors.toMap(genre -> genre, genre->
                        series.stream().filter(s2->s2.getGenres().contains(genre))
                                .count(), (a, b) -> a)
                ).entrySet().stream().max(Comparator.comparingLong(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + ": " + map.getValue() + " performances"));
        System.out.println("------------------------------------------------------------");
    }

    private void getBestActor() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Actor:");
        //TODO print the name and the average rating of the best Actor
        series.stream().flatMap(s -> s.getMainCast().stream()).distinct().collect(Collectors.toList()).stream()
                .collect(Collectors.toMap(actor -> actor, actor ->
                        series.stream().filter(s1 -> s1.getMainCast().contains(actor))
                                .collect(Collectors.averagingDouble(Series::getRating)), (a, b) -> a)
                ).entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + String.format(": %.3f", map.getValue()) + " (average Rating)")
                );
        System.out.println("------------------------------------------------------------");

    }

    private void getBestShounenStudio() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Shounen Studio:");
        //TODO print the name and the average rating (of the shounen series) of the best Shounen Studio - the studio with the best 'Shounen' (genre) series
        series.stream().filter(s1->s1.getGenres().contains("Shounen")).flatMap(s1->s1.getStudios().stream()).distinct().collect(Collectors.toList()).stream()
                .collect(Collectors.toMap(studio -> studio, studio->
                        series.stream().filter(s2->s2.getStudios().contains(studio))
                                .collect(Collectors.averagingDouble(Series::getRating)), (a, b) -> a)
                ).entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + String.format(": %.3f", map.getValue()) + " (average Rating)"));
        System.out.println("------------------------------------------------------------");

    }


}
