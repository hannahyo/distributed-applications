package be.ucll.da.postservice.domain;

import be.ucll.da.postservice.api.model.ApiPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void createPost(ApiPost data) {
        Post post = new Post(
                data.getContent(),
                data.getTaggedUsers(),
                data.getCreatedBy()
        );

        postRepository.save(post);
    }

    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post with id " + id + " does not exist");
        }
        postRepository.deleteById(id);
    }
}
