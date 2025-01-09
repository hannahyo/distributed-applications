package be.ucll.da.postservice.adapters.messaging;

import be.ucll.da.postservice.api.model.ApiPost;
import be.ucll.da.postservice.api.model.PostCreatedEvent;
//import be.ucll.da.postservice.client.notification.model.SendEmailCommand;
import be.ucll.da.postservice.client.feed.model.ValidatePostInFeedCommand;
import be.ucll.da.postservice.client.notification.model.SendEmailCommand;
import be.ucll.da.postservice.client.user.model.ApiUser;
import be.ucll.da.postservice.client.user.model.ValidateTaggedUsersCommand;
import be.ucll.da.postservice.client.user.model.ValidateUserCommand;
import be.ucll.da.postservice.client.user.model.ValidateUserLikedCommand;
import be.ucll.da.postservice.domain.post.Post;
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

    public void sendValidateUserCommand(Integer postId, Integer userId) {
        var command = new ValidateUserCommand();
        command.userId(userId);
        command.postId(postId);
        sendToQueue("q.user-service.validate-user", command);
    }

    public void sendValidateTaggedUsersCommand(Integer postId, List<Integer> taggedUsers) {
        var command = new ValidateTaggedUsersCommand();
        command.taggedUsers(taggedUsers);
        command.postId(postId);
        sendToQueue("q.user-service.validate-tagged-users", command);
    }

    public void sendPostCreatedEvent(Post post) {
        this.rabbitTemplate.convertAndSend("x.post-created", "", toEvent(post));
    }

    public void sendEmail(String recipient, String message) {
        var command = new SendEmailCommand();
        command.recipient(recipient);
        command.message(message);
        sendToQueue("q.notification-service.send-email", command);
    }

    public void sendValidateUserLikedCommand(Integer postId, Integer likedBy) {
        var command = new ValidateUserLikedCommand();
        command.postId(postId);
        command.likedBy(likedBy);
        sendToQueue("q.user-service.validate-user-liked", command);
    }

    public void sendValidatePostInFeedCommand(Integer postId, Integer likedBy) {
        var command = new ValidatePostInFeedCommand();
        command.postId(postId);
        command.userId(likedBy);
        sendToQueue("q.feed-service.validate-post-in-feed", command);
    }

    private void sendToQueue(String queue, Object message) {
        LOGGER.info("Sending message: " + message);

        this.rabbitTemplate.convertAndSend(queue, message);
    }

    private PostCreatedEvent toEvent(Post post) {
        return new PostCreatedEvent()
                .post(new ApiPost()
                        .id(post.getId())
                        .content(post.getContent())
                        .createdBy(post.getCreatedBy())
                        .taggedUsers(post.getTaggedUsers()));
    }
}
