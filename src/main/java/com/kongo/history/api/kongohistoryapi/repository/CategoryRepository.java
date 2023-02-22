package com.kongo.history.api.kongohistoryapi.repository;

import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Storage;
import com.kongo.history.api.kongohistoryapi.model.entity.Category;

@Repository
public class CategoryRepository extends AbstractFirestoreRepository<Category> {

    private static final String NAME = "category";

    protected CategoryRepository(Firestore firestore, Storage storage) {
        super(firestore, CategoryRepository.NAME, storage);
    }

}
