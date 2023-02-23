package com.kongo.history.api.kongohistoryapi.repository;

import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.dsig.keyinfo.KeyValue;

import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Storage;
import java.util.Optional;
import com.kongo.history.api.kongohistoryapi.model.entity.Category;
import com.kongo.history.api.kongohistoryapi.model.form.FindCategoryForm;
import com.kongo.history.api.kongohistoryapi.utils.AppConst;
import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;

@Repository
public class CategoryRepository extends AbstractFirestoreRepository<Category> {

    private static final String NAME = "category";

    protected CategoryRepository(Firestore firestore, Storage storage) {
        super(firestore, CategoryRepository.NAME, storage);
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

    private final List<KeyValue> getFindCategoryForm(final FindCategoryForm findCategoryForm) {
        List<KeyValue> values = new ArrayList<>();

        values.add(new KeyValue<>(Category.STATUS, findCategoryForm.getStatus()));
        values.add(new KeyValue<>(Category.NAME, findCategoryForm.getName()));

        return values;
    }

    public Optional<List<Category>> searchCriteria(final Integer limit) throws ValueDataException, Exception {
        if (limit < 10)
            throw new ValueDataException("Limit is too small. Must be at least 10.", AppConst._KEY_CODE_PARAMS_ERROR);

        final var querySnapshot = this.getCollectionReference().limit(limit).get().get();
        return Optional.of(this.makeListFromQuerySnapshots(querySnapshot));
    }

    public Optional<List<Category>> searchCriteria(final Integer limit, final FindCategoryForm findCategoryForm)
            throws ValueDataException, Exception {
        final var values = this.getFindCategoryForm(findCategoryForm);
        if (limit < 10)
            throw new ValueDataException("Limit is too small. Must be at least 10.", AppConst._KEY_CODE_PARAMS_ERROR);
        if (values.isEmpty())
            throw new ValueDataException("SOMETHING WENT WRONG! FindCategoryForm is empty",
                    AppConst._KEY_CODE_PARAMS_ERROR);

        final var query = this.getCollectionReference()
                .whereEqualTo(values.get(0).getKey(), values.get(0).getValue())
                .whereEqualTo(values.get(1).getKey(), values.get(1).getValue());
        final var querySnapshot = query.get().get();
        return Optional.ofNullable(this.makeListFromQuerySnapshots(querySnapshot));
    }

}
