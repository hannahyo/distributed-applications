package be.ucll.da.apigateway.web;

import be.ucll.da.apigateway.api.SocialNetworkApiDelegate;
import be.ucll.da.apigateway.api.model.SocialNetworkPost;
import be.ucll.da.apigateway.api.model.SocialNetworkUser;
import be.ucll.da.apigateway.client.feed.api.FeedApi;
import be.ucll.da.apigateway.client.post.api.PostApi;
import be.ucll.da.apigateway.client.post.model.ApiPost;
import be.ucll.da.apigateway.client.user.api.UserApi;
import be.ucll.da.apigateway.client.user.model.ApiUser;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class SocialNetworkController implements SocialNetworkApiDelegate {

    private final UserApi userApi;
    private final PostApi postApi;
    private final FeedApi feedApi;
    private EurekaClient discoveryClient;
    private CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    public SocialNetworkController(UserApi userApi, PostApi postApi, FeedApi feedApi, EurekaClient discoveryClient, CircuitBreakerFactory circuitBreakerFactory) {
        this.userApi = userApi;
        this.postApi = postApi;
        this.feedApi = feedApi;
        this.discoveryClient = discoveryClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public ResponseEntity<Void> addFriend(Integer id, Integer friendId) {
        userApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("user-service", false).getHomePageUrl());
        return circuitBreakerFactory.create("userApi")
                .run(() -> userApi.addFriendWithHttpInfo(id, friendId));
    }

    @Override
    public ResponseEntity<Void> commentPost(Integer postId, Integer userId, String comment) {
        postApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("post-service", false).getHomePageUrl());
        return circuitBreakerFactory.create("userApi")
                .run(() -> postApi.commentPostWithHttpInfo(postId, userId, comment));
    }

    @Override
    public ResponseEntity<Void> createPost(SocialNetworkPost post) {
        postApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("post-service", false).getHomePageUrl());

        ApiPost apiPost = mapToPostApi(post);

        return circuitBreakerFactory.create("postApi")
                .run(() -> postApi.createPostWithHttpInfo(apiPost));
    }

    @Override
    public ResponseEntity<Void> createUser(SocialNetworkUser user) {
        userApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("user-service", false).getHomePageUrl());

        ApiUser apiUser = mapToUserApi(user);

        return circuitBreakerFactory.create("userApi")
                .run(() -> userApi.createUserWithHttpInfo(apiUser));
    }

    @Override
    public ResponseEntity<Void> deletePost(Integer postId) {
        postApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("post-service", false).getHomePageUrl());
        return circuitBreakerFactory.create("postApi")
                .run(() -> postApi.deletePostWithHttpInfo(postId));
    }

//    @Override
//    public ResponseEntity<SocialNetworkFeed> getUserFeed(Integer userId) {
//        feedApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("feed-service", false).getHomePageUrl());
//
//        ApiFeed feed = circuitBreakerFactory.create("feedApi")
//                .run(() -> feedApi.getUserFeed(userId));
//        return ResponseEntity.ok(feed);
//    }

    @Override
    public ResponseEntity<Void> likePost(Integer postId, Integer userId) {
        postApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("post-service", false).getHomePageUrl());
        return circuitBreakerFactory.create("postApi")
                .run(() -> postApi.likePostWithHttpInfo(postId, userId));
    }

//    @Override
//    public ResponseEntity<SocialNetworkFeed> searchFeed(Integer userId, String contentQuery, Integer friendId, Integer taggedUser) {
//        return null;
//    }

    @Override
    public ResponseEntity<Void> unlikePost(Integer postId, Integer userId) {
        postApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("post-service", false).getHomePageUrl());
        return circuitBreakerFactory.create("postApi")
                .run(() -> postApi.unlikePostWithHttpInfo(postId, userId));
    }

    @Override
    public ResponseEntity<Void> uncommentPost(Integer postId, Integer userId) {
        postApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("post-service", false).getHomePageUrl());
        return circuitBreakerFactory.create("postApi")
                .run(() -> postApi.uncommentPostWithHttpInfo(postId, userId));
    }

    private ApiUser mapToUserApi(SocialNetworkUser user) {
        ApiUser apiUser = new ApiUser();
        apiUser.setId(user.getId());
        apiUser.setEmail(user.getEmail());
        apiUser.setFirstName(user.getFirstName());
        apiUser.setLastName(user.getLastName());
        apiUser.setFriends(user.getFriends());
        return apiUser;
    }

    private ApiPost mapToPostApi(SocialNetworkPost post) {
        ApiPost apiPost = new ApiPost();
        apiPost.setId(post.getId());
        apiPost.setCreatedBy(post.getCreatedBy());
        apiPost.setTaggedUsers(post.getTaggedUsers());
        apiPost.setComments(post.getComments());
        apiPost.setCommentedBy(post.getCommentedBy());
        apiPost.setLikedBy(post.getLikedBy());
        apiPost.setLikes(post.getLikes());
        return apiPost;
    }

//    private SocialNetworkFeed mapToSocialNetworkFeed(ApiFeed feed) {
//        SocialNetworkFeed socialNetworkFeed = new SocialNetworkFeed();
//
//        socialNetworkFeed.setPosts(feed.getPosts().stream()
//                .map(this::mapToSocialNetworkPost)
//                .collect(Collectors.toList()));
//
//        return socialNetworkFeed;
//    }

//    private SocialNetworkPost mapToSocialNetworkPost(ApiPost apiPost) {
//        SocialNetworkPost socialNetworkPost = new SocialNetworkPost();
//        socialNetworkPost.setId(apiPost.getId());
//        socialNetworkPost.setCreatedBy(apiPost.getCreatedBy());
//        socialNetworkPost.setTaggedUsers(apiPost.getTaggedUsers());
//        socialNetworkPost.setComments(apiPost.getComments());
//        socialNetworkPost.setCommentedBy(apiPost.getCommentedBy());
//        socialNetworkPost.setLikedBy(apiPost.getLikedBy());
//        socialNetworkPost.setLikes(apiPost.getLikes());
//        return socialNetworkPost;
//    }
}
