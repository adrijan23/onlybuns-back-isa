package team5.onlybuns.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import team5.onlybuns.dto.JwtAuthenticationRequest;
import team5.onlybuns.dto.UserRequest;
import team5.onlybuns.dto.UserTokenState;
import team5.onlybuns.exception.ResourceConflictException;
import team5.onlybuns.model.User;
import team5.onlybuns.service.EmailService;
import team5.onlybuns.service.UserService;
import team5.onlybuns.util.TokenUtils;


@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@PostMapping("/login")
	public ResponseEntity<UserTokenState> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		User user = (User) authentication.getPrincipal();
		String jwt = tokenUtils.generateToken(user.getUsername());
		int expiresIn = tokenUtils.getExpiredIn();

		return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest) {
		User existUser = userService.findByUsername(userRequest.getUsername());

		if (existUser != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Username already exists");
		}

		User user = userService.save(userRequest);

		// Generate activation link
		String token = tokenUtils.generateToken(user.getUsername());
		String activationLink = "http://localhost:3000/activate?token=" + token;

		// Send email
		emailService.sendEmail(userRequest.getEmail(),
				"Account Activation",
				"Please click the link to activate your account: " + activationLink);

		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	@GetMapping("/activate")
	public ResponseEntity<?> activateUser(@RequestParam String token) {
		String username = tokenUtils.getUsernameFromToken(token);
		User user = userService.findByUsername(username);

		if (user != null) {
			user.setEnabled(true);
			userService.update(user);
			return ResponseEntity.ok("Account activated successfully");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid activation link");
		}
	}


	@GetMapping("/test-email")
	public ResponseEntity<String> testEmail() {
		emailService.sendEmail("tadik16734@gianes.com", "Test Subject", "This is a test email.");
		return ResponseEntity.ok("Email sent successfully");
	}

}