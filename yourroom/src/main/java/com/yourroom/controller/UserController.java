package com.yourroom.controller;

import com.yourroom.model.User;
import com.yourroom.repository.UserRepository;
import com.yourroom.service.UserService;
import org.springframework.security.core.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import com.yourroom.util.JwtUtil;
import com.yourroom.dto.AuthResponse;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    // Endpoint para registrar un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        // Aquí podrías validar si ya existe el email
        Optional<User> existing = userService.getUserByEmail(user.getEmail());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().build(); // Email ya en uso
        }
        User newUser = userService.registerUser(user);
        return ResponseEntity.ok(newUser);
    }

    // Endpoint para obtener usuario por email (simula login básico)
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) {
        Optional<User> existing = userService.getUserByEmail(user.getEmail());
        if (existing.isPresent() &&
                passwordEncoder.matches(user.getPassword(), existing.get().getPassword())) {

            String token = jwtUtil.generateToken(existing.get().getEmail());
            return ResponseEntity.ok(new AuthResponse(token));
        }
        return ResponseEntity.status(401).build(); // Unauthorized
    }

    //Endpoint para obtener todos los usuarios.
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }


}
