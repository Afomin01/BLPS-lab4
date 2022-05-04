package ru.ifmo.se.service;

import org.springframework.stereotype.Service;
import ru.ifmo.se.model.Tag;
import ru.ifmo.se.repository.TagRepository;
import ru.ifmo.se.service.impl.CamundaTagsUtils;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Set;

@Service
public class CamundaTagsUtilsImpl implements CamundaTagsUtils {
    private final TagRepository tagRepository;

    public CamundaTagsUtilsImpl(final TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Set<Tag> getTagsByNames(Set<String> tagNames) throws EntityNotFoundException {
        Set<Tag> tagSet = new HashSet<>();

        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName).orElseThrow(() -> new EntityNotFoundException("Can not find tag with name " + tagName));
            tagSet.add(tag);
        }

        return tagSet;
    }
}
