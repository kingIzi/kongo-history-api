package com.kongo.history.api.kongohistoryapi.model.entity;

import com.kongo.history.api.kongohistoryapi.utils.DocumentId;
import java.util.List;

import lombok.Data;

@Data
public class Comic {
    @DocumentId
    private String id;
    private String title;
    private String dateReleased;
    private String description;
    private List<String> categories;
    private Author author;
    private Integer views;
    private boolean liked;
    private String dateCreated;
    private String dataUpdated;
    private String thumbnailUrl;
    private String thumbnailFileName;
    private String dataUrl;
    private String dataFileName;
}
