package com.yourroom.favourites.repository;

import com.yourroom.favourites.model.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavouriteRepository extends JpaRepository<Favourite, Long> {

    List<Favourite> findByUserId(Long userId);

    Optional<Favourite> findByUserIdAndSpaceId(Long userId, Long spaceId);

    void deleteByUserIdAndSpaceId(Long userId, Long spaceId);

    boolean existsByUserIdAndSpaceId(Long userId, Long spaceId);
}
