package ru.ifmo.se.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.se.model.Tag;
import ru.ifmo.se.repository.TagRepository;

@Component
public class CreateNewTag implements JavaDelegate {
    private final TagRepository tagRepository;

    public CreateNewTag(final TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String tagName = (String) delegateExecution.getVariable("newTag");
        Tag tag = new Tag(tagName);
        tagRepository.save(tag);
    }
}
