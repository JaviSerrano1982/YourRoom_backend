package com.yourroom.space.repository;

import com.yourroom.space.model.Space;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceRepository extends JpaRepository<Space, Long> {}
