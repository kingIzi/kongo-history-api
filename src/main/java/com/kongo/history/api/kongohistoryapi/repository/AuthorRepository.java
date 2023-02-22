package com.kongo.history.api.kongohistoryapi.repository;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

import com.kongo.history.api.kongohistoryapi.model.form.UpdateAuthorForm;
import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import com.kongo.history.api.kongohistoryapi.model.entity.Author;
import com.kongo.history.api.kongohistoryapi.utils.AppConst;
import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;
import com.kongo.history.api.kongohistoryapi.model.form.FindAuthorForm;

@Repository
public class AuthorRepository extends AbstractFirestoreRepository<Author> {

    private static final String NAME = "author";

    protected AuthorRepository(Firestore firestore, Storage storage) {
        super(firestore, AuthorRepository.NAME, storage);
    }

    private class KeyValue<V> {
        private String key;
        private V value;

        private KeyValue(String key, V value) {
            this.key = key;
            this.value = value;
        }

        private void setKey(final String key) {
            this.key = key;
        }

        private String getKey() {
            return this.key;
        }

        private void setValue(final V value) {
            this.value = value;
        }

        private V getValue() {
            return this.value;
        }
    }

    private final List<KeyValue> getFindAuthorFormData(final FindAuthorForm findAuthorForm) {
        List<KeyValue> values = new ArrayList();
        values.add(new KeyValue<>(Author.STATUS, findAuthorForm.getStatus()));
        if (findAuthorForm.getId() != null && !findAuthorForm.getId().isBlank())
            values.add(new KeyValue<>(Author.ID, findAuthorForm.getId()));
        if (findAuthorForm.getFirstName() != null && !findAuthorForm.getFirstName().isBlank())
            values.add(new KeyValue<>(Author.FIRST_NAME, findAuthorForm.getFirstName()));
        if (findAuthorForm.getLastName() != null && !findAuthorForm.getLastName().isBlank())
            values.add(new KeyValue<>(Author.LAST_NAME, findAuthorForm.getLastName()));

        return values;
    }

    public Optional<List<Author>> searchByCriteria(final int limit, final FindAuthorForm findAuthorForm)
            throws ValueDataException, Exception {
        final var values = this.getFindAuthorFormData(findAuthorForm);
        if (limit < 10)
            throw new ValueDataException("Limit is too small. Must be at least 10.", AppConst._KEY_CODE_PARAMS_ERROR);
        if (values.isEmpty())
            throw new ValueDataException("SOMETHING WENT WRONG! FindAuthorForm is empty",
                    AppConst._KEY_CODE_PARAMS_ERROR);
        if (values.size() == 1) {
            final var query = this.getCollectionReference()
                    .whereEqualTo(values.get(0).getKey(), values.get(0).getValue()).limit(limit);
            final var querySnapshot = query.get().get();
            return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
        }
        if (values.size() == 2) {
            final var query = this.getCollectionReference()
                    .whereEqualTo(values.get(0).getKey(), values.get(0).getValue())
                    .whereEqualTo(values.get(1).getKey(), values.get(1).getValue());
            final var querySnapshot = query.get().get();
            return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
        }
        if (values.size() == 3) {
            final var query = this.getCollectionReference()
                    .whereEqualTo(values.get(0).getKey(), values.get(0).getValue())
                    .whereEqualTo(values.get(1).getKey(), values.get(1).getValue())
                    .whereEqualTo(values.get(2).getKey(), values.get(2).getValue());
            final var querySnapshot = query.get().get();
            return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
        }
        if (values.size() == 4) {
            final var query = this.getCollectionReference()
                    .whereEqualTo(values.get(0).getKey(), values.get(0).getValue())
                    .whereEqualTo(values.get(1).getKey(), values.get(1).getValue())
                    .whereEqualTo(values.get(2).getKey(), values.get(2).getValue())
                    .whereEqualTo(values.get(3).getKey(), values.get(3).getValue());
            final var querySnapshot = query.get().get();
            return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
        }
        return Optional.empty();
    }

    public Optional<List<Author>> searchByCriteria(final int limit) throws ValueDataException, Exception {
        if (limit < 10)
            throw new ValueDataException("Limit is too small. Must be at least 10.", AppConst._KEY_CODE_PARAMS_ERROR);

        final var querySnapshot = this.getCollectionReference().limit(limit).get().get();
        return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
    }

}
