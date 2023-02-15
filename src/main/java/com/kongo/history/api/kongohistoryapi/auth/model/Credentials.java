package com.kongo.history.api.kongohistoryapi.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.google.firebase.auth.FirebaseToken;

@Data
@AllArgsConstructor
public class Credentials {
    public enum CredentialType {
        ID_TOKEN, SESSION
    }

    private CredentialType type;
    private FirebaseToken decodedToken;
    private String idToken;
    private String session;
}
