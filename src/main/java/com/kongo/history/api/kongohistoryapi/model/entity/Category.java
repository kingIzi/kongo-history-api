package com.kongo.history.api.kongohistoryapi.model.entity;

import java.util.Date;

import javax.validation.constraints.Pattern;

import com.kongo.history.api.kongohistoryapi.model.entity.BaseEntity;
import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
import com.google.cloud.firestore.annotation.DocumentId;

import lombok.Data;

@Data
public class Category extends BaseEntity {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String THUMBNAIL_URL = "thumbnailUrl";
    public static final String THUMBNAIL_FILENAME = "thumbnailFileName";
    public static final String COLOR = "color";
    public static final String STATUS = "status";
    public static final String DATE_CREATED = "dateCreated";
    public static final String DATE_UPDATED = "dateUpdated";

    @DocumentId
    private String id;
    private String name;
    private String description;
    @Pattern(regexp = "ON|OFF")
    private String status = "ON";
    private Date dateCreated;
    private Date dateUpdated;
    private String thumbnailUrl;
    private String thumbnailFileName;
    private String color;

    public Category(final String name, final String description, final String color, final String thumbnailUrl,
            final String thumbnailFileName) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.thumbnailUrl = thumbnailUrl;
        this.thumbnailFileName = thumbnailFileName;
        this.dateCreated = new Date();
        this.dateUpdated = new Date();
    }

    public Category() {
    }
}
