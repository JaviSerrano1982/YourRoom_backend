package com.yourroom.space.service;

import com.yourroom.space.dto.*;

public interface SpaceService {
    SpaceResponse create(SpaceCreateRequest req);
    SpaceResponse updateDetails(Long id, SpaceDetailsRequest req);
}
