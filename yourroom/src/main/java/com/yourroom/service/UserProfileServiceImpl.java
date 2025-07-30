package com.yourroom.service;

import com.yourroom.dto.UserProfileRequest;
import com.yourroom.dto.UserProfileResponse;
import com.yourroom.model.User;
import com.yourroom.model.UserProfile;
import com.yourroom.repository.UserProfileRepository;
import com.yourroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public UserProfileResponse getUserProfile(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Optional<UserProfile> profile = userProfileRepository.findByUser(user.get());
        if (profile.isEmpty()) {
            throw new RuntimeException("User profile not found");
        }

        return toResponse(profile.get());
    }

    @Override
    public UserProfileResponse createOrUpdateProfile(Long userId, UserProfileRequest request) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UserProfile profile = userProfileRepository.findByUser(user.get())
                .orElse(new UserProfile());

        profile.setUser(user.get());
        profile.setFirstName(request.firstName);
        profile.setLastName(request.lastName);
        profile.setLocation(request.location);
        profile.setGender(request.gender);
        profile.setBirthDate(request.birthDate);
        profile.setPhone(request.phone);
        profile.setEmail(request.email);
        profile.setPhotoUrl(request.photoUrl);

        userProfileRepository.save(profile);

        return toResponse(profile);
    }

    private UserProfileResponse toResponse(UserProfile profile) {
        UserProfileResponse response = new UserProfileResponse();
        response.firstName = profile.getFirstName();
        response.lastName = profile.getLastName();
        response.location = profile.getLocation();
        response.gender = profile.getGender();
        response.birthDate = profile.getBirthDate();
        response.phone = profile.getPhone();
        response.email = profile.getEmail();
        response.photoUrl = profile.getPhotoUrl();
        return response;
    }
}
