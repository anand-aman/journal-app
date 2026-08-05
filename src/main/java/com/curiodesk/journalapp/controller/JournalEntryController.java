package com.curiodesk.journalapp.controller;

import com.curiodesk.journalapp.entity.JournalEntry;
import com.curiodesk.journalapp.service.JournalEntryService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
