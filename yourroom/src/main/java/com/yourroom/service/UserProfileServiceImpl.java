package com.yourroom.service;

import com.yourroom.model.User;
import com.yourroom.model.UserProfile;
import com.yourroom.repository.UserProfileRepository;
import com.yourroom.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserProfileServiceImpl(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Optional<UserProfile> getProfileByUserId(Long userId) {
        return userRepository.findById(userId)
                .flatMap(userProfileRepository::findByUser);
    }

    @Override
    @Transactional
    public UserProfile createOrUpdateProfile(UserProfile incoming, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + userId));

        // Buscar SIEMPRE por userId (no por id del perfil)
        UserProfile profile = userProfileRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    UserProfile p = new UserProfile();
                    p.setUser(user); // vincular el usuario
                    System.out.println("👤 Creando perfil vacío para userId: " + userId);
                    return p;
                });

        // Copiar campos (modo "reemplazo"; si quieres "patch", ver variante abajo)
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
