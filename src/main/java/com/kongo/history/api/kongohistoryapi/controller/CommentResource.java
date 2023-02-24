package com.kongo.history.api.kongohistoryapi.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;

@RestController
@RequestMapping("comment")
public class CommentResource {

    @GetMapping("/find")
    public HttpDataResponse<?> findComments(@RequestParam(required = true) final String comicId) {
        return null;
    }

    @PostMapping("/add")
    public HttpDataResponse<?> addComment(@RequestParam(required = true) final String userId,
            @RequestParam(required = true) final String comicId) {
        return null;
    }

    @PutMapping("/like")
    public HttpDataResponse<?> likeComment(@RequestParam(required = true) final String userId,
            @RequestParam(required = true) final String commentId) {
        return null;
    }

    @DeleteMapping("/remove")
    public HttpDataResponse<?> removeComment(@RequestParam(required = true) final String commentId) {
        return null;
    }
}
