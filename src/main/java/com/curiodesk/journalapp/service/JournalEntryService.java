package com.curiodesk.journalapp.service;

import com.curiodesk.journalapp.repository.JournalEntryRepository;

public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;

    public JournalEntryService(JournalEntryRepository journalEntryRepository) {
        this.journalEntryRepository = journalEntryRepository;
    }
}
