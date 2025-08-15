package com.yourroom.storage.dto;

public record PresignResponse(
        String key,
        String putUrl,
        String getUrl
) {}
