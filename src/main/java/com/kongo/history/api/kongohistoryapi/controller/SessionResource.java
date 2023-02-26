package com.kongo.history.api.kongohistoryapi.controller;

//app imports
import com.kongo.history.api.kongohistoryapi.auth.models.User;
import com.kongo.history.api.kongohistoryapi.model.form.LoginForm;
import com.kongo.history.api.kongohistoryapi.model.form.RegisterForm;
import com.kongo.history.api.kongohistoryapi.model.response.LoginResponse;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;
import com.kongo.history.api.kongohistoryapi.service.SessionService;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session")
public class SessionResource {

    @Autowired
    private SessionService sessionService;

    @PostMapping("/login")
    public HttpDataResponse<LoginResponse> loginUser(@Valid @RequestBody LoginForm loginForm) {
        return this.sessionService.login(loginForm);
    }

    @PostMapping("/register")
    public HttpDataResponse<LoginResponse> registerUser(@Valid @RequestBody RegisterForm registerForm) {
        return this.sessionService.register(registerForm);
    }
}