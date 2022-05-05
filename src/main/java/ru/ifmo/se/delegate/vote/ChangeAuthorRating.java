package ru.ifmo.se.delegate.vote;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ru.ifmo.se.model.Question;
import ru.ifmo.se.repository.QuestionRepository;

@Component
public class ChangeAuthorRating implements JavaDelegate {
    private final QuestionRepository questionRepository;

    public ChangeAuthorRating(final QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long questionId = Long.parseLong((String) delegateExecution.getVariable("questionId"));
        Question question = questionRepository.getById(questionId);

        boolean isUpvote = (boolean) delegateExecution.getVariable("likeQuestion");

        if((int) delegateExecution.getVariable("lastVote") == 0) {
            if (isUpvote) {
                question.getUser().setRating(question.getUser().getRating() + 1);
            } else {
                question.getUser().setRating(question.getUser().getRating() - 1);
            }
        }else {
            if (isUpvote) {
                question.getUser().setRating(question.getUser().getRating() + 2);
            } else {
                question.getUser().setRating(question.getUser().getRating() - 2);
            }
        }
    }
}
