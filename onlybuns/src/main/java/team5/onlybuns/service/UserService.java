package team5.onlybuns.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import team5.onlybuns.dto.UserRequest;
import team5.onlybuns.model.User;

public interface UserService {
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll ();
	User save(UserRequest userRequest);
    void update(User user);
    Page<User> getPaginated (int page, int size);
//    Set<User> getFollowers(Long id);
//    Set<User> getFollowing(Long id);
//    void follow(Long userId, Long targetId);
//    void unfollow(Long userId, Long targetId);
}
