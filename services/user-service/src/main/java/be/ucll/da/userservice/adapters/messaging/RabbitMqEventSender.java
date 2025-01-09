package be.ucll.da.userservice.adapters.messaging;

import be.ucll.da.userservice.api.model.ApiUser;
import be.ucll.da.userservice.api.model.UserCreatedEvent;
import be.ucll.da.userservice.domain.EventSender;
import be.ucll.da.userservice.domain.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqEventSender implements EventSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMqEventSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendUserCreatedEvent(User user) {
        this.rabbitTemplate.convertAndSend("x.user-created", "", toEvent(user));
    }

    private UserCreatedEvent toEvent(User user) {
        return new UserCreatedEvent()
                .user(new ApiUser()
                        .id(user.getId())
                        .lastName(user.getLastName())
                        .firstName(user.getFirstName())
                        .email(user.getEmail())
                        .friends(user.getFriends()));
    }
}
