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


    @DocumentId
    private String id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private String phoneNumber;
    //@Pattern(regexp = "ON|OFF")
    @Pattern(regexp = "ON|OFF")
    private String status = "ON";
    private String photoUrl;
    private String photoFileName;

    public Author(final String firstName,final String lastName,final Date dateOfBirth,final String address,final String phoneNumber,final String photoUrl,final String photoFileName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = AppUtilities.convertDateToString(dateOfBirth);
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.photoUrl = photoUrl;
        this.photoFileName = photoFileName;
    }

    public Author(){}

    @Override
    public void setId(final String id){
        this.id = id;
    }

    @Override
    public final String getId(){
        return this.id;
    }
}
