package com.yourroom.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

@Configuration
public class FirebaseAdminConfig {

    private static final Logger log = Logger.getLogger(FirebaseAdminConfig.class.getName());

    @Value("${firebase.storage.bucket}")
    private String bucketName;


    @Value("${firebase.credentials:}")
    private String credentialsLocation;

    @PostConstruct
    public void init() throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) return;

        GoogleCredentials creds;
        if (credentialsLocation != null && !credentialsLocation.isBlank()) {
            log.info(() -> "[Firebase] Cargando credenciales desde: " + credentialsLocation);
            if (credentialsLocation.startsWith("classpath:")) {
                String path = credentialsLocation.substring("classpath:".length());
                Resource res = new ClassPathResource(path);
                try (InputStream is = res.getInputStream()) {
                    creds = GoogleCredentials.fromStream(is);
                }
            } else if (credentialsLocation.startsWith("file:")) {
                String path = credentialsLocation.substring("file:".length());
                try (InputStream is = new FileInputStream(path)) {
                    creds = GoogleCredentials.fromStream(is);
                }
            } else {
                throw new IllegalArgumentException("firebase.credentials debe empezar por classpath: o file:");
            }
        } else {
            log.info("[Firebase] Usando Application Default Credentials (GOOGLE_APPLICATION_CREDENTIALS).");
            creds = GoogleCredentials.getApplicationDefault();
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(creds)
                .setStorageBucket(bucketName)
                .build();

        FirebaseApp.initializeApp(options);
        log.info("[Firebase] Inicializado con bucket=" + bucketName);
    }
}
