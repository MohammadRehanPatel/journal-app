package com.ja.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailServiceTest {
    @Autowired
    private EmailService emailService;

    @Test
    public void testMail(){
        emailService.sendEmail("mohammadrehanpatel.007@gmail.com","Testing","Hii This is Test");
    }

}