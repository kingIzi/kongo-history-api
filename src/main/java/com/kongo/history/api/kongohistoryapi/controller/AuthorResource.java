package com.kongo.history.api.kongohistoryapi.controller;

import javax.validation.Valid;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kongo.history.api.kongohistoryapi.model.entity.Author;
import com.kongo.history.api.kongohistoryapi.model.form.AuthorForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindAuthorForm;
import com.kongo.history.api.kongohistoryapi.service.AuthorService;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;

import com.kongo.history.api.kongohistoryapi.interfaceResource.AuthorInterface;


@RestController
@RequestMapping("/author")
public class AuthorResource implements AuthorInterface{

    @Autowired
    private AuthorService authorService;

    @Override
    @PostMapping("/createOne")
    public HttpDataResponse<Author> createAuthor(@Valid @RequestBody AuthorForm authorForm) {
        return this.authorService.create(authorForm);
    }

    @Override
    public HttpDataResponse<Author> updateAuthor(String authorId, AuthorForm authorForm) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<Author> updateAuthor(AuthorForm authorForm) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<Author> removeAuthor(String authorId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<Author> removeAuthor(AuthorForm authorForm) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<Author> activate(String authorId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<Author> activate(AuthorForm authorForm) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<Author> deactivate(String authorId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<Author> deactivate(AuthorForm authorForm) {
        // TODO Auto-generated method stub
        return null;
    }

    @GetMapping("/findOne")
    @Override
    public HttpDataResponse<Author> findAuthor(@RequestParam(required = true) String authorId) {
        return this.authorService.findAuthor(authorId);
    }

    @PostMapping("/list")
    @Override
    public HttpDataResponse<List<Author>> getAuthorList(@RequestParam(required = false) int limit,@RequestBody final FindAuthorForm authorForm) {
        return this.authorService.findAuthor(limit,authorForm);
    }

    @Override
    @GetMapping("/list")
    public HttpDataResponse<?> getAuthorList(@RequestParam(required = true) int limit) {
        return this.authorService.findAuthor(limit);
    }
    
}
