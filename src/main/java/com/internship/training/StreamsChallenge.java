package com.internship.training;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        count += series.stream().mapToInt(Series::getEpisodes).sum();


        System.out.println(String.format("Total episodes: %d", count));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageEpisodesCount() {
        System.out.println("------------------------------------------------------------");
        double average = 0;
        average = series.stream().mapToDouble(Series::getEpisodes).average().getAsDouble();
        System.out.println(String.format("Average number of episodes: %f", average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMaxEpisodeCount() {
        System.out.println("------------------------------------------------------------");
        int count = 0;
        //TODO get the maximun number of episodes, put the result in count
        count = series.stream().mapToInt(Series::getEpisodes).max().getAsInt();
        System.out.println(String.format("Max number of episodes: %d", count));
        System.out.println("------------------------------------------------------------");

    }

    private void getBest10SeriesByRating() {
        System.out.println("------------------------------------------------------------");
        System.out.println("These are the top 10 series:");
        series.stream().sorted(Comparator.comparing(Series::getRating).reversed()).limit(10)
                .map(Series::getName).forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getAllGenres() {
        System.out.println("------------------------------------------------------------");
        System.out.println("These are all the genres found:");
        //TODO print all the genres of the series sorted alphabetically, they can not be repeated - one by line
        series.stream().map(Series::getGenres)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet())
                .forEach(System.out::println);
        System.out.println("------------------------------------------------------------");
    }

    private void getSeriesByStudioShaft() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Series by Studio Shaft:");
        //TODO print the name of all Studio 'Shaft' series - one by line
        series.stream()
                .filter((series1) -> series1.getStudios().contains("Shaft"))
                .forEach(series2 -> System.out.println(series2.getName()));
        System.out.println("------------------------------------------------------------");
    }

    private void getMostEpisodesSeries() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Show with most episodes:");
        //TODO print the name and the episode count of the show with the most episodes
        series.stream()
                .filter((series1) -> series1.getEpisodes() == series.stream().mapToInt(Series::getEpisodes).max().getAsInt())
                .forEach(series2 -> System.out.println(series2.getName() + ", " + series2.getEpisodes()));
        System.out.println("------------------------------------------------------------");
    }

    private void getBestStudio() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Studio:");
        //TODO print the name and the average rating of the best Studio
        Map<String, Double> studios = new HashMap<>();
        series.stream()
                .map(Series::getStudios)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(studio -> series.stream()
                        .filter(series1 -> series1.getStudios().contains(studio))
                        .mapToDouble(Series::getRating)
                        .average().ifPresent(value -> studios.put(studio, value)));
        studios.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(1)
                .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getBestGenre() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best genre:");
        //TODO print the name and the average rating of the best Genre
        Map<String, Double> genres = new HashMap<>();
        series.stream()
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(genre -> series.stream()
                        .filter(series1 -> series1.getGenres().contains(genre))
                        .mapToDouble(Series::getRating)
                        .average().ifPresent(value -> genres.put(genre, value)));
        genres.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(1)
                .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getWorstGenre() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Worst genre:");
        //TODO print the name and the average rating of the worst Genre
        Map<String, Double> genres = new HashMap<>();
        series.stream()
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(genre -> series.stream()
                        .filter(series1 -> series1.getGenres().contains(genre))
                        .mapToDouble(Series::getRating)
                        .average().ifPresent(v -> genres.put(genre, v)));
        genres.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(1)
                .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getTop5MostCommmonEpisodeCount() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Top 5 episode count:");
        //TODO print the count and value of the of the top 5 most common episode count  - example:  100 shows have 25 episodes
        Map<Integer, Integer> episodes = new HashMap<>();
        series.stream()
                .map(Series::getEpisodes)
                .distinct()
                .forEach(episode -> episodes.put(episode, (int) series.stream()
                        .filter(series1 -> series1.getEpisodes() == episode)
                        .count()));
        episodes.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(5)
                .forEach(entry-> System.out.println(entry.getValue()+" shows have "+entry.getKey()+" episodes"));

        System.out.println("------------------------------------------------------------");
    }

    private void getAverageRatingOfCommedySeries() {
        System.out.println("------------------------------------------------------------");
        double average = 0;
        //TODO get the average rating of the 'Comedy' shows, put the result in average
        average=series.stream()
                .filter(series1->series1.getGenres().contains("Comedy"))
                .mapToDouble(Series::getRating)
                .average().getAsDouble();

        System.out.println(String.format("Average rating of comedy series: %f", average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMostCommonGenreWhereSugitaTomokazuActs() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Sugita Tomokazu most common genre:");
        //TODO print the most common genre where 'Sugita,Tomokazu' acts - the most common genre of the shows where 'Sugita,Tomokazu' is in mainCast
        Map<String, Integer> genres = new HashMap<>();
        series.stream()
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(genre->genres.put(genre,(int)series.stream().filter(s->s.getMainCast().contains("Sugita,Tomokazu")).filter(series1->series1.getGenres().contains(genre)).count()));

        genres.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(1)
                .forEach(entry-> System.out.println(entry.getKey()+" with "+entry.getValue()+" series"));

        System.out.println("------------------------------------------------------------");
    }

    private void getBestActor() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Actor:");
        //TODO print the name and the average rating of the best Actor
        Map<String, Double> actors = new HashMap<>();
        series.stream()
                .map(Series::getMainCast)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(actor -> series.stream()
                        .filter(series1 -> series1.getMainCast().contains(actor))
                        .mapToDouble(Series::getRating)
                        .average().ifPresent(value -> actors.put(actor, value)));
        actors.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(1)
                .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");

    }

    private void getBestShounenStudio() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Shounen Studio:");
        //TODO print the name and the average rating (of the shounen series) of the best Shounen Studio - the studio with the best 'Shounen' (genre) series
        Map<String, Double> studios = new HashMap<>();
        series.stream()
                .filter(series1 -> series1.getGenres().contains("Shounen"))
                .map(Series::getStudios)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(studio -> series.stream()
                        .filter(series1 -> series1.getGenres().contains("Shounen"))
                        .filter(series1 -> series1.getStudios().contains(studio))
                        .mapToDouble(Series::getRating)
                        .average().ifPresent(value -> studios.put(studio, value)));
        studios.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(1)
                .forEach(System.out::println);
        System.out.println("------------------------------------------------------------");

    }


}
