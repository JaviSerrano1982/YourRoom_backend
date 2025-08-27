package com.yourroom.service;

import com.yourroom.model.User;
import com.yourroom.model.UserProfile;
import com.yourroom.repository.UserProfileRepository;
import com.yourroom.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// -----------------------------------------------------------------------------
// USER SERVICE IMPLEMENTATION
// -----------------------------------------------------------------------------

/**
 * Implementación de UserService.
 *
 * Propósito:
 * - Gestionar la lógica de negocio de usuarios.
 * - Encargarse de registrar nuevos usuarios, buscar por email y listar usuarios.
 * - Crear automáticamente un perfil vacío asociado a cada usuario nuevo.
 *
 * Funcionalidades principales:
 * - Registro con encriptación de contraseña y rol por defecto.
 * - Búsqueda de usuario por email.
 * - Listado de todos los usuarios.
 */
@Service
public class UserServiceImpl implements UserService {

    // -------------------------------------------------------------------------
    // DEPENDENCIAS
    // -------------------------------------------------------------------------
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserProfileRepository userProfileRepository;

    // -------------------------------------------------------------------------
    // MÉTODO: REGISTRAR USUARIO
    // -------------------------------------------------------------------------
    /**
     * Registra un nuevo usuario en el sistema.
     *
     * Flujo:
     * - Asigna rol por defecto ("USUARIO") si no tiene uno definido.
     * - Encripta la contraseña con BCryptPasswordEncoder.
     * - Guarda el usuario en la base de datos.
     * - Si el usuario no tiene perfil, crea un perfil vacío vinculado al mismo.
     *
     * @param user objeto User con los datos del nuevo usuario
     * @return usuario guardado en la base de datos
     */
    @Override
    @Transactional
    public User registerUser(User user) {
        // Rol por defecto
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USUARIO");
        }

        // Cifrar contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Guardar usuario
        User saved = userRepository.save(user);

        // Crear perfil vacío si no existe (upsert por userId)
        userProfileRepository.findByUser_Id(saved.getId())
                .orElseGet(() -> {
                    UserProfile p = new UserProfile();
                    p.setUser(saved); // vincular al usuario
                    // Deja el resto de campos en null/por defecto
                    return userProfileRepository.save(p);
                });

        return saved;
    }

    // -------------------------------------------------------------------------
    // MÉTODO: BUSCAR USUARIO POR EMAIL
    // -------------------------------------------------------------------------
    /**
     * Busca un usuario en la base de datos por su email.
     *
     * @param email correo electrónico a buscar
     * @return Optional con el usuario si existe
     */
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // -------------------------------------------------------------------------
    // MÉTODO: LISTAR TODOS LOS USUARIOS
    // -------------------------------------------------------------------------
    /**
     * Devuelve la lista completa de usuarios registrados.
     *
     * @return lista de usuarios
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
