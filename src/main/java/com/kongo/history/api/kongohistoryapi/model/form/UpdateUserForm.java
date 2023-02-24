package com.kongo.history.api.kongohistoryapi.model.form;

import java.util.List;
import lombok.Data;

@Data
public class UpdateUserForm {
    private String email;
    private String phoneNumber;
    private List<String> favorites;
    private String photoFileName;
    private String photoUrl;
}
