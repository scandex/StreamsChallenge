package com.internship.training;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.*;

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
        count=series.stream().mapToInt(Series::getEpisodes).sum();
        //TODO add all episodes of all series, put the result in count

        System.out.println(String.format("Total episodes: %d",count));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageEpisodesCount(){
        System.out.println("------------------------------------------------------------");
        double average = 0;
        average= series.stream().mapToDouble(Series::getEpisodes).average().getAsDouble();

        //TODO get the average number of episodes, put the result in average

        System.out.println(String.format("Average number of episodes: %f",average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMaxEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        int count = 0;
        count=series.stream().map(Series::getEpisodes).collect(Collectors.reducing(Integer::max)).get();
        //TODO get the maximun number of episodes, put the result in count

        System.out.println(String.format("Max number of episodes: %d",count));
        System.out.println("------------------------------------------------------------");

    }

    private void getBest10SeriesByRating(){
        System.out.println("------------------------------------------------------------");
        System.out.println("These are the top 10 series:");
        //TODO print the name of the top 10 series - one by line
        series.stream().sorted(Comparator.comparing(Series::getEpisodes)).limit(10).forEach(a->System.out.print(a.getName()+" "));
         System.out.println("------------------------------------------------------------");
    }

    private void getAllGenres(){
        System.out.println("------------------------------------------------------------");
        System.out.println("These are all the genres found:");
        //TODO print all the genres of the series sorted alphabetically, they can not be repeated - one by line
        series.stream().flatMap(c -> c.getGenres().stream()).collect(Collectors.toList()).stream().distinct().sorted().forEach(System.out::println);
        System.out.println("------------------------------------------------------------");
    }

    private void getSeriesByStudioShaft(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Series by Studio Shaft:");
        //TODO print the name of all Studio 'Shaft' series - one by line
        series.stream().filter(x-> x.getStudios().contains("Shaft")).forEach(studio->System.out.println(studio.getName()));
        System.out.println("------------------------------------------------------------");
    }

    private void getMostEpisodesSeries(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Show with most episodes:");
        //TODO print the name and the episode count of the show with the most episodes
        series.stream().sorted(Comparator.comparing(Series::getEpisodes).reversed()).limit(1).forEach(a->System.out.println(a.getName()+" number episodies "+a.getEpisodes()));
        System.out.println("------------------------------------------------------------");
    }

    private void getBestStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Studio:");
        //TODO print the name and the average rating of the best Studio

        Map<String, Double> studios = new HashMap<>();
        series.stream().flatMap(a -> a.getStudios().stream()).distinct()
                .forEach(studio -> studios.put(studio, series.stream()
                        .filter(b -> b.getStudios().contains(studio))
                        .collect(Collectors.averagingDouble(Series::getRating))));
        studios.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + String.format(": %.3f", map.getValue()) + " Average "));

        System.out.println("------------------------------------------------------------");
    }

    private void getBestGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best genre:");
        //TODO print the name and the average rating of the best Genre

        Map<String, Double> studios = new HashMap<>();
        series.stream().flatMap(a -> a.getGenres().stream()).distinct()
                .forEach(studio -> studios.put(studio, series.stream()
                        .filter(b -> b.getGenres().contains(studio))
                        .collect(Collectors.averagingDouble(Series::getRating))));
        studios.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + String.format(": %.3f", map.getValue()) + " Average "));



        System.out.println("------------------------------------------------------------");
    }

    private void getWorstGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Worst genre:");
        //TODO print the name and the average rating of the worst Genre

        Map<String, Double> studios = new HashMap<>();
        series.stream().flatMap(a -> a.getGenres().stream()).distinct()
                .forEach(studio -> studios.put(studio, series.stream()
                        .filter(b -> b.getGenres().contains(studio))
                        .collect(Collectors.averagingDouble(Series::getRating))));
        studios.entrySet().stream().min(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + String.format(": %.3f", map.getValue()) + "Average"));

        System.out.println("------------------------------------------------------------");
    }

    private void getTop5MostCommmonEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Top 5 episode count:");
        //TODO print the count and value of the of the top 5 most common episode count  - example:  100 shows have 25 episodes

        Map<Integer, Long> common = new HashMap<>();
        series.stream().map(Series::getEpisodes).distinct()
                .forEach(episode -> common.put(episode, series.stream()
                        .filter(a -> a.getEpisodes() == episode)
                        .count()));
        common.entrySet().stream().sorted(reverseOrder(Map.Entry.comparingByValue()))
                .limit(5)
                .forEach(map -> System.out.println(map.getValue() + " shows have " + map.getKey() + " episodes"));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageRatingOfCommedySeries(){
        System.out.println("------------------------------------------------------------");
        double average = 0;
        //TODO get the average rating of the 'Comedy' shows, put the result in average

        average=series.stream().filter(x-> x.getGenres().contains("Comedy")).mapToDouble(Series::getRating).average().getAsDouble();
        System.out.println(String.format("Average rating of comedy series: %f",average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMostCommonGenreWhereSugitaTomokazuActs(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Sugita Tomokazu most common genre:");
        //TODO print the most common genre where 'Sugita,Tomokazu' acts - the most common genre of the shows where 'Sugita,Tomokazu' is in mainCast
        Map<String, Long> Sugita= new HashMap<>();
        series.stream().filter(a -> a.getMainCast().contains("Sugita,Tomokazu"))
                .flatMap(a -> a.getGenres().stream())
                .distinct()
                .forEach(genre -> Sugita.put(genre, series.stream()
                        .filter(b -> b.getGenres().contains(genre))
                        .count()
                ));
        Sugita.entrySet().stream().max(Comparator.comparingLong(Map.Entry::getValue))
                .ifPresent(map -> System.out.println("genre: "+ map.getKey()+""+map.getValue()));

        System.out.println("------------------------------------------------------------");


        System.out.println("------------------------------------------------------------");
    }

    private void getBestActor(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Actor:");
        //TODO print the name and the average rating of the best Actor

        Map<String, Double> studios = new HashMap<>();
        series.stream().flatMap(a -> a.getMainCast().stream()).distinct()
                .forEach(studio -> studios.put(studio, series.stream()
                        .filter(b -> b.getMainCast().contains(studio))
                        .collect(Collectors.averagingDouble(Series::getRating))));
        studios.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + String.format(": %.3f", map.getValue()) + " Average"));

        System.out.println("------------------------------------------------------------");

    }

    private void getBestShounenStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Shounen Studio:");
        //TODO print the name and the average rating (of the shounen series) of the best Shounen Studio - the studio with the best 'Shounen' (genre) series
        Map<String, Double> studiosShounen = new HashMap<>();
        series.stream().filter(a -> a.getGenres().contains("Shounen"))
                .flatMap(a -> a.getStudios().stream())
                .distinct()
                .forEach(studio -> studiosShounen.put(studio, series.stream()
                        .filter(b -> b.getStudios().contains(studio))
                        .collect(Collectors.averagingDouble(Series::getRating))
                ));
        studiosShounen.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + String.format(": %.3f", map.getValue()) + " Average "));
        System.out.println("------------------------------------------------------------");
        System.out.println("------------------------------------------------------------");

    }
}
