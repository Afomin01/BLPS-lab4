package ru.ifmo.se.delegate.vote;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ru.ifmo.se.model.User;
import ru.ifmo.se.service.impl.CamundaUserAdapter;

@Component
public class ChangeVoterRating implements JavaDelegate {
    private final CamundaUserAdapter camundaUserAdapter;

    public ChangeVoterRating(final CamundaUserAdapter camundaUserAdapter) {
        this.camundaUserAdapter = camundaUserAdapter;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        User user = camundaUserAdapter.getCurrentUserEntity();

        if ((int) delegateExecution.getVariable("lastVote") != 0) {
            user.setRating(
                    user.getRating() + 1
            );
        }
    }
}
