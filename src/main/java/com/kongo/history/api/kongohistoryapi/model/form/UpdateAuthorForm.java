package com.kongo.history.api.kongohistoryapi.model.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateAuthorForm {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private String phoneNumber;
    private String photoUrl;
    private String photoFileName;
    private String status;
}
