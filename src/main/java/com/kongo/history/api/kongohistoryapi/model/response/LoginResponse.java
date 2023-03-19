package com.kongo.history.api.kongohistoryapi.model.response;

import org.springframework.web.bind.annotation.ResponseBody;

import lombok.Data;

@Data
@ResponseBody
public class LoginResponse {
    private String email;
    private Long expiresIn;
    private String idToken;
    private String localId;
    private String refreshToken;
}