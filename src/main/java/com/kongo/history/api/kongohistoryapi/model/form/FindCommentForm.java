package com.kongo.history.api.kongohistoryapi.model.form;

import javax.validation.constraints.NotBlank;

import com.kongo.history.api.kongohistoryapi.model.entity.Comment;

import lombok.Data;

@Data
public class FindCommentForm {

    @NotBlank
    private String email;
    @NotBlank
    private String message;
    private Comment comment;
}
