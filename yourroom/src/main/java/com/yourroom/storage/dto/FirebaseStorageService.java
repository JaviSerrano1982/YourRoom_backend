package com.yourroom.storage.dto;

import com.google.firebase.cloud.StorageClient;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@Service
public class FirebaseStorageService {

    private static final Logger log = Logger.getLogger(FirebaseStorageService.class.getName());

    @Value("${firebase.storage.bucket}")
    private String bucketName;

    public void deleteByUrl(String url) {
        try {
            String objectPath = extractObjectPath(url);
            if (objectPath == null || objectPath.isBlank()) {
                log.warning(() -> "[Firebase delete] URL no soportada o sin path: " + url);
                return;
            }

            Bucket bucket = StorageClient.getInstance().bucket(bucketName);
            if (bucket == null) {
                log.severe(() -> "[Firebase delete] Bucket nulo. Revisa FirebaseAdminConfig/credenciales. bucket=" + bucketName);
                return;
            }

            Blob blob = bucket.get(objectPath);
            if (blob == null) {
                log.warning(() -> "[Firebase delete] Objeto no encontrado en bucket=" + bucketName + " path=" + objectPath);
                return;
            }

            boolean ok = blob.delete();
            if (ok) {
                log.info(() -> "[Firebase delete] Borrado OK: " + objectPath);
            } else {
                log.warning(() -> "[Firebase delete] delete() devolvió false para: " + objectPath);
            }

        } catch (Exception e) {
            // No rompas la transacción de BD, pero deja rastro
            log.severe("[Firebase delete] Error eliminando " + url + " : " + e.getMessage());
        }
    }

    // Soporta:
    // - https://firebasestorage.googleapis.com/v0/b/<bucket>/o/<encPath>?...
    // - https://storage.googleapis.com/<bucket>/<path>
    // - gs://<bucket>/<path>
    private String extractObjectPath(String url) {
        if (url == null) return null;

        if (url.startsWith("gs://")) {
            String after = url.substring("gs://".length());
            int slash = after.indexOf('/');
            return (slash >= 0) ? after.substring(slash + 1) : null;
        }

        try {
            URI uri = new URI(url);

            // v0 API
            if (uri.getHost() != null && uri.getHost().contains("firebasestorage.googleapis.com")) {
                String path = uri.getPath(); // /v0/b/<bucket>/o/<encPath>
                int idx = path.indexOf("/o/");
                if (idx >= 0) {
                    String enc = path.substring(idx + 3);
                    return URLDecoder.decode(enc, StandardCharsets.UTF_8);
                }
            }

            // storage.googleapis.com/<bucket>/<path>
            if (uri.getHost() != null && uri.getHost().equals("storage.googleapis.com")) {
                String path = uri.getPath(); // /<bucket>/<path>
                String prefix = "/" + bucketName + "/";
                if (path.startsWith(prefix)) {
                    return path.substring(prefix.length());
                }
            }
        } catch (Exception ignored) { }
        return null;
    }
}
