package com.kongo.history.api.kongohistoryapi.resource;


import com.kongo.history.api.kongohistoryapi.core.model.form.LoginForm;
import com.kongo.history.api.kongohistoryapi.core.model.response.LoginResponse;
import com.kongo.history.api.kongohistoryapi.util.HttpDataResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/session")
public class AuthResource {
    @PostMapping("/login")
    public HttpDataResponse<LoginResponse> loginUser(@Valid @RequestBody LoginForm loginForm){
        return new HttpDataResponse<>();
    }
}
