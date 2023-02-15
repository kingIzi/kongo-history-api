package com.kongo.history.api.kongohistoryapi.core.model.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String email;
    private String expiresIn;
    private String idToken;
    private String localId;
    private String refreshToken;
    private String kind;
    private boolean registered;

    private String displayName;
}
