package com.yourroom.space.service;

import com.yourroom.space.dto.PhotoRequest;
import com.yourroom.space.dto.PhotoResponse;
import com.yourroom.space.model.Photo;
import com.yourroom.space.model.Space;
import com.yourroom.space.repository.PhotoRepository;
import com.yourroom.space.repository.SpaceRepository;
import com.yourroom.storage.dto.FirebaseStorageService;
import com.yourroom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final SpaceRepository spaceRepo;
    private final PhotoRepository photoRepo;
    private final UserRepository userRepo;
    private final FirebaseStorageService storageService;

    private Long ownerId(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"))
                .getId();
    }

    private Space spaceOwnedBy(Long spaceId, Long ownerId) {
        Space s = spaceRepo.findById(spaceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Space no encontrado"));
        if (!s.getOwnerId().equals(ownerId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puede operar este espacio");
        return s;
    }

    @Override
    @Transactional
    public PhotoResponse addPhoto(Long spaceId, String ownerEmail, PhotoRequest req) {
        Long oid = ownerId(ownerEmail);
        Space s = spaceOwnedBy(spaceId, oid);

        if (req.primary) {
            // Asegura unicidad de principal
            for (Photo p : photoRepo.findBySpace_IdOrderByIdAsc(spaceId)) {
                if (p.isPrimaryPhoto()) { p.setPrimaryPhoto(false); }
            }
        }

        Photo p = new Photo();
        p.setSpace(s);
        p.setUrl(req.url);
        p.setPrimaryPhoto(req.primary);

        Photo saved = photoRepo.save(p);

        PhotoResponse resp = new PhotoResponse();
        resp.id = saved.getId();
        resp.spaceId = s.getId();
        resp.url = saved.getUrl();
        resp.primary = saved.isPrimaryPhoto();
        return resp;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoResponse> list(Long spaceId, String ownerEmail) {
        Long oid = ownerId(ownerEmail);
        spaceOwnedBy(spaceId, oid);
        return photoRepo.findBySpace_IdOrderByIdAsc(spaceId).stream().map(p -> {
            PhotoResponse r = new PhotoResponse();
            r.id = p.getId();
            r.spaceId = p.getSpace().getId();
            r.url = p.getUrl();
            r.primary = p.isPrimaryPhoto();
            return r;
        }).toList();
    }

    @Override
    @Transactional
    public void deleteAllForSpace(Long spaceId, String ownerEmail) {
        Long oid = ownerId(ownerEmail);
        spaceOwnedBy(spaceId, oid);

        // Borra en Storage + BD
        for (Photo p : photoRepo.findBySpace_IdOrderByIdAsc(spaceId)) {
            storageService.deleteByUrl(p.getUrl());
        }
        photoRepo.deleteBySpace_Id(spaceId);
    }

    @Override
    @Transactional
    public void deleteOne(Long photoId, String ownerEmail) {
        Long oid = ownerId(ownerEmail);
        Photo p = photoRepo.findById(photoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Foto no encontrada"));

        Space s = p.getSpace();
        if (!s.getOwnerId().equals(oid))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puede borrar esta foto");

        storageService.deleteByUrl(p.getUrl());
        photoRepo.deleteById(photoId);
    }
}
