package com.yourroom.space.service;

import com.yourroom.space.dto.*;
import com.yourroom.space.model.Space;
import com.yourroom.space.model.SpaceStatus;
import com.yourroom.space.repository.PhotoRepository;
import com.yourroom.space.repository.SpaceRepository;
import com.yourroom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceServiceImpl implements SpaceService {

    private final SpaceRepository repo;
    private final UserRepository userRepository;
    private final PhotoService photoService;
    private final PhotoRepository photoRepo;

    private static SpaceResponse toResponse(Space s) {
        SpaceResponse r = new SpaceResponse();
        r.id = s.getId();
        r.ownerId = s.getOwnerId();
        r.status = s.getStatus().name();
        r.title = s.getTitle();
        r.location = s.getLocation();
        r.addressLine = s.getAddressLine();
        r.capacity = s.getCapacity();
        r.hourlyPrice = s.getHourlyPrice();
        r.sizeM2 = s.getSizeM2();
        r.availability = s.getAvailability();
        r.services = s.getServices();
        r.description = s.getDescription();
        return r;
    }

    // ===================== BORRADO (idempotente por owner) =====================
    @Override
    @Transactional
    public void deleteDraft(Long id, String ownerEmail) {
        // En esta opción permitimos borrar cualquier status si es del owner (UX sencilla).
        Long ownerId = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"))
                .getId();

        repo.findById(id).ifPresent(space -> {
            if (!ownerId.equals(space.getOwnerId())) {
                // No es tuyo: no lanzamos excepción para idempotencia (o lanza FORBIDDEN si prefieres)
                return;
            }
            // 1) Borrar fotos asociadas (BD + Storage)
            photoService.deleteAllForSpace(space.getId(), ownerEmail);
            // 2) Borrar Space
            repo.delete(space);
        });
        // Si no existe, devolvemos 204 igualmente (idempotente): el Controller responde 204.
    }
    @Override
    @Transactional
    public SpaceResponse publish(Long id, String ownerEmail) {
        Long ownerId = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"))
                .getId();

        Space s = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Space no encontrado"));

        if (!s.getOwnerId().equals(ownerId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes publicar un space de otro usuario");

        // Validaciones mínimas (ajusta a tus reglas reales)
        if (s.getTitle() == null || s.getTitle().trim().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el título");
        if (s.getHourlyPrice() == null || s.getHourlyPrice().signum() < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Precio por hora inválido");
        if (photoRepo.countBySpace_Id(id) == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes subir al menos una foto");

        s.setStatus(SpaceStatus.PUBLISHED);
        return toResponse(repo.save(s));
    }

    // ===================== CREATE BASICS =====================
    @Override
    @Transactional
    public SpaceResponse create(Long ownerId, SpaceBasicsRequest req) {
        if (req.title == null || req.title.trim().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title es obligatorio");
        if (req.hourlyPrice == null || req.hourlyPrice.signum() < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "hourlyPrice debe ser >= 0");

        Space s = new Space();
        s.setOwnerId(ownerId);
        s.setStatus(SpaceStatus.DRAFT);
        s.setTitle(req.title.trim());
        s.setLocation(req.location);
        s.setAddressLine(req.addressLine);
        s.setCapacity(req.capacity);
        s.setHourlyPrice(req.hourlyPrice);
        return toResponse(repo.save(s));
    }

    // ===================== UPDATE BASICS =====================
    @Override
    @Transactional
    public SpaceResponse updateBasics(Long id, String ownerEmail, SpaceBasicsRequest req) {
        Long ownerId = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"))
                .getId();

        Space s = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Space no encontrado"));

        if (!s.getOwnerId().equals(ownerId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes editar un space de otro usuario");

        if (req.title == null || req.title.trim().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title es obligatorio");
        if (req.hourlyPrice == null || req.hourlyPrice.signum() < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "hourlyPrice debe ser >= 0");

        s.setTitle(req.title.trim());
        s.setLocation(req.location);
        s.setAddressLine(req.addressLine);
        s.setCapacity(req.capacity);
        s.setHourlyPrice(req.hourlyPrice);
        return toResponse(repo.save(s));
    }

    // ===================== UPDATE DETAILS =====================
    @Override
    @Transactional
    public SpaceResponse updateDetails(Long id, String ownerEmail, SpaceDetailsRequest req) {
        Space space = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Space no encontrado"));

        Long ownerId = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"))
                .getId();

        if (!ownerId.equals(space.getOwnerId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No eres el dueño de este espacio");

        if (req.sizeM2 != null) space.setSizeM2(req.sizeM2);
        if (req.availability != null) space.setAvailability(req.availability);
        if (req.services != null) space.setServices(req.services);
        if (req.description != null) space.setDescription(req.description);

        repo.save(space);
        return toResponse(space);
    }

    // ===================== GET ONE =====================
    @Override
    @Transactional(readOnly = true)
    public SpaceResponse getOneForOwner(Long id, String ownerEmail) {
        Long ownerId = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"))
                .getId();

        Space s = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Space no encontrado"));

        if (!s.getOwnerId().equals(ownerId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes ver un space de otro usuario");

        return toResponse(s);
    }

    // ===================== GET ALL =====================
    @Override
    @Transactional(readOnly = true)
    public List<SpaceResponse> getAllForOwner(String ownerEmail) {
        Long ownerId = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"))
                .getId();


        return repo.findAllByOwnerId(ownerId)
                .stream().map(SpaceServiceImpl::toResponse).toList();
    }

    // ===================== CREATE DRAFT =====================
    @Override
    @Transactional
    public SpaceResponse createDraft(String ownerEmail) {
        Long ownerId = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"))
                .getId();

        Space s = new Space();
        s.setOwnerId(ownerId);
        s.setStatus(SpaceStatus.DRAFT);

        // Si tu esquema aún exige NOT NULL, deja defaults. Si ya hiciste nullable, puedes quitarlos.
        s.setTitle("");
        s.setHourlyPrice(BigDecimal.ZERO);

        Space saved = repo.save(s);
        return toResponse(saved);
    }
}
