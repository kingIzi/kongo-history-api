package com.kongo.history.api.kongohistoryapi.model.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class RegisterAdminForm {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String fullName;
    private String phoneNumber;
    @NotBlank
    private String role;
    private final boolean returnSecureToken = true;
}
