package com.curiodesk.journalapp.service;

import com.curiodesk.journalapp.entity.JournalEntry;
import com.curiodesk.journalapp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;

    public JournalEntryService(JournalEntryRepository journalEntryRepository) {
        this.journalEntryRepository = journalEntryRepository;
    }

    public JournalEntry createEntry(JournalEntry journalEntry) {
        journalEntry.setId(null);
        return journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAllEntries() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId journalId) {
        return journalEntryRepository.findById(journalId);
    }

    public JournalEntry updateEntry(ObjectId journalId, JournalEntry entry) {
        JournalEntry existingEntry = findById(journalId).orElse(null);
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
