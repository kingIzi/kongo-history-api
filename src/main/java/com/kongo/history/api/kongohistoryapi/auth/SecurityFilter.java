package com.kongo.history.api.kongohistoryapi.auth;

import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import com.kongo.history.api.kongohistoryapi.auth.model.SecurityProperties;
import com.kongo.history.api.kongohistoryapi.auth.model.Credentials;
import com.kongo.history.api.kongohistoryapi.auth.model.User;
import com.kongo.history.api.kongohistoryapi.util.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private SecurityService securityService;
    @Autowired
    SecurityProperties restSecProps;
    @Autowired
    CookieUtils cookieUtils;
    @Autowired
    SecurityProperties securityProps;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        this.verifyToken(request);
        filterChain.doFilter(request, response);
    }

    private void verifyToken(HttpServletRequest request){
        String session = null;
        FirebaseToken decodedToken = null;
        Credentials.CredentialType type = null;
        boolean strictServerSessionEnabled = this.securityProps.getFirebaseProps().isEnableStrictServerSession();
        Cookie sessionCookie = this.cookieUtils.getCookie("session");
        String token = this.securityService.getBearerToken(request);
        this.logger.info(token);
        try {
            if (sessionCookie != null) {
                session = sessionCookie.getValue();
                decodedToken = FirebaseAuth.getInstance().verifySessionCookie(session,
                        securityProps.getFirebaseProps().isEnableCheckSessionRevoked());
                type = Credentials.CredentialType.SESSION;
            } else if (!strictServerSessionEnabled) {
                if (token != null && !token.equalsIgnoreCase("undefined")) {
                    decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                    type = Credentials.CredentialType.ID_TOKEN;
                }
            }
            this.transformFirebaseTokenToDto(decodedToken,type,token,session,request);
        }
        catch (FirebaseAuthException e) {
            e.printStackTrace();
            log.error("Firebase Exception:: ", e.getLocalizedMessage());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void transformFirebaseTokenToDto(FirebaseToken decodedToken,Credentials.CredentialType type,String token,String session,HttpServletRequest request) throws Exception{
        User user = this.firebaseTokenToUserDto(decodedToken);
        if (user != null)
            throw new Exception("Failed to transform FirebaseToken to User DTO");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                new Credentials(type, decodedToken, token, session), null);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private User firebaseTokenToUserDto(FirebaseToken decodedToken) {
        User user = null;
        if (decodedToken != null) {
            user = new User();
            user.setUid(decodedToken.getUid());
            user.setName(decodedToken.getName());
            user.setEmail(decodedToken.getEmail());
            user.setPicture(decodedToken.getPicture());
            user.setIssuer(decodedToken.getIssuer());
            user.setEmailVerified(decodedToken.isEmailVerified());
        }
        return user;
    }
}
