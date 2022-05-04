package ru.ifmo.se.delegate;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ru.ifmo.se.model.User;
import ru.ifmo.se.service.impl.CamundaUserAdapter;

@Component
public class CheckUserReputation implements JavaDelegate {
    private final CamundaUserAdapter camundaUserAdapter;

    public CheckUserReputation(final CamundaUserAdapter camundaUserAdapter) {
        this.camundaUserAdapter = camundaUserAdapter;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        User user = camundaUserAdapter.getCurrentUserEntity();

        if(user.getRating() < 0){
            throw new BpmnError("No_Rep_For_Question", "You must have positive reputation to ask questions.");
        }

        delegateExecution.setVariable("reputation", user.getRating());
    }
}
