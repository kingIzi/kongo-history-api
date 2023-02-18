package com.kongo.history.api.kongohistoryapi.model.entity;

import java.util.Date;

import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
import com.kongo.history.api.kongohistoryapi.utils.DocumentId;

import lombok.Data;


@Data
public class Author {
    @DocumentId
    private String id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private String phoneNumber;

    public Author(final String firstName,final String lastName,final Date dateOfBirth,final String address,final String phoneNumber){
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = AppUtilities.convertDateToString(dateOfBirth);
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Author(){}
}
