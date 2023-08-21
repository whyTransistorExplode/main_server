package com.netomass.main_server.repository;

import com.netomass.main_server.entity.Path;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PathRepository extends JpaRepository<Path, Long> {
    Optional<List<Path>> findAllByUserId(long user_id);
    Optional<Path> findByUserId(long user_id);
}
