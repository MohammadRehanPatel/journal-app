package com.ja.journalApp.controller;

import com.ja.journalApp.api.response.WeatherResponse;
import com.ja.journalApp.entity.User;
import com.ja.journalApp.service.UserService;
import com.ja.journalApp.service.WeatherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/user")
@Tag(name = "User APIs" ,description = "Read , Update & Delete User")
public class UserController {

    private final UserService userService;
    private final WeatherService weatherService;

    public UserController(UserService userService, WeatherService weatherService) {
        this.userService = userService;
        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        log.info("UserController::getAllUsers Started");
        List<User> allUsers = userService.getAllUsers();
        if(allUsers!=null && !allUsers.isEmpty() ){

            log.info("UserController::getAllUsers Ended");
            return new ResponseEntity<>(allUsers,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Boolean> createUser(@RequestBody User user){
        log.info("UserController::createUser Started");
        return new ResponseEntity<>(userService.createNewUser(user),HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable ObjectId id){
        log.info("UserController::getUserById Started with Id: {}",id);
        Optional<User> user = userService.findUserById(id);
        log.info("UserController::getUserById Ended");
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user){
        log.info("UserController::updateUser Started with Id: {}",user.getId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        userService.updateUser(user,userName);
        log.info("UserController::updateUser Ended");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@RequestParam ObjectId id){
        try {
            log.info("UserController::deleteUserById Started with Id: {}",id);
            userService.deleteUserById(id);
            log.info("UserController::deleteUserById Ended");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserByUsername(){
        try {
            log.info("UserController::deleteUserByUsername Started ");
            userService.deleteUserByUserName();
            log.info("UserController::deleteUserByUsername Ended ");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/greeting")
    public ResponseEntity<?> greeting(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weather = weatherService.getWeather("indore");
        String greeting = "";
        if(weather!=null){
            greeting= ", Weather Feels like " + weather.getMain().getFeelsLike();
        }
        return new ResponseEntity<>("Hi " + authentication.getName() + greeting  ,HttpStatus.OK);
    }


}
