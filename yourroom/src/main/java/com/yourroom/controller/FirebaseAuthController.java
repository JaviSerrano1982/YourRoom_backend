package com.yourroom.controller;

import com.yourroom.service.FirebaseTokenService;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//endpoint GET /api/auth/firebase-token que devuelve { "token": "..." } para el usuario ya autenticado con tu JWT.
@RestController
@RequestMapping("/api/auth")
public class FirebaseAuthController {

    private final FirebaseTokenService firebaseTokenService;

    public FirebaseAuthController(FirebaseTokenService firebaseTokenService) {
        this.firebaseTokenService = firebaseTokenService;
    }

    @GetMapping("/firebase-token")
    public Map<String, String> getFirebaseToken(Authentication authentication) throws FirebaseAuthException {
        // Ajusta cómo obtienes el ID real del usuario
        String userId = authentication.getName(); // si aquí viene email, tradúcelo a ID con tu UserService
        String token = firebaseTokenService.createCustomTokenForUser(String.valueOf(userId));
        return Map.of("token", token);
    }
}