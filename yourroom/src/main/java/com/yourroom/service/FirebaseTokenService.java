package com.yourroom.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.stereotype.Service;


//crea el custom token con uid = userId
@Service
public class FirebaseTokenService {
    public String createCustomTokenForUser(String userId) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().createCustomToken(userId);
    }
}