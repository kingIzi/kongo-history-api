package com.kongo.history.api.kongohistoryapi.interfaceResource;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import com.kongo.history.api.kongohistoryapi.model.form.AddCategoryForm;
import com.kongo.history.api.kongohistoryapi.model.entity.Category;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;

public interface CategoryInterface {
    public HttpDataResponse<Category> createOne(@RequestParam("thumbnail") MultipartFile thumbnail,
            @ModelAttribute @Valid AddCategoryForm addCategoryForm);
}
