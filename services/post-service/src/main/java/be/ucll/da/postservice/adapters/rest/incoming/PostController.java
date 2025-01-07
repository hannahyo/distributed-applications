package be.ucll.da.postservice.adapters.rest.incoming;

import be.ucll.da.postservice.api.PostApiDelegate;
import be.ucll.da.postservice.api.model.ApiPost;
import be.ucll.da.postservice.domain.Post;
import be.ucll.da.postservice.domain.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class PostController implements PostApiDelegate {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Override
    public ResponseEntity<Void> createPost(ApiPost post) {
        postService.createPost(post);
        return ResponseEntity.ok().build();
    }

    private ApiPost toDto(Post post) {
        return new ApiPost()
                .id(post.getId())
                .content(post.getContent())
                .taggedUsers(post.getTaggedUsers())
                .createdBy(post.getCreatedBy());
    }
}
