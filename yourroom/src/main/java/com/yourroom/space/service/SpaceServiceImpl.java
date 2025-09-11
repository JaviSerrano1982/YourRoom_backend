package com.yourroom.space.service;

import com.yourroom.space.dto.*;
import com.yourroom.space.model.Space;
import com.yourroom.space.model.SpaceStatus;
import com.yourroom.space.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpaceServiceImpl implements SpaceService {

    private final SpaceRepository repo;

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
    public SpaceResponse create(SpaceCreateRequest req) {
        if (req.ownerId == null) throw new IllegalArgumentException("ownerId es obligatorio (temporal).");
        if (req.title == null || req.title.trim().isEmpty()) throw new IllegalArgumentException("title es obligatorio.");
        if (req.hourlyPrice == null || req.hourlyPrice.signum() < 0) throw new IllegalArgumentException("hourlyPrice >= 0.");

        Space s = new Space();
        s.setOwnerId(req.ownerId);
        s.setStatus(SpaceStatus.DRAFT);
        s.setTitle(req.title.trim());
        s.setLocation(req.location);
        s.setAddressLine(req.addressLine);
        s.setCapacity(req.capacity);
        s.setHourlyPrice(req.hourlyPrice);
        s.setSizeM2(req.sizeM2);
        s.setAvailability(req.availability);
        s.setServices(req.services);
        s.setDescription(req.description);

        return toResponse(repo.save(s));
    }

    @Override
    @Transactional
    public SpaceResponse updateDetails(Long id, SpaceDetailsRequest req) {
        Space s = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Space no encontrado"));
        if (req.description != null && req.description.trim().length() < 30)
            throw new IllegalArgumentException("La descripción debe tener al menos 30 caracteres.");
        if (req.sizeM2 != null && req.sizeM2 < 0)
            throw new IllegalArgumentException("La superficie no puede ser negativa.");

        if (req.sizeM2 != null) s.setSizeM2(req.sizeM2);
        if (req.availability != null) s.setAvailability(req.availability);
        if (req.services != null) s.setServices(req.services);
        if (req.description != null) s.setDescription(req.description.trim());

        return toResponse(repo.save(s));
    }
}
