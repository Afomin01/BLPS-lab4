package ru.ifmo.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ifmo.se.model.Tag;

import java.util.Optional;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByName(String name);

    Optional<Tag> findByName(String name);


}