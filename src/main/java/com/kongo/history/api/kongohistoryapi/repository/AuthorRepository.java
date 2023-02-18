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
    
    public Author updateInsertedAuthorId(final String documentId) throws ValueDataException{
        final var author = this.get(documentId);
        if (!author.isPresent())
            throw new ValueDataException(ValueDataException.itemNotFoundErrorMsg("Author", documentId));
        author.get().setId(documentId);
        return this.save(author.get()).isEmpty() ? null : author.get();
    }
}
