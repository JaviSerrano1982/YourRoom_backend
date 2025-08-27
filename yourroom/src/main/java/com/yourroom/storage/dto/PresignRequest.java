package com.yourroom.storage.dto;

import jakarta.validation.constraints.NotBlank;

// -----------------------------------------------------------------------------
// PRESIGN REQUEST DTO
// -----------------------------------------------------------------------------

/**
 * DTO para solicitar la generación de una URL prefirmada (presigned URL).
 *
 * Propósito:
 * - Representar los datos que envía el cliente al backend para crear una
 *   URL temporal donde subir archivos (por ejemplo, imágenes de perfil).
 *
 * Campos:
 * - contentType: tipo MIME del archivo (obligatorio, ej: image/jpeg, image/png).
 * - ext: extensión del archivo (opcional, ej: jpg, png). Por defecto "jpg".
 *
 * Validaciones:
 * - @NotBlank en contentType → asegura que el cliente siempre lo envía.
 */
public record PresignRequest(
        @NotBlank String contentType, // ej: image/jpeg o image/png
        String ext                    // ej: jpg, png (opcional; por defecto jpg)
) {}
