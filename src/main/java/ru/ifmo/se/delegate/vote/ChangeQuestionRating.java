package ru.ifmo.se.delegate.vote;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.se.model.Question;
import ru.ifmo.se.model.User;
import ru.ifmo.se.model.UserQuestionVote;
import ru.ifmo.se.repository.QuestionRepository;
import ru.ifmo.se.repository.UserQuestionVoteRepository;
import ru.ifmo.se.service.impl.CamundaUserAdapter;

@Component
public class ChangeQuestionRating implements JavaDelegate {
    private final QuestionRepository questionRepository;
    private final UserQuestionVoteRepository userQuestionVoteRepository;
    private final CamundaUserAdapter camundaUserAdapter;

    public ChangeQuestionRating(final QuestionRepository questionRepository,
                                final UserQuestionVoteRepository userQuestionVoteRepository,
                                final CamundaUserAdapter camundaUserAdapter) {
        this.questionRepository = questionRepository;
        this.userQuestionVoteRepository = userQuestionVoteRepository;
        this.camundaUserAdapter = camundaUserAdapter;
    }

    @Override
    @Transactional
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long questionId = Long.parseLong((String) delegateExecution.getVariable("questionId"));
        Question question = questionRepository.getById(questionId);
        User user = camundaUserAdapter.getCurrentUserEntity();

        boolean isUpvote = (boolean) delegateExecution.getVariable("likeQuestion");

        if ((int) delegateExecution.getVariable("lastVote") == 0) {
            UserQuestionVote userQuestionVote = new UserQuestionVote(
                    question,
                    user,
                    isUpvote
            );
            question.setRating(question.getRating() + (isUpvote ? 1 : -1));
            userQuestionVoteRepository.save(userQuestionVote);
        } else {
            UserQuestionVote userQuestionVote = userQuestionVoteRepository.findByQuestionIdAndUserId(questionId, user.getId()).get();
            userQuestionVote.setUpvote(isUpvote);
            question.setRating(question.getRating() + (isUpvote ? 2 : -2));
        }
    }
}
