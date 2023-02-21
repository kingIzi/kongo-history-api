package com.kongo.history.api.kongohistoryapi.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.kongo.history.api.kongohistoryapi.utils.AppConst;
import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.database.annotations.Nullable;
import com.kongo.history.api.kongohistoryapi.model.entity.BaseEntity;
//import com.kongo.history.api.kongohistoryapi.model.entity.BaseEntity;
import com.kongo.history.api.kongohistoryapi.utils.DocumentId;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.common.reflect.TypeToken;

import javax.validation.ValidationException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


@Slf4j
public class AbstractFirestoreRepository<T> {
    private final CollectionReference collectionReference;
    private final String collectionName;
    private final Class<T> parameterizedType;
    private final Storage storage;
    public static final String BUCKET_NAME = "kongo-history-database.appspot.com";
    public static final String PHOTOS = "photos";

    // protected AbstractFirestoreRepository(Firestore firestore, String collection) {
    //     this.collectionReference = firestore.collection(collection);
    //     this.collectionName = collection;
    //     this.parameterizedType = this.getParameterizedType();
    // }

    protected AbstractFirestoreRepository(Firestore firestore,String collectionName,Storage storage){
        this.collectionReference = firestore.collection(collectionName);
        this.collectionName = collectionName;
        this.parameterizedType = this.getParameterizedType();
        this.storage = storage;
    }

    protected Class<T> getParameterizedType(){
        final var type = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>)type.getActualTypeArguments()[0];
    }

    @Getter
    @Setter
    public class Pair{
        Object first;
        Object second;
        private Pair(final Object first,final Object second){
            this.first = first;
            this.second = second;
        }
    }

    public final Optional<T> save(T model){
        final var documentId = this.getDocumentId(model);
        try {
            if (documentId.isEmpty())
                throw new Exception("SOMETHING WENT WRONG PLEASE CONTACT SUPPORT!");
            
            ((BaseEntity) model).setId(documentId);
            final var resultApiFuture = collectionReference.document(documentId).set(model);
            log.info("{}-{} saved at{}", collectionName, documentId, resultApiFuture.get().getUpdateTime());
            return Optional.of(model);
            //return model;
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error saving {}={} {}", collectionName, documentId, e.getMessage());
        }
        catch(Exception e){
            e.printStackTrace();
            log.error("Error handling {}",e.getMessage());
        }
        return Optional.empty();
    }

    public final void save(final String documentId,final Map<String,Object> model){
        try {
            if (documentId.isEmpty())
                throw new Exception("SOMETHING WENT WRONG. DOCUMENT_ID WAS NULL FOR UPDATE");

            final var resultApiFuture = collectionReference.document(documentId).update(model);
            log.info("{}-{} saved at{}", collectionName, documentId, resultApiFuture.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error saving {}={} {}", collectionName, documentId, e.getMessage());
        }
        catch(Exception e){
            e.printStackTrace();
            log.error("Error handling {}",e.getMessage());
        }
    }

    public void delete(T model){
        final var documentId = this.getDocumentId(model);
        final var resultApiFuture = this.collectionReference.document(documentId).delete();
    }

    public void delete(final String documentId) throws Exception{
        if (documentId.isEmpty())
            throw new Exception("DOCUMENT_ID CANNOT BE EMPTY FOR DELETE!");

        final var resultApiFuture = this.collectionReference.document(documentId).delete();
    }

    public List<T> retrieveAll(){
        final var querySnapshotApiFuture = this.collectionReference.get();

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

    protected Storage getStorage(){
        return this.storage;
    }

    public List<T> makeListFromQuerySnapshots(final QuerySnapshot querySnapshot){
        final var documents = querySnapshot.getDocuments();
        return documents.stream().map(e -> e.toObject(this.parameterizedType)).collect(Collectors.toList());
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) {
        File tempFile = new File(fileName);
        try (final var fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private String uploadFile(File file, String fileName,String contentType) throws IOException {
        final var blobId = BlobId.of(AbstractFirestoreRepository.BUCKET_NAME, fileName);
        final var blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        final var DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media";
        final var blob = this.storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return String.format(DOWNLOAD_URL, AbstractFirestoreRepository.BUCKET_NAME,URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    public Optional<Pair> uploadFile(final MultipartFile file){
        try{
            String fileName = file.getOriginalFilename();                        // to get original file name
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName)); 
            File newFile = this.convertToFile(file, fileName);                      // to convert multipartFile to File
            final var TEMP_URL = this.uploadFile(newFile, fileName,file.getContentType());                                   // to get uploaded file link
            newFile.delete();                         
            return Optional.ofNullable(new Pair(fileName,TEMP_URL));
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean removeFile(final String fileName){
        final var blobId = BlobId.of(AbstractFirestoreRepository.BUCKET_NAME, fileName);
        return this.storage.delete(blobId);
    }

    //FILE DOWNLOAD
    // public Object download(String fileName) throws IOException {
    //     String destFileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));     // to set random strinh for destination file name
    //     String destFilePath = "Z:\\New folder\\" + destFileName;                                    // to set destination file path
        
    //     ////////////////////////////////   Download  ////////////////////////////////////////////////////////////////////////
    //     Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("path of JSON with genarated private key"));
    //     Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    //     Blob blob = storage.get(BlobId.of("your bucket name", fileName));
    //     blob.downloadTo(Paths.get(destFilePath));
    //     return sendResponse("200", "Successfully Downloaded!");
    // }

}




