package com.ja.journalApp.controller;

import com.ja.journalApp.entity.JournalEntry;
import com.ja.journalApp.entity.User;
import com.ja.journalApp.repository.JournalRepository;
import com.ja.journalApp.service.JournalService;
import com.ja.journalApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
@Tag(name = "Journal APIs")
public class JournalEntryController {

    private final JournalService journalService;
    private final UserService userService;
    private final JournalRepository journalRepository;

    public JournalEntryController(JournalService journalService, UserService userService, JournalRepository journalRepository) {
        this.journalService = journalService;
        this.userService = userService;
        this.journalRepository = journalRepository;
    }

    @GetMapping
    @Operation(summary = "Get All Journal Entries of a User")
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName();
        User user = userService.findUserByUserName(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if(all!=null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntry>  createEntry(@RequestBody JournalEntry journalEntry){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

            User user = userService.findUserByUserName(userName);
            journalEntry.setId(new ObjectId());
                journalService.createEntry(journalEntry,user);
            return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> findEntryById(@PathVariable ObjectId id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findUserByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalService.findEntryById(id);
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?>   deleteEntryById(@PathVariable ObjectId id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed = journalService.deleteEntryById(id, userName);
        if(removed){

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntry>  updateEntryById(@PathVariable ObjectId id,
                                                         @RequestBody JournalEntry newEntry){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User user = userService.findUserByUserName(userName);
            List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());

            if(!collect.isEmpty()){
                Optional<JournalEntry> journalEntry =  journalRepository.findById(id);
                if(journalEntry.isPresent()){
                    JournalEntry old = journalEntry.get();
                    old.setTitle(newEntry.getTitle()!=null && !newEntry.getTitle().equals("")?newEntry.getTitle():old.getTitle());
                    old.setContent(newEntry.getContent()!= null && !newEntry.getContent().equals("")? newEntry.getContent(): old.getContent());
                    return new ResponseEntity<>( journalService.updateEntryById(id,old,userName),HttpStatus.OK);
                }
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }


}
