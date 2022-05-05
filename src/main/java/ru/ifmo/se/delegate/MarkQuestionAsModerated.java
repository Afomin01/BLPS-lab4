package ru.ifmo.se.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.se.model.Question;
import ru.ifmo.se.repository.QuestionRepository;

@Component
public class MarkQuestionAsModerated implements JavaDelegate {
    private final QuestionRepository questionRepository;

    public MarkQuestionAsModerated(final QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    @Transactional
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long id = (Long) delegateExecution.getVariable("questionId");
        Question question = questionRepository.getById(id);
        question.setNeedsModeration(false);
        question.setTitle((String) delegateExecution.getVariable("questionTitle"));
        question.setText((String) delegateExecution.getVariable("questionText"));

    }
}
