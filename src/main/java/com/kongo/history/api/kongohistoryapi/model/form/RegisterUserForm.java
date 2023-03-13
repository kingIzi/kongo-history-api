package com.kongo.history.api.kongohistoryapi.model.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class RegisterUserForm {
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    private String password;
    @NotBlank(message = "Please provide a name")
    private String fullName;
    private String phoneNumber;
    @NotBlank(message = "Please provide a user role")
    private String role;
    private final boolean returnSecureToken = true;
}
