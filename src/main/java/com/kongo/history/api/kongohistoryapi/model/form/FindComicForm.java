package com.kongo.history.api.kongohistoryapi.model.form;

import com.kongo.history.api.kongohistoryapi.model.entity.Comment;
import lombok.Data;

import java.util.List;

@Data
public class FindComicForm {
    private String name;
    private String authorId;
}
