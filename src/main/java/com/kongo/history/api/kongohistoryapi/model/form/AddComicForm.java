package com.kongo.history.api.kongohistoryapi.model.form;

import com.kongo.history.api.kongohistoryapi.utils.DocumentId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class AddComicForm {
    @NotBlank
    private String name;
    @NotBlank
    private String dateReleased;
    @NotBlank
    private String description;
    @NotBlank
    private String authorId;
    private List<String> categories;
    private List<String> viewers;
    private List<String> likers;
    private String thumbnailUrl;
    private String thumbnailFileName;
    private String dataUrl;
    private String dataFileName;
    private String status;
}
