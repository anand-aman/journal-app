package com.curiodesk.journalapp.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document //mongo-db mapped
public class JournalEntry {

    @Id
    private String id;

    private String title;

    private String content;

}
