package com.yourroom.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

// -----------------------------------------------------------------------------
// JWT UTIL
// -----------------------------------------------------------------------------

/**
 * Clase utilitaria para la gestión de tokens JWT.
 *
 * Propósito:
 * - Generar tokens JWT firmados con una clave secreta.
 * - Extraer información (subject/username) de un token.
 * - Validar que un token sea correcto (firma, expiración, coincidencia con usuario).
 *
 * Notas:
 * - Utiliza la librería io.jsonwebtoken (jjwt).
 * - La clave secreta se inyecta desde application.properties (jwt.secret).
 * - El tiempo de expiración está configurado a 24 horas.
 */
@Component
public class JwtUtil {

    // -------------------------------------------------------------------------
    // CONFIGURACIÓN
    // -------------------------------------------------------------------------
    @Value("${jwt.secret}")
    private String secret; // Clave secreta definida en application.properties

    private final long expirationMillis = 1000 * 60 * 60 * 24; // 24h en milisegundos

    // -------------------------------------------------------------------------
    // MÉTODO INTERNO: obtener clave de firma
    // -------------------------------------------------------------------------
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // -------------------------------------------------------------------------
    // GENERAR TOKEN
    // -------------------------------------------------------------------------
    /**
     * Genera un token JWT para un email dado.
     *
     * @param email identificador del usuario (se usará como subject)
     * @return token JWT firmado y con expiración
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // subject = email del usuario
                .setIssuedAt(new Date()) // fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis)) // expiración
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // firma HS256
                .compact();
    }

    // -------------------------------------------------------------------------
    // OBTENER USERNAME (SUBJECT) DESDE EL TOKEN
    // -------------------------------------------------------------------------
    /**
     * Extrae el subject (username/email) desde el token.
     *
     * @param token JWT
     * @return subject (email)
     */
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // -------------------------------------------------------------------------
    // VALIDAR TOKEN
    // -------------------------------------------------------------------------
    /**
     * Valida que el token sea correcto y pertenezca al usuario.
     *
     * @param token       JWT a validar
     * @param userDetails objeto con datos del usuario autenticado
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // -------------------------------------------------------------------------
    // COMPROBAR EXPIRACIÓN
    // -------------------------------------------------------------------------
    private boolean isTokenExpired(String token) {
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }

    // -------------------------------------------------------------------------
    // OBTENER CLAIMS (CUERPO DEL TOKEN)
    // -------------------------------------------------------------------------
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
