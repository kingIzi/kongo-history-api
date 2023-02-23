package com.kongo.history.api.kongohistoryapi.model.form;

import lombok.Data;

@Data
public class UpdateCategoryForm {
    private String name;
    private String description;
    private String dateCreated;
    private String dateUpdated;
    private String thumbnailUrl;
    private String thumbnailFileName;
    private String color;
    private String status;
}
