package be.ucll.da.feedservice.adapters.messaging;

import be.ucll.da.feedservice.api.model.PostInFeedValidatedEvent;
import be.ucll.da.feedservice.api.model.ValidatePostInFeedCommand;
import be.ucll.da.feedservice.client.post.model.PostCreatedEvent;
import be.ucll.da.feedservice.client.post.model.PostUpdatedEvent;
import be.ucll.da.feedservice.client.user.model.UserCreatedEvent;
import be.ucll.da.feedservice.domain.feed.FeedService;
import be.ucll.da.feedservice.domain.post.Post;
import be.ucll.da.feedservice.domain.post.PostRepository;
import be.ucll.da.feedservice.domain.user.User;
import be.ucll.da.feedservice.domain.user.UserRepository;
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

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FeedService feedService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageListener(UserRepository userRepository, PostRepository postRepository, FeedService feedService, RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.feedService = feedService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = {"q.user-feed-service"})
    public void onUserCreated(UserCreatedEvent event) {
        LOGGER.info("Received userCreatedEvent...");
        this.userRepository.save(new User(event.getUser().getId(), event.getUser().getFriends(), event.getUser().getEmail()));
    }

    @RabbitListener(queues = {"q.post-feed-service"})
    public void onPostCreated(PostCreatedEvent event) {
        LOGGER.info("Received postCreatedEvent...");
        this.postRepository.save(new Post(event.getPost().getId(), event.getPost().getContent(), event.getPost().getTaggedUsers(),  event.getPost().getCreatedBy(), event.getPost().getLikes(), event.getPost().getLikedBy(), event.getPost().getComments(), event.getPost().getCommentedBy()));
    }

    @RabbitListener(queues = {"q.post-update-feed-service"})
    public void onPostUpdated(PostUpdatedEvent event) {
        LOGGER.info("Received postUpdatedEvent...");
        this.postRepository.save(new Post(event.getPost().getId(), event.getPost().getContent(), event.getPost().getTaggedUsers(), event.getPost().getCreatedBy(), event.getPost().getLikes(), event.getPost().getLikedBy(), event.getPost().getComments(), event.getPost().getCommentedBy()));
    }

    @RabbitListener(queues = {"q.feed-service.validate-post-in-feed"})
    public void onValidatePostInFeed(ValidatePostInFeedCommand command) {
        LOGGER.info("Received validatePostInFeedCommand...");

        Post post = feedService.validateUserFeed(command.getPostId(), command.getUserId());

        PostInFeedValidatedEvent event = new PostInFeedValidatedEvent();
        event.postId(post.getId());
        event.userId(post.getLastLiked());
        event.isValid(post != null);

        LOGGER.info("Sending event: " + event);
        this.rabbitTemplate.convertAndSend("x.post-in-feed-validated", "", event);
    }

    @RabbitListener(queues = {"q.feed-service.validate-post-in-feed-comment"})
    public void onValidatePostInFeedComment(ValidatePostInFeedCommand command) {
        LOGGER.info("Received validatePostInFeedCommandComment...");

        Post post = feedService.validateUserFeed(command.getPostId(), command.getUserId());

        PostInFeedValidatedEvent event = new PostInFeedValidatedEvent();
        event.postId(post.getId());
        event.userId(post.getLastCommented());
        event.isValid(post != null);

        LOGGER.info("Sending event: " + event);
        this.rabbitTemplate.convertAndSend("x.post-in-feed-comment-validated", "", event);
    }
}
