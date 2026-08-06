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
                    .body(journalEntryService.createEntry(myEntry));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{journalId}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId journalId) {
        Optional<JournalEntry> journalEntry = journalEntryService.findById(journalId);
        return journalEntry.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{journalId}")
    public ResponseEntity<JournalEntry> updateEntry(@PathVariable ObjectId journalId,
                                                    @RequestBody JournalEntry entry) {
        return Optional.ofNullable(journalEntryService.updateEntry(journalId, entry))
                .map(updatedEntry -> ResponseEntity.status(HttpStatus.OK).body(updatedEntry))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{journalId}")
    public ResponseEntity<Void> deleteEntry(@PathVariable ObjectId journalId) {
        journalEntryService.deleteEntry(journalId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
