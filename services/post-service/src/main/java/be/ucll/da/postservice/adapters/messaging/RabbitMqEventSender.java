package be.ucll.da.postservice.adapters.messaging;

import be.ucll.da.postservice.api.model.ApiPost;
import be.ucll.da.postservice.api.model.UserTaggedEvent;
import be.ucll.da.postservice.domain.post.EventSender;
import be.ucll.da.postservice.domain.post.Post;
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
    public void sendUserTaggedEvent(Post post) {
        this.rabbitTemplate.convertAndSend("x.user-tagged", "", toEvent(post));
    }

    private UserTaggedEvent toEvent(Post post) {
        return new UserTaggedEvent()
                .post(new ApiPost()
                        .id(post.getId())
                        .content(post.getContent())
                        .createdBy(post.getCreatedBy())
                        .taggedUsers(post.getTaggedUsers()));
    }
}
