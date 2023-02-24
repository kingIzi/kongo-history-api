package com.kongo.history.api.kongohistoryapi.model.entity;

import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
import com.kongo.history.api.kongohistoryapi.utils.DocumentId;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Comment {
    private String email;
    private String message;
    private List<String> likes;
    private List<Comment> replies;

    private String dateCreated;
    private String dateUpdated;

    public Comment(final String email,final String message){
        this.email = email;
        this.message = message;
        this.dateCreated = AppUtilities.convertDateToString(new Date());
        this.dateUpdated = AppUtilities.convertDateToString(new Date());
    }
}
