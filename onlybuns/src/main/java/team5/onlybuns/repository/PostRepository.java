package team5.onlybuns.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team5.onlybuns.model.Post;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Find posts by user ID
    List<Post> findByUserId(Long userId);
    Post findPostById(Long postId);
    @Query("SELECT COUNT(p) FROM Post p WHERE p.createdAt >= :startDate")
    Integer countPostsInLastMonth(LocalDateTime startDate);

    @Query("SELECT p FROM Post p WHERE p.createdAt >= :sevenDaysAgo ORDER BY size(p.likes) DESC")
    List<Post> findTopPostsLast7Days(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo, Pageable pageable);

    @Query("SELECT p FROM Post p ORDER BY size(p.likes) DESC")
    List<Post> findTopPostsAllTime(Pageable pageable);

}
