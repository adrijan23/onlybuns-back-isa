package team5.onlybuns.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import team5.onlybuns.dto.UserRequest;
import team5.onlybuns.model.Role;
import team5.onlybuns.model.User;
import team5.onlybuns.repository.UserRepository;
import team5.onlybuns.service.RoleService;
import team5.onlybuns.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleService roleService;

	@Override
	public User findByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username);
	}

	public User findById(Long id) throws AccessDeniedException {
		return userRepository.findById(id).orElseGet(null);
	}

	public List<User> findAll() throws AccessDeniedException {
		return userRepository.findAll();
	}

	@Override
	public User save(UserRequest userRequest) {
		User u = new User();
		u.setUsername(userRequest.getUsername());

		u.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		
		u.setFirstName(userRequest.getFirstname());
		u.setLastName(userRequest.getLastname());
		u.setEnabled(false);
		u.setEmail(userRequest.getEmail());

		List<Role> roles = roleService.findByName("ROLE_USER");
		u.setRoles(roles);
		
		return this.userRepository.save(u);
	}

	public void update(User user) {
		userRepository.save(user);
	}

	public Page<User> getPaginated(int page, int size) {
		Pageable pageable = PageRequest.of(page,size);
		return userRepository.findAll(pageable);
	}

//	public void follow(Long userId, Long targetUserId) {
//		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//		User targetUser = userRepository.findById(targetUserId).orElseThrow(() -> new RuntimeException("User not found"));
//
//		user.getFollowing().add(targetUser);
//		userRepository.save(user);
//	}
//
//	public void unfollow(Long userId, Long targetUserId) {
//		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//		User targetUser = userRepository.findById(targetUserId).orElseThrow(() -> new RuntimeException("User not found"));
//
//		user.getFollowing().remove(targetUser);
//		userRepository.save(user);
//	}
//
//	public Set<User> getFollowers(Long userId) {
//		User user = userRepository.findById(userId).orElseThrow();
//		return user.getFollowers();
//	}
//
//	public Set<User> getFollowing(Long userId) {
//		User user = userRepository.findById(userId).orElseThrow();
//		return user.getFollowing();
//	}

}
