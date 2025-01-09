package be.ucll.da.feedservice.domain.feed;

import be.ucll.da.feedservice.domain.post.Post;
import be.ucll.da.feedservice.domain.post.PostRepository;
import be.ucll.da.feedservice.domain.user.User;
import be.ucll.da.feedservice.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FeedService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public FeedService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<Post> getUserFeed(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        List<Post> ownPosts = postRepository.findAllByCreatedBy(userId);

        List<Post> friendsPosts = new ArrayList<>();
        for (Integer friendId : user.getFriends()) {
            friendsPosts.addAll(postRepository.findAllByCreatedBy(friendId));
        }

        List<Post> taggedPosts = postRepository.findAllByTagged(userId);

        List<Post> feedPosts = new ArrayList<>();
        feedPosts.addAll(ownPosts);
        feedPosts.addAll(friendsPosts);
        feedPosts.addAll(taggedPosts);

        return feedPosts;
    }

    public List<Post> searchFeed(Integer userId, String contentQuery, Integer friendId, Integer taggedUserId) {
        List<Post> userFeed = getUserFeed(userId);

        return userFeed.stream()
                .filter(post ->
                        (contentQuery == null || post.getContent().toLowerCase().contains(contentQuery.toLowerCase())) &&
                                (friendId == null || post.getCreatedBy().equals(friendId)) &&
                                (taggedUserId == null || post.getTaggedUsers().contains(taggedUserId))
                )
                .collect(Collectors.toList());
    }

    public Post validateUserFeed(Integer postId, Integer userId) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isEmpty()) {
            throw new IllegalArgumentException("Post not found with ID: " + postId);
        }

        Post post = postOptional.get();
        List<Post> userFeed = getUserFeed(userId);

        if (!userFeed.contains(post)) {
            return null;
        }
        return post;
    }

}
