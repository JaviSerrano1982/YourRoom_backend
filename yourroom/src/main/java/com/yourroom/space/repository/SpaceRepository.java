package com.yourroom.space.repository;

import com.yourroom.space.model.Space;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceRepository extends JpaRepository<Space, Long> {

    List<Space> findAllByOwnerIdOrderByIdDesc(Long ownerId);

}
