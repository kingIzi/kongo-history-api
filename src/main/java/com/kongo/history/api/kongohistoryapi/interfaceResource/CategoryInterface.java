package com.kongo.history.api.kongohistoryapi.interfaceResource;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import com.kongo.history.api.kongohistoryapi.model.form.AddCategoryForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindCategoryForm;
import com.kongo.history.api.kongohistoryapi.model.form.UpdateCategoryForm;
import com.kongo.history.api.kongohistoryapi.model.entity.Category;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;

public interface CategoryInterface {
        public HttpDataResponse<Category> createOne(@RequestParam("thumbnail") MultipartFile thumbnail,
                        @ModelAttribute @Valid AddCategoryForm addCategoryForm);

        public HttpDataResponse<Category> updateOne(@RequestParam("categoryId") String categoryId,
                        @RequestParam("thumbnail") MultipartFile thumbnail,
                        @ModelAttribute UpdateCategoryForm updateCategoryForm);

        public HttpDataResponse<Category> findCategory(@RequestParam("categoryId") String categoryId);

        public HttpDataResponse<Category> removeCategory(@RequestParam("categoryId") String categoryId);

        public HttpDataResponse<List<Category>> getCategoryList(@RequestParam("limit") Integer limit,
                        @Valid final FindCategoryForm findCategoryForm);

        public HttpDataResponse<List<Category>> getCategoryList(@RequestParam("limit") Integer limit);
}
