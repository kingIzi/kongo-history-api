package com.kongo.history.api.kongohistoryapi.controller;

//app imports
import com.kongo.history.api.kongohistoryapi.auth.models.User;
import com.kongo.history.api.kongohistoryapi.model.form.LoginForm;
import com.kongo.history.api.kongohistoryapi.model.form.RegisterUserForm;
import com.kongo.history.api.kongohistoryapi.model.response.LoginResponse;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;
import com.kongo.history.api.kongohistoryapi.service.SessionService;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.validation.BindingResult;

@RestController
@RequestMapping("/session")
public class SessionResource {

    @Autowired
    private SessionService sessionService;

    @PostMapping("/login")
    public HttpDataResponse<LoginResponse> loginUser(@Valid @RequestBody LoginForm loginForm,
            BindingResult bindingResult) {
        return this.sessionService.login(loginForm, bindingResult);
    }

    @PostMapping("/register")
    public HttpDataResponse<LoginResponse> registerUser(@Valid @RequestBody RegisterUserForm registerForm,
            BindingResult bindingResult) {
        return this.sessionService.register(registerForm, bindingResult);
    }
}