package be.ucll.da.userservice.adapters.messaging;

import be.ucll.da.userservice.domain.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
