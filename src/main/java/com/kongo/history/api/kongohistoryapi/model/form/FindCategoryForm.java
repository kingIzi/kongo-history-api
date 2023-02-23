package com.kongo.history.api.kongohistoryapi.model.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class FindCategoryForm {
    @NotBlank
    private String name;
    private String status = "ON";
}
