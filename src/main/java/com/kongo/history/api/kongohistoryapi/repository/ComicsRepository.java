package com.kongo.history.api.kongohistoryapi.repository;

import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Storage;
import com.kongo.history.api.kongohistoryapi.model.entity.Comic;

@Repository
public class ComicsRepository extends AbstractFirestoreRepository<Comic> {

    private static final String NAME = "comic";

    // protected ComicsRepository(Firestore firestore) {
    //     super(firestore, ComicsRepository.NAME);
    // }

    protected ComicsRepository(Firestore firestore,Storage storage){
        super(firestore, ComicsRepository.NAME,storage);
    }
    
}
