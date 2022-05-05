package ru.ifmo.se.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ru.ifmo.se.model.Question;
import ru.ifmo.se.model.Tag;
import ru.ifmo.se.repository.QuestionRepository;
import ru.ifmo.se.service.impl.CamundaTagsUtils;
import ru.ifmo.se.service.impl.CamundaUserAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PublishQuestion implements JavaDelegate {
    private final QuestionRepository questionRepository;
    private final CamundaTagsUtils camundaTagsUtils;
    private final CamundaUserAdapter camundaUserAdapter;

    public PublishQuestion(final QuestionRepository questionRepository,
                           final CamundaTagsUtils camundaTagsUtils,
                           final CamundaUserAdapter camundaUserAdapter) {
        this.questionRepository = questionRepository;
        this.camundaTagsUtils = camundaTagsUtils;
        this.camundaUserAdapter = camundaUserAdapter;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        boolean needsModeration = false;

        if (delegateExecution.hasVariable("needsModeration")) {
            needsModeration = (boolean) delegateExecution.getVariable("needsModeration");
        }

        List<String> tagNames = (List<String>) delegateExecution.getVariable("questionTagsCollection");

        Set<Tag> tags = camundaTagsUtils.getTagsByNames(new HashSet<>(tagNames));

        Question question = new Question(
                (String) delegateExecution.getVariable("questionTitle"),
                (String) delegateExecution.getVariable("questionText"),
                0,
                camundaUserAdapter.getCurrentUserEntity(),
                tags,
                needsModeration
        );

        question = questionRepository.save(question);
    }
}
