package ru.ifmo.se.delegate;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ValidateQuestionData implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String title = (String) delegateExecution.getVariable("questionTitle");
        String text = (String) delegateExecution.getVariable("questionText");
        List<String> tagsNamesList = Arrays.asList(((String) delegateExecution.getVariable("questionTags")).split(","));
        Set<String> tagNamesUnique = new HashSet<>(tagsNamesList);

        if(StringUtils.isEmpty(title) || title.length() < 16 || title.length() > 255){
            throw new BpmnError("Invalid_Question_Data", "Title length must be in range [16;255]");
        }
        if(StringUtils.isEmpty(text) || text.length() < 64){
            throw new BpmnError("Invalid_Question_Data", "Question must be longer than 64 symbols");
        }
        if(CollectionUtils.isEmpty(tagNamesUnique) || tagNamesUnique.size() < 3 || tagNamesUnique.size() > 10){
            throw new BpmnError("Invalid_Question_Data", "Question must have at least 3 tags and no more than 10 tags.");
        }

        delegateExecution.setVariable("questionTagsCollection", tagsNamesList);
    }
}
