package ru.ifmo.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.ifmo.se.model.UserQuestionVote;

import java.util.Optional;

public interface UserQuestionVoteRepository extends JpaRepository<UserQuestionVote, Long>, JpaSpecificationExecutor<UserQuestionVote> {
    @Query("select (count(u) > 0) from UserQuestionVote u where u.question.id = ?1 and u.user.id = ?2")
    boolean doesVoteForQuestionByUserExists(Long questionId, Long userId);

    @Query("select u from UserQuestionVote u where u.question.id = ?1 and u.user.id = ?2")
    Optional<UserQuestionVote> findByQuestionIdAndUserId(Long questionId, Long userId);
}