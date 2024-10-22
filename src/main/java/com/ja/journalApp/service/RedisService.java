package com.ja.journalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    private final RedisTemplate redisTemplate;

    public RedisService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> T get(String key, Class<T> entiyClass){
      try {
          Object o = redisTemplate.opsForValue().get(key);
          ObjectMapper mapper = new ObjectMapper();
          return mapper.readValue(o.toString(),entiyClass);
      }catch (Exception e){
          log.error("Exception {}" , e.getMessage());
          return null;
      }
    }

    public void set(String key, Object o , Long ttl){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(o);

           redisTemplate.opsForValue().set(key,jsonValue,ttl, TimeUnit.SECONDS);
        }catch (Exception e){
            log.error("Exception {}" , e.getMessage());
        }
    }


}
