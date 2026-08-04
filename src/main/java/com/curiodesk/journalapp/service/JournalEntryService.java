package com.curiodesk.journalapp.service;

import com.curiodesk.journalapp.entity.JournalEntry;
import com.curiodesk.journalapp.repository.JournalEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;

    public JournalEntryService(JournalEntryRepository journalEntryRepository) {
        this.journalEntryRepository = journalEntryRepository;
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAllEntries() {
        return journalEntryRepository.findAll();
    }
}
