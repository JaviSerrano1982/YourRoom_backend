package com.yourroom.user.dto;

import java.time.LocalDate;

// -----------------------------------------------------------------------------
// USER PROFILE REQUEST DTO
// -----------------------------------------------------------------------------

/**
 * DTO para recibir los datos del perfil de usuario desde el cliente.
 *
 * Propósito:
 * - Representar el cuerpo de la petición cuando se crea o actualiza un perfil.
 * - Se utiliza principalmente en UserProfileController y UserProfileService.
 *
 * Atributos incluidos:
 * - firstName: nombre del usuario.
 * - lastName: apellido del usuario.
 * - location: ubicación o ciudad.
 * - gender: género del usuario.
 * - birthDate: fecha de nacimiento.
 * - phone: número de teléfono de contacto.
 * - email: correo electrónico.
 * - photoUrl: URL de la foto de perfil.
 */
public class UserProfileRequest {

    // -------------------------------------------------------------------------
    // CAMPOS
    // -------------------------------------------------------------------------
    public String firstName;
    public String lastName;
    public String location;
    public String gender;
    public LocalDate birthDate;
    public String phone;
    public String email;
    public String photoUrl;
}
