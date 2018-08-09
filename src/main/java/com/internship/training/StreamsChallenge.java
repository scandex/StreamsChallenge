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
        //adding all episodes of all series, put the result in count
        int count = 0;
        count = series.stream()
                .map(Series::getEpisodes)
                .reduce((a, b) -> a + b)
                .get();
        System.out.println(String.format("Total episodes: %d", count));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageEpisodesCount() {
        System.out.println("------------------------------------------------------------");
        //getting the average number of episodes, put the result in average
        OptionalDouble average = series.stream()
                .mapToDouble(Series::getEpisodes).average();
        if (average.isPresent()) {
            System.out.println(String.format("Average number of episodes: %f", average.getAsDouble()));
        } else {
            System.out.println("The stream was empty");
        }
        System.out.println("------------------------------------------------------------");
    }

    private void getMaxEpisodeCount() {
        System.out.println("------------------------------------------------------------");
        //getting the maximum number of episodes, put the result in count
        OptionalInt count = series.stream()
                .mapToInt(Series::getEpisodes).max();
        if (count.isPresent()) {
            System.out.println(String.format("Max number of episodes: %d", count.getAsInt()));
        } else {
            System.out.println("The stream was empty");
        }
        System.out.println("------------------------------------------------------------");

    }

    private void getBest10SeriesByRating() {
        System.out.println("------------------------------------------------------------");
        System.out.println("These are the top 10 series:");
        //printing the name of the top 10 series - one by line
        series.stream()
                .sorted(Comparator.comparing(Series::getRating).reversed())
                .limit(10)
                .map(Series::getName)
                .forEach(System.out::println);
        System.out.println("------------------------------------------------------------");
    }

    private void getAllGenres() {
        System.out.println("------------------------------------------------------------");
        System.out.println("These are all the genres found:");
        //printing all the genres of the series sorted alphabetically, they can not be repeated - one by line
        series.stream().map(Series::getGenres)
                .collect(ArrayList::new, List::addAll, List::addAll)
                .stream()
                .sorted()
                .distinct()
                .forEach(System.out::println);
        System.out.println("------------------------------------------------------------");
    }

    private void getSeriesByStudioShaft() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Series by Studio Shaft:");
        //printing the name of all Studio 'Shaft' series - one by line
        series.stream().filter(x -> x.getStudios().contains("Shaft"))
                .map(Series::getName)
                .forEach(System.out::println);
        System.out.println("------------------------------------------------------------");
    }

    private void getMostEpisodesSeries() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Show with most episodes:");
        //printing the name and the episode count of the show with the most episodes
        series.stream()
                .sorted(Comparator.comparing(Series::getEpisodes).reversed())
                .limit(1)
                .map(x -> x.getName() + " No. episodes: " + x.getEpisodes())
                .forEach(s -> System.out.println(s));

        System.out.println("------------------------------------------------------------");
        System.out.println("------------------------------------------------------------");
        System.out.println("------------------------------------------------------------");
    }

    private void getBestStudio() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Studio:");
        //printing the name and the average rating of the best Studio
        Map<String, Double> map = new HashMap<>();
        series.stream().map(x -> x.getStudios())
                .flatMap(x -> x.stream())
                .sorted()
                .distinct()
                .forEach(i -> map.put(i, series.stream().filter(s -> s.getStudios().contains(i))
                        .mapToDouble(Series::getRating).average().getAsDouble()));
        map.entrySet()
                .stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(System.out::println);

        System.out.println("------------------------------------------------------------");

    }

    private void getBestGenre() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best genre:");
        //printing the name and the average rating of the best Genre
        Map<String, Double> map = new HashMap<>();
        series.stream().map(x -> x.getGenres())
                .flatMap(x -> x.stream())
                .sorted()
                .distinct()
                .forEach(i -> map.put(i, series.stream().filter(s -> s.getGenres().contains(i))
                        .mapToDouble(Series::getRating).average().getAsDouble()));
        map.entrySet()
                .stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getWorstGenre() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Worst genre:");
        //printing the name and the average rating of the worst Genre
        Map<String, Double> map = new HashMap<>();
        series.stream().map(x -> x.getGenres())
                .flatMap(x -> x.stream())
                .sorted()
                .distinct()
                .forEach(i -> map.put(i, series.stream().filter(s -> s.getGenres().contains(i))
                        .mapToDouble(Series::getRating).average().getAsDouble()));
        map.entrySet()
                .stream()
                .min(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getTop5MostCommmonEpisodeCount() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Top 5 episode count:");
        //printing the count and value of the of the top 5 most common episode count  - example:  100 shows have 25 episodes
        Map<Integer, Long> map = new HashMap<>();

        series.stream().
                collect(Collectors.groupingBy(Series::getEpisodes, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(integerLongEntry ->
                        System.out.println(integerLongEntry.getValue() + " shows have "
                                + integerLongEntry.getKey() + " episodes"));

        System.out.println("------------------------------------------------------------");
    }

    private void getAverageRatingOfCommedySeries() {
        System.out.println("------------------------------------------------------------");
        //getting the average rating of the 'Comedy' shows, put the result in average
        double average = 0;
        average = series.stream()
                .filter(s -> s.getGenres().contains("Comedy"))
                .mapToDouble(Series::getRating).average().getAsDouble();

        System.out.println(String.format("Average rating of comedy series: %f", average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMostCommonGenreWhereSugitaTomokazuActs() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Sugita Tomokazu most common genre:");
        //printing the most common genre where 'Sugita,Tomokazu' acts -
        // the most common genre of the shows where 'Sugita,Tomokazu' is in mainCast
        Map<String, Long> map = new HashMap<>();
        series.stream()
                .filter(x -> x.getMainCast().contains("Sugita,Tomokazu"))
                .map(x -> x.getGenres())
                .flatMap(x -> x.stream())
                .distinct()
                .forEach(i -> map.put(i, series.stream().filter(s -> s.getGenres().contains(i)).count()));

        map.entrySet()
                .stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .ifPresent(x->System.out.println(x.getKey()));

        System.out.println("------------------------------------------------------------");
    }

    private void getBestActor() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Actor:");
        //printing the name and the average rating of the best Actor
        Map<String, Double> map = new HashMap<>();
        series.stream().map(x -> x.getMainCast())
                .flatMap(x -> x.stream())
                .sorted()
                .distinct()
                .forEach(i -> map.put(i, series.stream().filter(s -> s.getMainCast().contains(i))
                        .mapToDouble(Series::getRating).average().getAsDouble()));
        map.entrySet()
                .stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(System.out::println);

        System.out.println("------------------------------------------------------------");

    }

    private void getBestShounenStudio() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Shounen Studio:");
        //printing the name and the average rating (of the shounen series) of the best Shounen Studio -
        //the studio with the best 'Shounen' (genre) series
        Map<String, Double> map = new HashMap<>();
        series.stream()
                .filter(x -> x.getGenres().contains("Shounen"))
                .map(x -> x.getStudios())
                .flatMap(x -> x.stream())
                .sorted()
                .distinct()
                .forEach(i -> map.put(i, series.stream().filter(s -> s.getStudios().contains(i))
                        .mapToDouble(Series::getRating).average().getAsDouble()));
        map.entrySet()
                .stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(System.out::println);

        System.out.println("------------------------------------------------------------");

    }


}
