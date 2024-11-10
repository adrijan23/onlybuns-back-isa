package team5.onlybuns.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team5.onlybuns.dto.PostRequest;
import team5.onlybuns.model.Post;
import team5.onlybuns.model.User;
import team5.onlybuns.repository.PostRepository;
import team5.onlybuns.repository.UserRepository;
import team5.onlybuns.service.PostService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    // Endpoint to publish a new post with an image
    @PostMapping
    public Post createPost(@RequestBody PostRequest postRequest) {
        // Find the user by ID
        User user = userRepository.findById(postRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create a new post
        Post post = new Post();
        post.setDescription(postRequest.getDescription());
        post.setAddress(postRequest.getAddress());
        post.setImagePath(postRequest.getImagePath()); // Set the image path if provided
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());

        // Save the post to the database
        return postRepository.save(post);
    }

    @GetMapping("/{postId}")
    public Post getPost(@PathVariable Long postId) {
        return postRepository.findPostById(postId);
    }

    // Fetch all posts (for verification/testing)
    @GetMapping
    public List<Post> getAllPosts() {
        System.out.println("SADASDASDADSASDASD");

        return postRepository.findAll();
    }
    @GetMapping("/user/{userId}")
    public List<Post> getAllByUserId(@PathVariable Long userId) {

        return postRepository.findByUserId(userId);
    }
}
