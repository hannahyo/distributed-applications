package be.ucll.da.feedservice.domain.post;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue
    private Integer id;

    private String content;

    @ElementCollection
    private List<Integer> taggedUsers;

    private Integer createdBy;

    private int likes;

    @ElementCollection
    private List<Integer> likedBy;

    protected Post() {
    }

    public Post(Integer id, String content, List<Integer> taggedUsers, Integer createdBy) {
        this.id = id;
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

    public int getLikes() {
        return likes;
    }

    public List<Integer> getLikedBy() {
        return likedBy;
    }

}
