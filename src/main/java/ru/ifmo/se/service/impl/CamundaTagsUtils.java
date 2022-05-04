package ru.ifmo.se.service.impl;

import ru.ifmo.se.model.Tag;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

public interface CamundaTagsUtils {
    Set<Tag> getTagsByNames(Set<String> tagNames) throws EntityNotFoundException;
}
