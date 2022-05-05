package ru.ifmo.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ifmo.se.model.Question;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findByRatingLessThan(int rating);

    @Query("select q from Question q where q.rating <= ?1 and q.needsModeration = false ")
    List<Question> findUnmoderatedQuestionsByRatingLessThan(int rating);

    @Query(value = "select q.id, q.title, q.text, q.creation_timeutc, q.rating, q.needs_moderation, q.user_id from Question q where q.needs_moderation = true limit 1", nativeQuery = true)
    Optional<Question> getAnyUnmoderatedQuestion();

}