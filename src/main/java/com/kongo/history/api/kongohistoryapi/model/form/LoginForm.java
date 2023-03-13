package com.kongo.history.api.kongohistoryapi.model.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginForm {
    @NotBlank(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "Please enter your password")
    private String password;

    private final boolean returnSecureToken = true;
}