package com.kongo.history.api.kongohistoryapi.controller;

import javax.validation.Valid;
import java.util.List;


import com.kongo.history.api.kongohistoryapi.model.form.UpdateAuthorForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kongo.history.api.kongohistoryapi.model.entity.Author;
import com.kongo.history.api.kongohistoryapi.model.form.AddAuthorForm;
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
    public HttpDataResponse<Author> createAuthor(@Valid @RequestBody AddAuthorForm addAuthorForm) {
        return this.authorService.create(addAuthorForm);
    }

    @Override
    @PostMapping("/updateOne")
    public HttpDataResponse<Author> updateAuthor(@RequestParam(required = true) String authorId, @RequestBody UpdateAuthorForm updateAuthorForm) {
        return this.authorService.updateAuthor(authorId,updateAuthorForm);
    }

    @Override
    @DeleteMapping("/deleteOne")
    public HttpDataResponse<Author> removeAuthor(String authorId) {
        return this.authorService.removeAuthor(authorId);
    }

    @GetMapping("/findOne")
    @Override
    public HttpDataResponse<Author> findAuthor(@RequestParam(required = true) String authorId) {
        return this.authorService.findAuthor(authorId);
    }

    @PostMapping("/list")
    @Override
    public HttpDataResponse<List<Author>> getAuthorList(@RequestParam(required = false) Integer limit,@RequestBody final FindAuthorForm authorForm) {
        if (limit == null) { limit = 10; }
        return this.authorService.getAuthorList(limit,authorForm);
    }

    @Override
    @GetMapping("/list")
    public HttpDataResponse<?> getAuthorList(@RequestParam(required = false) Integer limit) {
        if (limit == null) { limit = 10; }
        return this.authorService.getAuthorList(limit);
    }
    
}
