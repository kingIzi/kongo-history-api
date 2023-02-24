package com.kongo.history.api.kongohistoryapi.model.form;

import com.kongo.history.api.kongohistoryapi.model.entity.Comment;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class UpdateComicForm {
    private String name;
    private String dateReleased;
    private String description;
    private List<String> categories;
    private String authorId;
    private List<String> viewers;
    private List<String> likers;
    private List<Comment> comments;
    private String thumbnailUrl;
    private String thumbnailFileName;
    private String dataUrl;
    private String dataFileName;
    private String status;
}
