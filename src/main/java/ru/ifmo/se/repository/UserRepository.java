package ru.ifmo.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ifmo.se.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCamundaId(String camundaId);
}