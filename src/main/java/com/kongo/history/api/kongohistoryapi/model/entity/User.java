package com.kongo.history.api.kongohistoryapi.model.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Pattern;

import com.kongo.history.api.kongohistoryapi.model.entity.BaseEntity;
import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
import com.kongo.history.api.kongohistoryapi.utils.DocumentId;

import lombok.Data;

@Data
public class User extends BaseEntity {
    public static final String ID = "id";
    public static final String FULL_NAME = "fullName";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String LOCAL_ID = "localId";
    public static final String FAVORITES = "favorites";
    public static final String EMAIL = "email";
    public static final String PHOTO_FILENAME = "photoFileName";
    public static final String PHOTO_URL = "photoUrl";
    public static final String DATE_CREATED = "dateCreated";
    public static final String DATE_UPDATED = "dateUpdated";
    public static final String STATUS = "status";

    @DocumentId
    private String id;
    private String fullName;
    private String phoneNumber;
    private String localId;
    private String email;
    private String photoFileName;
    private String photoUrl;
    private List<String> favorites;
    private String dateCreated;
    private String dateUpdated;
    private String role;
    @Pattern(regexp = "ON|OFF")
    private String status = "ON";

    public User(final String fullName, final String phoneNumber, final String email, final String localId,
            final String role,
            final Date dateCreaded, final Date dateUpdated) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.localId = localId;
        this.favorites = new ArrayList<>();
        this.email = email;
        this.role = role;
        this.dateCreated = AppUtilities.convertDateToString(dateCreaded);
        this.dateUpdated = AppUtilities.convertDateToString(dateUpdated);
    }

    public User() {
    }
}
