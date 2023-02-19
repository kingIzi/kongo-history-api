package com.kongo.history.api.kongohistoryapi.interfaceResource;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.kongo.history.api.kongohistoryapi.model.entity.Author;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;
import com.kongo.history.api.kongohistoryapi.model.form.AuthorForm;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



public interface AuthorInterface {

    public HttpDataResponse<Author> createAuthor(@Valid @RequestBody AuthorForm authorForm);
    
    public HttpDataResponse<Author> updateAuthor(@RequestParam(required = true) String authorId,@RequestBody AuthorForm authorForm);
    public HttpDataResponse<Author> updateAuthor(@RequestBody AuthorForm authorForm);
    
    public HttpDataResponse<Author> removeAuthor(@RequestParam(required = true) String authorId);
    public HttpDataResponse<Author> removeAuthor(@RequestBody AuthorForm authorForm);

    public HttpDataResponse<Author> activate(@RequestParam(required = true) String authorId);
    public HttpDataResponse<Author> activate(@RequestBody AuthorForm authorForm);

    public HttpDataResponse<Author> deactivate(@RequestParam(required = true) String authorId);
    public HttpDataResponse<Author> deactivate(@RequestBody AuthorForm authorForm);

    public HttpDataResponse<Author> findAuthor(@RequestParam(required = true) String authorId);
    public HttpDataResponse<Author> findAuthor(@RequestBody AuthorForm authorForm);

    public HttpDataResponse<?> getAuthorList();
    public HttpDataResponse<?> getAuthorList(@RequestParam(required = true) Long limit);
}
