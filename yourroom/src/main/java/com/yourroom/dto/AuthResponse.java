package com.yourroom.dto;

// -----------------------------------------------------------------------------
// AUTH RESPONSE DTO
// -----------------------------------------------------------------------------

/**
 * DTO (Data Transfer Object) para la respuesta de autenticación.
 *
 * Propósito:
 * - Encapsular los datos que se devuelven al cliente después de un login correcto.
 * - Contiene:
 *   - token: JWT generado para el usuario autenticado.
 *   - userId: identificador único del usuario en la base de datos.
 *
 * Se utiliza en el UserController → login().
 */
public class AuthResponse {

    // -------------------------------------------------------------------------
    // ATRIBUTOS
    // -------------------------------------------------------------------------
    private String token;  // Token JWT de autenticación
    private Long userId;   // ID del usuario autenticado

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------
    /**
     * Constructor con parámetros para inicializar el DTO.
     *
     * @param token  Token JWT
     * @param userId ID del usuario
     */
    public AuthResponse(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    // -------------------------------------------------------------------------
    // GETTERS Y SETTERS
    // -------------------------------------------------------------------------
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
