package com.yourroom.service;

import com.yourroom.model.UserProfile;

import java.util.Optional;

// -----------------------------------------------------------------------------
// USER PROFILE SERVICE (INTERFACE)
// -----------------------------------------------------------------------------

/**
 * Interfaz para definir las operaciones relacionadas con el perfil de usuario.
 *
 * Propósito:
 * - Establecer los métodos que deberá implementar la capa de servicio
 *   para gestionar perfiles de usuario.
 * - Facilitar la abstracción: el controlador depende de esta interfaz y no
 *   de una implementación concreta.
 *
 * Métodos principales:
 * - getProfileByUserId(Long userId): obtener el perfil de un usuario por su ID.
 * - createOrUpdateProfile(UserProfile profile, Long userId): crear o actualizar
 *   un perfil de usuario asociado a un ID concreto.
 */
public interface UserProfileService {

    // -------------------------------------------------------------------------
    // OBTENER PERFIL POR USER ID
    // -------------------------------------------------------------------------
    Optional<UserProfile> getProfileByUserId(Long userId);

    // -------------------------------------------------------------------------
    // CREAR O ACTUALIZAR PERFIL
    // -------------------------------------------------------------------------
    UserProfile createOrUpdateProfile(UserProfile profile, Long userId);
}
