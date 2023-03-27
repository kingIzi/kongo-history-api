package com.kongo.history.api.kongohistoryapi.model.form;

import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class FindUserForm {
    private String localId;
    private String email;
    @Pattern(regexp = "ON|OFF")
    private String status = "ON";
    private String role;
}
