package com.yourroom.space.repository;

import com.yourroom.space.model.Space;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpaceRepository extends JpaRepository<Space, Long> {

    // Buscar un space concreto que pertenezca a un owner
    Optional<Space> findByIdAndOwnerId(Long id, Long ownerId);

    // Listar todos los spaces de un owner
    List<Space> findAllByOwnerId(Long ownerId);
}
