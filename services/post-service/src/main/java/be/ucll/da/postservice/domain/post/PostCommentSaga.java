package be.ucll.da.postservice.domain.post;

import be.ucll.da.postservice.adapters.messaging.RabbitMqMessageSender;
import be.ucll.da.postservice.client.feed.model.PostInFeedValidatedEvent;
import be.ucll.da.postservice.client.user.model.UserCommentedValidatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostCommentSaga {

    private final RabbitMqMessageSender eventSender;
    private final PostRepository postRepository;

    @Autowired
    public PostCommentSaga(RabbitMqMessageSender eventSender, PostRepository postRepository) {
        this.eventSender = eventSender;
        this.postRepository = postRepository;
    }

    public void executeSaga(Post post, Integer userId) {
        post.validatingUser();
        eventSender.sendValidateUserCommentCommand(post.getId(), userId);
    }

    public void executeSaga(Integer postId, UserCommentedValidatedEvent event) {
        Post post = getPostById(postId);
        Integer userId = getLastCommentedUser(post);

        if (event.getIsValid()) {
            post.userCommentValid();
            eventSender.sendValidatePostInFeedCommentCommand(post.getId(), userId);
        } else {
            post.userCommentInvalid();
            post.uncomment(event.getCommentedBy());
        }
    }

    public void executeSaga(Integer postId, PostInFeedValidatedEvent event) {
        Post post = getPostById(postId);

        if (event.getIsValid()) {
            post.postInFeedValid();
            eventSender.sendEmail(event.getEmail(), generateMessage(post.getId(), "User with id " + event.getUserId() + " has commented on your post."));
        } else {
            post.postInFeedInvalid();
            post.uncomment(event.getUserId());
        }
    }

    private Post getPostById(Integer postId) {
        return postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found: " + postId));
    }

    private Integer getLastCommentedUser(Post post) {
        List<Integer> commentedBy = post.getCommentedBy();
        if (commentedBy.isEmpty()) {
            throw new RuntimeException("No users have commented on the post yet.");
        }
        return commentedBy.get(commentedBy.size() - 1);
    }

    private String generateMessage(Integer postId, String message) {
        return "Regarding post " + postId + ". " + message;
    }
}
