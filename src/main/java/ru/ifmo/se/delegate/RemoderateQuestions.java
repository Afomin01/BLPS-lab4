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
public class RemoderateQuestions implements JavaDelegate {
    private final QuestionRepository questionRepository;

    private final static int QUESTION_RATING_FOR_NEW_MODERATION = -10;

    public RemoderateQuestions(final QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    @Transactional
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Started managing bad but moderated questions.");

        AtomicInteger backToModerationQuestions = new AtomicInteger(0);

        List<Question> badQuestions = questionRepository.findUnmoderatedQuestionsByRatingLessThan(QUESTION_RATING_FOR_NEW_MODERATION);

        badQuestions.stream().parallel().forEach(question -> {
            question.setNeedsModeration(true);
            log.info("Question with id " + question.getId() + " with rating " + question.getRating() + " was sent back to moderation.");
            backToModerationQuestions.incrementAndGet();
        });

        log.info("Finished managing bad but moderated questions. Send back to moderation: " + backToModerationQuestions.get());
    }
}
