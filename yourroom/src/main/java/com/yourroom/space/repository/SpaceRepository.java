package com.yourroom.space.repository;

import com.yourroom.space.model.Space;
import com.yourroom.space.model.SpaceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpaceRepository extends JpaRepository<Space, Long> {

    // Buscar un space concreto que pertenezca a un owner
    Optional<Space> findByIdAndOwnerId(Long id, Long ownerId);

    // Listar todos los spaces de un owner
    List<Space> findAllByOwnerId(Long ownerId);

    @Query("""
        SELECT s FROM Space s
        WHERE s.status = :status
          AND (
            :q IS NULL OR :q = '' OR
            LOWER(s.title) LIKE LOWER(CONCAT('%', :q, '%')) OR
            LOWER(s.location) LIKE LOWER(CONCAT('%', :q, '%')) OR
            LOWER(s.services) LIKE LOWER(CONCAT('%', :q, '%')) OR
            LOWER(s.description) LIKE LOWER(CONCAT('%', :q, '%'))
          )
        ORDER BY s.updatedAt DESC
    """)
    List<Space> searchByTextAndStatus(@Param("q") String q, @Param("status") SpaceStatus status);
}
