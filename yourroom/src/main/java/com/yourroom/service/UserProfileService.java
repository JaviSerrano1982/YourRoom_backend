package com.yourroom.service;

import com.yourroom.model.UserProfile;

import java.util.Optional;

public interface UserProfileService {
    Optional<UserProfile> getProfileByUserId(Long userId);
    UserProfile createOrUpdateProfile(UserProfile profile, Long userId);
}
