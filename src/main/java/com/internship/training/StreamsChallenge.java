package com.internship.training;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        count = series.stream()
                .mapToInt(Series::getEpisodes)
                .sum();

        System.out.println(String.format("Total episodes: %d",count));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageEpisodesCount(){
        System.out.println("------------------------------------------------------------");
        double average = 0;
        //TODO get the average number of episodes, put the result in average

        average = series.stream()
                .mapToInt(Series::getEpisodes)
                .average()
                .getAsDouble();


        System.out.println(String.format("Average number of episodes: %f",average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMaxEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        int count = 0;
        //TODO get the maximun number of episodes, put the result in count

        count = series.stream()
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
                .sorted(Comparator.comparing(Series::getRating).reversed())
                .limit(10)
                .forEach(best->System.out.println(best.getName()));

        System.out.println("------------------------------------------------------------");
    }

    private void getAllGenres(){
        System.out.println("------------------------------------------------------------");
        System.out.println("These are all the genres found:");
        //TODO print all the genres of the series sorted alphabetically, they can not be repeated - one by line
        series.stream()
                .flatMap(serie->serie.getGenres().stream())
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
                .filter(serie -> serie.getStudios().contains("Shaft"))
                .forEach(studio->System.out.println(studio.getName()));

        System.out.println("------------------------------------------------------------");
    }

    private void getMostEpisodesSeries(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Show with most episodes:");
        //TODO print the name and the episode count of the show with the most episodes

        series.stream()
                .max(Comparator.comparing(Series::getEpisodes))
                .ifPresent(best->System.out.println(best.getName()+" with "+best.getEpisodes()+" episodes."));

        System.out.println("------------------------------------------------------------");
    }

    private void getBestStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Studio:");
        //TODO print the name and the average rating of the best Studio

        Map<String, Double> studiosWithRating = new HashMap<>();

        series.stream()
                .flatMap(series -> series.getStudios()
                .stream())
                .distinct()
                .forEach(studio -> studiosWithRating.put(studio, series.stream()
                        .filter(ser -> ser.getStudios().contains(studio))
                        .mapToDouble(Series::getRating).average().getAsDouble()));

        studiosWithRating.entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry<String,Double>::getValue))
                .ifPresent(best->System.out.println(best.getKey() +" with a rating of "+best.getValue()));

         System.out.println("------------------------------------------------------------");
    }

    private void getBestGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best genre:");
        //TODO print the name and the average rating of the best Genre

        Map<String, Double> genresWithRating = new HashMap<>();

        series.stream()
                .flatMap(serie -> serie.getGenres().stream())
                .distinct()
                .forEach(genre -> genresWithRating.put(genre, series.stream()
                        .filter(ser -> ser.getGenres().contains(genre))
                        .mapToDouble(Series::getRating).average().getAsDouble()));

        genresWithRating.entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry<String,Double>::getValue))
                .ifPresent(best->System.out.println(best.getKey() +" with a rating of "+best.getValue()));
        System.out.println("------------------------------------------------------------");
    }

    private void getWorstGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Worst genre:");
        //TODO print the name and the average rating of the worst Genre

        Map<String, Double> genreWithRating = new HashMap<>();

        series.stream()
                .flatMap(serie -> serie.getGenres().stream())
                .distinct()
                .forEach(studio -> genreWithRating.put(studio, series.stream()
                        .filter(ser -> ser.getGenres().contains(studio))
                        .mapToDouble(Series::getRating).average().getAsDouble()));

        genreWithRating.entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry<String,Double>::getValue))
                .ifPresent(worst->System.out.println(worst.getKey() +" with a rating of "+worst.getValue()));

        System.out.println("------------------------------------------------------------");
    }

    private void getTop5MostCommmonEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Top 5 episode count:");
        //TODO print the count and value of the of the top 5 most common episode count  - example:  100 shows have 25 episodes

        Map<Integer,Long> numEpisodesWithCount = new HashMap();

        series.stream()
                .flatMapToInt(n-> IntStream.of(n.getEpisodes()))
                .distinct()
                .forEach(serie->numEpisodesWithCount.put(serie,series.stream()
                        .filter(ser-> ( ser.getEpisodes()== serie)).count()));

        numEpisodesWithCount
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry<Integer,Long>::getValue).reversed())
                .limit(5)
                .forEach(best->System.out.println(best.getValue() +" shows have "+best.getKey()+" episodes."));

        System.out.println("------------------------------------------------------------");
    }

    private void getAverageRatingOfCommedySeries(){
        System.out.println("------------------------------------------------------------");
        double average = 0;
        //TODO get the average rating of the 'Comedy' shows, put the result in average

        average = series.stream()
                .filter(serie->serie.getGenres().contains("Comedy"))
                        .mapToDouble(Series::getRating)
                        .average()
                        .getAsDouble();

        System.out.println(String.format("Average rating of comedy series: %f",average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMostCommonGenreWhereSugitaTomokazuActs(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Sugita Tomokazu most common genre:");
        //TODO print the most common genre where 'Sugita,Tomokazu' acts - the most common genre of the shows where 'Sugita,Tomokazu' is in mainCast

        Map<String, Long> genreWithCount= new HashMap<>();
        series.stream()
                .filter(s -> s.getMainCast().contains("Sugita,Tomokazu"))
                .flatMap(series1 -> series1.getGenres().stream()).distinct()
                .forEach(studio -> genreWithCount.put(studio, series.stream()
                        .filter(ser -> ser.getGenres().contains(studio))
                        .count()));

        genreWithCount.entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry<String,Long>::getValue))
                .ifPresent(common->System.out.println(common.getKey() +" with a count of "+common.getValue()));

        System.out.println("------------------------------------------------------------");
    }

    private void getBestActor(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Actor:");
        //TODO print the name and the average rating of the best Actor

        Map<String, Double> actorWithRating = new HashMap<>();

        series.stream()
                .flatMap(serie -> serie.getMainCast().stream())
                .distinct()
                .forEach(actor -> actorWithRating.put(actor, series.stream()
                        .filter(ser -> ser.getMainCast().contains(actor))
                        .mapToDouble(Series::getRating).average().getAsDouble()));

        actorWithRating.entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry<String,Double>::getValue))
                .ifPresent(best->System.out.println(best.getKey() +" with a rating of "+best.getValue()));

        System.out.println("------------------------------------------------------------");
    }

    private void getBestShounenStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Shounen Studio:");
        //TODO print the name and the average rating (of the shounen series) of the best Shounen Studio - the studio with the best 'Shounen' (genre) series

        Map<String, Double> studioWithRating = new HashMap<>();

        series.stream()
                .filter(s -> s.getGenres().contains("Shounen")).flatMap(series1 -> series1.getStudios().stream())
                .distinct()
                .forEach(studio -> studioWithRating.put(studio, series.stream()
                        .filter(ser -> ser.getStudios().contains(studio))
                        .mapToDouble(Series::getRating).average().getAsDouble()));

        studioWithRating.entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry<String,Double>::getValue))
                .ifPresent(best->System.out.println(best.getKey() +" with a rating of "+best.getValue()));

        System.out.println("------------------------------------------------------------");

    }




}
