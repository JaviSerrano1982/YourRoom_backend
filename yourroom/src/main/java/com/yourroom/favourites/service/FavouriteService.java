package com.yourroom.favourites.service;

import com.yourroom.favourites.model.Favourite;
import com.yourroom.favourites.repository.FavouriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavouriteService {

    private final FavouriteRepository favouriteRepository;

    public FavouriteService(FavouriteRepository favouriteRepository) {
        this.favouriteRepository = favouriteRepository;
    }

    @Transactional
    public void addFavourite(Long userId, Long spaceId) {
        if (!favouriteRepository.existsByUserIdAndSpaceId(userId, spaceId)) {
            favouriteRepository.save(new Favourite(userId, spaceId));
        }
    }

    @Transactional
    public void removeFavourite(Long userId, Long spaceId) {
        favouriteRepository.deleteByUserIdAndSpaceId(userId, spaceId);
    }

    public List<Favourite> getUserFavourites(Long userId) {
        return favouriteRepository.findByUserId(userId);
    }
}
