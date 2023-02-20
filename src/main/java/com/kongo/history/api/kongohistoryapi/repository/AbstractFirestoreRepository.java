package com.kongo.history.api.kongohistoryapi.repository;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.annotations.Nullable;
import com.kongo.history.api.kongohistoryapi.model.entity.BaseEntity;
//import com.kongo.history.api.kongohistoryapi.model.entity.BaseEntity;
import com.kongo.history.api.kongohistoryapi.utils.DocumentId;

import lombok.extern.slf4j.Slf4j;

import com.google.cloud.firestore.WriteResult;

import com.google.common.reflect.TypeToken;
import java.lang.reflect.Type;


@Slf4j
public class AbstractFirestoreRepository<T> {
    private final CollectionReference collectionReference;
    private final String collectionName;
    private final Class<T> parameterizedType;

    protected AbstractFirestoreRepository(Firestore firestore, String collection) {
        this.collectionReference = firestore.collection(collection);
        this.collectionName = collection;
        this.parameterizedType = this.getParameterizedType();
    }

    protected Class<T> getParameterizedType(){
        final var type = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>)type.getActualTypeArguments()[0];
    }

    public final T save(T model){
        final var documentId = this.getDocumentId(model);
        try {
            if (documentId.isEmpty())
                throw new Exception("SOMETHING WENT WRONG PLEASE CONTACT SUPPORT!");
            
            ((BaseEntity) model).setId(documentId);
            final var resultApiFuture = collectionReference.document(documentId).set(model);
            log.info("{}-{} saved at{}", collectionName, documentId, resultApiFuture.get().getUpdateTime());
            return model;
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error saving {}={} {}", collectionName, documentId, e.getMessage());
        }
        catch(Exception e){
            e.printStackTrace();
            log.error("Error handling {}",e.getMessage());
        }
        return null;
    }

    public void delete(T model){
        final var documentId = this.getDocumentId(model);
        final var resultApiFuture = collectionReference.document(documentId).delete();
    }

    public List<T> retrieveAll(){
        final var querySnapshotApiFuture = collectionReference.get();

        try {
            final var queryDocumentSnapshots = querySnapshotApiFuture.get().getDocuments();

            return queryDocumentSnapshots.stream()
                    .map(queryDocumentSnapshot -> queryDocumentSnapshot.toObject(parameterizedType))
                    .collect(Collectors.toList());

        } catch (InterruptedException | ExecutionException e) {
            log.error("Exception occurred while retrieving all document for {}", collectionName);
        }
        return Collections.<T>emptyList();

    }

    public Optional<T> get(String documentId){
        final var documentReference = collectionReference.document(documentId);
        final var documentSnapshotApiFuture = documentReference.get();

        try {
            final var documentSnapshot = documentSnapshotApiFuture.get();

            if(documentSnapshot.exists()){
                return Optional.ofNullable(documentSnapshot.toObject(parameterizedType));
            }

        } catch (InterruptedException | ExecutionException e) {
            log.error("Exception occurred retrieving: {} {}, {}", collectionName, documentId, e.getMessage());
        }

        return Optional.empty();

    }

    protected String getDocumentId(T t) {
        Object key;
        Class clzz = t.getClass();
        do{
            key = this.getKeyFromFields(clzz, t);
            clzz = clzz.getSuperclass();
        } while(key == null && clzz != null);

        if(key==null){
            return UUID.randomUUID().toString();
        }
        return String.valueOf(key);
    }

    private Object getKeyFromFields(Class<?> clazz, Object t) {

		return Arrays.stream(clazz.getDeclaredFields())
						.filter(field -> field.isAnnotationPresent(DocumentId.class))
						.findFirst()
						.map(field -> getValue(t, field))
						.orElse(null);
	  }
    
    @Nullable
	  private Object getValue(Object t, java.lang.reflect.Field field) {
		  field.setAccessible(true);
      try {
        return field.get(t);
      } catch (IllegalAccessException e) {
        log.error("Error in getting documentId key", e);
      }
		  return null;
	  }
  
    protected CollectionReference getCollectionReference(){
        return this.collectionReference;
    }
    
    protected Class<T> getType(){ 
        return this.parameterizedType; 
    }

    public List<T> makeListFromQuerySnapshots(final QuerySnapshot querySnapshot){
        final var documents = querySnapshot.getDocuments();
        return documents.stream().map(e -> e.toObject(this.parameterizedType)).collect(Collectors.toList());
    }

}




