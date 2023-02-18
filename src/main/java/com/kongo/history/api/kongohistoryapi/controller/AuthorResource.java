package com.kongo.history.api.kongohistoryapi.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kongo.history.api.kongohistoryapi.model.entity.Author;
import com.kongo.history.api.kongohistoryapi.model.form.AuthorForm;
import com.kongo.history.api.kongohistoryapi.service.AuthorService;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;

import com.kongo.history.api.kongohistoryapi.interfaceResource.AuthorInterface;


@RestController
@RequestMapping("author")
public class AuthorResource implements AuthorInterface{

    @Autowired
    private AuthorService authorService;

    @Override
    @PostMapping("create")
    public HttpDataResponse<Author> createAuthor(@Valid AuthorForm authorForm) {
        // TODO Auto-generated method stub
        return this.authorService.create(authorForm);
    }

    @Override
    public HttpDataResponse<Author> updateAuthor(Long authorId, AuthorForm authorForm) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<Author> updateAuthor(AuthorForm authorForm) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<Author> removeAuthor(Long authorId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<Author> removeAuthor(AuthorForm authorForm) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<Author> activate(Long authorId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<Author> activate(AuthorForm authorForm) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<Author> deactivate(Long authorId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<Author> deactivate(AuthorForm authorForm) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<Author> findAuthor(Long authorId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<Author> findAuthor(AuthorForm authorForm) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<?> getAuthorList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<?> getAuthorList(Long limit) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpDataResponse<Author> findAuthor(String documentId) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}
