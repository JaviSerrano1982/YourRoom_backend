package com.yourroom.space.controller;

import com.yourroom.space.dto.PhotoRequest;
import com.yourroom.space.dto.PhotoResponse;
import com.yourroom.space.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/{spaceId}/photos")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PhotoResponse> addPhoto(
            @PathVariable Long spaceId,
            @RequestBody PhotoRequest req,
            Authentication auth) {
        return ResponseEntity.ok(photoService.addPhoto(spaceId, auth.getName(), req));
    }

    @GetMapping("/{spaceId}/photos")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PhotoResponse>> list(
            @PathVariable Long spaceId,
            Authentication auth) {
        return ResponseEntity.ok(photoService.list(spaceId, auth.getName()));
    }

    @DeleteMapping("/{spaceId}/photos")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteAll(
            @PathVariable Long spaceId,
            Authentication auth) {
        photoService.deleteAllForSpace(spaceId, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/photos/{photoId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteOne(
            @PathVariable Long photoId,
            Authentication auth) {
        photoService.deleteOne(photoId, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
