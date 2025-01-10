package be.ucll.da.feedservice.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    @ElementCollection
    private List<Integer> friends = new ArrayList<>();

    private String email;

    protected User() {}

    public User(Integer id, List<Integer> friends, String email) {
        this.id = id;
        this.friends = friends;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public List<Integer> getFriends() {
        return friends;
    }

    public String getEmail() {
        return email;
    }

    public void addFriend(Integer friendId) {
        friends.add(friendId);
    }
}
