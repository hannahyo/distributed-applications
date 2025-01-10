package be.ucll.da.feedservice.domain.post;

import jakarta.persistence.*;

import java.util.ArrayList;
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

    @ElementCollection
    private List<String> comments = new ArrayList<>();

    @ElementCollection
    private List<Integer> commentedBy = new ArrayList<>();

    protected Post() {
    }

    public Post(Integer id, String content, List<Integer> taggedUsers, Integer createdBy, int likes, List<Integer> likedBy, List<String> comments, List<Integer> commentedBy) {
        this.id = id;
        this.content = content;
        this.taggedUsers = taggedUsers;
        this.createdBy = createdBy;
        this.likes = likes;
        this.likedBy = likedBy;
        this.comments = comments;
        this.commentedBy = commentedBy;
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

    public Integer getLastLiked() {
        if (likedBy == null || likedBy.isEmpty()) {
            return null;
        }
        return likedBy.get(likedBy.size() - 1);
    }

    public Integer getLastCommented() {
        if (commentedBy == null || commentedBy.isEmpty()) {
            return null;
        }
        return commentedBy.get(commentedBy.size() - 1);
    }

}
