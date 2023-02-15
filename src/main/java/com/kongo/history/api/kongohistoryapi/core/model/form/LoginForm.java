package com.kongo.history.api.kongohistoryapi.core.model.form;


import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class LoginForm {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private final boolean returnSecureToken = true;
}
