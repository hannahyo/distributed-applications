package be.ucll.da.feedservice.adapters.messaging;

import be.ucll.da.feedservice.client.post.model.PostCreatedEvent;
import be.ucll.da.feedservice.client.user.model.UserCreatedEvent;
import be.ucll.da.feedservice.domain.post.Post;
import be.ucll.da.feedservice.domain.post.PostRepository;
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
    private final PostRepository postRepository;

    @Autowired
    public MessageListener(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @RabbitListener(queues = {"q.user-feed-service"})
    public void onUserCreated(UserCreatedEvent event) {
        LOGGER.info("Received userCreatedEvent...");
        this.userRepository.save(new User(event.getUser().getId(), event.getUser().getFriends()));
    }

    @RabbitListener(queues = {"q.post-feed-service"})
    public void onPostCreated(PostCreatedEvent event) {
        LOGGER.info("Received postCreatedEvent...");
        this.postRepository.save(new Post(event.getPost().getId(), event.getPost().getContent(), event.getPost().getTaggedUsers(),  event.getPost().getCreatedBy()));
    }
}
