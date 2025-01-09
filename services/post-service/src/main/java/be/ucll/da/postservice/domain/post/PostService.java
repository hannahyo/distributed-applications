package be.ucll.da.postservice.domain.post;

import be.ucll.da.postservice.api.model.ApiPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostService {

    private final PostRepository postRepository;
    private final CreatePostSaga createPostSaga;

    @Autowired
    public PostService(PostRepository postRepository, CreatePostSaga createPostSaga) {
        this.postRepository = postRepository;
        this.createPostSaga = createPostSaga;
    }

    public void createPost(ApiPost data) {
        var post = new Post(data.getContent(), data.getTaggedUsers(), data.getCreatedBy());

        postRepository.save(post);
        createPostSaga.executeSaga(post);
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
}
