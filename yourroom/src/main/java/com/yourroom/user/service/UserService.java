package com.yourroom.user.service;

import com.yourroom.user.model.User;

import java.util.Optional;
import java.util.List;

// -----------------------------------------------------------------------------
// USER SERVICE (INTERFACE)
// -----------------------------------------------------------------------------

/**
 * Interfaz para definir las operaciones relacionadas con la gestión de usuarios.
 *
 * Propósito:
 * - Establecer el contrato de métodos que la implementación deberá cumplir.
 * - Servir de capa intermedia entre los controladores y el acceso a datos.
 *
 * Métodos principales:
 * - registerUser(User user): registrar un nuevo usuario en la base de datos.
 * - getUserByEmail(String email): buscar un usuario por su email.
 * - getAllUsers(): obtener la lista completa de usuarios.
 */
public interface UserService {

    // -------------------------------------------------------------------------
    // REGISTRAR USUARIO
    // -------------------------------------------------------------------------
    User registerUser(User user);

    // -------------------------------------------------------------------------
    // BUSCAR USUARIO POR EMAIL
    // -------------------------------------------------------------------------
    Optional<User> getUserByEmail(String email);

    // -------------------------------------------------------------------------
    // OBTENER TODOS LOS USUARIOS
    // -------------------------------------------------------------------------
    List<User> getAllUsers();
}
