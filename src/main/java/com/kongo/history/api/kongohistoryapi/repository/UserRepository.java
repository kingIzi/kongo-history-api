package com.kongo.history.api.kongohistoryapi.repository;

import com.google.firebase.database.utilities.Pair;
import com.kongo.history.api.kongohistoryapi.model.entity.Author;
import com.kongo.history.api.kongohistoryapi.model.form.FindUserForm;
import com.kongo.history.api.kongohistoryapi.model.form.UpdateUserForm;
import org.springframework.stereotype.Repository;

import com.google.cloud.storage.Storage;
import com.google.cloud.firestore.Firestore;

import com.kongo.history.api.kongohistoryapi.model.entity.User;
import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;
import com.kongo.history.api.kongohistoryapi.utils.AppConst;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.util.Objects;

@Repository
public class UserRepository extends AbstractFirestoreRepository<User> {

    public static final String NAME = "user";

    protected UserRepository(Firestore firestore, Storage storage) {
        super(firestore, UserRepository.NAME, storage);
    }

    private List<Pair<String, Object>> getFindUserForm(final FindUserForm findUserForm) {
        final var values = new ArrayList<Pair<String, Object>>();
        values.add(new Pair<>(User.STATUS, findUserForm.getStatus()));
        if (findUserForm.getEmail() != null && !findUserForm.getEmail().isEmpty())
            values.add(new Pair<>(User.EMAIL, findUserForm.getEmail()));
        if (findUserForm.getLocalId() != null && !findUserForm.getLocalId().isEmpty())
            values.add(new Pair<>(User.LOCAL_ID, findUserForm.getLocalId()));
        if (Objects.nonNull(findUserForm.getRole()) && !findUserForm.getRole().isEmpty())
            values.add(new Pair<>(User.ROLE, findUserForm.getRole()));
        return values;
    }

    public Optional<List<User>> searchByCriteria(final Integer limit) throws ValueDataException, Exception {
        if (Objects.isNull(limit)) {
            final var querySnapshot = this.getCollectionReference().get().get();
            final var documents = this.makeListFromQuerySnapshots(querySnapshot);
            return Optional.ofNullable(documents);
        } else {
            final var querySnapshot = this.getCollectionReference().limit(limit).get().get();
            final var documents = this.makeListFromQuerySnapshots(querySnapshot);
            return Optional.ofNullable(documents);
        }
    }

    public Optional<List<User>> searchByCriteria(final FindUserForm findUserForm) throws Exception {
        final var values = this.getFindUserForm(findUserForm);
        if (values.size() == 1) {
            final var query = this.getCollectionReference()
                    .whereEqualTo(values.get(0).getFirst(), values.get(0).getSecond());
            final var querySnapshot = query.get().get();
            return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
        } else if (values.size() == 2) {
            final var query = this.getCollectionReference()
                    .whereEqualTo(values.get(0).getFirst(), values.get(0).getSecond())
                    .whereEqualTo(values.get(1).getFirst(), values.get(1).getSecond());
            final var querySnapshot = query.get().get();
            return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
        } else if (values.size() == 3) {
            final var query = this.getCollectionReference()
                    .whereEqualTo(values.get(0).getFirst(), values.get(0).getSecond())
                    .whereEqualTo(values.get(1).getFirst(), values.get(1).getSecond())
                    .whereEqualTo(values.get(2).getFirst(), values.get(2).getSecond());
            final var querySnapshot = query.get().get();
            return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
        } else if (values.size() == 4) {
            final var query = this.getCollectionReference()
                    .whereEqualTo(values.get(0).getFirst(), values.get(0).getSecond())
                    .whereEqualTo(values.get(1).getFirst(), values.get(1).getSecond())
                    .whereEqualTo(values.get(2).getFirst(), values.get(2).getSecond())
                    .whereEqualTo(values.get(3).getFirst(), values.get(3).getSecond());
            final var querySnapshot = query.get().get();
            return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
        } else
            return Optional.empty();
    }

    public Optional<List<User>> searchByCriteria(final int limit, final FindUserForm findUserForm) throws Exception {
        final var values = this.getFindUserForm(findUserForm);
        if (values.size() == 1) {
            final var query = this.getCollectionReference()
                    .whereEqualTo(values.get(0).getFirst(), values.get(0).getSecond()).limit(limit);
            final var querySnapshot = query.get().get();
            return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
        } else if (values.size() == 2) {
            final var query = this.getCollectionReference()
                    .whereEqualTo(values.get(0).getFirst(), values.get(0).getSecond()).limit(limit)
                    .whereEqualTo(values.get(1).getFirst(), values.get(1).getSecond()).limit(limit);
            final var querySnapshot = query.get().get();
            return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
        } else if (values.size() == 3) {
            final var query = this.getCollectionReference()
                    .whereEqualTo(values.get(0).getFirst(), values.get(0).getSecond()).limit(limit)
                    .whereEqualTo(values.get(1).getFirst(), values.get(1).getSecond()).limit(limit)
                    .whereEqualTo(values.get(2).getFirst(), values.get(2).getSecond()).limit(limit);
            final var querySnapshot = query.get().get();
            return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
        } else if (values.size() == 4) {
            final var query = this.getCollectionReference()
                    .whereEqualTo(values.get(0).getFirst(), values.get(0).getSecond()).limit(limit)
                    .whereEqualTo(values.get(1).getFirst(), values.get(1).getSecond()).limit(limit)
                    .whereEqualTo(values.get(2).getFirst(), values.get(2).getSecond()).limit(limit)
                    .whereEqualTo(values.get(3).getFirst(), values.get(3).getSecond()).limit(limit);
            final var querySnapshot = query.get().get();
            return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
        } else
            return Optional.empty();
    }

}
