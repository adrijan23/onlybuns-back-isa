package team5.onlybuns.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team5.onlybuns.model.User;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username); // Non-transactional use

    @Lock(LockModeType.PESSIMISTIC_WRITE) // Or READ if required
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsernameWithLock(@Param("username") String username);

    //Users that havent logged in last 7 days
    @Query(value = "SELECT * FROM public.users WHERE last_active < NOW() - INTERVAL '7 days'", nativeQuery = true)
    List<User> findInactiveUsers();

    Page<User> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.enabled = false")
    void deleteDisabledUsers();

    @Query("SELECT u FROM User u " +
            "JOIN u.likes l " +
            "WHERE l.likedAt > :sevenDaysAgo " +
            "GROUP BY u " +
            "ORDER BY COUNT(l) DESC")
    List<User> findTopLikersInLastSevenDays(LocalDateTime sevenDaysAgo, Pageable pageable);

    @Query(value = "SELECT (COUNT(DISTINCT p.user_id) * 100.0 / COUNT(DISTINCT u.id)) AS userPostPercentage " +
            "FROM users u " +
            "LEFT JOIN posts p ON u.id = p.user_id", nativeQuery = true)
    Double getUsersPostedPercentage();

    @Query(value = "SELECT " +
            "(COUNT(DISTINCT u.id) * 100.0 / (SELECT COUNT(*) FROM users)) AS percentage_only_commented " +
            "FROM users u " +
            "LEFT JOIN posts p ON u.id = p.user_id " +
            "JOIN comments c ON u.id = c.user_id " +
            "WHERE p.user_id IS NULL", nativeQuery = true)
    Double getUsersOnlyCommentedPercentage();

    //if needed users - u.* instead of percentage_...
    @Query(value = "SELECT " +
            "(COUNT(DISTINCT u.id) * 100.0 / (SELECT COUNT(*) FROM users)) AS percentage_no_posts_no_comments " +
            "FROM users u " +
            "LEFT JOIN posts p ON u.id = p.user_id " +
            "LEFT JOIN comments c ON u.id = c.user_id " +
            "WHERE p.user_id IS NULL AND c.user_id IS NULL", nativeQuery = true)
    Double getUsersWithNoPostsOrCommentsPercentage();


}

