package com.yourroom.controller;

import com.yourroom.service.FirebaseTokenService;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// -----------------------------------------------------------------------------
// FIREBASE AUTH CONTROLLER
// -----------------------------------------------------------------------------

/**
 * Controlador REST para integración con Firebase Authentication.
 *
 * Objetivo principal:
 * - Exponer un endpoint GET `/api/auth/firebase-token` que devuelve un token
 *   personalizado de Firebase para el usuario autenticado mediante JWT interno.
 *
 * Flujo:
 * - El cliente se autentica primero contra el backend con JWT propio.
 * - Una vez autenticado, puede pedir un token Firebase usando este endpoint.
 * - El backend genera y devuelve un token válido de Firebase para ese usuario.
 */
@RestController
@RequestMapping("/api/auth")
public class FirebaseAuthController {

    // -------------------------------------------------------------------------
    // DEPENDENCIAS
    // -------------------------------------------------------------------------
    private final FirebaseTokenService firebaseTokenService; // Servicio que genera tokens Firebase

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------
    /**
     * Inyección del servicio de Firebase para generar tokens.
     */
    public FirebaseAuthController(FirebaseTokenService firebaseTokenService) {
        this.firebaseTokenService = firebaseTokenService;
    }

    // -------------------------------------------------------------------------
    // ENDPOINT: GET /api/auth/firebase-token
    // -------------------------------------------------------------------------
    /**
     * Devuelve un token personalizado de Firebase para el usuario autenticado.
     *
     * @param authentication Objeto de autenticación de Spring Security (contiene el usuario actual)
     * @return Mapa con la clave "token" y el valor del token Firebase generado
     * @throws FirebaseAuthException si ocurre un error al generar el token en Firebase
     */
    @GetMapping("/firebase-token")
    public Map<String, String> getFirebaseToken(Authentication authentication) throws FirebaseAuthException {
        // Obtiene el identificador del usuario autenticado.
        // Nota: authentication.getName() puede devolver email u otro dato según la configuración.
        // Si se requiere un ID distinto, usar un UserService para mapear email → ID.
        String userId = authentication.getName();

        // Generación del token de Firebase usando el ID del usuario
        String token = firebaseTokenService.createCustomTokenForUser(String.valueOf(userId));

        // Respuesta en formato JSON: { "token": "<firebase_token>" }
        return Map.of("token", token);
    }
}
