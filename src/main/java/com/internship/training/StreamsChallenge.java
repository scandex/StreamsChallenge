package com.internship.training;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.*;

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
        int count;
        //TODO add all episodes of all series, put the result in count

        count = series.stream()
                .mapToInt(Series::getEpisodes)
                .sum();

        System.out.println(String.format("Total episodes: %d", count));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageEpisodesCount() {
        System.out.println("------------------------------------------------------------");
        double average;
        //TODO get the average number of episodes, put the result in average

        average = series.stream()
                .mapToInt(Series::getEpisodes)
                .average()
                .getAsDouble();

        System.out.println(String.format("Average number of episodes: %f", average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMaxEpisodeCount() {
        System.out.println("------------------------------------------------------------");
        int count = 0;
        //TODO get the maximun number of episodes, put the result in count
        count = series.stream()
                .mapToInt(Series::getEpisodes)
                .max()
                .getAsInt();

        System.out.println(String.format("Max number of episodes: %d", count));
        System.out.println("------------------------------------------------------------");

    }

    private void getBest10SeriesByRating() {
        System.out.println("------------------------------------------------------------");
        System.out.println("These are the top 10 series:");
        //TODO print the name of the top 10 series - one by line

        series.stream()
                .sorted(comparing(Series::getRating).reversed())
                .limit(10)
                .map(Series::getName)
                .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getAllGenres() {
        System.out.println("------------------------------------------------------------");
        System.out.println("These are all the genres found:");
        //TODO print all the genres of the series sorted alphabetically, they can not be repeated - one by line

        series.stream()
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .sorted(String::compareToIgnoreCase)
                .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getSeriesByStudioShaft() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Series by Studio Shaft:");
        //TODO print the name of all Studio 'Shaft' series - one by line

        series.stream()
                .filter(currentSeries -> currentSeries.getStudios().contains("Shaft"))
                .map(Series::getName)
                .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getMostEpisodesSeries() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Show with most episodes:");
        //TODO print the name and the episode count of the show with the most episodes
        Series mostWatchedSeries = series.stream()
                .sorted(comparing(Series::getEpisodes).reversed())
                .findFirst().get();

        System.out.println(mostWatchedSeries.getName());
        System.out.println("Number of episodes:");
        System.out.println(mostWatchedSeries.getEpisodes());

        System.out.println("------------------------------------------------------------");
    }

    private void getBestStudio() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Studio:");
        //TODO print the name and the average rating of the best Studio


        Map.Entry<String, Double> bestStudio = series.stream()
                .map(currentSeries -> {
                            List<Pair<String, Double>> thisList = new ArrayList<>();
                            for (String studio : currentSeries.getStudios()) {
                                Pair<String, Double> pair = new Pair<>(studio, currentSeries.getRating());
                                thisList.add(pair);
                            }
                            return thisList;
                        }
                ).flatMap(Collection::stream)
                .collect(Collectors.groupingBy((Pair::getKey), Collectors.averagingDouble(Pair::getValue)))
                .entrySet().stream().max(Map.Entry.comparingByValue()).get();

        System.out.println(bestStudio.getKey());
        System.out.println("Average rating:" + bestStudio.getValue());
        System.out.println("------------------------------------------------------------");
    }

    private void getBestGenre() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best genre:");
        //TODO print the name and the average rating of the best Genre

        Map.Entry<String, Double> bestGenre = series.stream()
                .map(currentSeries -> {
                    List<Pair<String, Double>> listGenresOfThisSeries = new ArrayList<>();
                    for (String genre : currentSeries.getGenres()) {
                        Pair<String, Double> pairGenreRating = new Pair(genre, currentSeries.getRating());
                        listGenresOfThisSeries.add(pairGenreRating);
                    }
                    return listGenresOfThisSeries;
                }).flatMap(Collection::stream)
                .collect(Collectors.groupingBy((Pair::getKey), Collectors.averagingDouble(Pair::getValue)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue()).get();


        System.out.println(bestGenre.getKey());
        System.out.println("Average rating:" + bestGenre.getValue());

        System.out.println("------------------------------------------------------------");
    }

    private void getWorstGenre() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Worst genre:");
        //TODO print the name and the average rating of the worst Genre

        Map.Entry<String, Double> worstGenre = series.stream()
                .map(currentSeries -> {
                    List<Pair<String, Double>> listGenresOfThisSeries = new ArrayList<>();
                    for (String genre : currentSeries.getGenres()) {
                        Pair<String, Double> pairGenreRating = new Pair(genre, currentSeries.getRating());
                        listGenresOfThisSeries.add(pairGenreRating);
                    }
                    return listGenresOfThisSeries;
                }).flatMap(Collection::stream)
                .collect(Collectors.groupingBy((Pair::getKey), Collectors.averagingDouble(Pair::getValue)))
                .entrySet().stream()
                .min(Map.Entry.comparingByValue()).get();


        System.out.println(worstGenre.getKey());
        System.out.println("Average rating:" + worstGenre.getValue());
        System.out.println("------------------------------------------------------------");
    }

    private void getTop5MostCommmonEpisodeCount() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Top 5 episode count:");
        //TODO print the count and value of the of the top 5 most common episode count  - example:  100 shows have 25 episodes

        series.stream()
                .map(Series::getEpisodes)
                .collect(Collectors.groupingBy(currentSeries -> currentSeries, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(commonEpisodeCount -> System.out.println(commonEpisodeCount.getValue() + " shows have " + commonEpisodeCount.getKey() + " episodes"));

        System.out.println("------------------------------------------------------------");
    }

    private void getAverageRatingOfCommedySeries() {
        System.out.println("------------------------------------------------------------");
        double average = 0;
        //TODO get the average rating of the 'Comedy' shows, put the result in average
        average = series.stream()
                .filter(currentSeries -> currentSeries.getGenres().contains("Comedy"))
                .collect(Collectors.averagingDouble(currentComedySeries -> currentComedySeries.getRating()));

        System.out.println(String.format("Average rating of comedy series: %f", average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMostCommonGenreWhereSugitaTomokazuActs() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Sugita Tomokazu most common genre:");
        //TODO print the most common genre where 'Sugita,Tomokazu' acts - the most common genre of the shows where 'Sugita,Tomokazu' is in mainCast
        String mostCommonGenreOfSugita = series.stream()
                .filter(currentSeries -> currentSeries.getMainCast().contains("Sugita,Tomokazu"))
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(currentSeries -> currentSeries, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(genreCount -> genreCount.getKey()).get();

        System.out.println(mostCommonGenreOfSugita);
        System.out.println("------------------------------------------------------------");
    }

    private void getBestActor() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Actor:");
        //TODO print the name and the average rating of the best Actor

        Map.Entry<String, Double> bestActor = series.stream()
                .map(currentSeries -> {
                    List<Pair<String, Double>> listOfActors = new ArrayList<>();
                    for (String actor : currentSeries.getMainCast()) {
                        Pair<String, Double> actorRating = new Pair<>(actor, currentSeries.getRating());
                        listOfActors.add(actorRating);
                    }
                    return listOfActors;
                })
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy((Pair::getKey), Collectors.averagingDouble(Pair::getValue)))
                .entrySet().stream().max(Map.Entry.comparingByValue()).get();


        System.out.println(bestActor.getKey());
        System.out.println("Average rating:" + bestActor.getValue());
        System.out.println("------------------------------------------------------------");

    }

    private void getBestShounenStudio() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Shounen Studio:");
        //TODO print the name and the average rating (of the shounen series) of the best Shounen Studio - the studio with the best 'Shounen' (genre) series

        Map.Entry<String, Double> bestShounenStudio =
                series.stream()
                        .filter(currentSeries -> currentSeries.getGenres().contains("Shounen"))
                        .map(currentSeries -> {
                            List<Pair<String, Double>> currentListOfShounenStd = new ArrayList<>();
                            for (String studio : currentSeries.getStudios()) {
                                Pair<String, Double> studioRating = new Pair<>(studio, currentSeries.getRating());
                                currentListOfShounenStd.add(studioRating);
                            }
                            return currentListOfShounenStd;
                        }).flatMap(Collection::stream)
                        .collect(Collectors.groupingBy((Pair::getKey), Collectors.averagingDouble(Pair::getValue)))
                        .entrySet().stream()
                        .max(Map.Entry.comparingByValue()).get();


        System.out.println(bestShounenStudio.getKey());
        System.out.println("Average rating:" + bestShounenStudio.getValue());
        System.out.println("------------------------------------------------------------");

    }


}
