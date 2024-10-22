package com.ja.journalApp.service;

import com.ja.journalApp.entity.JournalEntry;
import com.ja.journalApp.entity.User;
import com.ja.journalApp.repository.JournalRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class JournalService {

     private final JournalRepository journalRepository;
     private final UserService userService;

//     private static final  Logger logger = LoggerFactory.getLogger(JournalService.class);


    public JournalService(JournalRepository journalRepository, UserService userService) {
        this.journalRepository = journalRepository;
        this.userService = userService;
    }

    public List<JournalEntry> getAllJournalEntriesOfUser(){
        return journalRepository.findAll();
    }

//    @Transactional
    public void   createEntry(JournalEntry journalEntry, User user){
       try {
           String userName = user.getUserName();
           journalEntry.setDate(LocalDateTime.now());
           JournalEntry saved = journalRepository.save(journalEntry);
           user.getJournalEntries().add(saved);
           userService.createUser(user);
       }
       catch (Exception e){
           log.error("Error While Saving Entry");
           throw new RuntimeException("An Error occured while saving the entry : ",e);
       }
    }
    public void   createEntry(JournalEntry journalEntry){
        journalEntry.setDate(LocalDateTime.now());
         journalRepository.save(journalEntry);

    }

    public Optional<JournalEntry> findEntryById(ObjectId id){
        return journalRepository.findById(id);
    }

    public boolean  deleteEntryById(ObjectId id, String userName){
        boolean removed=false;
       try {
           User user = userService.findUserByUserName(userName);
           removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
           if(removed){
               userService.createUser(user);
               journalRepository.deleteById(id);
           }
       }catch (Exception e){
           log.error("Error {} " ,e.getMessage());
           throw new RuntimeException("An Error occurred while deleting the entry");
       }
        return removed;
    }

    public JournalEntry  updateEntryById(ObjectId id, JournalEntry newEntry, String userName){
        User user = userService.findUserByUserName(userName);


        return journalRepository.save(newEntry);
    }

//    public List<JournalEntry> findByUserName(String username){
//
//    }


}
