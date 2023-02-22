package com.kongo.history.api.kongohistoryapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import com.kongo.history.api.kongohistoryapi.interfaceResource.CategoryInterface;
import com.kongo.history.api.kongohistoryapi.model.entity.Category;
import com.kongo.history.api.kongohistoryapi.model.form.AddCategoryForm;
import com.kongo.history.api.kongohistoryapi.service.CategoryService;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;

@RestController
@RequestMapping("/category")
public class CategoryResource implements CategoryInterface {
    @Autowired
    private CategoryService categoryService;

    @Override
    @PostMapping("/createOne")
    public HttpDataResponse<Category> createOne(@RequestParam("thumbnail") MultipartFile thumbnail,
            @ModelAttribute @Valid AddCategoryForm addCategoryForm) {
        return this.categoryService.createOne(thumbnail, addCategoryForm);
    }
}
