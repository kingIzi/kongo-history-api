package com.kongo.history.api.kongohistoryapi.repository;

import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Storage;
import com.kongo.history.api.kongohistoryapi.model.entity.Comment;

@Repository
public class CommentRepository extends AbstractFirestoreRepository<Comment> {
    private static final String NAME = "comments";

    protected CommentRepository(Firestore firestore, Storage storage) {
        super(firestore, CommentRepository.NAME, storage);
    }

}
