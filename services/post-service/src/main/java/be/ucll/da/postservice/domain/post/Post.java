package be.ucll.da.postservice.domain.post;

import jakarta.persistence.*;

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

    public void validatingUser() {
        this.status = PostStatus.VALIDATING_USER;
    }

    public void userValid() {
        this.status = PostStatus.VALIDATING_TAGGED_USERS;
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
}
