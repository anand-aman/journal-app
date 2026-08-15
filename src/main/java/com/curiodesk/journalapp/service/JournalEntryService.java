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
        return journalEntryRepository.save(journalEntry);
    }

    private Optional<JournalEntry> findById(final ObjectId journalId) {
        return journalEntryRepository.findById(journalId);
    }

    public Optional<JournalEntry> findById(final String username,
                                           final ObjectId journalId) {
        User user = userService.findByUsername(username);
        List<JournalEntry> journalEntries = user.getJournalEntries();
        return journalEntries.stream()
                .filter(entry -> entry.getId().equals(journalId))
                .findFirst();
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
    public JournalEntry saveJournalEntryOfUser(final String username,
                                               final JournalEntry journalEntry) {
        User user = userService.findByUsername(username);
        JournalEntry savedJournalEntry = journalEntryRepository.save(journalEntry);
        user.getJournalEntries().add(savedJournalEntry);
        userService.save(user);
        return journalEntryRepository.save(journalEntry);
    }

    @Transactional
    public boolean deleteJournalEntryOfUser(final String username, final ObjectId journalId) {
        boolean removed = false;
        try{
            User user = userService.findByUsername(username);
            removed = user.getJournalEntries().removeIf(entry -> entry.getId().equals(journalId));

            if(removed) {
                userService.save(user);
                journalEntryRepository.deleteById(journalId);
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while deleting journal entry");
        }
        return removed;
    }

    public JournalEntry updateJournalEntryOfUser(final String username, ObjectId journalId, JournalEntry entry) {
        User  user = userService.findByUsername(username);
        boolean isUserJournal =  user.getJournalEntries().stream().anyMatch(journalEntry -> journalEntry.getId().equals(journalId));
        if (isUserJournal) {
            JournalEntry existingEntry = findById(journalId).orElse(null);
            if (existingEntry != null) {
                existingEntry.setTitle(entry.getTitle());
                existingEntry.setContent(entry.getContent());
                return saveEntry(existingEntry);
            }
        }
        return null;
    }
}
