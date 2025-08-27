package com.yourroom.dto;

import java.time.LocalDate;

// -----------------------------------------------------------------------------
// USER PROFILE RESPONSE DTO
// -----------------------------------------------------------------------------

/**
 * DTO para devolver los datos del perfil de usuario al cliente.
 *
 * Propósito:
 * - Representar la respuesta cuando se consulta un perfil de usuario.
 * - Se utiliza en UserProfileController y UserProfileService.
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
public class UserProfileResponse {

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
