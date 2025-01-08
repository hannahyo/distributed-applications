package be.ucll.da.postservice.domain.post;

import be.ucll.da.postservice.adapters.messaging.RabbitMqMessageSender;
import be.ucll.da.postservice.client.user.model.TaggedUsersValidatedEvent;
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
        post.validatingTaggedUsers();
        eventSender.validateTaggedUsersCommand(post.getId(), post.getTaggedUsers());
    }

    public void executeSaga(Integer postId, TaggedUsersValidatedEvent event) {
        Post post = getPostById(postId);

        if (event.getIsValid()) {
            post.taggedUsersValid();
            eventSender.sendEmail(event.getEmails(), generateMessage(post.getId(), "Your post was created successfully."));
        } else {
            post.taggedUsersInvalid();
            eventSender.sendEmail(event.getEmails(), generateMessage(post.getId(), "Your post creation failed because some tagged users do not exist."));
        }
    }

    private Post getPostById(Integer postId) {
        return postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found: " + postId));
    }

    private String generateMessage(Integer postId, String message) {
        return "Regarding post " + postId + ". " + message;
    }
}
