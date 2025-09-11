package com.yourroom.space.controller;

import com.yourroom.space.dto.*;
import com.yourroom.space.service.SpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService service;

    @PostMapping
    public ResponseEntity<SpaceResponse> create(@RequestBody SpaceCreateRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @PutMapping("/{id}/details")
    public ResponseEntity<SpaceResponse> updateDetails(
            @PathVariable Long id, @RequestBody SpaceDetailsRequest req) {
        return ResponseEntity.ok(service.updateDetails(id, req));
    }
}
