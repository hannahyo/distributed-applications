package be.ucll.da.postservice.domain.post;

import be.ucll.da.postservice.adapters.messaging.RabbitMqMessageSender;
import be.ucll.da.postservice.client.feed.model.PostInFeedValidatedEvent;
import be.ucll.da.postservice.client.user.model.UserLikedValidatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LikePostSaga {

    private final RabbitMqMessageSender eventSender;
    private final PostRepository postRepository;

    @Autowired
    public LikePostSaga(RabbitMqMessageSender eventSender, PostRepository postRepository) {
        this.eventSender = eventSender;
        this.postRepository = postRepository;
    }

    public void executeSaga(Post post, Integer userId) {
        post.validatingUser();
        eventSender.sendValidateUserLikedCommand(post.getId(), userId);
    }

    public void executeSaga(Integer postId, UserLikedValidatedEvent event) {
        Post post = getPostById(postId);
        Integer userId = getLastLikedUser(post);

        if (event.getIsValid()) {
            post.userLikedValid();
            eventSender.sendValidatePostInFeedCommand(post.getId(), userId);
        } else {
            post.userInvalid();
            post.unlike(event.getLikedBy());
        }
    }

    public void executeSaga(Integer postId, PostInFeedValidatedEvent event) {
        Post post = getPostById(postId);

        if (event.getIsValid()) {
            post.postInFeedValid();
            eventSender.sendEmail(event.getEmail(), generateMessage(post.getId(), "User with id " + event.getUserId() + " has liked your post."));
        } else {
            post.postInFeedInvalid();
            post.unlike(event.getUserId());
        }
    }

    private Post getPostById(Integer postId) {
        return postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found: " + postId));
    }

    private Integer getLastLikedUser(Post post) {
        List<Integer> likedBy = post.getLikedBy();
        if (likedBy.isEmpty()) {
            throw new RuntimeException("No users have liked the post yet.");
        }
        return likedBy.get(likedBy.size() - 1);
    }

    private String generateMessage(Integer postId, String message) {
        return "Regarding post " + postId + ". " + message;
    }
}
