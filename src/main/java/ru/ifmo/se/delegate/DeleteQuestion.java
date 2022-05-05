package ru.ifmo.se.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ru.ifmo.se.repository.QuestionRepository;

@Component
public class DeleteQuestion implements JavaDelegate {
    private final QuestionRepository questionRepository;

    public DeleteQuestion(final QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        questionRepository.deleteById((long) delegateExecution.getVariable("questionId"));
    }
}
