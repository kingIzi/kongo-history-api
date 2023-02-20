package com.kongo.history.api.kongohistoryapi.model.form;

import lombok.Data;

@Data
public class FindAuthorForm {
    private String id;
    private String firstName;
    private String lastName;
    private String status = "ON";
}
