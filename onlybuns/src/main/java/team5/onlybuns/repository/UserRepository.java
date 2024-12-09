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
}

