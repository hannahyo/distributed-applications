package be.ucll.da.feedservice.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findAllByCreatedBy(Integer userId);

    @Query("SELECT p FROM Post p JOIN p.taggedUsers t WHERE t = :userId")
    List<Post> findAllByTagged(Integer userId);
}
