package com.ja.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */

@Data
    public class WeatherResponse{
        private String base;
        private Main main;
        private int visibility;

        private int dt;
        private int timezone;
        private int id;
        private String name;
        private int cod;


        @Data
        public class Main{
            private double temp;
            @JsonProperty("feels_like")
            private double feelsLike;
            @JsonProperty("temp_min")
            private double tempMin;
            @JsonProperty("temp_max")
            private double tempMax;
            private int pressure;
            private int humidity;
            private int seaLevel;
            private int grndLevel;
        }






    }
