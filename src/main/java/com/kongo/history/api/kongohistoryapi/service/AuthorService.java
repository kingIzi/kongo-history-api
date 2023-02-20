package com.kongo.history.api.kongohistoryapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import com.kongo.history.api.kongohistoryapi.model.entity.Author;
import com.kongo.history.api.kongohistoryapi.model.entity.Comic;
import com.kongo.history.api.kongohistoryapi.model.form.AuthorForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindAuthorForm;
import com.kongo.history.api.kongohistoryapi.repository.AbstractFirestoreRepository;
import com.kongo.history.api.kongohistoryapi.repository.AuthorRepository;
import com.kongo.history.api.kongohistoryapi.repository.ComicsRepository;
import com.kongo.history.api.kongohistoryapi.utils.AppConst;
import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;
import com.kongo.history.api.kongohistoryapi.utils.UtilityFormatter;
import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;

import java.time.LocalDate;
import java.util.List;
import java.util.Date;




@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    private Author makeAuthor(final AuthorForm authorForm) throws ValueDataException{
        final var localDate = LocalDate.parse(authorForm.getDateOfBirth());
        if (localDate == null)
            throw new ValueDataException("Could not parse Date String. Please Ensure format is yyyy-MM-dd",AppConst._KEY_CODE_PARAMS_ERROR);

        final var dateOfBirth = AppUtilities.convertStringFormatToDate(authorForm.getDateOfBirth());
        final var author = new Author(authorForm.getFirstName().trim(),authorForm.getLastName().trim(),dateOfBirth,authorForm.getAddress().trim(),authorForm.getPhoneNumber().trim());
        return author;
    }

    public HttpDataResponse<Author> create(final AuthorForm authorForm){
        final var httpDataResponse = new HttpDataResponse<Author>();
        try{            
            final var saved = this.authorRepository.save(this.makeAuthor(authorForm));
            if (saved != null)
                httpDataResponse.setResponse(saved);    
            else
                throw new ValueDataException(ValueDataException.itemNotCreatedMsg("Author"),AppConst._KEY_CODE_PARAMS_ERROR);
        }
        catch(ValueDataException e){
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return httpDataResponse;
    }

    public HttpDataResponse<Author> findAuthor(final String documentId){
        final var httpDataResponse = new HttpDataResponse<Author>();
        try{
            final var author = this.authorRepository.get(documentId);
            if (author.isPresent())
                httpDataResponse.setResponse(author.get());
            else
                throw new ValueDataException(ValueDataException.itemNotFoundErrorMsg("Author", documentId),AppConst._KEY_CODE_PARAMS_ERROR);
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

    public HttpDataResponse<List<Author>> findAuthor(final int limit){
        final var httpDataResponse = new HttpDataResponse<List<Author>>();
        try{
            final var data = this.authorRepository.searchByCriteria(limit);
            httpDataResponse.setResponse(data.get());
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

    public HttpDataResponse<List<Author>> findAuthor(final int limit,final FindAuthorForm findAuthorForm){
        final var httpDataResponse = new HttpDataResponse<List<Author>>();
        try{
            final var data = this.authorRepository.searchByCriteria(limit,findAuthorForm);
            httpDataResponse.setResponse(data.get());
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
    
}
