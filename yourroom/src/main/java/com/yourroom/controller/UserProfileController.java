package com.yourroom.controller;

import com.yourroom.dto.UserProfileRequest;
import com.yourroom.dto.UserProfileResponse;
import com.yourroom.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public UserProfileResponse getProfile(@PathVariable Long userId) {
        return userProfileService.getUserProfile(userId);
    }

    @PostMapping("/{userId}")
    public UserProfileResponse updateProfile(@PathVariable Long userId,
                                             @RequestBody UserProfileRequest request) {
        return userProfileService.createOrUpdateProfile(userId, request);
    }
}
