package com.kongo.history.api.kongohistoryapi.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigResource{
    @GetMapping("/login")
    public String hello(){
        return "Hello world";
    }
}

