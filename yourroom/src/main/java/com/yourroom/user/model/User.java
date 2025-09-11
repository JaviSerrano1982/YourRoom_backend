package com.yourroom.user.model;

import jakarta.persistence.*;

// -----------------------------------------------------------------------------
// USER ENTITY
// -----------------------------------------------------------------------------

/**
 * Entidad JPA que representa a un usuario en la base de datos.
 *
 * Mapeo:
 * - Tabla: users
 * - Campos:
 *   - id: clave primaria autogenerada.
 *   - name: nombre del usuario.
 *   - email: único y obligatorio (usado para login).
 *   - password: contraseña encriptada (BCrypt).
 *   - role: rol de usuario (ej. TRAINER, CLIENT, ADMIN).
 *
 * Uso principal:
 * - Autenticación y autorización de usuarios.
 * - Relacionar al usuario con su perfil u otras entidades del sistema.
 */
@Entity
@Table(name = "users")
public class User {

    // -------------------------------------------------------------------------
    // ATRIBUTOS
    // -------------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único del usuario

    private String name; // Nombre visible del usuario

    @Column(unique = true, nullable = false)
    private String email; // Email único, usado como login

    private String password; // Contraseña encriptada

    // Roles futuros (ej: TRAINER, CLIENT, ADMIN)
    private String role;

    // -------------------------------------------------------------------------
    // GETTERS Y SETTERS
    // -------------------------------------------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
