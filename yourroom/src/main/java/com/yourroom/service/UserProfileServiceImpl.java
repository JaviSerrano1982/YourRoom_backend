package com.yourroom.service;

import com.yourroom.model.User;
import com.yourroom.model.UserProfile;
import com.yourroom.repository.UserProfileRepository;
import com.yourroom.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

// -----------------------------------------------------------------------------
// USER PROFILE SERVICE IMPLEMENTATION
// -----------------------------------------------------------------------------

/**
 * Implementación de UserProfileService.
 *
 * Propósito:
 * - Gestionar la lógica de negocio relacionada con perfiles de usuario.
 * - Interactuar con los repositorios UserRepository y UserProfileRepository.
 *
 * Funcionalidades principales:
 * - Obtener un perfil de usuario por su ID.
 * - Crear un perfil vacío si el usuario no lo tiene aún.
 * - Actualizar los datos de un perfil existente.
 */
@Service
public class UserProfileServiceImpl implements UserProfileService {

    // -------------------------------------------------------------------------
    // DEPENDENCIAS
    // -------------------------------------------------------------------------
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------
    @Autowired
    public UserProfileServiceImpl(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    // -------------------------------------------------------------------------
    // MÉTODO: OBTENER PERFIL POR USER ID
    // -------------------------------------------------------------------------
    /**
     * Busca un perfil de usuario a partir del ID del usuario.
     *
     * @param userId identificador del usuario
     * @return Optional con el perfil si existe
     */
    @Override
    public Optional<UserProfile> getProfileByUserId(Long userId) {
        return userRepository.findById(userId)
                .flatMap(userProfileRepository::findByUser);
    }

    // -------------------------------------------------------------------------
    // MÉTODO: CREAR O ACTUALIZAR PERFIL
    // -------------------------------------------------------------------------
    /**
     * Crea o actualiza el perfil de un usuario.
     *
     * Flujo:
     * - Comprueba si el usuario existe, si no lanza IllegalArgumentException.
     * - Busca el perfil asociado al usuario:
     *   - Si no existe, crea uno nuevo y lo vincula al usuario.
     * - Copia los datos del objeto recibido (incoming) al perfil existente o nuevo.
     * - Guarda los cambios en la base de datos.
     *
     * @param incoming objeto UserProfile con los datos nuevos/actualizados
     * @param userId   ID del usuario propietario del perfil
     * @return perfil guardado en base de datos
     */
    @Override
    @Transactional
    public UserProfile createOrUpdateProfile(UserProfile incoming, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + userId));

        // Buscar siempre por userId (no por id del perfil)
        UserProfile profile = userProfileRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    UserProfile p = new UserProfile();
                    p.setUser(user); // vincular el usuario
                    System.out.println("👤 Creando perfil vacío para userId: " + userId);
                    return p;
                });

        // Copiar campos del objeto recibido
        profile.setFirstName(incoming.getFirstName());
        profile.setLastName(incoming.getLastName());
        profile.setLocation(incoming.getLocation());
        profile.setGender(incoming.getGender());
        profile.setBirthDate(incoming.getBirthDate());
        profile.setPhone(incoming.getPhone());
        profile.setEmail(incoming.getEmail());
        profile.setPhotoUrl(incoming.getPhotoUrl());

        return userProfileRepository.save(profile);
    }
}
