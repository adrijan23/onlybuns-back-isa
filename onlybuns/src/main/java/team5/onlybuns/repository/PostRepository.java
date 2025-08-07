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

    @Query("SELECT p FROM Post p WHERE YEAR(p.createdAt) = :year ORDER BY p.createdAt DESC")
    List<Post> findPostsByYear(@Param("year") int year);

    @Query(value = "SELECT EXTRACT(MONTH FROM p.created_at) AS postMonth, COUNT(*) AS postCount " +
            "FROM posts p " +
            "WHERE EXTRACT(YEAR FROM p.created_at) = :year " +
            "GROUP BY postMonth " +
            "ORDER BY postMonth", nativeQuery = true)
    List<Object[]> getPostsPerMonth(@Param("year") int year);

    @Query(value = "SELECT EXTRACT(DAY FROM p.created_at) AS postDay, COUNT(*) AS postCount " +
            "FROM posts p " +
            "WHERE EXTRACT(YEAR FROM p.created_at) = :year " +
            "AND EXTRACT(MONTH FROM p.created_at) = :month " +
            "GROUP BY postDay " +
            "ORDER BY postDay", nativeQuery = true)
    List<Object[]> getPostsPerDay(@Param("year") int year, @Param("month") int month);

    @Query(value = "SELECT DISTINCT EXTRACT(YEAR FROM p.created_at) AS year FROM posts p ORDER BY year DESC", nativeQuery = true)
    List<Integer> getAvailablePostYears();

    @Query(value = "SELECT DISTINCT EXTRACT(MONTH FROM p.created_at) AS availableMonth " +
            "FROM posts p " +
            "WHERE EXTRACT(YEAR FROM p.created_at) = :year " +
            "ORDER BY availableMonth", nativeQuery = true)
    List<Integer> getAvailableMonthsByYear(@Param("year") int year);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId")
    Integer countPostsByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT EXTRACT(DAY FROM p.created_at) AS postDay, COUNT(*) AS postCount " +
            "FROM posts p " +
            "WHERE EXTRACT(YEAR FROM p.created_at) = :year " +
            "AND EXTRACT(MONTH FROM p.created_at) = :month " +
            "AND CEIL(EXTRACT(DAY FROM p.created_at) / 7.0) = :week " +
            "GROUP BY postDay " +
            "ORDER BY postDay", nativeQuery = true)
    List<Object[]> getPostsForSpecificWeek(@Param("year") int year, @Param("month")
    int month, @Param("week") int week);
}
