package ru.ifmo.se.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ru.ifmo.se.repository.TagRepository;

@Component
public class CheckIfTagExists implements JavaDelegate {
    private final TagRepository tagRepository;

    public CheckIfTagExists(final TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String tagName = (String) delegateExecution.getVariable("newTag");
        boolean tagExists = tagRepository.existsByName(tagName);
        delegateExecution.setVariable("exist", 1);
    }
}
