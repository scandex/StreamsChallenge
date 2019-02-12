package com.internship.training;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
        int count = 0;
        //TODO add all episodes of all series, put the result in count
        count= series.stream()
                .mapToInt(Series::getEpisodes)
                .sum();
        System.out.println(String.format("Total episodes: %d",count));
        System.out.println("------------------------------------------------------------");
    }
    private void getAverageEpisodesCount(){
        System.out.println("------------------------------------------------------------");
        double average = 0;
        //TODO get the average number of episodes, put the result in average
        average= series.stream()
                .mapToDouble(Series::getEpisodes)
                .average()
                .getAsDouble();
        System.out.println(String.format("Average number of episodes: %f",average));
        System.out.println("------------------------------------------------------------");
    }
    private void getMaxEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        int count = 0;
        //TODO get the maximun number of episodes, put the result in count
        count= series.stream()
                .mapToInt(Series::getEpisodes)
                .max()
                .getAsInt();
        System.out.println(String.format("Max number of episodes: %d",count));
        System.out.println("------------------------------------------------------------");

    }
    private void getBest10SeriesByRating(){
        System.out.println("------------------------------------------------------------");
        System.out.println("These are the top 10 series:");
        //TODO print the name of the top 10 series - one by line
        series.stream()
                .sorted(Comparator
                        .comparing(Series::getRating)
                        .reversed())
                .limit(10)
                .forEach(topTen ->
                        System.out.println(topTen.getName()));
        System.out.println("------------------------------------------------------------");
    }
    private void getAllGenres(){
        System.out.println("------------------------------------------------------------");
        System.out.println("These are all the genres found:");
        //TODO print all the genres of the series sorted alphabetically, they can not be repeated - one by line
        series.stream()
                .flatMap(genres ->
                        genres.getGenres()
                                .stream())
                .distinct()
                .sorted()
                .forEach(System.out::println);
        System.out.println("------------------------------------------------------------");
    }
    private void getSeriesByStudioShaft(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Series by Studio Shaft:");
        //TODO print the name of all Studio 'Shaft' series - one by line
        series.stream()
                .filter(studioShaft ->
                        studioShaft
                                .getStudios()
                                .contains("Shaft"))
                .forEach(studioShaft ->
                        System.out.println(studioShaft.getName()));
        System.out.println("------------------------------------------------------------");
    }
    private void getMostEpisodesSeries(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Show with most episodes:");
        //TODO print the name and the episode count of the show with the most episodes
        series.stream()
                .sorted(Comparator
                        .comparing(Series::getEpisodes)
                        .reversed())
                .limit(1)
                .forEach(mostESeries ->
                        System.out.println(mostESeries.getName()+" "+mostESeries.getEpisodes()));
        System.out.println("------------------------------------------------------------");
    }
    private void getBestStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Studio:");
        //TODO print the name and the average rating of the best Studio
        series.stream()
                .map(Series::getStudios)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors
                        .toMap(String::toString,serieStudio ->
                                series.stream()
                                        .filter(seriesAverage ->
                                                seriesAverage.getStudios()
                                                        .contains(serieStudio))
                                        .mapToDouble(Series::getRating)
                                        .average()
                                        .orElse(-1)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue()
                        .reversed())
                .limit(1).forEach(System.out::println);
        System.out.println("------------------------------------------------------------");
    }
    private void getBestGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best genre:");
        //TODO print the name and the average rating of the best Genre
        series.stream()
                .map(Series::getGenres)
                .flatMap(Collection::stream).distinct()
                .collect(Collectors
                        .toMap(String::toString,seriesGenre ->
                                series.stream()
                                        .filter(seriesAverage ->
                                                seriesAverage.getGenres()
                                                        .contains(seriesGenre))
                                        .mapToDouble(Series::getRating)
                                        .average()
                                        .orElse(-1)))
                .entrySet()
                .stream()
                .sorted(Map
                        .Entry.<String, Double>comparingByValue()
                        .reversed())
                .limit(1)
                .forEach(System.out::println);
        System.out.println("------------------------------------------------------------");
    }
    private void getWorstGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Worst genre:");
        //TODO print the name and the average rating of the worst Genre
        series.stream()
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors
                        .toMap(String::toString,serieGenre ->
                                series.stream()
                                        .filter(seriesAverage ->
                                                seriesAverage.getGenres()
                                                        .contains(serieGenre))
                                        .mapToDouble(Series::getRating)
                                        .average()
                                        .orElse(-1)))
                .entrySet()
                .stream()
                .sorted(Map
                        .Entry.<String, Double>comparingByValue())
                .limit(1)
                .forEach(System.out::println);
        System.out.println("------------------------------------------------------------");
    }
    private void getTop5MostCommmonEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Top 5 episode count:");
        //TODO print the count and value of the of the top 5 most common episode count  - example:  100 shows have 25 episodes
        series.stream()
                .collect(Collectors
                        .groupingBy(Series::getEpisodes, Collectors
                                .counting()))
                .entrySet()
                .stream()
                .sorted(Map
                        .Entry.<Integer, Long>comparingByValue()
                        .reversed())
                .limit(5)
                .forEach(episodeCount ->
                        System.out.println(String.format("%d shows have %d episodes",episodeCount.getValue(),episodeCount.getKey())));
        System.out.println("------------------------------------------------------------");
    }
    private void getAverageRatingOfCommedySeries(){
        System.out.println("------------------------------------------------------------");
        double average = 0;
        //TODO get the average rating of the 'Comedy' shows, put the result in average
        average = series.stream()
                .filter(seriesAverage ->
                        seriesAverage.getGenres()
                                .contains("Comedy"))
                .mapToDouble(Series::getRating)
                .average()
                .orElse(-1);
        System.out.println("Average rating of comedy series: " + average);
        System.out.println(String.format("Average rating of comedy series: %f",average));
        System.out.println("------------------------------------------------------------");
    }
    private void getMostCommonGenreWhereSugitaTomokazuActs(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Sugita Tomokazu most common genre:");
        //TODO print the most common genre where 'Sugita,Tomokazu' acts - the most common genre of the shows where 'Sugita,Tomokazu' is in mainCast
        series.stream()
                .filter(sugitaSeries ->
                        sugitaSeries.getMainCast()
                                .contains("Sugita,Tomokazu"))
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors
                        .toMap(String::toString, sugitaMap ->
                                series.stream()
                                        .filter(sugitaSeries ->
                                                sugitaSeries.getMainCast()
                                                        .contains("Sugita,Tomokazu"))
                                        .filter((sugitaSeries ->
                                                sugitaSeries.getGenres()
                                                        .contains(sugitaMap)))
                                        .count()))
                .entrySet()
                .stream()
                .sorted(Map
                        .Entry.<String, Long>comparingByValue()
                        .reversed())
                .limit(1)
                .forEach(System.out::println);
        System.out.println("------------------------------------------------------------");
    }
    private void getBestActor(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Actor:");
        //TODO print the name and the average rating of the best Actor
        series.stream()
                .map(Series::getMainCast)
                .flatMap(Collection::stream)
                .collect(Collectors
                        .toSet())
                .stream()
                .collect(Collectors
                        .toMap(String::toString, serieCast ->
                                series.stream()
                                        .filter(actorSeries ->
                                                actorSeries.getMainCast()
                                                        .contains(serieCast))
                                        .mapToDouble(Series::getRating)
                                        .average()
                                        .orElse(-1)))
                .entrySet()
                .stream()
                .sorted(Map
                        .Entry.<String, Double>comparingByValue()
                        .reversed())
                .limit(1)
                .forEach(System.out::println);
        System.out.println("------------------------------------------------------------");
    }
    private void getBestShounenStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Shounen Studio:");
        //TODO print the name and the average rating (of the shounen series) of the best Shounen Studio - the studio with the best 'Shounen' (genre) series
        series.stream()
                .filter(shounenSeries ->
                        shounenSeries.getGenres()
                                .contains("Shounen"))
                .map(Series::getStudios)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors
                        .toMap(String::toString,serieStudios ->
                                series.stream()
                                        .filter(shounenSeries ->
                                                shounenSeries.getStudios()
                                                        .contains(serieStudios))
                                        .mapToDouble(Series::getRating)
                                        .average()
                                        .orElse(-1)))
                .entrySet()
                .stream()
                .sorted(Map
                        .Entry.<String, Double>comparingByValue()
                        .reversed())
                .limit(1)
                .forEach(System.out::println);
        System.out.println("------------------------------------------------------------");

    }




}
