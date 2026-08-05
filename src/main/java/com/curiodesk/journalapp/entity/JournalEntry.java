package com.curiodesk.journalapp.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "journal_entries") //mongo-db mapped
public class JournalEntry {

    @Id
    private ObjectId id;

    private String title;

    private String content;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant updatedDate;

}
