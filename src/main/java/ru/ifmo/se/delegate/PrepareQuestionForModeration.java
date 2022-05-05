package ru.ifmo.se.delegate;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ru.ifmo.se.model.Question;
import ru.ifmo.se.repository.QuestionRepository;

import java.util.Optional;

@Component
public class PrepareQuestionForModeration implements JavaDelegate {
    private final QuestionRepository questionRepository;

    public PrepareQuestionForModeration(final QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Optional<Question> questionOptional = questionRepository.getAnyUnmoderatedQuestion();

        if(!questionOptional.isPresent()){
            throw new BpmnError("No_Question_To_Moderate", "No questions to moderate");
        }

        delegateExecution.setVariable("questionId", questionOptional.get().getId());
        delegateExecution.setVariable("questionTitle", questionOptional.get().getTitle());
        delegateExecution.setVariable("questionText", questionOptional.get().getText());
    }
}
