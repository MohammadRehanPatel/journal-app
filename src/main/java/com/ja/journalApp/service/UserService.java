package com.ja.journalApp.service;

import com.ja.journalApp.entity.User;
import com.ja.journalApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {


    private final UserRepository userRepository;
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
//    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public boolean  createNewUser( User user){
        try {
            String encodedPassword =PASSWORD_ENCODER.encode( user.getPassword());
            user.setPassword(encodedPassword);
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            return true;
        }catch (Exception e){
            log.error("Error While Creating User {} Message is {}",user.getUserName(),e.getMessage());
            log.debug("Error While Creating User {} Message is {}",user.getUserName(),e.getMessage());
            return false;
        }

    }

    public User  createUser( User user){

        return userRepository.save(user);
    }

    public Optional<User> findUserById(ObjectId id){
        return userRepository.findById(id);
    }
    public User findUserByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    public User updateUser(User user,String userName){
        User userInDb = userRepository.findByUserName(userName);
            userInDb.setUserName(user.getUserName());
            userInDb.setPassword( user.getPassword());

        return userRepository.save(userInDb);
    }

    public void  deleteUserById( ObjectId id){

        userRepository.deleteById(id);
    }
    public void  deleteUserByUserName( ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        userRepository.deleteByUserName(userName);
    }


    public User saveAdmin(User user) {
        String encodedPassword =PASSWORD_ENCODER.encode( user.getPassword());
        user.setPassword(encodedPassword);
        user.setRoles(Arrays.asList("USER","ADMIN"));
        return userRepository.save(user);
    }
}
