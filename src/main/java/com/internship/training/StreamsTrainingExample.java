package com.internship.training;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamsTrainingExample {

    public static void main(String[] args) {
        streamsMap();
        streamsFlatMap();
        streamsFilter();
        streamsDistinct();
        streamsSorted();
        streamsLimit();
        streamsReduce();
        streamsReduceInt();
    }


    public static void streamsMap() {
        List<String> strings = Stream.of("one", null, "three").map(s -> {
            if (s == null)
                return "[unknown]";
            else
                return s;
        }).collect(Collectors.toList());

        System.out.println(strings);

    }

    public static void streamsFlatMap() {
        List<List<String>> namesNested = Arrays.asList(
                Arrays.asList("Jeff", "Bezos"),
                Arrays.asList("Bill", "Gates"),
                Arrays.asList("Mark", "Zuckerberg"));

        List<String> namesFlatStream = namesNested.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        System.out.println(namesFlatStream);
    }

    public static void streamsFilter() {
        long elementsLessThanThree = Stream.of(1, 2, 3, 3, 3, 4)
                .filter(p -> p.intValue() <= 3).count();
        System.out.println(elementsLessThanThree);
    }

    public static void streamsDistinct() {

        List<Integer> distinctIntegers = IntStream.of(5, 6, 6, 6, 3, 2, 2)
                .distinct()
                .boxed()
                .collect(Collectors.toList());
        System.out.println(distinctIntegers);

    }

    public static void streamsSorted() {
        List<Integer> sortedNumbers = Stream.of(3, 3, 1, 3, 6).limit(4).sorted().distinct()
                .collect(Collectors.toList());
        System.out.println(sortedNumbers);

    }

    public static void streamsLimit() {
       Stream.of("limit", "by", "two").limit(3).forEach(System.out::println);
//                .collect(Collectors.toList());
//        System.out.println(vals);

    }

    public static void streamsReduce(){
        String[] myArray = { "this", " " , "is ", "", "sparta"};
        String result = Arrays.stream(myArray)
                .reduce("hola", (a,b) -> a + b);
        System.out.println(result);
    }

    public static void streamsReduceInt(){
        Integer[] myArray = { 1,2,3,4,5,6,7,8 };
        Integer result = Arrays.stream(myArray)
                .reduce(0, (a,b) -> a + b);
        System.out.println(result);
    }
}
