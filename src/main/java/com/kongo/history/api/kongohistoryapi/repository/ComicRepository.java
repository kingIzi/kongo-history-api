package com.kongo.history.api.kongohistoryapi.repository;

import com.google.firebase.database.utilities.Pair;
import com.kongo.history.api.kongohistoryapi.model.entity.Author;
import com.kongo.history.api.kongohistoryapi.model.form.FindAuthorForm;
import com.kongo.history.api.kongohistoryapi.model.form.FindComicForm;
import com.kongo.history.api.kongohistoryapi.utils.AppConst;
import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;
import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Storage;
import com.kongo.history.api.kongohistoryapi.model.entity.Comic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ComicRepository extends AbstractFirestoreRepository<Comic> {

    private static final String NAME = "comic";

    protected ComicRepository(Firestore firestore, Storage storage){
        super(firestore, ComicRepository.NAME,storage);
    }

    private List<Pair<String,Object>> getFindComicValues(final FindComicForm findComicForm){
        List<Pair<String,Object>> values = new ArrayList<>();
        values.add(new Pair<>(Comic.STATUS, AppConst._KEY_STATUS_ON));
        if (findComicForm.getName() != null && !findComicForm.getName().isEmpty())
            values.add(new Pair<>(Comic.NAME,findComicForm.getName()));
        if (findComicForm.getAuthorId() != null && !findComicForm.getAuthorId().isEmpty())
            values.add(new Pair<>(Comic.AUTHOR_ID,findComicForm.getAuthorId()));

        return values;
    }

    public Optional<List<Comic>> searchByCriteria(final Integer limit) throws Exception {
        final var querySnapshot = (limit == null) ? this.getCollectionReference().get().get() : this.getCollectionReference().limit(limit).get().get();
        return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
    }

    public Optional<List<Comic>> searchByCriteria(final Integer limit, final FindComicForm findComicForm) throws Exception{
        final var values = this.getFindComicValues(findComicForm);
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
        return Optional.empty();
    }
}
