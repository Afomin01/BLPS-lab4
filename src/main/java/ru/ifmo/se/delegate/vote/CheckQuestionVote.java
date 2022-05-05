package ru.ifmo.se.delegate.vote;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ru.ifmo.se.model.Question;
import ru.ifmo.se.model.User;
import ru.ifmo.se.model.UserQuestionVote;
import ru.ifmo.se.repository.QuestionRepository;
import ru.ifmo.se.repository.UserQuestionVoteRepository;
import ru.ifmo.se.service.impl.CamundaUserAdapter;

import java.util.Optional;

@Component
public class CheckQuestionVote implements JavaDelegate {
    private final QuestionRepository questionRepository;
    private final CamundaUserAdapter camundaUserAdapter;
    private final UserQuestionVoteRepository userQuestionVoteRepository;

    public CheckQuestionVote(final QuestionRepository questionRepository,
                             final CamundaUserAdapter camundaUserAdapter,
                             final UserQuestionVoteRepository userQuestionVoteRepository) {
        this.questionRepository = questionRepository;
        this.camundaUserAdapter = camundaUserAdapter;
        this.userQuestionVoteRepository = userQuestionVoteRepository;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long id = Long.parseLong((String) delegateExecution.getVariable("questionId"));
        Question question = questionRepository.getById(id);
        User user = camundaUserAdapter.getCurrentUserEntity();

        if (question.getUser().getId().equals(user.getId())) {
            throw new BpmnError("Can_Not_Vote_Own_Question", "You can not vote your own question.");
        }

        Optional<UserQuestionVote> userQuestionVote = userQuestionVoteRepository.findByQuestionIdAndUserId(question.getId(), user.getId());

        if (userQuestionVote.isPresent()) {
            boolean isLike = (boolean) delegateExecution.getVariable("likeQuestion");
            if (userQuestionVote.get().isUpvote() && isLike) {
                throw new BpmnError("Question_Already_Voted_Same", "You have already voted same on this question.");
            } else {
                delegateExecution.setVariable("lastVote", userQuestionVote.get().isUpvote() ? 1 : -1);
            }
        } else {
            delegateExecution.setVariable("lastVote", 0);
        }
    }
}
