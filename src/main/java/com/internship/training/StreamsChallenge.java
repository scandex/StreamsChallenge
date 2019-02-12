package com.internship.training;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
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
        int count=0;
        //TODO add all episodes of all series, put the result in count
        count = series.stream()
                .mapToInt(Series::getEpisodes).sum();
        System.out.println(String.format("Total episodes: %d",count));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageEpisodesCount(){
        System.out.println("------------------------------------------------------------");
        double average = 0;
        //TODO get the average number of episodes, put the result in average
        average = series.stream()
                .mapToInt(Series::getEpisodes).average().getAsDouble();
        System.out.println(String.format("Average number of episodes: %f",average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMaxEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        int count = 0;
        //TODO get the maximun number of episodes, put the result in count
        count = series.stream()
                .max(Comparator.comparing(Series::getEpisodes)).get().getEpisodes();
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
               .map(Series::getName)
               .forEach(System.out::println);
       System.out.println("------------------------------------------------------------");
    }

    private void getAllGenres(){
        System.out.println("------------------------------------------------------------");
        System.out.println("These are all the genres found:");
        //TODO print all the genres of the series sorted alphabetically, they can not be repeated - one by line
        List<String> genres = series.stream()
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .sorted()
                .distinct()
                .collect(Collectors.toList());
        System.out.print(genres);
        System.out.println("------------------------------------------------------------");
    }

    private void getSeriesByStudioShaft(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Series by Studio Shaft:");
        //TODO print the name of all Studio 'Shaft' series - one by line
        series.stream()
                .filter(s -> s.getStudios().contains("Shaft"))
                .forEach(thisSeries -> System.out.print(""+"--"+thisSeries.getName()+"--"+" "));
        System.out.println("------------------------------------------------------------");
    }

    private void getMostEpisodesSeries(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Show with most episodes:");
        //TODO print the name and the episode count of the show with the most episodes
        series.stream()
                .sorted(Comparator.comparing(Series::getEpisodes).reversed())
                .limit(1)
                .map(Series::getName)
                .forEach(System.out::println);
        System.out.println("------------------------------------------------------------");
    }

    private void getBestStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Studio:");
        //TODO print the name and the average rating of the best Studio

        List<String> studios = series.stream()
                .map(Series::getStudios)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        Map<String, Double> bestStudio = studios.stream()
                .collect(Collectors.toMap(s -> s, k->series.stream()
                        .filter(currentSeries -> currentSeries.getStudios()
                        .contains(k))
                        .mapToDouble(Series::getRating)
                        .average()
                        .orElse(0)))
                ;
        Map.Entry<String, Double> result = bestStudio.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue)).get();

        System.out.println(result.getKey()+":"+result.getValue());

        System.out.println("------------------------------------------------------------");
    }

    private void getBestGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best genre:");
        //TODO print the name and the average rating of the best Genre
            List<String> generos = series.stream()
                    .map(Series::getGenres)
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList());

            Map<String, Double> bestGenre = generos.stream()
                    .collect(Collectors.toMap(s-> s, k-> series.stream()
                    .filter(currentGenero -> currentGenero.getGenres()
                    .contains(k))
                    .mapToDouble(Series::getRating)
                    .average()
                    .orElse(0)));
            Map.Entry<String, Double> resul = bestGenre.entrySet().stream()
                    .max(Comparator.comparing(Map.Entry::getValue)).get();

            System.out.println(resul.getKey()+":"+resul.getValue());
        System.out.println("------------------------------------------------------------");
    }

    private void getWorstGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Worst genre:");
        //TODO print the name and the average rating of the worst Genre
        List<String> generos = series.stream()
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        Map<String, Double> bestGenre = generos.stream()
                .collect(Collectors.toMap(s-> s, k-> series.stream()
                        .filter(currentGenero -> currentGenero.getGenres()
                                .contains(k))
                        .mapToDouble(Series::getRating)
                        .average()
                        .orElse(0)));
        Map.Entry<String, Double> resul = bestGenre.entrySet().stream()
                .min(Comparator.comparing(Map.Entry::getValue)).get();

        System.out.println(resul.getKey()+":"+resul.getValue());
        System.out.println("------------------------------------------------------------");
    }

    private void getTop5MostCommmonEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Top 5 episode count:");
        //TODO print the count and value of the of the top 5 most common episode count  - example:  100 shows have 25 episodes
       series.stream()
               .collect(Collectors.groupingBy(Series::getEpisodes, Collectors.counting()))
               .entrySet()
               .stream()
               .sorted(Comparator.comparingLong(Map.Entry<Integer, Long>::getValue).reversed())
               .limit(5)
               .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getAverageRatingOfCommedySeries(){
        System.out.println("------------------------------------------------------------");
        double average = 0;
        //TODO get the average rating of the 'Comedy' shows, put the result in average
            average = series.stream()
                    .filter(s -> s.getGenres().contains("Comedy"))
                    .mapToDouble(Series::getRating).average().getAsDouble();

        System.out.println(String.format("Average rating of comedy series: %f",average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMostCommonGenreWhereSugitaTomokazuActs(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Sugita Tomokazu most common genre:");
        //TODO print the most common genre where 'Sugita,Tomokazu' acts - the most common genre of the shows where 'Sugita,Tomokazu' is in mainCast
               String mostCommonActor = series.stream()
                .filter(s -> s.getMainCast().contains("Sugita,Tomokazu"))
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue)).orElseThrow(NoSuchElementException::new).getKey();
               System.out.println(" "+mostCommonActor);
        System.out.println("------------------------------------------------------------");
    }

    private void getBestActor(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Actor:");
        //TODO print the name and the average rating of the best Actor
        List<String> generos = series.stream()
                .map(Series::getName)
                .distinct()
                .collect(Collectors.toList());

        Map<String, Double> bestGenre = generos.stream()
                .collect(Collectors.toMap(s-> s, k-> series.stream()
                        .filter(currentGenero -> currentGenero.getGenres()
                                .contains(k))
                        .mapToDouble(Series::getRating)
                        .average()
                        .orElse(0)));
        Map.Entry<String, Double> resul = bestGenre.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue)).get();

        System.out.println(resul.getKey()+":"+resul.getValue());
        System.out.println("------------------------------------------------------------");

    }

    private void getBestShounenStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Shounen Studio:");
        //TODO print the name and the average rating (of the shounen series) of the best Shounen Studio - the studio with the best 'Shounen' (genre) series

        System.out.println("------------------------------------------------------------");

    }




}
