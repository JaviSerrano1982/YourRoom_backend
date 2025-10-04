package com.yourroom.space.repository;

import com.yourroom.space.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findBySpace_IdOrderByIdAsc(Long spaceId);
    void deleteBySpace_Id(Long spaceId);
    long countBySpace_Id(Long spaceId);
}
