package com.yourroom.storage;

import com.yourroom.storage.dto.PresignRequest;
import com.yourroom.storage.dto.PresignResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/avatars")
public class AvatarController {

    private final MegaS4Service storage;

    public AvatarController(MegaS4Service storage) {
        this.storage = storage;
    }

    /**
     * Devuelve URLs firmadas para subir y leer un avatar.
     * - PUT: válido 15 minutos
     * - GET: válido 12 horas
     *
     * NOTA: contentType que firme el backend debe ser EXACTAMENTE
     * el que enviará el cliente en el PUT (ej: image/jpeg).
     */
    @PostMapping(value = "/presign", consumes = MediaType.APPLICATION_JSON_VALUE)
    public PresignResponse presign(
            @RequestParam long userId,
            @Valid @RequestBody PresignRequest body
    ) {
        String key = storage.buildProfileKey(userId, body.ext());
        String putUrl = storage.presignPut(key, body.contentType(), 15);
        String getUrl = storage.presignGet(key, 12);
        return new PresignResponse(key, putUrl, getUrl);
    }

    /**
     * Genera una URL firmada de lectura temporal para una key ya subida.
     * Ej: /api/avatars/url?key=profiles/42.jpg
     */
    @GetMapping("/url")
    public String signedGet(@RequestParam String key) {
        return storage.presignGet(key, 12);
    }
}
