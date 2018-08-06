package com.internship.training;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class StreamsChallenge {
    
    private List<Serie> series;

    public void executeChallenge() {
    }

    public StreamsChallenge(){
        init();
    }

    void init(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("series.json").getFile());
            this.series = mapper.readValue(file,new TypeReference<List<Serie>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
