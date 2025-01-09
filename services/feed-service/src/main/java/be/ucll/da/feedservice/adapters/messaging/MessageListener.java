package be.ucll.da.feedservice.adapters.messaging;

import be.ucll.da.feedservice.client.user.model.UserCreatedEvent;
import be.ucll.da.feedservice.domain.user.User;
import be.ucll.da.feedservice.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class MessageListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private final UserRepository userRepository;

    @Autowired
    public MessageListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = {"q.user-feed-service"})
    public void onUserCreated(UserCreatedEvent event) {
        LOGGER.info("Received userCreatedEvent...");
        this.userRepository.save(new User(event.getUser().getId(), event.getUser().getFriends()));
    }
}
