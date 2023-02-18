package com.kongo.history.api.kongohistoryapi.model.entity;

import com.kongo.history.api.kongohistoryapi.utils.DocumentId;

public class Comic {
    @DocumentId
    private String id;
    private String name;
    private String released;
    private String description;
    private String thumbnailPath;
    private String comicPath;
    private Author author;
    private Integer views;
    private boolean liked;
}
