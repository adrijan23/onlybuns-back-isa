package team5.onlybuns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team5.onlybuns.model.Comment;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT * FROM COMMENTS WHERE post_id = :postId", nativeQuery = true)
    List<Comment> findCommentsByPostId(@Param("postId") Long postId);

    @Query(value = "SELECT COALESCE((SELECT COUNT(*) FROM COMMENTS WHERE user_id = :userId AND created_at >= (NOW() - INTERVAL '1 hour')), 0)", nativeQuery = true)
    Long findCommentsCountFromLastHourForUser(@Param("userId") Long userId);
    Integer countByPostId(Long postId);
}
