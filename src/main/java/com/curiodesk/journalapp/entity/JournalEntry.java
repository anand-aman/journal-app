package com.curiodesk.journalapp.entity;

import lombok.Data;

@Data
public class JournalEntry {

    private long id;

    private String title;

    private String content;

}
