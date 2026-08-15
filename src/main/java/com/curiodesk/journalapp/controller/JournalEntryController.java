package com.curiodesk.journalapp.controller;

import com.curiodesk.journalapp.entity.JournalEntry;
import com.curiodesk.journalapp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PostMapping
    public ResponseEntity<JournalEntry> createJournalEntryOfUser(@RequestBody JournalEntry payload) {
        try {
            JournalEntry journalEntry = journalEntryService.saveJournalEntryOfUser(getCurrentUsername(), payload);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(journalEntry);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser() {
        List<JournalEntry> journalEntries = journalEntryService.getJournalEntryOfUser(getCurrentUsername());
        if (journalEntries != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(journalEntries);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/id/{journalId}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId journalId) {
        Optional<JournalEntry> journalEntry = journalEntryService.findById(getCurrentUsername(), journalId);
        return journalEntry.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{journalId}")
    public ResponseEntity<Void> deleteJournalEntryOfUser(@PathVariable ObjectId journalId) {
        boolean isDeleted = journalEntryService.deleteJournalEntryOfUser(getCurrentUsername(), journalId);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{journalId}")
    public ResponseEntity<JournalEntry> updateJournalEntryOfUser(@PathVariable ObjectId journalId,
                                                                 @RequestBody JournalEntry entry) {
        return Optional.ofNullable(journalEntryService.updateJournalEntryOfUser(getCurrentUsername(), journalId, entry))
                .map(updatedEntry -> ResponseEntity.status(HttpStatus.OK).body(updatedEntry))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Deprecated
    @PutMapping("/id/{journalId}")
    public ResponseEntity<JournalEntry> updateEntry(@PathVariable ObjectId journalId,
                                                    @RequestBody JournalEntry entry) {
        return Optional.ofNullable(journalEntryService.updateEntry(journalId, entry))
                .map(updatedEntry -> ResponseEntity.status(HttpStatus.OK).body(updatedEntry))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Deprecated
    @DeleteMapping("/id/{journalId}")
    public ResponseEntity<Void> deleteEntry(@PathVariable ObjectId journalId) {
        journalEntryService.deleteById(journalId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
