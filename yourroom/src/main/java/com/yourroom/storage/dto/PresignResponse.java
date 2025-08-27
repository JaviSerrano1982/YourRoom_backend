package com.yourroom.storage.dto;

// -----------------------------------------------------------------------------
// PRESIGN RESPONSE DTO
// -----------------------------------------------------------------------------

/**
 * DTO para devolver al cliente la información de una URL prefirmada (presigned URL).
 *
 * Propósito:
 * - Responder al cliente con los datos necesarios para subir y luego acceder
 *   a un archivo en el almacenamiento externo (ej. S3, Firebase Storage, etc.).
 *
 * Campos:
 * - key: identificador único del archivo en el almacenamiento.
 * - putUrl: URL prefirmada para subir el archivo (HTTP PUT).
 * - getUrl: URL pública o prefirmada para descargar/consultar el archivo.
 *
 * Flujo típico:
 * - El cliente envía un PresignRequest indicando el tipo de archivo.
 * - El backend genera la URL prefirmada y responde con PresignResponse.
 * - El cliente sube el archivo a putUrl y luego puede accederlo vía getUrl.
 */
public record PresignResponse(
        String key,
        String putUrl,
        String getUrl
) {}
