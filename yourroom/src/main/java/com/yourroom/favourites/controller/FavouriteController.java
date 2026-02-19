package com.yourroom.favourites.controller;

import com.yourroom.favourites.model.Favourite;
import com.yourroom.favourites.service.FavouriteService;
import com.yourroom.space.dto.SpaceResponse;
import com.yourroom.user.model.User;
import com.yourroom.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.yourroom.space.dto.SpaceResponse;
import com.yourroom.space.service.SpaceService;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favourites")
public class FavouriteController {

    private final FavouriteService favouriteService;
    private final UserService userService;
    private final SpaceService spaceService;


    public FavouriteController(FavouriteService favouriteService,
                               UserService userService,
                               SpaceService spaceService) {
        this.favouriteService = favouriteService;
        this.userService = userService;
        this.spaceService = spaceService;
    }


    private Long getUserId(Authentication authentication) {
        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "User not found for email: " + email
                ));

        return user.getId();
    }

    @PostMapping("/{spaceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFavourite(@PathVariable Long spaceId, Authentication authentication) {
        Long userId = getUserId(authentication);
        favouriteService.addFavourite(userId, spaceId);
    }

    @DeleteMapping("/{spaceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFavourite(@PathVariable Long spaceId, Authentication authentication) {
        Long userId = getUserId(authentication);
        favouriteService.removeFavourite(userId, spaceId);
    }

    @GetMapping("/ids")
    public List<Long> getFavouriteIds(Authentication authentication) {
        Long userId = getUserId(authentication);

        return favouriteService.getUserFavourites(userId)
                .stream()
                .map(Favourite::getSpaceId)
                .collect(Collectors.toList());
    }
    @GetMapping
    public List<SpaceResponse> getFavourites(Authentication authentication) {
        Long userId = getUserId(authentication);

        List<Long> spaceIds = favouriteService.getUserFavourites(userId)
                .stream()
                .map(Favourite::getSpaceId)
                .toList();

        return spaceService.getPublishedByIds(spaceIds);
    }

}
