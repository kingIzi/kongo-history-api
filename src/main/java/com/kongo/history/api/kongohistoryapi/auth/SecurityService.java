package com.kongo.history.api.kongohistoryapi.auth;

import com.kongo.history.api.kongohistoryapi.auth.model.Credentials;
import com.kongo.history.api.kongohistoryapi.auth.model.SecurityProperties;
import com.kongo.history.api.kongohistoryapi.auth.model.User;
import com.kongo.history.api.kongohistoryapi.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;


@Service
public class SecurityService {
    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    CookieUtils cookieUtils;

    @Autowired
    SecurityProperties securityProperties;

    public User getUser() {
        final var securityContext = SecurityContextHolder.getContext();
        final var principal = securityContext.getAuthentication().getPrincipal();
        if (principal instanceof User)
            return  ((User) principal);
        return new User();
    }

    public Credentials getCredentials() {
        final var securityContext = SecurityContextHolder.getContext();
        return (Credentials) securityContext.getAuthentication().getCredentials();
    }

    public boolean isPublic() {
        return this.securityProperties.getAllowedPublicApis().contains(httpServletRequest.getRequestURI());
    }

    public String getBearerToken(HttpServletRequest request) {
        String bearerToken = null;
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer "))
            return authorization.substring(7);
        return "";
    }


}
