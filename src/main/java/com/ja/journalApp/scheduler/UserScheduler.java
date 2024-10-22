package com.ja.journalApp.scheduler;

import com.ja.journalApp.cache.AppCache;
import com.ja.journalApp.entity.JournalEntry;
import com.ja.journalApp.entity.User;
import com.ja.journalApp.enums.Sentiment;
import com.ja.journalApp.repository.UserRepositoryImpl;
import com.ja.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    private final EmailService emailService;
    private final UserRepositoryImpl userRepository;
    private final AppCache appCache;

    public UserScheduler(EmailService emailService, UserRepositoryImpl userRepository, AppCache appCache) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.appCache = appCache;
    }

    //    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUserAndSendSaMail(){
        List<User> users = userRepository.getUserForSA();
        for (User user : users){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream()
                    .filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(x->x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment,Integer> sentimentCounts = new HashMap<>();
            for(Sentiment sentiment : sentiments){
                if(sentiment!=null){
                    sentimentCounts.put(sentiment,sentimentCounts.getOrDefault(sentiment,0)+1);
                }
            }
            Sentiment mostFrequesntSentiment = null;
            int maxCount =0;
            for(Map.Entry<Sentiment,Integer> entry: sentimentCounts.entrySet()){
                if(entry.getValue()>maxCount){
                    maxCount = entry.getValue();
                    mostFrequesntSentiment = entry.getKey();
                }
            }
            if(mostFrequesntSentiment!=null){
                emailService.sendEmail(user.getEmail(),"Sentiment for last 7 days",mostFrequesntSentiment.toString());
            }
        }
    }
    @Scheduled(cron = "0 0/10 0 ? * *")
    public void clearAppCache(){
     appCache.init();
    }

}
