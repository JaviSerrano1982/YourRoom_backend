package com.yourroom.space.service;

import com.yourroom.space.dto.*;
import com.yourroom.space.model.Space;
import com.yourroom.space.model.SpaceStatus;
import jakarta.transaction.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface SpaceService {
    SpaceResponse create(Long ownerId, SpaceBasicsRequest req);
    SpaceResponse updateBasics(Long id, String ownerEmail, SpaceBasicsRequest req);
    SpaceResponse updateDetails(Long id, String ownerEmail, SpaceDetailsRequest req);
    SpaceResponse getOneForOwner(Long id, String ownerEmail);
    List<SpaceResponse> getAllForOwner(String ownerEmail);
    void deleteDraft(Long id, String ownerEmail);
    SpaceResponse createDraft(String ownerEmail);
    SpaceResponse publish(Long id, String ownerEmail);

}
