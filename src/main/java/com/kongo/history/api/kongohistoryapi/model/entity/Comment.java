package com.kongo.history.api.kongohistoryapi.model.entity;

import com.google.api.client.util.Objects;
import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
import com.kongo.history.api.kongohistoryapi.utils.DocumentId;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class Comment {
    private String email;
    private String message;
    private List<String> likes;
    private List<Comment> replies;

    private String dateCreated;
    private String dateUpdated;

    public Comment(final String email, final String message) {
        this.email = email;
        this.message = message;
        this.dateCreated = AppUtilities.convertDateToString(new Date());
        this.dateUpdated = AppUtilities.convertDateToString(new Date());
    }

    public Comment() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Comment)
            return Objects.equal(this.email, ((Comment) obj).getEmail())
                    && Objects.equal(this.message, ((Comment) obj).getMessage());
        return false;
    }
}
