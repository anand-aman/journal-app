package com.curiodesk.journalapp.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "journal_entries") //mongo-db mapped
public class JournalEntry {

    @Id
    private String id;

    private String title;

    private String content;

    private Date date;

}
