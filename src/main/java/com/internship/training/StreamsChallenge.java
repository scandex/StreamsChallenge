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
        getTop5MostCommonEpisodeCount();
        getAverageRatingOfComedySeries();
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
            File file = new File("src\\main\\resources\\series.json");
            List<Series> importedSeries = mapper.readValue(file,new TypeReference<List<Series>>(){});
            this.series = importedSeries.stream().filter(x->x.getRating()>0).collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getEpisodesTotalCount(){
        System.out.println("------------------------------------------------------------");
        int count = 0;

        count = series.stream()
                .mapToInt(Series::getEpisodes)
                .sum();

        System.out.println(String.format("Total episodes: %d",count));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageEpisodesCount(){
        System.out.println("------------------------------------------------------------");
        double average = 0;

        average = series.stream()
                .mapToInt(Series::getEpisodes)
                .average()
                .orElseThrow(IllegalStateException::new);

        System.out.println(String.format("Average number of episodes: %f",average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMaxEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        int count = 0;

        count = series.stream()
                .mapToInt(Series::getEpisodes)
                .max()
                .orElseThrow(IllegalStateException::new);

        System.out.println(String.format("Max number of episodes: %d",count));
        System.out.println("------------------------------------------------------------");

    }

    private void getBest10SeriesByRating(){
        System.out.println("------------------------------------------------------------");
        System.out.println("These are the top 10 series:");

        series.stream()
                .sorted(Comparator.comparing(Series::getRating).reversed())
                .limit(10)
                .map(Series::getName)
                .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getAllGenres(){
        System.out.println("------------------------------------------------------------");
        System.out.println("These are all the genres found:");

        series.stream()
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

        series.stream()
                .filter(currentSeries -> currentSeries.getStudios().contains("Shaft"))
                .map(Series::getName)
                .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getMostEpisodesSeries(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Show with most episodes:");

        Series mostEpisodesSeries = series.stream()
                .max(Comparator.comparing(Series::getEpisodes))
                .orElseThrow(NoSuchElementException::new);

        System.out.println("Name: " + mostEpisodesSeries.getName());
        System.out.println("Episodes: "  + mostEpisodesSeries.getEpisodes());
        System.out.println("------------------------------------------------------------");
    }

    private void getBestStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Studio:");

        List<String> studios = getListOfAllParameters(Series::getStudios);

        Map<String, Double> averages = studios.stream().collect(
                Collectors.toMap(Function.identity(), currentStudio -> series.stream()
                                .filter(currentsSeries ->
                                        currentsSeries.getStudios().contains(currentStudio))
                                .mapToDouble(Series::getRating)
                                .average()
                                .orElseThrow(IllegalStateException::new)));

        Map.Entry<String, Double> maxAverage = getMaxAverage(averages);

        System.out.println("Name: " + maxAverage.getKey());
        System.out.println("Average Rating: "  + maxAverage.getValue());

        System.out.println("------------------------------------------------------------");
    }

    private void getBestGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best genre:");

        List<String> genres = getListOfAllParameters(Series::getGenres);

        Map<String, Double> averages = genres.stream().collect(
                Collectors.toMap(Function.identity(), currentGenre -> series.stream()
                        .filter(currentsSeries ->
                                currentsSeries.getGenres().contains(currentGenre))
                        .mapToDouble(Series::getRating)
                        .average()
                        .orElseThrow(IllegalStateException::new)));

        Map.Entry<String, Double> maxAverage = getMaxAverage(averages);

        System.out.println("Name: " + maxAverage.getKey());
        System.out.println("Average Rating: "  + maxAverage.getValue());

        System.out.println("------------------------------------------------------------");
    }

    private void getWorstGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Worst genre:");

        List<String> genres = getListOfAllParameters(Series::getGenres);

        Map<String, Double> averages = genres.stream().collect(
                Collectors.toMap(Function.identity(), currentGenre -> series.stream()
                        .filter(currentsSeries ->
                                currentsSeries.getGenres().contains(currentGenre))
                        .mapToDouble(Series::getRating)
                        .average()
                        .orElseThrow(IllegalStateException::new)));

        Map.Entry<String, Double> minAverage = getMinAverage(averages);

        System.out.println("Name: " + minAverage.getKey());
        System.out.println("Average Rating: "  + minAverage.getValue());

        System.out.println("------------------------------------------------------------");
    }

    private void getTop5MostCommonEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Top 5 episode count:");

        series.stream()
                .collect(Collectors.groupingBy(Series::getEpisodes, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Comparator.comparingLong(Map.Entry<Integer, Long>::getValue).reversed())
                .limit(5)
                .forEachOrdered(s -> System.out.println( s.getValue() + " shows have "
                        + s.getKey() + " episodes"));

        System.out.println("------------------------------------------------------------");
    }

    private void getAverageRatingOfComedySeries(){
        System.out.println("------------------------------------------------------------");
        double average = 0;

        average = series.stream()
                .filter(s -> s.getGenres().contains("Comedy"))
                .mapToDouble(Series::getRating)
                .average()
                .orElseThrow(IllegalStateException::new);

        System.out.println(String.format("Average rating of comedy series: %f",average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMostCommonGenreWhereSugitaTomokazuActs(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Sugita Tomokazu most common genre:");

        String mostCommonGenre = series.stream()
                .filter(currentSeries -> currentSeries.getMainCast().contains("Sugita,Tomokazu"))
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .orElseThrow(NoSuchElementException::new)
                .getKey();

        System.out.println(mostCommonGenre);

        System.out.println("------------------------------------------------------------");
    }

    private void getBestActor(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Actor:");

        List<String> actors = getListOfAllParameters(Series::getMainCast);

        Map<String, Double> averages = actors.stream().collect(
                Collectors.toMap(Function.identity(), currentActor -> series.stream()
                        .filter(currentsSeries ->
                                currentsSeries.getMainCast().contains(currentActor))
                        .mapToDouble(Series::getRating)
                        .average()
                        .orElseThrow(IllegalStateException::new)));

        Map.Entry<String, Double> maxAverage = getMaxAverage(averages);

        System.out.println("Name: " + maxAverage.getKey());
        System.out.println("Average Rating: "  + maxAverage.getValue());

        System.out.println("------------------------------------------------------------");

    }

    private void getBestShounenStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Shounen Studio:");

        List<Series> shounenSeries = series.stream()
                .filter(currentSeries -> currentSeries.getGenres().contains("Shounen"))
                .collect(Collectors.toList());

        List<String> shounenStudios = getListOfAllParameters(Series::getStudios, shounenSeries);

        Map<String, Double> averages = shounenStudios.stream().collect(
                Collectors.toMap(Function.identity(), currentShounenStudio -> series.stream()
                        .filter(currentsSeries ->
                                currentsSeries.getStudios().contains(currentShounenStudio))
                        .mapToDouble(Series::getRating)
                        .average()
                        .orElseThrow(IllegalStateException::new)));

        Map.Entry<String, Double> maxAverage = getMaxAverage(averages);

        System.out.println("Name: " + maxAverage.getKey());
        System.out.println("Average Rating: "  + maxAverage.getValue());

        System.out.println("------------------------------------------------------------");

    }

    private List<String> getListOfAllParameters(Function<Series, List<String>> function, List<Series> series) {
         return series.stream()
                .map(function)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> getListOfAllParameters(Function<Series, List<String>> function) {
        return getListOfAllParameters(function, this.series);
    }

    private Map.Entry<String, Double> getMaxAverage(Map<String, Double> averages) {
        return averages
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .orElseThrow(NoSuchElementException::new);
    }

    private Map.Entry<String, Double> getMinAverage(Map<String, Double> averages) {
        return averages
                .entrySet()
                .stream()
                .min(Comparator.comparing(Map.Entry::getValue))
                .orElseThrow(NoSuchElementException::new);
    }
}
