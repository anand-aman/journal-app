package com.curiodesk.journalapp.service;

import com.curiodesk.journalapp.entity.JournalEntry;
import com.curiodesk.journalapp.entity.User;
import com.curiodesk.journalapp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final UserService userService;

    public JournalEntryService(JournalEntryRepository journalEntryRepository,
                               UserService userService) {
        this.journalEntryRepository = journalEntryRepository;
        this.userService = userService;
    }

    public JournalEntry saveEntry(JournalEntry journalEntry) {
//        journalEntry.setId(null);
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

    public void deleteById(ObjectId journalId) {
        journalEntryRepository.deleteById(journalId);
    }

    public List<JournalEntry> getJournalEntryOfUser(final String username) {
        User user = userService.findByUsername(username);
        return user.getJournalEntries();
    }

    @Transactional
    public JournalEntry saveJournalEntryOfUser(String username, JournalEntry journalEntry) {
        User user = userService.findByUsername(username);
        JournalEntry savedJournalEntry = journalEntryRepository.save(journalEntry);
        user.getJournalEntries().add(savedJournalEntry);
        userService.save(user);
        return journalEntryRepository.save(journalEntry);
    }

    @Transactional
    public void deleteJournalEntryOfUser(final String username, final ObjectId journalId) {
        User user = userService.findByUsername(username);
        user.getJournalEntries().removeIf(entry -> entry.getId().equals(journalId));
        userService.save(user);
        journalEntryRepository.deleteById(journalId);
    }

    public JournalEntry updateJournalEntryOfUser(final String username, ObjectId journalId, JournalEntry entry) {
        JournalEntry existingEntry = findById(journalId).orElse(null);
        if (existingEntry != null) {
            existingEntry.setTitle(entry.getTitle());
            existingEntry.setContent(entry.getContent());
            return saveEntry(existingEntry);
        }
        return null;
    }
}
