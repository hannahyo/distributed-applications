package be.ucll.da.userservice.domain;

import be.ucll.da.userservice.api.model.ApiUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public void addFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException("User not found"));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new ServiceException("User not found"));

        if (user.getFriends().contains(friendId)) {
            throw new ServiceException("Friend already added");
        }

        user.addFriend(friendId);
        friend.addFriend(userId);

        userRepository.save(user);
        userRepository.save(friend);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
