package com.curiodesk.journalapp.service;

import com.curiodesk.journalapp.entity.JournalEntry;
import com.curiodesk.journalapp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;

    public JournalEntryService(JournalEntryRepository journalEntryRepository) {
        this.journalEntryRepository = journalEntryRepository;
    }

    public JournalEntry saveEntry(JournalEntry journalEntry) {
        return journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAllEntries() {
        return journalEntryRepository.findAll();
    }

    public JournalEntry getEntryById(ObjectId journalId) {
        return journalEntryRepository.findById(journalId).orElse(null);
    }

    public JournalEntry updateEntry(ObjectId journalId, JournalEntry entry) {
        JournalEntry existingEntry = getEntryById(journalId);
        if (existingEntry != null) {
            existingEntry.setTitle(entry.getTitle());
            existingEntry.setContent(entry.getContent());
            return journalEntryRepository.save(existingEntry);
        }
        return null;
    }

    public void deleteEntry(ObjectId journalId) {
        journalEntryRepository.deleteById(journalId);
    }
}
