package team5.onlybuns.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.hash.BloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import team5.onlybuns.config.ActiveUsersMetricsConfig;
import team5.onlybuns.config.BloomFilterConfig;
import team5.onlybuns.dto.JwtAuthenticationRequest;
import team5.onlybuns.dto.UserRequest;
import team5.onlybuns.dto.UserTokenState;
import team5.onlybuns.exception.ResourceConflictException;
import team5.onlybuns.model.User;
import team5.onlybuns.service.EmailService;
import team5.onlybuns.service.UserService;
import team5.onlybuns.util.TokenUtils;

import java.time.LocalDateTime;


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

	@Autowired
	private CacheManager cacheManager; // EhCache Cache Manager

	@Autowired
	private BloomFilterConfig bloomFilterConfig;

	@Autowired
	private ActiveUsersMetricsConfig metricsConfig;

	private static final int MAX_ATTEMPTS = 5; // Maksimalan broj pokušaja logina po IP adresi


	public void onLoginSuccess() {
		metricsConfig.getActiveUsers().incrementAndGet();
	}

	public void onLogout() {
		metricsConfig.getActiveUsers().decrementAndGet();
	}

	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletRequest request, HttpServletResponse response) {

		String clientIp = request.getRemoteAddr();

		// Provera da li je IP blokiran
		if (isBlocked(clientIp)) {
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
					.body(null); // Ne šaljemo token jer je IP blokiran
		}

		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							authenticationRequest.getUsername(), authenticationRequest.getPassword()
					)
			);

			SecurityContextHolder.getContext().setAuthentication(authentication);

			User user = (User) authentication.getPrincipal();
			user.setLastActive(LocalDateTime.now());
			userService.update(user);

			// Resetujemo pokušaje za IP adresu nakon uspešnog logina
			resetAttempts(clientIp);

			String jwt = tokenUtils.generateToken(user.getUsername());
			int expiresIn = tokenUtils.getExpiredIn();
			onLoginSuccess();
			return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
		} catch (DisabledException ex) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Account is not activated. Please check your email for the activation link.");
		}catch (Exception e) {
			// Registrujemo neuspešan pokušaj logina
			registerFailedAttempt(clientIp);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	private boolean isBlocked(String ip) {
		Cache cache = cacheManager.getCache("rateLimiterCache");
		if (cache == null) {
			return false; // Ako keš nije pronađen, IP nije blokiran
		}

		Integer attempts = cache.get(ip, Integer.class);
		return attempts != null && attempts >= MAX_ATTEMPTS;
	}

	private void registerFailedAttempt(String ip) {
		Cache cache = cacheManager.getCache("rateLimiterCache");
		if (cache == null) {
			return; // Ako keš nije pronađen, ne možemo pratiti pokušaje
		}

		Integer attempts = cache.get(ip, Integer.class);
		if (attempts == null) {
			cache.put(ip, 1); // Prvi neuspešan pokušaj
		} else {
			cache.put(ip, attempts + 1); // Uvećavamo broj pokušaja
		}
	}

	private void resetAttempts(String ip) {
		Cache cache = cacheManager.getCache("rateLimiterCache");
		if (cache != null) {
			cache.evict(ip); // Brišemo podatke za IP adresu nakon uspešnog logina
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest) {

		BloomFilter<String> usernameBloomFilter = bloomFilterConfig.getUsernameBloomFilter();
		String normalizedUsername = userRequest.getUsername().toLowerCase();

		if (usernameBloomFilter.mightContain(normalizedUsername)) {
			User existUser = userService.findByUsername(normalizedUsername);
			if (existUser != null) {
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body("Username already exists");
			}
		}

		User user = userService.saveTransactional(userRequest);
		usernameBloomFilter.put(user.getUsername().toLowerCase());

		// Generate activation link
		String token = tokenUtils.generateToken(user.getUsername());
		String activationLink = "http://localhost:3000/activate?token=" + token;

		// Send email
		emailService.sendEmail(userRequest.getEmail(),
				"Account Activation",
				"Please click the link to activate your account: " + activationLink);

		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout() {
		onLogout();
		return ResponseEntity.ok("Logged out");
	}

	@GetMapping("/activate")
	public ResponseEntity<?> activateUser(@RequestParam String token) {
		String username = tokenUtils.getUsernameFromToken(token);
		User user = userService.findByUsername(username);

		if (user != null) {
			user.setEnabled(true);
			user.setRegistrationDate(LocalDateTime.now());
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