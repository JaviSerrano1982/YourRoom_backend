package com.yourroom.user.repository;

import com.yourroom.user.model.UserProfile;
import com.yourroom.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// -----------------------------------------------------------------------------
// USER PROFILE REPOSITORY
// -----------------------------------------------------------------------------

/**
 * Repositorio JPA para la entidad UserProfile.
 *
 * Propósito:
 * - Proporcionar operaciones CRUD (crear, leer, actualizar, borrar) sobre la tabla user_profiles.
 * - Definir consultas personalizadas relacionadas con perfiles de usuario.
 *
 * Métodos principales:
 * - findByUser(User user): busca el perfil a partir del objeto User.
 * - findByUser_Id(Long userId): busca el perfil a partir del ID del usuario.
 *
 * Nota:
 * - JpaRepository ya incluye métodos como save(), findById(), findAll(), deleteById(), etc.
 * - Los métodos adicionales definidos aquí aprovechan la convención de nombres de Spring Data JPA.
 */
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    // Busca un perfil a partir del objeto User
    Optional<UserProfile> findByUser(User user);

    // Busca un perfil a partir del ID del usuario
    Optional<UserProfile> findByUser_Id(Long userId);
}
