package com.kongo.history.api.kongohistoryapi.model.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AddCategoryForm {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private String dateCreated;
    private String dateUpdated;
    private String thumbnailUrl;
    private String thumbnailFileName;
    @NotBlank
    private String color;
}
