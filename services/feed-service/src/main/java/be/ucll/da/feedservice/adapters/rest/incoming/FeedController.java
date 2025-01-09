package be.ucll.da.feedservice.adapters.rest.incoming;

import be.ucll.da.feedservice.api.FeedApiDelegate;
import be.ucll.da.feedservice.api.model.ApiFeed;
import be.ucll.da.feedservice.api.model.ApiPost;
import be.ucll.da.feedservice.domain.feed.FeedService;
import be.ucll.da.feedservice.domain.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeedController implements FeedApiDelegate {

    private final FeedService feedService;

    @Autowired
    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @Override
    public ResponseEntity<ApiFeed> getUserFeed(Integer userId) {
        List<Post> userFeed = feedService.getUserFeed(userId);

        List<ApiPost> apiPosts = userFeed.stream()
                .map(this::toDto)
                .toList();

        ApiFeed apiFeed = new ApiFeed().posts(apiPosts);
        return ResponseEntity.ok(apiFeed);
    }

    @Override
    public ResponseEntity<ApiFeed> searchFeed(Integer userId, String contentQuery, Integer friendId, Integer taggedUserId) {
        List<Post> filteredFeed = feedService.searchFeed(userId, contentQuery, friendId, taggedUserId);

        List<ApiPost> apiPosts = filteredFeed.stream()
                .map(this::toDto)
                .toList();

        ApiFeed apiFeed = new ApiFeed().posts(apiPosts);
        return ResponseEntity.ok(apiFeed);
    }

    private ApiPost toDto(Post post) {
        return new ApiPost()
                .id(post.getId())
                .content(post.getContent())
                .taggedUsers(post.getTaggedUsers())
                .createdBy(post.getCreatedBy());
    }
}
