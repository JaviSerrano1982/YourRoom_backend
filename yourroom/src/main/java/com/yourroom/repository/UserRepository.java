package com.yourroom.repository;

import com.yourroom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// -----------------------------------------------------------------------------
// USER REPOSITORY
// -----------------------------------------------------------------------------

/**
 * Repositorio JPA para la entidad User.
 *
 * Propósito:
 * - Proporcionar operaciones CRUD sobre la tabla users.
 * - Definir consultas personalizadas relacionadas con la entidad User.
 *
 * Métodos principales:
 * - findByEmail(String email): busca un usuario por su email.
 *
 * Nota:
 * - JpaRepository ya incluye métodos estándar como save(), findById(), findAll(), deleteById(), etc.
 * - El metodo findByEmail se añade porque el email es clave en la autenticación.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // Busca un usuario a partir de su email
    Optional<User> findByEmail(String email);
}
