package com.internship.training;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Map;


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
                .orElse(0.0);

        System.out.println(String.format("Average number of episodes: %f",average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMaxEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        int count = 0;

        count = series.stream()
                .mapToInt(Series::getEpisodes)
                .max()
                .orElse(0);

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
                .sorted(String::compareToIgnoreCase)
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
                .orElse(new Series());

        System.out.println("Name: " + mostEpisodesSeries.getName());
        System.out.println("Episodes: "  + mostEpisodesSeries.getEpisodes());
        System.out.println("------------------------------------------------------------");
    }

    private void getBestStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Studio:");

        Map<String, Double> averages = series.stream()
            .flatMap(currentSeries ->
                    currentSeries
                            .getStudios()
                            .stream()
                            .map(currentStudio -> new Pair<> (currentStudio, currentSeries.getRating())))
            .collect(Collectors.groupingBy(Pair::getKey, Collectors.averagingDouble(Pair::getValue)));

        Map.Entry<String, Double> maxAverage = getMaxAverage(averages);

        System.out.println("Name: " + maxAverage.getKey());
        System.out.println("Average Rating: "  + maxAverage.getValue());

        System.out.println("------------------------------------------------------------");
    }

    private void getBestGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best genre:");

        Map<String, Double> averages = series.stream()
                .flatMap(currentSeries ->
                        currentSeries
                                .getGenres()
                                .stream()
                                .map(currentGenres -> new Pair<> (currentGenres, currentSeries.getRating())))
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.averagingDouble(Pair::getValue)));

        Map.Entry<String, Double> maxAverage = getMaxAverage(averages);

        System.out.println("Name: " + maxAverage.getKey());
        System.out.println("Average Rating: "  + maxAverage.getValue());

        System.out.println("------------------------------------------------------------");
    }

    private void getWorstGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Worst genre:");

        Map<String, Double> averages = series.stream()
                .flatMap(currentSeries ->
                        currentSeries
                                .getGenres()
                                .stream()
                                .map(currentGenres -> new Pair<> (currentGenres, currentSeries.getRating())))
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.averagingDouble(Pair::getValue)));

        Map.Entry<String, Double> minAverage = getMinAverage(averages);

        System.out.println("Name: " + minAverage.getKey());
        System.out.println("Average Rating: "  + minAverage.getValue());

        System.out.println("------------------------------------------------------------");
    }

    private void getTop5MostCommmonEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Top 5  episode count:");

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

    private void getAverageRatingOfCommedySeries(){
        System.out.println("------------------------------------------------------------");
        double average = 0;

        average = series.stream()
                .filter(s -> s.getGenres().contains("Comedy"))
                .mapToDouble(Series::getRating)
                .average()
                .orElse(0.0);

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
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get()
                .getKey();

        System.out.println(mostCommonGenre);

        System.out.println("------------------------------------------------------------");
    }

    private void getBestActor(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Actor:");

        Map<String, Double> averages = series.stream()
                .flatMap(currentSeries ->
                        currentSeries
                                .getMainCast()
                                .stream()
                                .map(currentActor -> new Pair<> (currentActor, currentSeries.getRating())))
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.averagingDouble(Pair::getValue)));

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

        Map<String, Double> averages = shounenSeries.stream()
                .flatMap(currentSeries ->
                        currentSeries
                                .getStudios()
                                .stream()
                                .map(currentActor -> new Pair<> (currentActor, currentSeries.getRating())))
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.averagingDouble(Pair::getValue)));

        Map.Entry<String, Double> maxAverage = getMaxAverage(averages);

        System.out.println("Name: " + maxAverage.getKey());
        System.out.println("Average Rating: "  + maxAverage.getValue());

        System.out.println("------------------------------------------------------------");

    }

    private Map.Entry<String, Double> getMaxAverage(Map<String, Double> averages) {
        return averages
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get();
    }

    private Map.Entry<String, Double> getMinAverage(Map<String, Double> averages) {
        return averages
                .entrySet()
                .stream()
                .min(Comparator.comparing(Map.Entry::getValue))
                .get();
    }
}
