package com.kongo.history.api.kongohistoryapi.model.entity;

import com.kongo.history.api.kongohistoryapi.utils.DocumentId;

public class Comic {
    @DocumentId
    private String id;
    private String username;
    private String password;
    private String imageUrl;
    private String bio;
}
