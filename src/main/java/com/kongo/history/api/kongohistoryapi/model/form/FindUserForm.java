package com.kongo.history.api.kongohistoryapi.model.form;

import lombok.Data;

@Data
public class FindUserForm {
    private String localId;
    private String email;
    private String status;
}
