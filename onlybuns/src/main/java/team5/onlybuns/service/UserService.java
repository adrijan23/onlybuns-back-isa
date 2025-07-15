package team5.onlybuns.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import team5.onlybuns.dto.UserRequest;
import team5.onlybuns.model.User;

public interface UserService {
    User findById(Long id);
    User findByUsername(String username);
    User findByUsernameWithLock(String username);
    List<User> findAll ();
	User save(UserRequest userRequest);
    User saveTransactional(UserRequest userRequest);
    void update(User user);
    Page<User> getPaginated (int page, int size);
//    Set<User> getFollowers(Long id);
//    Set<User> getFollowing(Long id);
//    void follow(Long userId, Long targetId);
//    void unfollow(Long userId, Long targetId);
    void followUser(Long followerId, Long followingId);
    User findByIdWithFollowing(Long id);

    void unfollowUser(Long followerId, Long followingId);

    Set<User> getFollowers(Long userId);

    Set<User> getFollowing(Long userId);
    List<User> getTopLikersWeekly();
    boolean checkPassword(String rawPassword, String encodedPassword);
    void updatePassword(Long userId, String newPassword);
    Double getPostedPercentage();
    Double getOnlyCommentedPercentage();
    Double getNoPostOrCommentPercentage();

    List<String> findAllUsernames();
}
