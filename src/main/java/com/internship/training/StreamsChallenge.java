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
                .average().getAsDouble();
        System.out.println(String.format("Average number of episodes: %f",average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMaxEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        int count = 0;
        //TODO get the maximun number of episodes, put the result in count
        count = series.stream()
                .mapToInt(Series::getEpisodes)
                .max().getAsInt();
        System.out.println(String.format("Max number of episodes: %d",count));
        System.out.println("------------------------------------------------------------");

    }

    private void getBest10SeriesByRating(){
        System.out.println("------------------------------------------------------------");
        System.out.println("These are the top 10 series:");
        //TODO print the name of the top 10 series - one by line
        series.stream()
                .sorted(Comparator.comparing(Series::getRating)
                        .reversed())
                .limit(10)
                .forEach(serie->System.out.println(serie.getName()));

        System.out.println("------------------------------------------------------------");
    }

    private void getAllGenres(){
        System.out.println("------------------------------------------------------------");
        System.out.println("These are all the genres found:");
        //TODO print all the genres of the series sorted alphabetically, they can not be repeated - one by line
        series.stream()
            .map(Series::getGenres)
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .stream()
                .sorted()
                .distinct()
                .forEach(
                        serie->System.out.println(serie)
        );
        System.out.println("------------------------------------------------------------");
    }

    private void getSeriesByStudioShaft(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Series by Studio Shaft:");
        //TODO print the name of all Studio 'Shaft' series - one by line
        series.stream()
                .filter(serie->serie.getStudios().contains("Shaft") )
                .forEach(
                        serie->System.out.println(serie.getName())
                );

        System.out.println("------------------------------------------------------------");
    }

    private void getMostEpisodesSeries(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Show with most episodes:");
        //TODO print the name and the episode count of the show with the most episodes
        series.stream()
                .sorted(Comparator.comparing(Series::getEpisodes)
                        .reversed())
                .limit(1)
                .forEach(serie->System.out.println(serie.getName() + " " + serie.getEpisodes()));

        System.out.println("------------------------------------------------------------");
    }

    private void getBestStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Studio:");
        //TODO print the name and the average rating of the best
        Map<String, Double> list = new HashMap<String, Double>();
        series.stream()
                .map(Series::getStudios)
                .flatMap(Collection::stream)
                .distinct()
                .forEach( studio -> list.put( studio, series.stream()
                        .filter(serie-> serie.getStudios().contains(studio)
                        ).collect(Collectors.averagingDouble(Series::getRating))

                ));
        list.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + " " + map.getValue()));

        System.out.println("------------------------------------------------------------");
    }

    private void getBestGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best genre:");
        //TODO print the name and the average rating of the best Genre
        Map<String, Double> list = new HashMap<String, Double>();
        series.stream()
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .forEach( genre -> list.put( genre, series.stream()
                        .filter(serie-> serie.getGenres().contains(genre)
                        ).collect(Collectors.averagingDouble(Series::getRating))
                ));
        list.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + " " + map.getValue()));
        System.out.println("------------------------------------------------------------");
    }

    private void getWorstGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Worst genre:");
        //TODO print the name and the average rating of the worst Genre
        Map<String, Double> list = new HashMap<String, Double>();
        series.stream()
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .forEach( genre -> list.put( genre, series.stream()
                        .filter(serie-> serie.getGenres().contains(genre)
                        ).collect(Collectors.averagingDouble(Series::getRating))
                ));
        list.entrySet().stream().min(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + " " + map.getValue()));
        System.out.println("------------------------------------------------------------");
    }

    private void getTop5MostCommmonEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Top 5 episode count:");
        //TODO print the count and value of the of the top 5 most common episode count  - example:  100 shows have 25 episodes
        Map<Integer, Long> list = new HashMap<Integer, Long>();
        //Map<Integer, List<Series>> TempList=
        series.stream()
                .map(Series::getEpisodes)
                .distinct()
                .forEach( epi -> list.put( epi, series.stream()
                        .filter(serie-> serie.getEpisodes()==(epi)
                        ).count()
                ));
        //System.out.println(list.toString());
        //preguntar porque no reversed
        list.entrySet().stream().sorted(Collections.reverseOrder(Comparator.comparingDouble(Map.Entry::getValue)))
                .limit(5)
                .forEach(
                        map -> System.out.println(map.getKey() + " " + map.getValue())
                );
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageRatingOfCommedySeries(){
        System.out.println("------------------------------------------------------------");
        double average = 0;
        //TODO get the average rating of the 'Comedy' shows, put the result in average
        average = series.stream()
                        .filter(serie-> serie.getGenres().contains("Comedy")
                        ).collect(Collectors.averagingDouble(Series::getRating));
        System.out.println(String.format("Average rating of comedy series: %f",average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMostCommonGenreWhereSugitaTomokazuActs(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Sugita Tomokazu most common genre:");
        //TODO print the most common genre where 'Sugita,Tomokazu' acts - the most common genre of the shows where 'Sugita,Tomokazu' is in mainCast
        Map<String, Long> list = new HashMap<String, Long>();
        series.stream()
                .filter(serie->serie.getMainCast().contains("Sugita,Tomokazu"))
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .forEach( genre -> list.put( genre, series.stream()
                        .filter(serie-> serie.getGenres().contains(genre)
                        ).count()
                ));

        list.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey()));
        System.out.println("------------------------------------------------------------");
    }

    private void getBestActor(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Actor:");
        //TODO print the name and the average rating of the best Actor
        Map<String, Double> list = new HashMap<String, Double>();
        series.stream()
                .map(Series::getMainCast)
                .flatMap(Collection::stream)
                .distinct()
                .forEach( actor -> list.put( actor, series.stream()
                        .filter(serie-> serie.getMainCast().contains(actor)
                        ).collect(Collectors.averagingDouble(Series::getRating))
                ));
        list.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + " " + map.getValue()));
        System.out.println("------------------------------------------------------------");

    }

    private void getBestShounenStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Shounen Studio:");
        //TODO print the name and the average rating (of the shounen series) of the best Shounen Studio - the studio with the best 'Shounen' (genre) series
        Map<String, Double> list = new HashMap<String, Double>();
        series.stream()
                .filter(serie-> serie.getGenres().contains("Shounen")
                ).map(Series::getStudios)
                .flatMap(Collection::stream)
                .forEach( studio -> list.put( studio, series.stream()
                        .filter(serie-> serie.getStudios().contains(studio)
                        ).collect(Collectors.averagingDouble(Series::getRating))
                ));
        list.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                .ifPresent(map -> System.out.println(map.getKey() + " " + map.getValue()));
        System.out.println("------------------------------------------------------------");

    }




}


