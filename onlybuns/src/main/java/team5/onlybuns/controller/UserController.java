package team5.onlybuns.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import team5.onlybuns.model.Post;
import team5.onlybuns.model.User;
import team5.onlybuns.service.UserService;



@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class UserController {


	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;


	@GetMapping("/user/{userId}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")

	public User loadById(@PathVariable Long userId) {
		return this.userService.findById(userId);
	}

	@GetMapping("/user/all")
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> loadAll() {
		return this.userService.findAll();
	}

	@GetMapping("/whoami")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public User user(Principal user) {
		return this.userService.findByUsername(user.getName());
	}

//	@GetMapping("/user/{userId}/followers")
//	public ResponseEntity<Set<User>> getFollowers(@PathVariable Long userId) {
//		Set<User> followers = userService.getFollowers(userId);
//		return ResponseEntity.ok(followers);
//	}
//
//	@GetMapping("/user/{userId}/following")
//	public ResponseEntity<Set<User>> getFollowing(@PathVariable Long userId) {
//		Set<User> following = userService.getFollowing(userId);
//		return ResponseEntity.ok(following);
//	}
//
//	@PostMapping("/user/{userId}/follow")
//	public ResponseEntity<String> follow(@PathVariable Long userId, @RequestParam Long targetId) {
//		try {
//			userService.follow(userId, targetId);
//			return ResponseEntity.ok("User followed successfully.");
//
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//	}
//
//	@PostMapping("/user/{userId}/unfollow")
//	public ResponseEntity<String> unfollow(@PathVariable Long userId, @RequestParam Long targetId) {
//		try {
//			userService.follow(userId, targetId);
//			return ResponseEntity.ok("User followed successfully.");
//
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//	}

}
