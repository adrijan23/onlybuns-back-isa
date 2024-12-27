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

    @Query(value = "SELECT EXTRACT(MONTH FROM c.created_at) AS commentMonth, COUNT(*) AS commentCount " +
            "FROM comments c " +
            "WHERE EXTRACT(YEAR FROM c.created_at) = :year " +
            "GROUP BY commentMonth " +
            "ORDER BY commentMonth", nativeQuery = true)
    List<Object[]> getCommentsPerMonth(@Param("year") int year);

    @Query(value = "SELECT EXTRACT(DAY FROM c.created_at) AS commentDay, COUNT(*) AS commentCount " +
            "FROM comments c " +
            "WHERE EXTRACT(YEAR FROM c.created_at) = :year " +
            "AND EXTRACT(MONTH FROM c.created_at) = :month " +
            "GROUP BY commentDay " +
            "ORDER BY commentDay", nativeQuery = true)
    List<Object[]> getCommentsPerDay(@Param("year") int year, @Param("month") int month);

    @Query(value = "SELECT DISTINCT EXTRACT(YEAR FROM c.created_at) AS year FROM comments c ORDER BY year DESC", nativeQuery = true)
    List<Integer> getAvailableYears();

    @Query(value = "SELECT DISTINCT EXTRACT(MONTH FROM c.created_at) AS availableMonth " +
            "FROM comments c " +
            "WHERE EXTRACT(YEAR FROM c.created_at) = :year " +
            "ORDER BY availableMonth", nativeQuery = true)
    List<Integer> getAvailableMonthsByYear(@Param("year") int year);
}
