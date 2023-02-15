package com.kongo.history.api.kongohistoryapi.auth.model;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@ConfigurationProperties("security")
@Data
public class SecurityProperties {
    private CookieProperties cookieProperties;
    private FirebaseProperties firebaseProps;
    boolean allowCredentials;
    List<String> allowedOrigins;
    List<String> allowedHeaders;
    List<String> exposedHeaders;
    List<String> allowedMethods;
    List<String> allowedPublicApis;
}
