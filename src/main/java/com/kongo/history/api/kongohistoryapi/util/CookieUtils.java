package com.kongo.history.api.kongohistoryapi.util;


import com.kongo.history.api.kongohistoryapi.auth.model.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class CookieUtils{
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private SecurityProperties securityProperties;

    public Cookie getCookie(String name) {
        return WebUtils.getCookie(this.httpServletRequest, name);
    }

    public void setCookie(String name, String value, int expiryInMinutes) {
        int expiresInSeconds = expiryInMinutes * 60 * 60;
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(this.securityProperties.getCookieProperties().isSecure());
        cookie.setPath(this.securityProperties.getCookieProperties().getPath());
        cookie.setDomain(this.securityProperties.getCookieProperties().getDomain());
        cookie.setMaxAge(expiresInSeconds);
        this.httpServletResponse.addCookie(cookie);
    }

    public void setSecureCookie(String name, String value, int expiryInMinutes) {
        int expiresInSeconds = expiryInMinutes * 60 * 60;
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(this.securityProperties.getCookieProperties().isHttpOnly());
        cookie.setSecure(this.securityProperties.getCookieProperties().isSecure());
        cookie.setPath(this.securityProperties.getCookieProperties().getPath());
        cookie.setDomain(this.securityProperties.getCookieProperties().getDomain());
        cookie.setMaxAge(expiresInSeconds);
        this.httpServletResponse.addCookie(cookie);
    }

    public void setSecureCookie(String name, String value) {
        int expiresInMinutes = this.securityProperties.getCookieProperties().getMaxAgeInMinutes();
        this.setSecureCookie(name, value, expiresInMinutes);
    }

    public void deleteSecureCookie(String name) {
        int expiresInSeconds = 0;
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(this.securityProperties.getCookieProperties().isHttpOnly());
        cookie.setSecure(this.securityProperties.getCookieProperties().isSecure());
        cookie.setPath(this.securityProperties.getCookieProperties().getPath());
        cookie.setDomain(this.securityProperties.getCookieProperties().getDomain());
        cookie.setMaxAge(expiresInSeconds);
        this.httpServletResponse.addCookie(cookie);
    }

    public void deleteCookie(String name) {
        int expiresInSeconds = 0;
        Cookie cookie = new Cookie(name, null);
        cookie.setPath(this.securityProperties.getCookieProperties().getPath());
        cookie.setDomain(this.securityProperties.getCookieProperties().getDomain());
        cookie.setMaxAge(expiresInSeconds);
        this.httpServletResponse.addCookie(cookie);
    }
}