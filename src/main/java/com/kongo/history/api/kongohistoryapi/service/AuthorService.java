package com.kongo.history.api.kongohistoryapi.service;

import com.kongo.history.api.kongohistoryapi.model.form.UpdateAuthorForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kongo.history.api.kongohistoryapi.model.entity.Author;
import com.kongo.history.api.kongohistoryapi.model.form.AddAuthorForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindAuthorForm;
import com.kongo.history.api.kongohistoryapi.repository.AuthorRepository;
import com.kongo.history.api.kongohistoryapi.utils.AppConst;
import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;
import com.kongo.history.api.kongohistoryapi.utils.UtilityFormatter;
import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

    private Author makeAuthor(final AddAuthorForm addAuthorForm) throws ValueDataException{
        final var localDate = LocalDate.parse(addAuthorForm.getDateOfBirth());
        if (localDate == null)
            throw new ValueDataException("Could not parse Date String. Please Ensure format is yyyy-MM-dd",AppConst._KEY_CODE_PARAMS_ERROR);

        final var dateOfBirth = AppUtilities.convertStringFormatToDate(addAuthorForm.getDateOfBirth());
        return new Author(addAuthorForm.getFirstName().trim(), addAuthorForm.getLastName().trim(),dateOfBirth, addAuthorForm.getAddress().trim(), addAuthorForm.getPhoneNumber().trim());
    }
 

    public HttpDataResponse<Author> create(final MultipartFile photo,final AddAuthorForm addAuthorForm){
        final var httpDataResponse = new HttpDataResponse<Author>();
        try{
            final var photoUrl = this.authorRepository.uploadFile(photo);
            if (photoUrl != null && !photoUrl.isEmpty()){
                addAuthorForm.setPhotoUrl(photoUrl);
                final var saved = this.authorRepository.save(this.makeAuthor(addAuthorForm));
                saved.ifPresentOrElse(httpDataResponse::setResponse, saved::orElseThrow);
            }
            else{
                throw new ValueDataException("Failed to upload photo url. Please contact support",AppConst._KEY_CODE_INTERNAL_ERROR);
            }
        }
        catch(ValueDataException e){
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse,e);
        }
        catch(NoSuchElementException e){
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        catch(Exception e){
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<Author> findAuthor(final String documentId){
        final var httpDataResponse = new HttpDataResponse<Author>();
        try{
            final var author = this.authorRepository.get(documentId);
            author.ifPresentOrElse(httpDataResponse::setResponse,author::orElseThrow);
        }
        catch(NoSuchElementException e){
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        catch(Exception e){
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<List<Author>> getAuthorList(@Value("10") final int limit){
        final var httpDataResponse = new HttpDataResponse<List<Author>>();
        try{
            final var data = this.authorRepository.searchByCriteria(limit);
            data.ifPresentOrElse(httpDataResponse::setResponse,data::orElseThrow);
        }
        catch(NoSuchElementException e){
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        catch(ValueDataException e){
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        }
        catch(Exception e){
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<List<Author>> getAuthorList(@Value("10") final int limit, final FindAuthorForm findAuthorForm){
        final var httpDataResponse = new HttpDataResponse<List<Author>>();
        try{
            final var data = this.authorRepository.searchByCriteria(limit,findAuthorForm);
            data.ifPresentOrElse(httpDataResponse::setResponse,data::orElseThrow);
        }
        catch(NoSuchElementException e){
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        catch(ValueDataException e){
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        }
        catch(Exception e){
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    private Map<String,Object> updateAuthorValues(final Author author, final UpdateAuthorForm updateAuthorForm){
        final var values = new HashMap<String,Object>();
        try{
            if (AppUtilities.modifiableValue(author.getFirstName(),updateAuthorForm.getFirstName()))
                values.put(Author.FIRST_NAME,updateAuthorForm.getFirstName());
            if (AppUtilities.modifiableValue(author.getLastName(),updateAuthorForm.getLastName()))
                values.put(Author.LAST_NAME,updateAuthorForm.getLastName());
            if (AppUtilities.modifiableValue(author.getAddress(),updateAuthorForm.getAddress()))
                values.put(Author.ADDRESS,updateAuthorForm.getAddress());
            if (AppUtilities.modifiableValue(author.getPhoneNumber(),updateAuthorForm.getPhoneNumber()))
                values.put(Author.PHONE_NUMBER,updateAuthorForm.getPhoneNumber());
            if (AppUtilities.modifiableValue(author.getStatus(),updateAuthorForm.getStatus()))
                values.put(Author.STATUS,updateAuthorForm.getStatus());
            if (author.getDateOfBirth().isEmpty() && updateAuthorForm.getDateOfBirth() != null && !updateAuthorForm.getDateOfBirth().isBlank()){
                final var date = AppUtilities.convertStringFormatToDate(updateAuthorForm.getDateOfBirth());
                values.put(Author.DATE_OF_BIRTH,AppUtilities.convertDateToString(date));
            }
            return values.isEmpty() ? null : values;
        }
        catch (DateTimeParseException e){
            e.printStackTrace();
        }
        return null;
    }

    public HttpDataResponse<Author> updateAuthor(final String authorId,final UpdateAuthorForm addAuthorForm){
        final var httpDataResponse = new HttpDataResponse<Author>();
        try{
            final var author = this.findAuthor(authorId).getResponse();
            if (author == null)
                throw new ValueDataException("Author not found", AppConst._KEY_CODE_PARAMS_ERROR);

            final var newAuthor = this.updateAuthorValues(author,addAuthorForm);
            if (newAuthor != null){
                this.authorRepository.save(authorId,newAuthor);
                return this.findAuthor(authorId);
            }
        }
        catch(ValueDataException e){
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        }
        catch(Exception e){
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<Author> removeAuthor(final String authorId){
        final var httpDataResponse = new HttpDataResponse<Author>();
        try{
            this.authorRepository.delete(authorId);
        }
        catch (Exception e){
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }
    
}
