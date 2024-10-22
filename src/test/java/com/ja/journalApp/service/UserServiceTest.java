package com.ja.journalApp.service;

import com.ja.journalApp.UserArgumentsProvider;
import com.ja.journalApp.entity.User;
import com.ja.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Test
    public void testFindByUserName(){
        User user = userRepository.findByUserName("rehan");
        assertNotNull(user);
//        assertTrue(!user.getJournalEntries().isEmpty());
    }
    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    public void testCreateNewUser(User user){

    assertTrue(userService.createNewUser(user));
    }
}