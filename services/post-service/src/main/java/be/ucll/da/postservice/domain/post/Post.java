package be.ucll.da.postservice.domain.post;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue
    private Integer id;

    private Boolean isValidUser;

    private String content;

    @ElementCollection
    private List<Integer> taggedUsers;

    private Integer createdBy;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    private Boolean hasValidTaggedUsers;

    private int likes = 0;

    @ElementCollection
    private List<Integer> likedBy = new ArrayList<>();

    @ElementCollection
    private List<String> comments;

    @ElementCollection
    private List<Integer> commentedBy = new ArrayList<>();

    protected Post() {
    }

    public Post(String content, List<Integer> taggedUsers, Integer createdBy) {
        this.content = content;
        this.taggedUsers = taggedUsers;
        this.createdBy = createdBy;
    }

    public Integer getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public List<Integer> getTaggedUsers() {
        return taggedUsers;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public PostStatus getStatus() {
        return status;
    }

    public Boolean hasValidTaggedUsers() {
        return hasValidTaggedUsers;
    }

    public int getLikes() {
        return likes;
    }

    public List<String> getComments() {
        return comments;
    }

    public List<Integer> getLikedBy() {
        return likedBy;
    }

    public List<Integer> getCommentedBy() {
        return commentedBy;
    }


    public void like(Integer userId) {
        likes++;
        likedBy.add(userId);
    }

    public void unlike(Integer userId) {
        likes--;
        likedBy.remove(userId);
    }

    public void comment(String comment, Integer userId) {
        comments.add(comment);
        commentedBy.add(userId);
    }

    public void uncomment(Integer userId) {
        commentedBy.remove(userId);
    }

    public void validatingUser() {
        this.status = PostStatus.VALIDATING_USER;
    }

    public void userValid() {
        this.status = PostStatus.VALIDATING_TAGGED_USERS;
        this.isValidUser = true;
    }

    public void userLikedValid() {
        this.status = PostStatus.VALIDATING_FEED;
        this.isValidUser = true;
    }

    public void userInvalid() {
        this.status = PostStatus.NO_USER;
        this.isValidUser = false;
    }

    public void taggedUsersValid() {
        this.status = PostStatus.POST_CREATED;
        this.hasValidTaggedUsers = true;
    }

    public void taggedUsersInvalid() {
        this.status = PostStatus.TAGGED_USERS_INVALID;
        this.hasValidTaggedUsers = false;
    }

    public void postInFeedValid() {
        this.status = PostStatus.FEED_VALIDATED;
    }

    public void postInFeedInvalid() {
        this.status = PostStatus.FEED_INVALID;
    }

    public void userCommentValid() {
        this.status = PostStatus.VALIDATING_FEED;
    }

    public void userCommentInvalid() {
        this.status = PostStatus.FEED_INVALID;
    }
}
