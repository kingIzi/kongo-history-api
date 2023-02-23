package com.kongo.history.api.kongohistoryapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import com.kongo.history.api.kongohistoryapi.interfaceResource.CategoryInterface;
import com.kongo.history.api.kongohistoryapi.model.entity.Category;
import com.kongo.history.api.kongohistoryapi.model.form.AddCategoryForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindCategoryForm;
import com.kongo.history.api.kongohistoryapi.model.form.UpdateCategoryForm;
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

    @Override
    @PutMapping("/updateOne")
    public HttpDataResponse<Category> updateOne(@RequestParam("categoryId") String categoryId,
            @RequestParam("thumbnail") MultipartFile thumbnail,
            @ModelAttribute UpdateCategoryForm updateCategoryForm) {
        return this.categoryService.updateOne(categoryId, thumbnail, updateCategoryForm);
    }

    @Override
    @GetMapping("/findOne")
    public HttpDataResponse<Category> findCategory(@RequestParam("categoryId") String categoryId) {
        return this.categoryService.findCategory(categoryId);
    }

    @Override
    @GetMapping("/deleteOne")
    public HttpDataResponse<Category> removeCategory(@RequestParam("categoryId") String categoryId) {
        return this.categoryService.removeCategory(categoryId);
    }

    @Override
    @GetMapping("/list")
    public HttpDataResponse<List<Category>> getCategoryList(@RequestParam("limit") Integer limit) {
        if (limit == null)
            limit = 10;

        return this.categoryService.getCategoryList(limit);
    }

    @Override
    @PostMapping("/list")
    public HttpDataResponse<List<Category>> getCategoryList(@RequestParam("limit") Integer limit,
            @Valid final FindCategoryForm findCategoryForm) {
        if (limit == null)
            limit = 10;

        return this.categoryService.getCategoryList(limit, findCategoryForm);
    }
}
