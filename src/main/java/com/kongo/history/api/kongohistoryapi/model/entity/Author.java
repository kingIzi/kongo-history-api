package com.kongo.history.api.kongohistoryapi.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Pattern;

import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
import com.kongo.history.api.kongohistoryapi.utils.DocumentId;

import lombok.Data;

@Data
public class Author extends BaseEntity {
    public static final String ID = "id";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String DATE_OF_BIRTH = "dateOfBirth";
    public static final String ADDRESS = "address";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String STATUS = "status";
    public static final String PHOTO_URL = "photoUrl";
    public static final String PHOTO_FILENAME = "photoFileName";
    public static final String DATE_CREATED = "dateCreated";
    public static final String DATE_UPDATED = "dateUpdated";

    @DocumentId
    private String id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private String phoneNumber;
    // @Pattern(regexp = "ON|OFF")
    @Pattern(regexp = "ON|OFF")
    private String status = "ON";
    private String photoUrl;
    private String photoFileName;
    private Date dateCreated;
    private Date dataUpdated;

    public Author(final String firstName, final String lastName, final Date dateOfBirth, final String address,
            final String phoneNumber, final String photoUrl, final String photoFileName, final Date dateCreated,
            final Date dateUpdated) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.photoUrl = photoUrl;
        this.photoFileName = photoFileName;
        this.dateCreated = dateCreated;
        this.dataUpdated = dateUpdated;
    }

    public Author() {
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public final String getId() {
        return this.id;
    }
}
