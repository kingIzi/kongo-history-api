package com.kongo.history.api.kongohistoryapi.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Pattern;

import com.kongo.history.api.kongohistoryapi.model.entity.BaseEntity;
import com.kongo.history.api.kongohistoryapi.utils.DocumentId;

import lombok.Data;

@Data
public class User extends BaseEntity {
    public static final String ID = "id";
    public static final String FULL_NAME = "fullName";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String UID = "uid";
    public static final String FAVORITES = "favorites";

    @DocumentId
    private String id;
    private String fullName;
    private String phoneNumber;
    private String localId;
    private String email;
    private List<String> favorites;
    @Pattern(regexp = "ON|OFF")
    private String status = "ON";

    public User(final String fullName, final String phoneNumber, final String email, final String localId) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.localId = localId;
        this.favorites = new ArrayList<>();
        this.email = email;
    }

    public User() {
    }
}
