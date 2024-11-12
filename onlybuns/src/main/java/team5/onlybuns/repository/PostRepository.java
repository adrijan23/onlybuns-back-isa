package team5.onlybuns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team5.onlybuns.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Find posts by user ID
    List<Post> findByUserId(Long userId);
    Post findPostById(Long postId);
}
