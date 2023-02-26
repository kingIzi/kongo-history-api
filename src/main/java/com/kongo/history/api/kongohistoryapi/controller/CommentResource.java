package com.kongo.history.api.kongohistoryapi.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kongo.history.api.kongohistoryapi.model.entity.Comment;
import com.kongo.history.api.kongohistoryapi.model.form.AddCommentForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindCommentForm;
import com.kongo.history.api.kongohistoryapi.model.form.LikeCommentForm;
import com.kongo.history.api.kongohistoryapi.service.CommentService;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;

@RestController
@RequestMapping("comment")
public class CommentResource {

}
