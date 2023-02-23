package com.kongo.history.api.kongohistoryapi.model.form;

import javax.validation.constraints.NotBlank;

public class RegisterForm {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String fullName;
    @NotBlank
    private String phoneNumber;
    private final boolean returnSecureToken = true;
}
