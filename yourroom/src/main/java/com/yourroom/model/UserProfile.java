package com.yourroom.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

// -----------------------------------------------------------------------------
// USER PROFILE ENTITY
// -----------------------------------------------------------------------------

/**
 * Entidad JPA que representa el perfil de usuario en la base de datos.
 *
 * Mapeo:
 * - Tabla: user_profiles
 * - Relación: OneToOne con User (cada usuario tiene un perfil único).
 *
 * Campos principales:
 * - firstName, lastName: nombre y apellidos.
 * - location: ubicación o ciudad.
 * - gender: género.
 * - birthDate: fecha de nacimiento (formato dd/MM/yyyy en JSON).
 * - phone, email, photoUrl: información de contacto y foto de perfil.
 * - createdAt, updatedAt: marcas de tiempo automáticas.
 *
 * Ciclo de vida:
 * - @PrePersist → establece createdAt y updatedAt al crear.
 * - @PreUpdate → actualiza updatedAt en cada modificación.
 */
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    // -------------------------------------------------------------------------
    // ATRIBUTOS
    // -------------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único del perfil

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private User user; // Relación 1:1 con la entidad User

    private String firstName;
    private String lastName;
    private String location;
    private String gender;

    @JsonFormat(pattern = "dd/MM/yyyy") // Formato de fecha en la serialización JSON
    private LocalDate birthDate;

    private String phone;
    private String email;
    private String photoUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // -------------------------------------------------------------------------
    // EVENTOS DE ENTIDAD
    // -------------------------------------------------------------------------
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // -------------------------------------------------------------------------
    // GETTERS Y SETTERS
    // -------------------------------------------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
