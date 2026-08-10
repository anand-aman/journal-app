package com.curiodesk.journalapp.controller;

import com.curiodesk.journalapp.entity.JournalEntry;
import com.curiodesk.journalapp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/journal_entries")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;

    public JournalEntryController(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
    }

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(journalEntryService.getAllEntries());
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(journalEntryService.saveEntry(myEntry));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/id/{journalId}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId journalId) {
        Optional<JournalEntry> journalEntry = journalEntryService.findById(journalId);
        return journalEntry.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/id/{journalId}")
    public ResponseEntity<JournalEntry> updateEntry(@PathVariable ObjectId journalId,
                                                    @RequestBody JournalEntry entry) {
        return Optional.ofNullable(journalEntryService.updateEntry(journalId, entry))
                .map(updatedEntry -> ResponseEntity.status(HttpStatus.OK).body(updatedEntry))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/id/{journalId}")
    public ResponseEntity<Void> deleteEntry(@PathVariable ObjectId journalId) {
        journalEntryService.deleteById(journalId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser(@PathVariable String username) {
        List<JournalEntry> journalEntries = journalEntryService.getJournalEntryOfUser(username);
        if (journalEntries != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(journalEntries);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/{username}")
    public ResponseEntity<JournalEntry> createJournalEntryOfUser(@PathVariable String username,
                                                                 @RequestBody JournalEntry payload) {
        try {
            JournalEntry journalEntry = journalEntryService.createJournalEntryOfUser(username, payload);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(journalEntry);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{username}/{journalId}")
    public ResponseEntity<Void> deleteJournalEntryOfUser(@PathVariable String username,
                                                         @PathVariable ObjectId journalId) {
        journalEntryService.deleteJournalEntryOfUser(username, journalId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{username}/{journalId}")
    public ResponseEntity<JournalEntry> updateJournalEntryOfUser(@PathVariable String username,
                                                                 @PathVariable ObjectId journalId,
                                                                 @RequestBody JournalEntry entry) {
        return Optional.ofNullable(journalEntryService.updateJournalEntryOfUser(username, journalId, entry))
                .map(updatedEntry -> ResponseEntity.status(HttpStatus.OK).body(updatedEntry))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
