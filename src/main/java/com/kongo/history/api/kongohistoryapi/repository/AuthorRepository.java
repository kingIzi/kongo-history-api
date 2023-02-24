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

import com.google.firebase.database.utilities.Pair;
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

    private List<Pair<String,Object>> getFindAuthorFormData(final FindAuthorForm findAuthorForm){
        List<Pair<String,Object>> values = new ArrayList<>();
        values.add(new Pair<>(Author.STATUS, findAuthorForm.getStatus()));
        if (findAuthorForm.getId() != null && !findAuthorForm.getId().isBlank())
            values.add(new Pair<>(Author.ID, findAuthorForm.getId()));
        if (findAuthorForm.getFirstName() != null && !findAuthorForm.getFirstName().isBlank())
            values.add(new Pair<>(Author.FIRST_NAME, findAuthorForm.getFirstName()));
        if (findAuthorForm.getLastName() != null && !findAuthorForm.getLastName().isBlank())
            values.add(new Pair<>(Author.LAST_NAME, findAuthorForm.getLastName()));

        return values;
    }

    public Optional<List<Author>> searchByCriteria(final int limit, final FindAuthorForm findAuthorForm)
            throws Exception {
        final var values = this.getFindAuthorFormData(findAuthorForm);

        if (values.size() == 1) {
            final var query = this.getCollectionReference()
                    .whereEqualTo(values.get(0).getFirst(), values.get(0).getSecond()).limit(limit);
            final var querySnapshot = query.get().get();
            return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
        }
        if (values.size() == 2) {
            final var query = this.getCollectionReference()
                    .whereEqualTo(values.get(0).getFirst(), values.get(0).getSecond())
                    .whereEqualTo(values.get(1).getFirst(), values.get(1).getSecond());
            final var querySnapshot = query.get().get();
            return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
        }
        if (values.size() == 3) {
            final var query = this.getCollectionReference()
                    .whereEqualTo(values.get(0).getFirst(), values.get(0).getSecond())
                    .whereEqualTo(values.get(1).getFirst(), values.get(1).getSecond())
                    .whereEqualTo(values.get(2).getFirst(), values.get(2).getSecond());
            final var querySnapshot = query.get().get();
            return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
        }
        if (values.size() == 4) {
            final var query = this.getCollectionReference()
                    .whereEqualTo(values.get(0).getFirst(), values.get(0).getSecond())
                    .whereEqualTo(values.get(1).getFirst(), values.get(1).getSecond())
                    .whereEqualTo(values.get(2).getFirst(), values.get(2).getSecond())
                    .whereEqualTo(values.get(3).getFirst(), values.get(3).getSecond());
            final var querySnapshot = query.get().get();
            return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
        }
        return Optional.empty();
    }

    public Optional<List<Author>> searchByCriteria(final Integer limit) throws ValueDataException, Exception {
        final var querySnapshot = this.getCollectionReference().limit(limit == null ? 100 : limit).get().get();
        return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
    }

}
