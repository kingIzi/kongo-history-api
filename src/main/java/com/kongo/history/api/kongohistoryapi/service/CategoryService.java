package com.kongo.history.api.kongohistoryapi.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kongo.history.api.kongohistoryapi.model.entity.Category;
import com.kongo.history.api.kongohistoryapi.model.form.AddCategoryForm;
import com.kongo.history.api.kongohistoryapi.repository.CategoryRepository;
import com.kongo.history.api.kongohistoryapi.utils.AppConst;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;
import com.kongo.history.api.kongohistoryapi.utils.UtilityFormatter;
import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    private Category makeCategory(final String name, final String description, final String color,
            final String thumbnailUrl, final String thumbnailFileName) {
        final var category = new Category(name, description, color, thumbnailUrl, thumbnailFileName);
        return category;
    }

    public HttpDataResponse<Category> createOne(final MultipartFile thumbnail, final AddCategoryForm addCategoryForm) {
        final var httpDataResponse = new HttpDataResponse<Category>();
        try {
            final var thumbnailUrl = this.categoryRepository.uploadFile(thumbnail);
            if (!thumbnailUrl.isPresent())
                throw new ValueDataException("Failed to upload photo file", AppConst._KEY_CODE_INTERNAL_ERROR);

            addCategoryForm.setThumbnailFileName((String) thumbnailUrl.get().getFirst());
            addCategoryForm.setThumbnailUrl((String) thumbnailUrl.get().getSecond());
            final var saved = this.categoryRepository.save(this.makeCategory(addCategoryForm.getName(),
                    addCategoryForm.getDescription(), addCategoryForm.getColor(), addCategoryForm.getThumbnailUrl(),
                    addCategoryForm.getThumbnailFileName()));
            saved.ifPresentOrElse(httpDataResponse::setResponse, saved::orElseThrow);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }
}
