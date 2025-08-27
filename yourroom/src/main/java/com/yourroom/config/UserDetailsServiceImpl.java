package com.yourroom.config;

import com.yourroom.model.User;
import com.yourroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

// -----------------------------------------------------------------------------
// USER DETAILS SERVICE IMPL
// -----------------------------------------------------------------------------

/**
 * Implementación personalizada de UserDetailsService.
 *
 * Objetivo principal:
 * - Cargar un usuario desde la base de datos (UserRepository) a partir del email.
 * - Adaptar la entidad User propia al objeto UserDetails que utiliza Spring Security.
 *
 * Flujo:
 * - Spring Security invoca loadUserByUsername() durante el proceso de autenticación.
 * - Se busca el usuario en la base de datos por email.
 * - Si no existe, se lanza UsernameNotFoundException.
 * - Si existe, se devuelve un objeto UserDetails con email, password y roles (aquí vacío).
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // -------------------------------------------------------------------------
    // DEPENDENCIAS
    // -------------------------------------------------------------------------
    @Autowired
    private UserRepository userRepository; // Repositorio para acceder a la tabla de usuarios

    // -------------------------------------------------------------------------
    // MÉTODO PRINCIPAL: loadUserByUsername
    // -------------------------------------------------------------------------
    /**
     * Carga un usuario a partir del email y lo adapta a UserDetails.
     *
     * @param email Email del usuario a autenticar
     * @return UserDetails con las credenciales del usuario
     * @throws UsernameNotFoundException si el usuario no existe en la base de datos
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // Se devuelve un objeto User de Spring Security con email, password y lista de roles vacía
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}
