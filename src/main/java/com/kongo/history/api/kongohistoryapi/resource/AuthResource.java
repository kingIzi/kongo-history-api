package com.kongo.history.api.kongohistoryapi.resource;


import com.kongo.history.api.kongohistoryapi.core.model.form.LoginForm;
import com.kongo.history.api.kongohistoryapi.core.model.response.LoginResponse;
import com.kongo.history.api.kongohistoryapi.service.AuthService;
import com.kongo.history.api.kongohistoryapi.util.HttpDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/session")
public class AuthResource {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public HttpDataResponse<LoginResponse> loginUser(@Valid @RequestBody LoginForm loginForm){
        return this.authService.loginUser(loginForm);
    }
}