package com.yourroom.service;

import com.yourroom.model.User;
import com.yourroom.model.UserProfile;
import com.yourroom.repository.UserProfileRepository;
import com.yourroom.repository.UserRepository;
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
    public UserProfile createOrUpdateProfile(UserProfile profile, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + userId));

        Optional<UserProfile> existingProfile = userProfileRepository.findByUser(user);

        if (existingProfile.isPresent()) {
            UserProfile existing = existingProfile.get();

            existing.setFirstName(profile.getFirstName());
            existing.setLastName(profile.getLastName());
            existing.setLocation(profile.getLocation());
            existing.setGender(profile.getGender());
            existing.setBirthDate(profile.getBirthDate());
            existing.setPhone(profile.getPhone());
            existing.setEmail(profile.getEmail());
            existing.setPhotoUrl(profile.getPhotoUrl());

            return userProfileRepository.save(existing);
        } else {
            profile.setUser(user);
            return userProfileRepository.save(profile);
        }
    }
}
