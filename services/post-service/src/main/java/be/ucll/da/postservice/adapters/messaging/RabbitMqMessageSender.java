package be.ucll.da.postservice.adapters.messaging;

import be.ucll.da.postservice.client.notification.model.SendEmailCommand;
import be.ucll.da.postservice.client.user.model.ValidateTaggedUsersCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RabbitMqMessageSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(RabbitMqMessageSender.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMqMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void validateTaggedUsersCommand(Integer postId, List<Integer> taggedUsers) {
        var command = new ValidateTaggedUsersCommand();
        command.taggedUsers(taggedUsers);
        command.postId(postId);
        sendToQueue("q.post-service.validate-tagged-users", command);
    }

    public void sendEmail(List<String> recipients, String message) {
        var command = new SendEmailCommand();
        command.recipients(recipients);
        command.message(message);
        sendToQueue("q.notification-service.send-email", command);
    }

    private void sendToQueue(String queue, Object message) {
        LOGGER.info("Sending message: " + message);

        this.rabbitTemplate.convertAndSend(queue, message);
    }
}
