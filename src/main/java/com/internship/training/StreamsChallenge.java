package com.internship.training;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

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
        int count;
        //TODO add all episodes of all series, put the result in count

        count=series.stream()
                .mapToInt(Series::getEpisodes)
                .sum();

        System.out.println(String.format("Total episodes: %d",count));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageEpisodesCount(){
        System.out.println("------------------------------------------------------------");
        double average;
        //TODO get the average number of episodes, put the result in average

        average=series.stream()
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
        count=series.stream()
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
                .sorted(comparing(Series::getRating).reversed())
                .limit(10)
                .map(Series::getName)
                .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getAllGenres(){
        System.out.println("------------------------------------------------------------");
        System.out.println("These are all the genres found:");
        //TODO print all the genres of the series sorted alphabetically, they can not be repeated - one by line

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
        //TODO print the name of all Studio 'Shaft' series - one by line

        series.stream()
                .filter(currentSeries -> currentSeries.getStudios().contains("Shaft"))
                .map(Series::getName)
                .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
    }

    private void getMostEpisodesSeries(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Show with most episodes:");
        //TODO print the name and the episode count of the show with the most episodes
        Series mostWatchedSeries =series.stream()
                .sorted(comparing(Series::getEpisodes).reversed())
                .findFirst().get();

        System.out.println(mostWatchedSeries.getName());
        System.out.println("Number of episodes:");
        System.out.println(mostWatchedSeries.getEpisodes());

        System.out.println("------------------------------------------------------------");
    }

    private void getBestStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Studio:");
        //TODO print the name and the average rating of the best Studio

        Map<String,Double> averageRatingOfStudios = series.stream()
                .map(Series::getStudios)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toMap(Function.identity(),p ->0.0));

        averageRatingOfStudios.entrySet().stream()
                .forEach(currentStudio-> {
                    String nameOfCurrentStudio= currentStudio.getKey();
                    double averageRatingCurrentStudio =
                            series.stream()
                                    .filter(currentSeries->currentSeries.getStudios().contains(nameOfCurrentStudio))
                                    .collect(Collectors.averagingDouble(currentSeries->currentSeries.getRating()));
                    averageRatingOfStudios.put(nameOfCurrentStudio,averageRatingCurrentStudio);
                });

        String bestStudio = averageRatingOfStudios
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(currentStudio->currentStudio.getKey()).get();

        System.out.println(bestStudio);
        System.out.println("Average rating:"+averageRatingOfStudios.get(bestStudio));
        System.out.println("------------------------------------------------------------");
    }

    private void getBestGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best genre:");
        //TODO print the name and the average rating of the best Genre

        Map<String,Double> ratingOfEachGenre = series.stream()
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toMap(Function.identity(),averageRating->0.0));

        ratingOfEachGenre.entrySet()
                .stream()
                .forEach(currentGenreRating-> {
                    String currentGenre= currentGenreRating.getKey();
                    double averageRatingCurrentGenre= series.stream()
                            .filter(currentSeries->currentSeries.getGenres().contains(currentGenre))
                            .collect(Collectors.averagingDouble(currentSeries->currentSeries.getRating()));
                    ratingOfEachGenre.put(currentGenre,averageRatingCurrentGenre);
                });

        Map.Entry<String,Double> bestGenre= ratingOfEachGenre.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get();


        System.out.println(bestGenre.getKey());
        System.out.println("Average rating:"+bestGenre.getValue());

        System.out.println("------------------------------------------------------------");
    }

    private void getWorstGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Worst genre:");
        //TODO print the name and the average rating of the worst Genre

        Map<String,Double> ratingOfEachGenre = series.stream()
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toMap(Function.identity(),averageRating->0.0));

        ratingOfEachGenre.entrySet()
                .stream()
                .forEach(currentGenreRating-> {
                    String currentGenre= currentGenreRating.getKey();
                    double averageRatingCurrentGenre= series.stream()
                            .filter(currentSeries->currentSeries.getGenres().contains(currentGenre))
                            .collect(Collectors.averagingDouble(currentSeries->currentSeries.getRating()));
                    ratingOfEachGenre.put(currentGenre,averageRatingCurrentGenre);
                });

        Map.Entry<String,Double> worstGenre= ratingOfEachGenre.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .get();


        System.out.println(worstGenre.getKey());
        System.out.println("Average rating:"+worstGenre.getValue());
        System.out.println("------------------------------------------------------------");
    }

    private void getTop5MostCommmonEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Top 5 episode count:");
        //TODO print the count and value of the of the top 5 most common episode count  - example:  100 shows have 25 episodes

        series.stream()
                .map(Series::getEpisodes)
                .collect(Collectors.groupingBy(currentSeries->currentSeries, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer,Long>comparingByValue().reversed())
                .limit(5)
                .forEach(commonEpisodeCount->System.out.println(commonEpisodeCount.getValue()+" shows have "+commonEpisodeCount.getKey()+" episodes"));

        System.out.println("------------------------------------------------------------");
    }

    private void getAverageRatingOfCommedySeries(){
        System.out.println("------------------------------------------------------------");
        double average = 0;
        //TODO get the average rating of the 'Comedy' shows, put the result in average
        average=series.stream()
                .filter(currentSeries->currentSeries.getGenres().contains("Comedy"))
                .collect(Collectors.averagingDouble(currentComedySeries->currentComedySeries.getRating()));

        System.out.println(String.format("Average rating of comedy series: %f",average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMostCommonGenreWhereSugitaTomokazuActs(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Sugita Tomokazu most common genre:");
        //TODO print the most common genre where 'Sugita,Tomokazu' acts - the most common genre of the shows where 'Sugita,Tomokazu' is in mainCast
        String mostCommonGenreOfSugita=series.stream()
                .filter(currentSeries->currentSeries.getMainCast().contains("Sugita,Tomokazu"))
                .map(Series::getGenres)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(currentSeries->currentSeries, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(genreCount->genreCount.getKey()).get();

        System.out.println(mostCommonGenreOfSugita);
        System.out.println("------------------------------------------------------------");
    }

    private void getBestActor(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Actor:");
        //TODO print the name and the average rating of the best Actor

        Map<String,Double> ratingOfEachActor = series.stream()
                .map(Series::getMainCast)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toMap(Function.identity(),rating->0.0));


        ratingOfEachActor.entrySet()
                .stream()
                .forEach(currentActorRating-> {
                    String currentActor = currentActorRating.getKey();
                    double averageRatingCurrentActor= series.stream()
                            .filter(currentSeries->currentSeries.getMainCast().contains(currentActor))
                            .collect(Collectors.averagingDouble(currentSeries->currentSeries.getRating()));
                    ratingOfEachActor.put(currentActor,averageRatingCurrentActor);
                });


        Map.Entry<String,Double> bestActor = ratingOfEachActor.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get();


        System.out.println(bestActor.getKey());
        System.out.println("Average rating:"+bestActor.getValue());
        System.out.println("------------------------------------------------------------");

    }

    private void getBestShounenStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Shounen Studio:");
        //TODO print the name and the average rating (of the shounen series) of the best Shounen Studio - the studio with the best 'Shounen' (genre) series
        Map<String,Double> ratingOfShounenStudios = series.stream()
                .filter(currentSeries->currentSeries.getGenres().contains("Shounen"))
                .map(Series::getStudios)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toMap(Function.identity(),rating->0.0));

        ratingOfShounenStudios.entrySet()
                .stream()
                .forEach(currentShounenStudio-> {
                    String nameCurrentShounenStudio = currentShounenStudio.getKey();
                    double averageRatingCurrentStudio= series.stream()
                            .filter(currentSeries->currentSeries.getStudios().contains(nameCurrentShounenStudio))
                            .collect(Collectors.averagingDouble(currentSeries->currentSeries.getRating()));
                    ratingOfShounenStudios.put(nameCurrentShounenStudio,averageRatingCurrentStudio);
                });

        Map.Entry<String,Double>  bestShounenStudio = ratingOfShounenStudios.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get();


        System.out.println(bestShounenStudio.getKey());
        System.out.println("Average rating:"+bestShounenStudio.getValue());
        System.out.println("------------------------------------------------------------");

    }




}
