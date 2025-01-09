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

    protected User() {}

    public User(Integer id, List<Integer> friends) {
        this.id = id;
        this.friends = friends;
    }

    public Integer getId() {
        return id;
    }

    public List<Integer> getFriends() {
        return friends;
    }

    public void addFriend(Integer friendId) {
        friends.add(friendId);
    }
}
