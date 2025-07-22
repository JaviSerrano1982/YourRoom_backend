package com.yourroom.service;

import com.yourroom.model.User;

import java.util.Optional;
import java.util.List;

public interface UserService {
    User registerUser(User user);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
}
