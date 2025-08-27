package com.yourroom.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.io.IOException;

// -------------------------------------------------------------
// FIREBASE ADMIN: INICIALIZACIÓN DEL SDK
// -------------------------------------------------------------

/**
 * Clase de configuración que inicializa el **Firebase Admin SDK** al arrancar la aplicación.
 *
 * * ¿Qué hace?
 *   - Crea una instancia única de `FirebaseApp` si aún no existe.
 *   - Usa las credenciales por defecto de Google (`GoogleCredentials.getApplicationDefault()`).
 *
 * * ¿Cuándo corre?
 *   - En el arranque del contexto de Spring, gracias a `@PostConstruct`.
 *
 * * Requisitos de credenciales:
 *   - Local/Servidor: variable de entorno `GOOGLE_APPLICATION_CREDENTIALS` apuntando al JSON del service account
 *   ubicado en la carpeta secrets ( añadida al gitignore).
 *   - Alternativa: credenciales provistas por el entorno (p. ej., GCP).
 *
 * * Notas:
 *   - La condición `FirebaseApp.getApps().isEmpty()` evita inicializaciones duplicadas.
 *
 */
@Configuration
public class FirebaseAdminConfig {

    @PostConstruct
    public void init() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .build();
            FirebaseApp.initializeApp(options);
        }
    }
}
