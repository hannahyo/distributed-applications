package be.ucll.da.postservice.domain.post;

import be.ucll.da.postservice.adapters.messaging.RabbitMqMessageSender;
import be.ucll.da.postservice.api.model.ApiPost;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PostService {

    private final PostRepository postRepository;
    private final CreatePostSaga createPostSaga;
    private final LikePostSaga likePostSaga;
    private final RabbitMqMessageSender eventSender;

    @Autowired
    public PostService(PostRepository postRepository, CreatePostSaga createPostSaga, LikePostSaga likePostSaga, RabbitMqMessageSender eventSender) {
        this.postRepository = postRepository;
        this.createPostSaga = createPostSaga;
        this.likePostSaga = likePostSaga;
        this.eventSender = eventSender;
    }

    public void createPost(ApiPost data) {
        var post = new Post(data.getContent(), data.getTaggedUsers(), data.getCreatedBy());

        postRepository.save(post);
        createPostSaga.executeSaga(post);
        eventSender.sendPostCreatedEvent(post);
    }

    public void deletePost(Integer id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post with id " + id + " does not exist");
        }
        postRepository.deleteById(id);
    }

    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    public void likePost(Integer postId, Integer userId) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.like(userId);
            postRepository.save(post);
            likePostSaga.executeSaga(post);
        } else {
            throw new RuntimeException("Post not found");
        }
    }

    public void commentPost(Integer postId, Integer userId, String comment) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.comment(comment);
            System.out.println("Updated comments:" + post.getComments());
            postRepository.save(post);
        } else {
            throw new RuntimeException("Post not found");
        }    }
}
