package com.kongo.history.api.kongohistoryapi.repository;

import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreBundle;
import com.google.firebase.cloud.FirestoreClient;
import com.kongo.history.api.kongohistoryapi.model.entity.Author;
import com.kongo.history.api.kongohistoryapi.model.entity.Comic;
import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;

@Repository
public class AuthorRepository extends AbstractFirestoreRepository<Author> {

    private static final String NAME = "author";

    protected AuthorRepository(Firestore firestore) {
        super(firestore, AuthorRepository.NAME);
    }
    
}
