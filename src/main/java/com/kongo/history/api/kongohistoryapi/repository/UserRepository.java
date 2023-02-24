package com.kongo.history.api.kongohistoryapi.repository;

import org.springframework.stereotype.Repository;

import com.google.cloud.storage.Storage;
import com.google.cloud.firestore.Firestore;

import com.kongo.history.api.kongohistoryapi.model.entity.User;
import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;
import com.kongo.history.api.kongohistoryapi.utils.AppConst;

import java.util.Optional;
import java.util.List;

@Repository
public class UserRepository extends AbstractFirestoreRepository<User> {

    public static final String NAME = "user";

    protected UserRepository(Firestore firestore, Storage storage) {
        super(firestore, UserRepository.NAME, storage);
    }

    public Optional<List<User>> searchByCriteria(final int limit) throws ValueDataException, Exception {
        final var querySnapshot = this.getCollectionReference().limit(limit).get().get();
        final var documents = this.makeListFromQuerySnapshots(querySnapshot);
        return Optional.ofNullable(documents);
    }

}
