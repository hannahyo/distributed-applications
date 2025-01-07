package be.ucll.da.userservice.domain;

import be.ucll.da.userservice.api.model.ApiUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(ApiUser data) {
        User user = new User(
                data.getFirstName(),
                data.getLastName(),
                data.getEmail()
        );

        userRepository.save(user);
    }
}
