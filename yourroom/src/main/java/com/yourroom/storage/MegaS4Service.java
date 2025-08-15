package com.yourroom.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.util.Locale;

@Service
public class MegaS4Service {

    private final S3Presigner presigner;

    @Value("${mega.s4.bucket}")
    private String bucket;

    public MegaS4Service(S3Presigner presigner) {
        this.presigner = presigner;
    }

    /**
     * Genera una clave (key) única o estable para el perfil del usuario.
     * Ejemplo: profiles/42.jpg
     */
    public String buildProfileKey(long userId, String ext) {
        String clean = (ext == null ? "jpg" : ext.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", ""));
        if (clean.isBlank()) clean = "jpg";
        return "profiles/" + userId + "." + clean;
    }

    /**
     * Genera URL firmada para subir (PUT) un archivo.
     */
    public String presignPut(String key, String contentType, int minutes) {
        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        PresignedPutObjectRequest p = presigner.presignPutObject(b -> b
                .signatureDuration(Duration.ofMinutes(minutes))
                .putObjectRequest(put));

        return p.url().toString();
    }

    /**
     * Genera URL firmada para descargar (GET) un archivo.
     */
    public String presignGet(String key, int hours) {
        GetObjectRequest get = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        PresignedGetObjectRequest p = presigner.presignGetObject(b -> b
                .signatureDuration(Duration.ofHours(hours))
                .getObjectRequest(get));

        return p.url().toString();
    }
}
