package com.ja.journalApp.service;

import com.ja.journalApp.api.response.WeatherResponse;
import com.ja.journalApp.cache.AppCache;
import com.ja.journalApp.constants.Placeholders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {
    @Value("${weather.api.key}")
    private  String apiKey ;
//    private static final String API = "https://api.openweathermap.org/data/2.5/weather?q=CITY&appid=41ee3aa8d42ea142ae3296e3a421292b";

    private final RestTemplate restTemplate;

    private final RedisService redisService;

    private final AppCache appCache;

    public WeatherService( RestTemplate restTemplate, RedisService redisService, AppCache appCache) {
        this.restTemplate = restTemplate;
        this.redisService = redisService;
        this.appCache = appCache;
    }

    public WeatherResponse getWeather(String city){
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if(weatherResponse!=null){
            return weatherResponse;
        }else{
            String finalAPI = appCache.APP_CACHE.get(Placeholders.WEATHER_API).replace(Placeholders.CITY, city).replace(Placeholders.API_KEY, apiKey);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = response.getBody();
            if(body!=null){
                redisService.set("weather_of_" + city,body,300l);
            }
            return body;
        }


    }

}
