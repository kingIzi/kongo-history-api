package com.kongo.history.api.kongohistoryapi.service;

import com.kongo.history.api.kongohistoryapi.model.entity.Comic;
import com.kongo.history.api.kongohistoryapi.model.form.AddComicForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindComicForm;
import com.kongo.history.api.kongohistoryapi.model.form.UpdateComicForm;
import com.kongo.history.api.kongohistoryapi.repository.ComicRepository;
import com.kongo.history.api.kongohistoryapi.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Service
public class ComicService {
    @Autowired
    private ComicRepository comicRepository;

    private Comic makeComic(final AddComicForm addComicForm) {
        return new Comic(addComicForm.getName(), AppUtilities.convertStringFormatToDate(addComicForm.getDateReleased()),
                addComicForm.getDescription(), addComicForm.getAuthorId(), addComicForm.getCategories(),
                addComicForm.getThumbnailFileName(), addComicForm.getThumbnailUrl(), addComicForm.getDataFileName(),
                addComicForm.getDataUrl());
    }

    private Map<String, Object> updateComicValues(final Comic comic, final UpdateComicForm updateComicForm) {
        final var values = new HashMap<String, Object>();

        if (AppUtilities.modifiableValue(comic.getName(), updateComicForm.getName()))
            values.put(Comic.NAME, updateComicForm.getName());
        if (AppUtilities.modifiableValue(comic.getDescription(), updateComicForm.getDescription()))
            values.put(Comic.DESCRIPTION, updateComicForm.getDescription());
        if (AppUtilities.modifiableValue(comic.getAuthorId(), updateComicForm.getAuthorId()))
            values.put(Comic.AUTHOR_ID, updateComicForm.getAuthorId());
        if (updateComicForm.getCategories() != null)
            values.put(Comic.CATEGORIES, updateComicForm.getCategories());
        if (updateComicForm.getViewers() != null)
            values.put(Comic.VIEWERS, updateComicForm.getViewers());
        if (updateComicForm.getLikers() != null)
            values.put(Comic.LIKERS, updateComicForm.getLikers());
        if (updateComicForm.getComments() != null)
            values.put(Comic.COMMENTS, updateComicForm.getComments());
        if (!updateComicForm.getThumbnailFileName().isEmpty())
            values.put(Comic.THUMBNAIL_FILENAME, updateComicForm.getThumbnailFileName());
        if (!updateComicForm.getThumbnailUrl().isEmpty())
            values.put(Comic.THUMBNAIL_URL, updateComicForm.getThumbnailUrl());
        if (!updateComicForm.getDataFileName().isEmpty())
            values.put(Comic.DATA_FILENAME, updateComicForm.getDataFileName());
        if (!updateComicForm.getDataUrl().isEmpty())
            values.put(Comic.DATA_URL, updateComicForm.getDataUrl());

        return values;
    }

    public HttpDataResponse<Comic> createComic(final MultipartFile thumbnail, final MultipartFile data,
            final AddComicForm addComicForm) {
        final var httpDataResponse = new HttpDataResponse<Comic>();
        try {
            if (thumbnail.isEmpty())
                throw new ValueDataException("Please provide a photo for the author.", AppConst._KEY_CODE_PARAMS_ERROR);
            final var uploadedThumbnail = this.comicRepository.uploadFile(thumbnail).orElseThrow(
                    AppUtilities.supplyException("Failed to upload comic thumbnail. Please contact support.",
                            AppConst._KEY_CODE_INTERNAL_ERROR));
            addComicForm.setThumbnailFileName(uploadedThumbnail.getFirst());
            addComicForm.setThumbnailUrl(uploadedThumbnail.getSecond());
            final var uploadedData = this.comicRepository.uploadFile(data).orElseThrow(AppUtilities.supplyException(
                    "Failed to upload comic file. Please contact support.", AppConst._KEY_CODE_INTERNAL_ERROR));
            addComicForm.setDataFileName(uploadedData.getFirst());
            addComicForm.setDataUrl(uploadedData.getSecond());
            final var comic = this.comicRepository.save(this.makeComic(addComicForm)).orElseThrow(
                    AppUtilities.supplyException("Failed to upload comic.", AppConst._KEY_CODE_INTERNAL_ERROR));
            httpDataResponse.setResponse(comic);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<Comic> updateComic(final String comicId, final MultipartFile thumbnail,
            final MultipartFile data, final UpdateComicForm updateComicForm) {
        final var httpDataResponse = new HttpDataResponse<Comic>();
        try {
            final var comic = this.comicRepository.get(comicId)
                    .orElseThrow(AppUtilities.supplyException("Comic not found", AppConst._KEY_CODE_PARAMS_ERROR));
            if (thumbnail != null && !thumbnail.isEmpty()) {
                final var uploadedComic = this.comicRepository.uploadFile(thumbnail).orElseThrow(
                        AppUtilities.supplyException("Failed to upload thumbnail file. Please contact support.",
                                AppConst._KEY_CODE_INTERNAL_ERROR));
                updateComicForm.setThumbnailFileName(uploadedComic.getFirst());
                updateComicForm.setThumbnailUrl(uploadedComic.getSecond());
            }
            if (data != null && !data.isEmpty()) {
                final var uploadData = this.comicRepository.uploadFile(data).orElseThrow(AppUtilities.supplyException(
                        "Failed to upload comic file. Please contact support", AppConst._KEY_CODE_INTERNAL_ERROR));
                updateComicForm.setDataFileName(uploadData.getFirst());
                updateComicForm.setDataUrl(uploadData.getSecond());
            }
            final var updatedComic = this.updateComicValues(comic, updateComicForm);
            if (this.comicRepository.save(comicId, updatedComic))
                return this.findComic(comicId);
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, "No items found to update",
                    AppConst._KEY_MSG_SUCCESS);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, "SOMETHING WENT WRONG",
                    AppConst._KEY_CODE_INTERNAL_ERROR);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<Comic> findComic(final String comicId) {
        final var httpDataResponse = new HttpDataResponse<Comic>();
        try {
            final var comic = this.comicRepository.get(comicId)
                    .orElseThrow(AppUtilities.supplyException("Comic not found", AppConst._KEY_CODE_PARAMS_ERROR));
            httpDataResponse.setResponse(comic);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, "SOMETHING WENT WRONG",
                    AppConst._KEY_CODE_INTERNAL_ERROR);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<List<Comic>> getComicList(final Integer limit) {
        final var httpDataResponse = new HttpDataResponse<List<Comic>>();
        try {
            final var comics = this.comicRepository.searchByCriteria(limit == null ? 100 : limit).orElseThrow(
                    AppUtilities.supplyException("Failed to retrieve comics.", AppConst._KEY_CODE_INTERNAL_ERROR));
            httpDataResponse.setResponse(comics);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, "SOMETHING WENT WRONG!",
                    AppConst._KEY_CODE_INTERNAL_ERROR);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<List<Comic>> getComicList(final Integer limit, final FindComicForm findComicForm) {
        final var httpDataResponse = new HttpDataResponse<List<Comic>>();
        try {
            final var comics = this.comicRepository.searchByCriteria(limit == null ? 100 : limit, findComicForm)
                    .orElseThrow(AppUtilities.supplyException("Failed to retrieve comics.",
                            AppConst._KEY_CODE_INTERNAL_ERROR));
            httpDataResponse.setResponse(comics);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse,
                    "Something went wrong. Please contact support.", AppConst._KEY_CODE_INTERNAL_ERROR);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<String> removeComic(final String comicId) {
        final var httpDataResponse = new HttpDataResponse<String>();
        try {
            final var comic = this.comicRepository.get(comicId)
                    .orElseThrow(AppUtilities.supplyException("Comic not found", AppConst._KEY_CODE_PARAMS_ERROR));
            if (comic.getThumbnailFileName() != null && !comic.getThumbnailFileName().isBlank())
                this.comicRepository.removeFile(comic.getThumbnailFileName());
            if (comic.getDataFileName() != null && !comic.getDataFileName().isBlank())
                this.comicRepository.removeFile(comic.getDataFileName());

            this.comicRepository.delete(comicId);
            httpDataResponse.setResponse("Deleted Item Successfully");
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<?> likeComic(final String userId, final String comicId) {
        final var httpDataResponse = new HttpDataResponse<Comic>();
        try {
            final var comic = this.comicRepository.get(comicId)
                    .orElseThrow(AppUtilities.supplyException("Comic not found", AppConst._KEY_CODE_PARAMS_ERROR));
            if (comic.getLikers() == null) {
                comic.setLikers(new ArrayList<>());
                comic.getLikers().add(userId);
            } else if (comic.getLikers().contains(userId))
                comic.getLikers().remove(userId);
            else
                comic.getLikers().add(userId);
            Map<String, Object> update = new HashMap<>();
            update.put(Comic.LIKERS, comic.getLikers());
            this.comicRepository.save(comicId, update);
            return this.findComic(comicId);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<?> viewComic(final String userId, final String comicId) {
        final var httpDataResponse = new HttpDataResponse<Comic>();
        try {
            final var comic = this.comicRepository.get(comicId)
                    .orElseThrow(AppUtilities.supplyException("Comic not found", AppConst._KEY_CODE_PARAMS_ERROR));
            if (comic.getViewers() == null) {
                comic.setViewers(new ArrayList<>());
                comic.getViewers().add(userId);
            }
            if (!comic.getViewers().contains(userId))
                comic.getViewers().add(userId);
            Map<String, Object> update = new HashMap<>();
            update.put(Comic.LIKERS, comic.getLikers());
            this.comicRepository.save(comicId, update);
            return this.findComic(comicId);
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
