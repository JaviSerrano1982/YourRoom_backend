package com.yourroom.service;

import com.yourroom.dto.UserProfileRequest;
import com.yourroom.dto.UserProfileResponse;

public interface UserProfileService {
    UserProfileResponse getUserProfile(Long userId);
    UserProfileResponse createOrUpdateProfile(Long userId, UserProfileRequest request);
}
