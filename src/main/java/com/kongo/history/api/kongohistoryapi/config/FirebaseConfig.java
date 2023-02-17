package com.kongo.history.api.kongohistoryapi.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import  com.kongo.history.api.kongohistoryapi.auth.models.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Value;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;


@Configuration
public class FirebaseConfig {

    @Autowired
    SecurityProperties secProps;

    public static final String FIREBASE_PATH = "firebase_config.json";

    @Primary
    @Bean
    public void firebaseInit() {
        InputStream inputStream = null;
        try {
            inputStream = new ClassPathResource(FirebaseConfig.FIREBASE_PATH).getInputStream();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        try {

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
            System.out.println("Firebase Initialize");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public Firestore getFireStore(@Value(FirebaseConfig.FIREBASE_PATH) String credentialPath) throws IOException {
		var serviceAccount = new FileInputStream(credentialPath);
		var credentials = GoogleCredentials.fromStream(serviceAccount);

		var options = FirestoreOptions.newBuilder()
						.setCredentials(credentials).build();

		return options.getService();
	}
}
