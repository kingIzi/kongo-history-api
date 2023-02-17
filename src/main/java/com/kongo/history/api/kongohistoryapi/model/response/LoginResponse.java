package com.kongo.history.api.kongohistoryapi.model.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String email;
    private Long expiresIn;
    private String idToken;
    private String localId;
    private String refreshToken;
}