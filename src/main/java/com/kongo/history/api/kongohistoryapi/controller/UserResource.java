package com.kongo.history.api.kongohistoryapi.controller;

import com.kongo.history.api.kongohistoryapi.model.entity.User;
import com.kongo.history.api.kongohistoryapi.model.response.LoginResponse;
import com.kongo.history.api.kongohistoryapi.model.form.FindUserForm;
import com.kongo.history.api.kongohistoryapi.model.form.UpdateUserForm;
import com.kongo.history.api.kongohistoryapi.service.SessionService;
import com.kongo.history.api.kongohistoryapi.service.UserService;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserResource {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @GetMapping("details")
    public HttpDataResponse<?> getUserInfo(
            @AuthenticationPrincipal com.kongo.history.api.kongohistoryapi.auth.models.User user) {
        return new HttpDataResponse<>(user);
    }

    @GetMapping("/list")
    public HttpDataResponse<?> getUsersList(@RequestParam(required = false) final Integer limit) {
        return this.userService.getUsersList(limit);
    }

    @PostMapping("/list")
    public HttpDataResponse<List<User>> getUsersList(@RequestParam(required = false) final Integer limit,
            final @RequestBody FindUserForm findUserForm) {
        return this.userService.getUsersList(limit, findUserForm);
    }

    @GetMapping("/findOne")
    public HttpDataResponse<User> findUser(@RequestParam(required = true) final String userId) {
        return this.userService.findUser(userId);
    }

    @PutMapping("/updateOne")
    public HttpDataResponse<User> updateUser(@RequestParam(required = true) final String userId,
            @RequestParam(required = false) MultipartFile photo, @ModelAttribute UpdateUserForm updateUserForm) {
        return this.userService.updateUser(userId, photo, updateUserForm);
    }

    @PostMapping("/update/favorites")
    public HttpDataResponse<User> updateFavorites(@RequestParam(required = true) final String userId,
            @RequestParam(required = true) final String comicId) {
        return this.userService.favorites(userId, comicId);
    }

}
