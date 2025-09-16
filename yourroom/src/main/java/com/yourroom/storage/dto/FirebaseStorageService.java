package com.yourroom.storage.dto;



import com.google.firebase.cloud.StorageClient;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
public class FirebaseStorageService {

    @Value("${firebase.storage.bucket}")
    private String bucketName;

    public void deleteByUrl(String url) {
        try {
            String objectPath = extractObjectPath(url);
            if (objectPath == null || objectPath.isBlank()) return;

            Bucket bucket = StorageClient.getInstance().bucket(bucketName);
            Blob blob = bucket.get(objectPath);
            if (blob != null) {
                blob.delete();
            }
        } catch (Exception ignored) {
            // Si falla el borrado en Storage, no rompemos la transacción de BD
        }
    }

    // Soporta URLs tipo:
    // - https://firebasestorage.googleapis.com/v0/b/<bucket>/o/<obj-URL-encoded>?...
    // - gs://<bucket>/<path>
    private String extractObjectPath(String url) {
        if (url.startsWith("gs://")) {
            String after = url.substring("gs://".length());
            int slash = after.indexOf('/');
            return (slash >= 0) ? after.substring(slash + 1) : null;
        }
        try {
            URI uri = new URI(url);
            if (uri.getHost() != null && uri.getHost().contains("firebasestorage.googleapis.com")) {
                String path = uri.getPath(); // /v0/b/<bucket>/o/<encPath>
                int idx = path.indexOf("/o/");
                if (idx >= 0) {
                    String enc = path.substring(idx + 3);
                    return URLDecoder.decode(enc, StandardCharsets.UTF_8);
                }
            }
        } catch (Exception ignored) { }
        return null;
    }
}
