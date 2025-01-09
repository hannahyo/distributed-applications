package be.ucll.da.userservice.adapters.messaging;

import be.ucll.da.userservice.api.model.*;
import be.ucll.da.userservice.domain.User;
import be.ucll.da.userservice.domain.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Transactional
public class MessageListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private final UserService userService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageListener(UserService userService, RabbitTemplate rabbitTemplate) {
        this.userService = userService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = {"q.user-service.validate-user"})
    public void onValidateUser(ValidateUserCommand command) {
        LOGGER.info("Received command: " + command);

        User user = userService.validateUser(command.getUserId());
        UserValidatedEvent event = new UserValidatedEvent();
        event.postId(command.getPostId());
        if (user != null) {
            event.userId(user.getId());
            event.firstName(user.getFirstName());
            event.lastName(user.getLastName());
            event.email(user.getEmail());
            event.isValid(true);
        } else {
            event.isValid(false);
        }

        LOGGER.info("Sending event: " + event);
        this.rabbitTemplate.convertAndSend("x.user-validated", "", event);
    }

    @RabbitListener(queues = {"q.user-service.validate-tagged-users"})
    public void onValidateTaggedUsers(ValidateTaggedUsersCommand command) {
        LOGGER.info("Received command: " + command);

        List<User> taggedUsers = userService.getUsersById(command.getTaggedUsers());

        TaggedUsersValidatedEvent event = new TaggedUsersValidatedEvent();
        event.postId(command.getPostId());

        if (taggedUsers != null) {
            for (User taggedUser : taggedUsers) {
                ApiUser user = new ApiUser();
                user.setId(taggedUser.getId());
                user.setFirstName(taggedUser.getFirstName());
                user.setLastName(taggedUser.getLastName());
                user.setEmail(taggedUser.getEmail());
                user.friends(taggedUser.getFriends());
                event.addTaggedUsersItem(user.getId());
                event.addEmailsItem(user.getEmail());
                event.isValid(true);
            }
        } else {
            event.isValid(false);
        }

        LOGGER.info("Sending event: " + event);
        this.rabbitTemplate.convertAndSend("x.tagged-users-validated", "", event);
    }

}
