package ru.ifmo.se.service;

import org.camunda.bpm.engine.IdentityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.se.model.User;
import ru.ifmo.se.repository.UserRepository;
import ru.ifmo.se.service.impl.CamundaUserAdapter;

import java.util.Optional;

@Service
public class CamundaUserAdapterImpl implements CamundaUserAdapter {
    private final UserRepository userRepository;
    private final IdentityService identityService;

    public CamundaUserAdapterImpl(final UserRepository userRepository,
                                  final IdentityService identityService) {
        this.userRepository = userRepository;
        this.identityService = identityService;
    }

    public User getCurrentUserEntity(){
        String camundaUserId = identityService.getCurrentAuthentication().getUserId();
        Optional<User> userOptional = userRepository.findByCamundaId(camundaUserId);

        return userOptional.orElseGet(() -> createNewUser(camundaUserId));
    }

    @Transactional
    public User createNewUser(String camundaUserId){
        User user = new User();
        user.setCamundaId(camundaUserId);
        user = userRepository.save(user);
        return user;
    }
}
