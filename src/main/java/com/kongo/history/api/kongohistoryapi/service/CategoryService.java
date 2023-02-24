package com.kongo.history.api.kongohistoryapi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.crypto.dsig.keyinfo.KeyValue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.threeten.bp.DateTimeException;

import com.kongo.history.api.kongohistoryapi.model.entity.Category;
import com.kongo.history.api.kongohistoryapi.model.form.AddCategoryForm;
import com.kongo.history.api.kongohistoryapi.model.form.UpdateCategoryForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindCategoryForm;
import com.kongo.history.api.kongohistoryapi.repository.CategoryRepository;
import com.kongo.history.api.kongohistoryapi.utils.AppConst;
import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
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
            if (thumbnail.isEmpty())
                throw new ValueDataException("You must provide a thumbnail for a category.",
                        AppConst._KEY_CODE_PARAMS_ERROR);
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

    public HttpDataResponse<Category> findCategory(final String categoryId) {
        final var httpDataResponse = new HttpDataResponse<Category>();
        try {
            final var category = this.categoryRepository.get(categoryId).orElseThrow(AppUtilities.supplyException("Category not found",AppConst._KEY_CODE_PARAMS_ERROR));
            httpDataResponse.setResponse(category);
        }
        catch (ValueDataException e){
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse,e);
        }
        catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    private Map<String, Object> updatedCategoryValues(final Category category,
            final UpdateCategoryForm updateCategoryForm) {
        final var values = new HashMap<String, Object>();
        try {
            if (AppUtilities.modifiableValue(category.getName(), updateCategoryForm.getName()))
                values.put(Category.NAME, updateCategoryForm.getName());
            if (AppUtilities.modifiableValue(category.getDescription(), updateCategoryForm.getDescription()))
                values.put(Category.DESCRIPTION, updateCategoryForm.getDescription());
            if (AppUtilities.modifiableValue(category.getColor(), updateCategoryForm.getColor()))
                values.put(Category.COLOR, updateCategoryForm.getColor());
            if (AppUtilities.modifiableValue(Category.STATUS, updateCategoryForm.getStatus()))
                values.put(Category.STATUS, updateCategoryForm.getStatus());
            if (AppUtilities.modifiableValue(category.getThumbnailFileName(),updateCategoryForm.getThumbnailFileName()))
                values.put(Category.THUMBNAIL_FILENAME, updateCategoryForm.getThumbnailFileName());
            if (AppUtilities.modifiableValue(category.getThumbnailUrl(), updateCategoryForm.getThumbnailUrl()))
                values.put(Category.THUMBNAIL_URL, updateCategoryForm.getThumbnailUrl());
            if (!values.isEmpty())
                values.put(Category.DATE_UPDATED, AppUtilities.convertDateToString(new Date()));

            return values;
        } catch (DateTimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpDataResponse<Category> updateOne(final String categoryId, final MultipartFile thumbnail,
            final UpdateCategoryForm updateCategoryForm) {
        final var httpDataResponse = new HttpDataResponse<Category>();
        try {
            final var category = this.findCategory(categoryId).getResponse();
            if (category == null)
                throw new ValueDataException("Category not found", AppConst._KEY_CODE_PARAMS_ERROR);

            if (thumbnail != null && !thumbnail.isEmpty()) {
                final var thumbnailUrl = this.categoryRepository.uploadFile(thumbnail).orElseThrow(AppUtilities.supplyException("Failed to upload thumbnail data",AppConst._KEY_CODE_INTERNAL_ERROR));
                this.categoryRepository.removeFile(category.getThumbnailFileName());
                updateCategoryForm.setThumbnailUrl((String) thumbnailUrl.getSecond());
                updateCategoryForm.setThumbnailFileName((String) thumbnailUrl.getFirst());
            }
            final var newCategory = this.updatedCategoryValues(category, updateCategoryForm);
            if (this.categoryRepository.save(categoryId, newCategory))
                return this.findCategory(categoryId);
            UtilityFormatter.formatMessagesParamsError(httpDataResponse,"No items found to update",AppConst._KEY_MSG_SUCCESS);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<Category> removeCategory(final String categoryId) {
        final var httpDataResponse = new HttpDataResponse<Category>();
        try {
            final var category = this.findCategory(categoryId).getResponse();
            if (category == null)
                throw new ValueDataException("Category not found", AppConst._KEY_CODE_PARAMS_ERROR);

            final var thumbnailFileName = category.getThumbnailFileName();
            if (!thumbnailFileName.isBlank()) {
                this.categoryRepository.removeFile(thumbnailFileName);
                this.categoryRepository.delete(categoryId);
            }
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<List<Category>> getCategoryList(final Integer limit){
        final var httpDataResponse = new HttpDataResponse<List<Category>>();
        try{
            final var data = this.categoryRepository.searchCriteria(limit);
            data.ifPresentOrElse(httpDataResponse::setResponse, data::orElseThrow);
        }
        catch (NoSuchElementException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        }
        catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<List<Category>> getCategoryList(final Integer limit,
            final FindCategoryForm findCategoryForm) {
        final var httpDataResponse = new HttpDataResponse<List<Category>>();
        try {
            final var data = this.categoryRepository.searchCriteria(limit, findCategoryForm);
            data.ifPresentOrElse(httpDataResponse::setResponse, data::orElseThrow);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }
}
