package be.ucll.da.postservice.adapters.messaging;

import be.ucll.da.postservice.client.feed.model.PostInFeedValidatedEvent;
import be.ucll.da.postservice.client.user.model.TaggedUsersValidatedEvent;
import be.ucll.da.postservice.client.user.model.UserCommentedValidatedEvent;
import be.ucll.da.postservice.client.user.model.UserLikedValidatedEvent;
import be.ucll.da.postservice.client.user.model.UserValidatedEvent;
import be.ucll.da.postservice.domain.post.CreatePostSaga;
import be.ucll.da.postservice.domain.post.LikePostSaga;
import be.ucll.da.postservice.domain.post.PostCommentSaga;
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
    private final LikePostSaga likePostSaga;
    private final PostCommentSaga postCommentSaga;

    @Autowired
    public MessageListener(CreatePostSaga createPostSaga, LikePostSaga likePostSaga, PostCommentSaga postCommentSaga) {
        this.createPostSaga = createPostSaga;
        this.likePostSaga = likePostSaga;
        this.postCommentSaga = postCommentSaga;
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

    @RabbitListener(queues = {"q.user-liked-validated.post-service"})
    public void onUserLikedValidated(UserLikedValidatedEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.likePostSaga.executeSaga(event.getPostId(), event);
    }

    @RabbitListener(queues = {"q.post-in-feed-validated.post-service"})
    public void onPostInFeedValidated(PostInFeedValidatedEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.likePostSaga.executeSaga(event.getPostId(), event);
    }

    @RabbitListener(queues = {"q.user-comment-validated.post-service"})
    public void onUserCommentedValidated(UserCommentedValidatedEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.postCommentSaga.executeSaga(event.getPostId(), event);
    }

    @RabbitListener(queues = {"q.post-in-feed-validated-comment.post-service"})
    public void onPostInFeedCommentValidated(PostInFeedValidatedEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.postCommentSaga.executeSaga(event.getPostId(), event);
    }

}
