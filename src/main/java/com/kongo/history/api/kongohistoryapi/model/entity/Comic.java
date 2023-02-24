package com.kongo.history.api.kongohistoryapi.model.entity;

import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
import com.kongo.history.api.kongohistoryapi.utils.DocumentId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class Comic extends BaseEntity {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DATE_RELEASE = "dateReleased";
    public static final String DESCRIPTION = "description";
    public static final String CATEGORIES = "categories";
    public static final String AUTHOR_ID = "authorId";
    public static final String VIEWERS = "viewers";
    public static final String LIKERS = "likers";
    public static final String COMMENTS = "comments";
    public static final String DATE_CREATED = "dateCreated";
    public static final String DATE_UPDATED = "dataUpdated";
    public static final String THUMBNAIL_URL = "thumbnailUrl";
    public static final String THUMBNAIL_FILENAME = "thumbnailFileName";
    public static final String DATA_URL = "dataUrl";
    public static final String DATA_FILENAME = "dataFileName";
    public static final String STATUS = "status";

    @DocumentId
    private String id;
    private String name;
    private String dateReleased;
    private String description;
    private List<String> categories;
    private String authorId;
    private List<String> viewers;
    private List<String> likers;
    private List<Comment> comments;
    private String dateCreated;
    private String dataUpdated;
    private String thumbnailUrl;
    private String thumbnailFileName;
    private String dataUrl;
    private String dataFileName;
    @Pattern(regexp = "ON|OFF")
    private String status;

    public Comic(){}

    public Comic(final String name, final Date dateReleased, final String description,final String authorId,final List<String> categories){
        this.name = name;
        this.dateReleased = AppUtilities.convertDateToString(dateReleased);
        this.description = description;
        this.authorId = authorId;
        this.categories = categories;
        this.dateCreated = AppUtilities.convertDateToString(new Date());
        this.dataUpdated = AppUtilities.convertDateToString(new Date());
        this.status = "ON";
        this.viewers = new ArrayList<>();
        this.likers = new ArrayList<>();
        this.comments = new ArrayList<>();
    }
}
