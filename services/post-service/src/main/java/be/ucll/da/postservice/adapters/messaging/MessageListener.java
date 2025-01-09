package be.ucll.da.postservice.adapters.messaging;

import be.ucll.da.postservice.client.user.model.TaggedUsersValidatedEvent;
import be.ucll.da.postservice.client.user.model.UserValidatedEvent;
import be.ucll.da.postservice.domain.post.CreatePostSaga;
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

    private final CreatePostSaga createPostSaga;

    @Autowired
    public MessageListener(CreatePostSaga createPostSaga) {
        this.createPostSaga = createPostSaga;
    }

    @RabbitListener(queues = {"q.user-validated.post-service"})
    public void onUserValidated(UserValidatedEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.createPostSaga.executeSaga(event.getPostId(), event);
    }

    @RabbitListener(queues = {"q.tagged-users-validated.post-service"})
    public void onTaggedUserValidated(TaggedUsersValidatedEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.createPostSaga.executeSaga(event.getPostId(), event);
    }
}
