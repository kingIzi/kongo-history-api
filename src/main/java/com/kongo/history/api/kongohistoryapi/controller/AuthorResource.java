package com.kongo.history.api.kongohistoryapi.controller;

import javax.validation.Valid;
import java.util.List;

import com.kongo.history.api.kongohistoryapi.model.form.UpdateAuthorForm;
import com.kongo.history.api.kongohistoryapi.utils.UtilityFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.kongo.history.api.kongohistoryapi.model.entity.Author;
import com.kongo.history.api.kongohistoryapi.model.form.AddAuthorForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindAuthorForm;
import com.kongo.history.api.kongohistoryapi.service.AuthorService;
import com.kongo.history.api.kongohistoryapi.utils.AppConst;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;
import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/author")
public class AuthorResource {

    @Autowired
    private AuthorService authorService;

    @PostMapping(path = "/createOne", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE })
    public HttpDataResponse<Author> createAuthor(@RequestParam("photo") MultipartFile photo,
            @ModelAttribute @Valid AddAuthorForm addAuthorForm) {
        final var httpDataResponse = new HttpDataResponse<Author>();
        try {
            if (photo.isEmpty())
                throw new ValueDataException("Failed to parse form data. Photo param missing.",
                        AppConst._KEY_CODE_PARAMS_ERROR);
            return this.authorService.create(photo, addAuthorForm);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        }
        return httpDataResponse;
    }

    @PostMapping("/updateOne")
    public HttpDataResponse<Author> updateAuthor(@RequestParam(required = true) String authorId,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @ModelAttribute @Valid UpdateAuthorForm updateAuthorForm) {
        return this.authorService.updateAuthor(authorId, photo, updateAuthorForm);
    }

    @DeleteMapping("/deleteOne")
    public HttpDataResponse<Author> removeAuthor(String authorId) {
        return this.authorService.removeAuthor(authorId);
    }

    @GetMapping("/findOne")
    public HttpDataResponse<Author> findAuthor(@RequestParam(required = true) String authorId) {
        return this.authorService.findAuthor(authorId);
    }

    @PostMapping("/list")
    public HttpDataResponse<List<Author>> getAuthorList(@RequestParam(required = false) Integer limit,
            @RequestBody final FindAuthorForm authorForm) {
        if (limit == null) {
            limit = 10;
        }
        return this.authorService.getAuthorList(limit, authorForm);
    }

    @GetMapping("/list")
    public HttpDataResponse<?> getAuthorList(@RequestParam(required = false) Integer limit) {
        if (limit == null) {
            limit = 10;
        }
        return this.authorService.getAuthorList(limit);
    }

}
