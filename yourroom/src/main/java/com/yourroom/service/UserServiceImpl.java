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

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserProfileRepository userProfileRepository;


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


    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
