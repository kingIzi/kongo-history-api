package com.kongo.history.api.kongohistoryapi.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;
import org.springframework.web.multipart.MultipartFile;

import com.google.firebase.database.utilities.Pair;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.database.annotations.Nullable;
import com.kongo.history.api.kongohistoryapi.model.entity.BaseEntity;
//import com.kongo.history.api.kongohistoryapi.model.entity.BaseEntity;
import com.kongo.history.api.kongohistoryapi.utils.DocumentId;

import lombok.extern.slf4j.Slf4j;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

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

    public final Optional<T> save(T model){
        final var documentId = this.getDocumentId(model);
        try {
            final var resultApiFuture = collectionReference.document(documentId).set(model);
            log.info("{}-{} saved at{}", collectionName, documentId, resultApiFuture.get().getUpdateTime());
            return Optional.of(model);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error saving {}={} {}", collectionName, documentId, e.getMessage());
        }
        catch(Exception e){
            e.printStackTrace();
            log.error("Error handling {}",e.getMessage());
        }
        return Optional.empty();
    }

    public final boolean save(final String documentId,final Map<String,Object> model){
        try {
            final var resultApiFuture = collectionReference.document(documentId).update(model);
            log.info("{}-{} saved at{}", collectionName, documentId, resultApiFuture.get().getUpdateTime());
            return true;
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error saving {}={} {}", collectionName, documentId, e.getMessage());
        }
        catch(Exception e){
            e.printStackTrace();
            log.error("Error handling {}",e.getMessage());
        }
        return false;
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

    public Optional<Pair<String,String>> uploadFile(final MultipartFile file){
        try{
            String fileName = file.getOriginalFilename();                        // to get original file name
            assert fileName != null;
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));
            final var newFile = this.convertToFile(file, fileName);                      // to convert multipartFile to File
            assert newFile != null;
            final var TEMP_URL = this.uploadFile(newFile, fileName,file.getContentType());                                   // to get uploaded file link
            final var isDeleted = newFile.delete();
            return Optional.of(new Pair<>(fileName,TEMP_URL));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void removeFile(final String fileName) throws ValueDataException{
        final var blobId = BlobId.of(AbstractFirestoreRepository.BUCKET_NAME, fileName);
        if (!this.storage.delete(blobId))
            throw new ValueDataException("Failed to remove file. FileName: " + fileName);
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




