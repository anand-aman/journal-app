package com.curiodesk.journalapp.controller;

import com.curiodesk.journalapp.entity.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    private Map<Long, JournalEntry> journalEntries = new HashMap<>();

    @GetMapping
    public List<JournalEntry> getAll() {
        return journalEntries.values().stream().toList();
    }

    @PostMapping
    public void createEntry(@RequestBody JournalEntry myEntry) {
        journalEntries.put(myEntry.getId(), myEntry);
    }
}
