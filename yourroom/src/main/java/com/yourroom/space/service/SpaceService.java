package com.yourroom.space.service;

import com.yourroom.space.dto.*;

import java.util.List;

public interface SpaceService {
    SpaceResponse create(Long ownerId, SpaceBasicsRequest req);
    SpaceResponse updateBasics(Long id, String ownerEmail, SpaceBasicsRequest req);
    SpaceResponse updateDetails(Long id, SpaceDetailsRequest req);
    SpaceResponse getOneForOwner(Long id, String ownerEmail);
    List<SpaceResponse> getAllForOwner(String ownerEmail);
}
