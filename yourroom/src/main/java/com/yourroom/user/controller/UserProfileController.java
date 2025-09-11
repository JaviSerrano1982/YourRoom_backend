package com.yourroom.user.controller;


import com.yourroom.user.dto.UserProfileResponse;
import com.yourroom.user.model.UserProfile;
import com.yourroom.user.repository.UserRepository;
import com.yourroom.user.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


// -----------------------------------------------------------------------------
// USER PROFILE CONTROLLER
// -----------------------------------------------------------------------------

/**
 * Controlador REST para la gestión del perfil de usuario.
 *
 * Endpoints principales:
 * - GET /api/profile/{userId} → Obtener el perfil de un usuario por su ID.
 * - POST /api/profile/{userId} → Crear o actualizar el perfil de un usuario.
 *
 * Notas:
 * - Usa UserProfileService para la lógica de negocio.
 * - Devuelve ResponseEntity para manejar respuestas HTTP correctas o de error.
 */
@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    // -------------------------------------------------------------------------
    // DEPENDENCIAS
    // -------------------------------------------------------------------------
    private final UserProfileService userProfileService;
    private final UserRepository userRepository;

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------
    /**
     * Inyección de dependencias mediante constructor.
     *
     * @param userProfileService servicio que gestiona la lógica de perfiles de usuario
     */
    @Autowired
    public UserProfileController(UserProfileService userProfileService, UserRepository userRepository) {
        this.userProfileService = userProfileService;
        this.userRepository = userRepository;
    }

    // -------------------------------------------------------------------------
    // ENDPOINT: OBTENER PERFIL DE USUARIO
    // -------------------------------------------------------------------------
    /**
     * Devuelve el perfil asociado a un usuario según su ID.
     *
     * @param userId ID del usuario
     * @return ResponseEntity con el perfil si existe, o 404 si no encontrado
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getProfile(@PathVariable Long userId) {
        return userProfileService.getProfileByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // -------------------------------------------------------------------------
    // ENDPOINT: CREAR O ACTUALIZAR PERFIL DE USUARIO
    // -------------------------------------------------------------------------
    /**
     * Crea o actualiza el perfil de un usuario.
     *
     * @param userId  ID del usuario
     * @param profile Datos del perfil a guardar
     * @return ResponseEntity con el perfil guardado o error si la petición no es válida
     */
    @PostMapping("/{userId}")
    public ResponseEntity<UserProfile> createOrUpdateProfile(
            @PathVariable Long userId,
            @RequestBody UserProfile profile
    ) {
        try {
            UserProfile saved = userProfileService.createOrUpdateProfile(profile, userId);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
