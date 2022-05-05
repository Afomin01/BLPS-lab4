package ru.ifmo.se.delegate.vote;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ru.ifmo.se.model.Question;
import ru.ifmo.se.repository.QuestionRepository;

import java.util.Optional;

@Component
public class CheckIfQuestionExists implements JavaDelegate {
    private final QuestionRepository questionRepository;

    public CheckIfQuestionExists(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long id = Long.parseLong((String) delegateExecution.getVariable("questionId"));
        Optional<Question> questionOptional = questionRepository.findById(id);

        if (!questionOptional.isPresent()) {
            throw new BpmnError("Question_Not_Exists", "Question with id " + id);
        }
    }
}
