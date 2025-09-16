package com.yourroom.space.service;

import com.yourroom.space.dto.PhotoRequest;
import com.yourroom.space.dto.PhotoResponse;

import java.util.List;

public interface PhotoService {
    PhotoResponse addPhoto(Long spaceId, String ownerEmail, PhotoRequest req);
    List<PhotoResponse> list(Long spaceId, String ownerEmail);
    void deleteAllForSpace(Long spaceId, String ownerEmail);
    void deleteOne(Long photoId, String ownerEmail);
}
