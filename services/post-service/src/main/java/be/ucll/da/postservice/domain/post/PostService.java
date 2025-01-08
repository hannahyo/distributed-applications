package be.ucll.da.postservice.domain.post;

import be.ucll.da.postservice.api.model.ApiPost;
import be.ucll.da.postservice.domain.user.User;
import be.ucll.da.postservice.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void createPost(ApiPost data) {
        List<Integer> taggedUsers = data.getTaggedUsers();
        for (Integer taggedUser : taggedUsers) {
            Optional<User> user = userRepository.findById(taggedUser);
            if (user.isEmpty()) {
                throw new PostException("User with id " + taggedUser + " does not exist");
            }
        }

        Post post = new Post(
                data.getContent(),
                data.getTaggedUsers(),
                data.getCreatedBy()
        );

        postRepository.save(post);
    }

    public void deletePost(Integer id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post with id " + id + " does not exist");
        }
        postRepository.deleteById(id);
    }
}
