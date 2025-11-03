package com.yourroom.space.controller;

import com.yourroom.space.dto.*;
import com.yourroom.space.service.SpaceService;
import com.yourroom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService service;
    private final UserRepository userRepository;

    // -------------------------------------------------------------------------
// POST pantalla 1: Básicos -> crea un draft con ownerId + datos básicos
// -------------------------------------------------------------------------
    @PostMapping("/basics")
    public ResponseEntity<SpaceResponse> createBasics(
            @RequestBody SpaceBasicsRequest req,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {

        String email = principal.getUsername();
        Long ownerId = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"))
                .getId();

        SpaceResponse created = service.create(ownerId, req); // reutiliza tu lógica de create
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // -------------------------------------------------------------------------
// POST pantalla 2: Detalles -> añade/actualiza detalles de un space existente
// -------------------------------------------------------------------------
    @PostMapping("/{id}/details")
    public ResponseEntity<SpaceResponse> addDetails(
            @PathVariable Long id,
            @RequestBody SpaceDetailsRequest req,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {

        // Si quieres, aquí puedes validar propietario igual que en GET o en updateBasics.
        // Por simplicidad reutilizamos la misma lógica que tu PUT /{id}/details:
        return ResponseEntity.ok(service.updateDetails(id, principal.getUsername(), req));
    }



    @PutMapping("/{id}/basics")
    public ResponseEntity<SpaceResponse> updateBasics(
            @PathVariable Long id,
            @RequestBody SpaceBasicsRequest req,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {

        return ResponseEntity.ok(service.updateBasics(id, principal.getUsername(), req));
    }

    @PutMapping("/{id}/details")
    public ResponseEntity<SpaceResponse> updateDetails(
            @PathVariable Long id,
            @RequestBody SpaceDetailsRequest req,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {

        return ResponseEntity.ok(service.updateDetails(id, principal.getUsername(), req));
    }


    @GetMapping("/{id}")
    public ResponseEntity<SpaceResponse> getOne(
            @PathVariable Long id,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {

        return ResponseEntity.ok(service.getOneForOwner(id, principal.getUsername()));
    }

    @GetMapping("/me")
    public ResponseEntity<List<SpaceResponse>> getMine(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {

        return ResponseEntity.ok(service.getAllForOwner(principal.getUsername()));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteSpace(
            @PathVariable Long id,
            Authentication authentication
    ) {
        service.deleteDraft(id, authentication.getName());
        return ResponseEntity.noContent().build(); // 204
    }

    @PostMapping("/draft")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SpaceResponse> createDraft(Authentication auth) {
        System.out.println("➡ Usuario autenticado: " + auth.getName());//PRUEBAS
        return ResponseEntity.ok(service.createDraft(auth.getName()));
    }
    @PatchMapping("/{id}/publish")
    public ResponseEntity<SpaceResponse> publish(
            @PathVariable Long id,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        return ResponseEntity.ok(service.publish(id, principal.getUsername()));
    }




}
