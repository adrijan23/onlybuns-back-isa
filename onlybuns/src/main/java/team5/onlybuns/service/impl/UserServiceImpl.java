package team5.onlybuns.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
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

	@Autowired
	private EmailServiceImpl emailService;

	@Transactional
	@Override
	public User findByUsernameWithLock(String username) throws UsernameNotFoundException {
		return userRepository.findByUsernameWithLock(username);
	}


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

	@Scheduled(cron = "${notify-inactive.cron}")
	public void notifyInactiveUsers() {

		// Fetch users who have been inactive for more than 7 days
		List<User> inactiveUsers = userRepository.findInactiveUsers();

		// Send notification emails to inactive users
		for (User user : inactiveUsers) {
			System.out.println("Sending notification to : " + user.getUsername());
			emailService.sendEmail(
					user.getEmail(),
					String.format("%s, You were inactive for 7 days. Check out the news!", user.getUsername()),
					"Nema te nema"
					);
		}
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	@Override
	public User saveTransactional(UserRequest userRequest) {
		try {
			// Log entry into the method
			System.out.println("Entering save method: " + userRequest.getUsername() + " by thread " + Thread.currentThread().getName());

			// Lock the username row
			System.out.println("Locking username: " + userRequest.getUsername());
			User existingUser = userRepository.findByUsernameWithLock(userRequest.getUsername());
			if (existingUser != null) {
				throw new IllegalArgumentException("Username already exists");
			}



			// Create a new user
			User u = new User();
			u.setUsername(userRequest.getUsername());
			u.setPassword(passwordEncoder.encode(userRequest.getPassword()));
			u.setFirstName(userRequest.getFirstname());
			u.setLastName(userRequest.getLastname());
			u.setEnabled(false);
			u.setEmail(userRequest.getEmail());

			// Assign roles
			List<Role> roles = roleService.findByName("ROLE_USER");
			u.setRoles(roles);

			// Save user and return result
			User savedUser = userRepository.save(u);

			// Log successful method exit
			System.out.println("Exiting save method successfully: " + userRequest.getUsername() + " by thread " + Thread.currentThread().getName());
			return savedUser;

		} catch (org.springframework.dao.PessimisticLockingFailureException e) {
			System.out.println("Locking failure for username: " + userRequest.getUsername() + " by thread " + Thread.currentThread().getName());
			throw new IllegalArgumentException("Transaction conflict: username already in use");

		} catch (Exception e) {
			System.out.println("Unexpected exception for username: " + userRequest.getUsername() + " by thread " + Thread.currentThread().getName() + " - " + e.getMessage());
			throw e; // Rethrow to ensure other issues are not silently swallowed
		}
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

	@Scheduled(cron = "59 59 23 L * ?")
	@Transactional
	public void deleteDisabledUsers() {
		userRepository.deleteDisabledUsers();
	}

	@Transactional
	public void followUser(Long followerId, Long followingId) {
		User follower = userRepository.findById(followerId).orElseThrow(() -> new RuntimeException("User not found"));
		User following = userRepository.findById(followingId).orElseThrow(() -> new RuntimeException("User not found"));

		follower.getFollowing().add(following);
		userRepository.save(follower);
	}

	@Transactional
	public void unfollowUser(Long followerId, Long followingId) {
		User follower = userRepository.findById(followerId).orElseThrow(() -> new RuntimeException("User not found"));
		User following = userRepository.findById(followingId).orElseThrow(() -> new RuntimeException("User not found"));

		follower.getFollowing().remove(following);
		userRepository.save(follower);
	}

	public Set<User> getFollowers(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		return user.getFollowers();
	}

	public Set<User> getFollowing(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		return user.getFollowing();
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
