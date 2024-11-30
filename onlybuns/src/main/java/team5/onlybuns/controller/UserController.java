package team5.onlybuns.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import team5.onlybuns.model.Post;
import team5.onlybuns.model.User;
import team5.onlybuns.repository.ImageRepository;
import team5.onlybuns.service.UserService;



@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class UserController {


	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private ImageRepository imageRepository;


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

	@GetMapping("/user/page")
	@PreAuthorize("hasRole('ADMIN')")
	public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
							   @RequestParam(defaultValue = "5") int size) {
		return this.userService.getPaginated(page, size);
	}

	@PostMapping("/follow/{followingId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<?> followUser(@PathVariable Long followingId, Principal user) {
		User currentUser = this.user(user);
		userService.followUser(currentUser.getId(), followingId);
		return ResponseEntity.ok("Successfully followed user");
	}

	@PostMapping("/unfollow/{followingId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<?> unfollowUser(@PathVariable Long followingId, Principal user) {
		User currentUser = this.user(user);
		userService.unfollowUser(currentUser.getId(), followingId);
		return ResponseEntity.ok("Successfully unfollowed user");
	}

	@GetMapping("/{userId}/followers")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<Set<User>> getFollowers(@PathVariable Long userId) {
		Set<User> followers = userService.getFollowers(userId);
		return ResponseEntity.ok(followers);
	}

	@GetMapping("/{userId}/following")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<Set<User>> getFollowing(@PathVariable Long userId) {
		Set<User> following = userService.getFollowing(userId);
		return ResponseEntity.ok(following);
	}

	@PostMapping("/user/{userId}/profile-image")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<?> updateProfileImage(
			@PathVariable Long userId,
			@RequestPart("image") MultipartFile image,
			Principal principal) {

		try {
			// Fetch the current user making the request
			User currentUser = this.user(principal);

			// Check if the user is authorized to update the profile
			if (!currentUser.getId().equals(userId)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body("You can only update your own profile image");
			}

			// Fetch the user from the database
			User user = userService.findById(userId);

			// Save the image and get its path
			String imagePath = null;
			if(image != null){
				imagePath = imageRepository.saveImage(image);
			}


			// Update the user's profile image
			user.setProfileImage(imagePath);
			userService.update(user); // Save updated user

			return ResponseEntity.ok().build();

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/user/{userId}/remove-profile-image")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<?> removeProfileImage(@PathVariable Long userId, Principal principal) {
		try {
			// Fetch the current user making the request
			User currentUser = this.user(principal);

			// Check if the user is authorized to update the profile
			if (!currentUser.getId().equals(userId)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body("You can only update your own profile image");
			}

			// Fetch the user from the database
			User user = userService.findById(userId);

			// Set profile image to null
			user.setProfileImage(null);
			userService.update(user); // Save updated user

			return ResponseEntity.ok("Profile image removed successfully");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
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
