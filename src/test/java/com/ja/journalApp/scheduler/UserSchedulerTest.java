package com.ja.journalApp.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserSchedulerTest {


    @Autowired
    private UserScheduler userScheduler;
    @Test
    public void testFetchUserAndSaMail(){
        userScheduler.fetchUserAndSendSaMail();
    }
}