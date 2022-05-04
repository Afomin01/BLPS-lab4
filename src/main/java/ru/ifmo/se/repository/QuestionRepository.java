package ru.ifmo.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ifmo.se.model.Question;

import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
}