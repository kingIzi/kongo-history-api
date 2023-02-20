package com.kongo.history.api.kongohistoryapi.repository;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;
import java.util.Optional;


import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreBundle;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.QuerySnapshot;

import com.kongo.history.api.kongohistoryapi.model.entity.Author;
import com.kongo.history.api.kongohistoryapi.model.entity.Comic;
import com.kongo.history.api.kongohistoryapi.utils.AppConst;
import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;
import com.kongo.history.api.kongohistoryapi.model.form.AuthorForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindAuthorForm;


@Repository
public class AuthorRepository extends AbstractFirestoreRepository<Author> {

    private static final String NAME = "author";

    protected AuthorRepository(Firestore firestore) {
        super(firestore, AuthorRepository.NAME);
    }

    // private final List<AbstractMap.SimpleEntry<String,String>> getFindAuthorFormData(final FindAuthorForm findAuthorForm){
    //     List<AbstractMap.SimpleEntry<String,String>> values = new ArrayList<>();
    //     values.add(new AbstractMap.SimpleEntry<>(Author.ID,findAuthorForm.getStatus()));
    //     if (findAuthorForm.getId() != null && !findAuthorForm.getId().isBlank())
    //         values.add(new AbstractMap.SimpleEntry<>(Author.ID,findAuthorForm.getId()));
    //     if (findAuthorForm.getFirstName() != null && !findAuthorForm.getFirstName().isBlank())
    //         values.add(new AbstractMap.SimpleEntry<>(Author.FIRST_NAME,findAuthorForm.getFirstName()));
    //     if (findAuthorForm.getLastName() != null && !findAuthorForm.getLastName().isBlank())
    //         values.add(new AbstractMap.SimpleEntry<>(Author.LAST_NAME,findAuthorForm.getLastName()));
        
    //     return values;
    // }

    private class KeyValue <K,V>{
        private K key;
        private V value;
        private KeyValue(K key,V value){
            this.key = key;
            this.value = value;
        }
        private void setKey(final K key){
            this.key = key;
        }
        private K getKey(){
            return this.key;
        }
        private void setValue(final V value){
            this.value = value;
        }
        private V getValue(){
            return this.value;
        }
    }

    private final List<KeyValue> getFindAuthorFormData(final FindAuthorForm findAuthorForm){
        List<KeyValue> values = new ArrayList();
        values.add(new KeyValue<String,String>(Author.STATUS,findAuthorForm.getStatus()));
        if (findAuthorForm.getId() != null && !findAuthorForm.getId().isBlank())
            values.add(new KeyValue<String,String>(Author.ID,findAuthorForm.getId()));
        if (findAuthorForm.getFirstName() != null && !findAuthorForm.getFirstName().isBlank())
            values.add(new KeyValue<String,String>(Author.FIRST_NAME,findAuthorForm.getFirstName()));
        if (findAuthorForm.getLastName() != null && !findAuthorForm.getLastName().isBlank())
            values.add(new KeyValue<String,String>(Author.LAST_NAME,findAuthorForm.getLastName()));

        return values;
    }


    public Optional<List<Author>> searchByCriteria(final int limit,final FindAuthorForm findAuthorForm) throws ValueDataException,Exception{
        // final var values = this.getFindAuthorFormData(findAuthorForm);
        // if (values.size() <= 0)
        //     throw new ValueDataException("Form values cannot all be empty.",AppConst._KEY_CODE_PARAMS_ERROR);
        // else if (values.size() > 3)
        //     throw new ValueDataException("Invalid parameter passed. Failed to search by criteria.",AppConst._KEY_CODE_PARAMS_ERROR);
        // else{}
        
        // System.out.println(values.size());
        // if (values.size() == 2){
        //     final var query = this.getCollectionReference().whereEqualTo(values.get(0).getKey(), values.get(0).getValue()).whereEqualTo(values.get(1).getKey(), values.get(1).getValue());
        //     final var querySnapshot = query.get().get();
        //     return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot)); 
        // }
        // else if (values.size() == 3){
        //     final var query = this.getCollectionReference().whereEqualTo(values.get(0).getKey(), values.get(0).getValue()).whereEqualTo(values.get(1).getKey(), values.get(1).getValue()).whereEqualTo(values.get(2).getKey(), values.get(2).getValue());
        //     final var querySnapshot = query.get().get();
        //     return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));         }
        // else if (values.size() == 4){
        //     final var query = this.getCollectionReference().whereEqualTo(values.get(0).getKey(), values.get(0).getValue()).whereEqualTo(values.get(1).getKey(), values.get(1).getValue()).whereEqualTo(values.get(2).getKey(), values.get(2).getValue()).whereEqualTo(values.get(3).getKey(), values.get(3).getValue());
        //     final var querySnapshot = query.get().get();
        //     return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot)); 
        // }
        // else{
        //     final var query = this.getCollectionReference().whereEqualTo(values.get(0).getKey(), values.get(0).getValue());
        //     final var querySnapshot = query.get().get();
        //     return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
        // }
        final var author = new Author();
        if (findAuthorForm.getId() != null && !findAuthorForm.getId().isBlank())
            author.setId(findAuthorForm.getId());
        if (findAuthorForm.getFirstName() != null && !findAuthorForm.getFirstName().isBlank())
            author.setFirstName(findAuthorForm.getFirstName());
        if (findAuthorForm.getLastName() != null && !findAuthorForm.getLastName().isBlank())
            author.setLastName(findAuthorForm.getLastName());

        
        final var query = this.getCollectionReference().whereEqualTo("null",author);
        final var querySnapshot = query.get().get();
        return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
    }

    public Optional<List<Author>> searchByCriteria(final int limit) throws ValueDataException,Exception{
        if (limit < 10)
            throw new ValueDataException("Limit is too small. Must be at least 10.",AppConst._KEY_CODE_PARAMS_ERROR);
        
        final var querySnapshot = this.getCollectionReference().limit(limit).get().get();
        return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot)); 
    }    
}
