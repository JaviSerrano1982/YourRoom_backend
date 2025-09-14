package com.yourroom.space.service;

import com.yourroom.space.dto.*;
import com.yourroom.space.model.Space;
import com.yourroom.space.model.SpaceStatus;
import com.yourroom.space.repository.SpaceRepository;
import com.yourroom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceServiceImpl implements SpaceService {

    private final SpaceRepository repo;
    private final UserRepository userRepository;

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
    @Override
    @Transactional
    public void deleteDraft(Long id, String ownerEmail) {
        // 1) Cargar el Space
        Space space = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Space no encontrado"));

        // 2) Obtener el id del usuario autenticado a partir del email
        Long ownerId = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"))
                .getId();

        // 3) Comprobar que el space pertenece al usuario (usa getOwnerId, no getOwner)
        if (!ownerId.equals(space.getOwnerId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para borrar este espacio");
        }

        // 4) Solo permitir borrar si está en DRAFT
        if (space.getStatus() != SpaceStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se pueden eliminar espacios en estado DRAFT");
        }

        // 5) Borrar
        repo.delete(space);
    }



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

    @Override
    @Transactional
    public SpaceResponse updateDetails(Long id, SpaceDetailsRequest req) {
        Space s = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Space no encontrado"));

        if (req.description != null && req.description.trim().length() < 30)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La descripción debe tener al menos 30 caracteres");
        if (req.sizeM2 != null && req.sizeM2 < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La superficie no puede ser negativa");

        if (req.sizeM2 != null) s.setSizeM2(req.sizeM2);
        if (req.availability != null) s.setAvailability(req.availability);
        if (req.services != null) s.setServices(req.services);
        if (req.description != null) s.setDescription(req.description.trim());

        return toResponse(repo.save(s));
    }

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

    @Override
    @Transactional(readOnly = true)
    public List<SpaceResponse> getAllForOwner(String ownerEmail) {
        Long ownerId = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"))
                .getId();

        return repo.findAllByOwnerIdOrderByIdDesc(ownerId)
                .stream().map(SpaceServiceImpl::toResponse).toList();
    }
}
