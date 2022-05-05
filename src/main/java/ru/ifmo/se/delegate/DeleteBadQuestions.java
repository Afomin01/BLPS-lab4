package ru.ifmo.se.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.se.model.Question;
import ru.ifmo.se.repository.QuestionRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class DeleteBadQuestions implements JavaDelegate {
    private final QuestionRepository questionRepository;

    private final static int QUESTION_RATING_TO_DELETE = -25;

    public DeleteBadQuestions(final QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    @Transactional
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Started deleting bad questions.");
        AtomicInteger deletedQuestions = new AtomicInteger(0);

        List<Question> awfulQuestions = questionRepository.findByRatingLessThan(QUESTION_RATING_TO_DELETE);

        awfulQuestions.stream().parallel().forEach(question -> {
            questionRepository.delete(question);
            log.info("Question with id " + question.getId() + " with rating " + question.getRating() + " was deleted because it has too low rating.");
            deletedQuestions.incrementAndGet();
        });

        log.info("Finished managing bad questions. Deleted " + deletedQuestions.get() + " questions.");
    }
}
