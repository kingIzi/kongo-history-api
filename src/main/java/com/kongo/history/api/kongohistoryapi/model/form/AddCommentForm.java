package com.kongo.history.api.kongohistoryapi.model.form;

import com.kongo.history.api.kongohistoryapi.model.entity.Comment;
import lombok.Data;

import java.util.List;

import javax.validation.constraints.NotBlank;

@Data
public class AddCommentForm {
    @NotBlank
    private String email;
    @NotBlank
    private String message;
}
