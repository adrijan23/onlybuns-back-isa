package team5.onlybuns.service;

import java.util.List;

import team5.onlybuns.dto.UserRequest;
import team5.onlybuns.model.User;

public interface UserService {
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll ();
	User save(UserRequest userRequest);
    void update(User user);
}
