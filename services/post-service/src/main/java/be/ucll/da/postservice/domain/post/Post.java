package be.ucll.da.postservice.domain.post;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    @ElementCollection
    private List<Long> taggedUsers;

    private Long createdBy;

    protected Post() {
    }

    public Post(String content, List<Long> taggedUsers, Long createdBy) {
        this.content = content;
        this.taggedUsers = taggedUsers;
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public List<Long> getTaggedUsers() {
        return taggedUsers;
    }

    public Long getCreatedBy() {
        return createdBy;
    }
}
