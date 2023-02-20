package com.kongo.history.api.kongohistoryapi.interfaceResource;

import javax.validation.Valid;

import com.kongo.history.api.kongohistoryapi.model.form.UpdateAuthorForm;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.kongo.history.api.kongohistoryapi.model.entity.Author;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;
import com.kongo.history.api.kongohistoryapi.model.form.AddAuthorForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindAuthorForm;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


public interface AuthorInterface {
    public HttpDataResponse<Author> createAuthor(@RequestPart(value = "file") MultipartFile multipartFile, @Valid @RequestBody AddAuthorForm addAuthorForm);
    public HttpDataResponse<Author> updateAuthor(@RequestParam(required = true) String authorId,@RequestBody UpdateAuthorForm updateAuthorForm);
    public HttpDataResponse<Author> removeAuthor(@RequestParam(required = true) String authorId);
    public HttpDataResponse<Author> findAuthor(@RequestParam(required = true) String authorId);
    public HttpDataResponse<?> getAuthorList(@RequestParam(required = false) Integer limit, @RequestBody final FindAuthorForm authorForm);
    public HttpDataResponse<?> getAuthorList(@RequestParam(required = false) Integer limit);
}
