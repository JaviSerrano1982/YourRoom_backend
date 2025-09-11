package com.yourroom.user.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.stereotype.Service;

// -----------------------------------------------------------------------------
// FIREBASE TOKEN SERVICE
// -----------------------------------------------------------------------------

/**
 * Servicio encargado de la integración con Firebase Authentication.
 *
 * Propósito:
 * - Generar un token personalizado de Firebase (custom token) para un usuario
 *   autenticado en el sistema con JWT propio.
 *
 * Flujo de uso:
 * - El controlador FirebaseAuthController invoca este servicio pasando el userId.
 * - Este servicio utiliza el SDK de Firebase para crear el token.
 * - El token devuelto permite al cliente autenticarse en Firebase.
 */
@Service
public class FirebaseTokenService {

    // -------------------------------------------------------------------------
    // MÉTODO: createCustomTokenForUser
    // -------------------------------------------------------------------------
    /**
     * Genera un token personalizado de Firebase para el usuario indicado.
     *
     * @param userId identificador del usuario (usado como UID en Firebase)
     * @return token personalizado de Firebase
     * @throws FirebaseAuthException si ocurre un error al crear el token
     */
    public String createCustomTokenForUser(String userId) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().createCustomToken(userId);
    }
}
