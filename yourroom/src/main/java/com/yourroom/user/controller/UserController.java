package com.yourroom.user.controller;

import com.yourroom.user.model.User;
import com.yourroom.user.repository.UserRepository;
import com.yourroom.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import com.yourroom.util.JwtUtil;
import com.yourroom.user.dto.AuthResponse;

// -----------------------------------------------------------------------------
// USER CONTROLLER
// -----------------------------------------------------------------------------

/**
 * Controlador REST para la gestión de usuarios.
 *
 * Endpoints principales:
 * - POST /api/users/register → Registrar un nuevo usuario.
 * - POST /api/users/login → Autenticar usuario y devolver JWT + userId.
 * - GET /api/users → Obtener todos los usuarios (requiere autenticación).
 *
 * Dependencias inyectadas:
 * - UserService → Lógica de negocio para usuarios.
 * - JwtUtil → Generación de tokens JWT.
 * - BCryptPasswordEncoder → Validación de contraseñas en login.
 * - UserRepository → Acceso directo a la base de datos de usuarios.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    // -------------------------------------------------------------------------
    // DEPENDENCIAS
    // -------------------------------------------------------------------------
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    // -------------------------------------------------------------------------
    // ENDPOINT: REGISTRO DE USUARIO
    // -------------------------------------------------------------------------
    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param user Objeto con los datos del nuevo usuario
     * @return ResponseEntity con el usuario creado o error si el email ya existe
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        // Validación: comprobar si ya existe un usuario con ese email
        Optional<User> existing = userService.getUserByEmail(user.getEmail());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().build(); // Email ya en uso
        }
        User newUser = userService.registerUser(user);
        return ResponseEntity.ok(newUser);
    }

    // -------------------------------------------------------------------------
    // ENDPOINT: LOGIN DE USUARIO
    // -------------------------------------------------------------------------
    /**
     * Autentica un usuario verificando su email y contraseña.
     * Si es válido, genera un JWT y devuelve también el ID del usuario.
     *
     * @param user Objeto con email y password introducidos
     * @return AuthResponse con token JWT y userId, o 401 si credenciales incorrectas
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) {
        Optional<User> existing = userService.getUserByEmail(user.getEmail());
        if (existing.isPresent() &&
                passwordEncoder.matches(user.getPassword(), existing.get().getPassword())) {

            String token = jwtUtil.generateToken(existing.get().getEmail());
            Long userId = existing.get().getId();
            return ResponseEntity.ok(new AuthResponse(token, userId));
        }
        return ResponseEntity.status(401).build(); // Unauthorized
    }

    // -------------------------------------------------------------------------
    // ENDPOINT: LISTAR TODOS LOS USUARIOS
    // -------------------------------------------------------------------------
    /**
     * Devuelve la lista completa de usuarios de la base de datos.
     *
     * @return ResponseEntity con lista de usuarios
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
