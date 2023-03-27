package com.kongo.history.api.kongohistoryapi.service;

import com.kongo.history.api.kongohistoryapi.model.form.UpdateAuthorForm;
import com.kongo.history.api.kongohistoryapi.model.response.PopularAuthorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kongo.history.api.kongohistoryapi.model.entity.Author;
import com.kongo.history.api.kongohistoryapi.model.form.AddAuthorForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindAuthorForm;
import com.kongo.history.api.kongohistoryapi.repository.AbstractFirestoreRepository;
import com.kongo.history.api.kongohistoryapi.repository.AuthorRepository;
import com.kongo.history.api.kongohistoryapi.utils.AppConst;
import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;
import com.kongo.history.api.kongohistoryapi.utils.UtilityFormatter;
import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    private Author makeAuthor(final AddAuthorForm addAuthorForm) throws Exception {
        final var dateOfBirth = AppUtilities.convertStringFormatToDate(addAuthorForm.getDateOfBirth());
        return new Author(addAuthorForm.getFirstName().trim(), addAuthorForm.getLastName().trim(), dateOfBirth,
                addAuthorForm.getAddress().trim(), addAuthorForm.getPhoneNumber().trim(),
                addAuthorForm.getPhotoUrl().trim(), addAuthorForm.getPhotoFileName().trim(), new Date(), new Date());
    }

    public HttpDataResponse<Author> create(final MultipartFile photo, final AddAuthorForm addAuthorForm) {
        final var httpDataResponse = new HttpDataResponse<Author>();
        try {
            if (photo.isEmpty())
                throw new ValueDataException("Please provide a photo for the author.",
                        AppConst._KEY_CODE_PARAMS_ERROR);
            final var photoUrl = this.authorRepository.uploadFile(photo);
            if (!photoUrl.isPresent())
                throw new ValueDataException("Failed to upload photo file", AppConst._KEY_CODE_INTERNAL_ERROR);
            addAuthorForm.setPhotoFileName((String) photoUrl.get().getFirst());
            addAuthorForm.setPhotoUrl((String) photoUrl.get().getSecond());
            final var saved = this.authorRepository.save(this.makeAuthor(addAuthorForm));
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

    public HttpDataResponse<Author> findAuthor(final String documentId) {
        final var httpDataResponse = new HttpDataResponse<Author>();
        try {
            final var author = this.authorRepository.get(documentId)
                    .orElseThrow(AppUtilities.supplyException("Author not found", AppConst._KEY_CODE_PARAMS_ERROR));
            httpDataResponse.setResponse(author);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<List<Author>> getAuthorList(final int limit) {
        final var httpDataResponse = new HttpDataResponse<List<Author>>();
        try {
            final var data = this.authorRepository.searchByCriteria(limit);
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

    public HttpDataResponse<List<Author>> getAuthorList(@Value("10") final int limit,
            final FindAuthorForm findAuthorForm) {
        final var httpDataResponse = new HttpDataResponse<List<Author>>();
        try {
            final var authors = this.authorRepository.searchByCriteria(limit, findAuthorForm);
            authors.ifPresentOrElse(httpDataResponse::setResponse, authors::orElseThrow);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    private Map<String, Object> updateAuthorValues(final Author author, final UpdateAuthorForm updateAuthorForm) {
        final var values = new HashMap<String, Object>();
        try {
            if (AppUtilities.modifiableValue(author.getFirstName(), updateAuthorForm.getFirstName()))
                values.put(Author.FIRST_NAME, updateAuthorForm.getFirstName());
            if (AppUtilities.modifiableValue(author.getLastName(), updateAuthorForm.getLastName()))
                values.put(Author.LAST_NAME, updateAuthorForm.getLastName());
            if (AppUtilities.modifiableValue(author.getAddress(), updateAuthorForm.getAddress()))
                values.put(Author.ADDRESS, updateAuthorForm.getAddress());
            if (AppUtilities.modifiableValue(author.getPhoneNumber(), updateAuthorForm.getPhoneNumber()))
                values.put(Author.PHONE_NUMBER, updateAuthorForm.getPhoneNumber());
            if (AppUtilities.modifiableValue(author.getStatus(), updateAuthorForm.getStatus()))
                values.put(Author.STATUS, updateAuthorForm.getStatus());
            if (AppUtilities.modifiableValue(author.getPhotoUrl(), updateAuthorForm.getPhotoUrl()))
                values.put(Author.PHOTO_URL, updateAuthorForm.getPhotoUrl());
            if (AppUtilities.modifiableValue(author.getPhotoFileName(), updateAuthorForm.getPhotoFileName()))
                values.put(Author.PHOTO_FILENAME, updateAuthorForm.getPhotoFileName());
            if (updateAuthorForm.getDateOfBirth() != null && !updateAuthorForm.getDateOfBirth().isBlank())
                values.put(Author.DATE_OF_BIRTH, AppUtilities.convertDateToString(
                        AppUtilities.convertStringFormatToDate(updateAuthorForm.getDateOfBirth())));
            if (!values.isEmpty())
                values.put(Author.DATE_UPDATED, new Date());

            return values;
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpDataResponse<Author> updateAuthor(final String authorId, final MultipartFile photo,
            final UpdateAuthorForm updateAuthorForm) {
        final var httpDataResponse = new HttpDataResponse<Author>();
        try {
            final var author = this.findAuthor(authorId).getResponse();
            if (author == null)
                throw new ValueDataException("Author not found", AppConst._KEY_CODE_PARAMS_ERROR);

            if (photo != null && !photo.isEmpty()) {
                final var photoUrl = this.authorRepository.uploadFile(photo).orElseThrow(
                        AppUtilities.supplyException("Failed to upload file", AppConst._KEY_CODE_INTERNAL_ERROR));
                this.authorRepository.removeFile(author.getPhotoFileName());
                updateAuthorForm.setPhotoUrl(photoUrl.getSecond());
                updateAuthorForm.setPhotoFileName(photoUrl.getFirst());
            }
            final var newAuthor = this.updateAuthorValues(author, updateAuthorForm);
            if (this.authorRepository.save(authorId, newAuthor))
                return this.findAuthor(authorId);
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, "No items found to update",
                    AppConst._KEY_MSG_SUCCESS);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<Author> removeAuthor(final String authorId) {
        final var httpDataResponse = new HttpDataResponse<Author>();
        try {
            final var author = this.authorRepository.get(authorId)
                    .orElseThrow(AppUtilities.supplyException("Author not found", AppConst._KEY_CODE_PARAMS_ERROR));

            if (!author.getPhotoFileName().isBlank()) {
                this.authorRepository.removeFile(author.getPhotoFileName());
                this.authorRepository.delete(authorId);
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
}
