package be.ucll.da.postservice.domain.post;

import be.ucll.da.postservice.adapters.messaging.RabbitMqMessageSender;
import be.ucll.da.postservice.client.user.model.TaggedUsersValidatedEvent;
import be.ucll.da.postservice.client.user.model.UserValidatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreatePostSaga {

    private final RabbitMqMessageSender eventSender;
    private final PostRepository postRepository;

    @Autowired
    public CreatePostSaga(RabbitMqMessageSender eventSender, PostRepository postRepository) {
        this.eventSender = eventSender;
        this.postRepository = postRepository;
    }

    public void executeSaga(Post post) {
        post.validatingUser();
        eventSender.sendValidateUserCommand(post.getId(), post.getCreatedBy());
    }

    public void executeSaga(Integer postId, UserValidatedEvent event) {
        Post post = getPostById(postId);

        if (event.getIsValid()) {
            post.userValid();
            eventSender.sendValidateTaggedUsersCommand(post.getId(), post.getTaggedUsers());
        } else {
            post.userInvalid();
            postRepository.delete(post);
        }
    }

    public void executeSaga(Integer postId, TaggedUsersValidatedEvent event) {
        Post post = getPostById(postId);
        if (event.getIsValid()) {
            post.taggedUsersValid();
            event.getEmails().forEach(email ->
                    eventSender.sendEmail(email, generateMessage(post.getId(), "You have been tagged in a post."))
            );
        } else {
            post.taggedUsersInvalid();
            postRepository.delete(post);
        }
    }

    private Post getPostById(Integer postId) {
        return postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found: " + postId));
    }

    private String generateMessage(Integer postId, String message) {
        return "Regarding post " + postId + ". " + message;
    }
}
