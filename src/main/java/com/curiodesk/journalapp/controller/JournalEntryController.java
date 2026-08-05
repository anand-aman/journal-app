package com.curiodesk.journalapp.controller;

import com.curiodesk.journalapp.entity.JournalEntry;
import com.curiodesk.journalapp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;

    public JournalEntryController(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
    }

    @GetMapping
    public List<JournalEntry> getAll() {
        return journalEntryService.getAllEntries();
    }

    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry myEntry) {
        return journalEntryService.saveEntry(myEntry);
    }

    @GetMapping("id/{journal-id}")
    public JournalEntry getEntryById(@PathVariable("journal-id") ObjectId journalId) {
        return journalEntryService.getEntryById(journalId);
    }

    @PutMapping("id/{journal-id}")
    public JournalEntry updateEntry(@PathVariable("journal-id") ObjectId journalId, @RequestBody JournalEntry entry) {
        return journalEntryService.updateEntry(journalId, entry);
    }

    @DeleteMapping("id/{journal-id}")
    public void deleteEntry(@PathVariable("journal-id") ObjectId journalId) {
        journalEntryService.deleteEntry(journalId);
    }
}
