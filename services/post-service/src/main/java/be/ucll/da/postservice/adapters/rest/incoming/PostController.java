package be.ucll.da.postservice.adapters.rest.incoming;

import be.ucll.da.postservice.api.PostApiDelegate;
import be.ucll.da.postservice.api.model.ApiPost;
import be.ucll.da.postservice.api.model.ApiPosts;
import be.ucll.da.postservice.domain.post.Post;
import be.ucll.da.postservice.domain.post.PostService;
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

    @Override
    public ResponseEntity<Void> deletePost(Integer id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ApiPosts> getPosts() {
        ApiPosts posts = new ApiPosts();
        posts.addAll(
                postService.getPosts().stream()
                        .map(this::toDto)
                        .toList()
        );
        return ResponseEntity.ok(posts);
    }

    @Override
    public ResponseEntity<Void> likePost(Integer postId, Integer userId) {
        postService.likePost(postId, userId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> commentPost(Integer postId, Integer userId, String comment) {
        postService.commentPost(postId, userId, comment);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> unlikePost(Integer postId, Integer userId) {
        postService.unLikePost(postId, userId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> uncommentPost(Integer postId, Integer userId) {
        postService.unCommentPost(postId, userId);
        return ResponseEntity.ok().build();
    }



    private ApiPost toDto(Post post) {
        return new ApiPost()
                .id(post.getId())
                .content(post.getContent())
                .taggedUsers(post.getTaggedUsers())
                .createdBy(post.getCreatedBy())
                .likes(post.getLikes())
                .comments(post.getComments());
    }
}
