package com.yourroom.storage.dto;

import jakarta.validation.constraints.NotBlank;

public record PresignRequest(
        @NotBlank String contentType, // ej: image/jpeg o image/png
        String ext                     // ej: jpg, png (opcional; por defecto jpg)
) {}
