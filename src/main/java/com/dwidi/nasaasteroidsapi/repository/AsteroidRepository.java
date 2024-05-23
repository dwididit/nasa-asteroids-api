package com.dwidi.nasaasteroidsapi.repository;

import com.dwidi.nasaasteroidsapi.entity.AsteroidEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsteroidRepository extends JpaRepository<AsteroidEntity, Long> {
}
