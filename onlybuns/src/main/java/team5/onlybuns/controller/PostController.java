package team5.onlybuns.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team5.onlybuns.dto.PostRequest;
import team5.onlybuns.model.Post;
import team5.onlybuns.model.User;
import team5.onlybuns.repository.ImageRepository;
import team5.onlybuns.repository.PostRepository;
import team5.onlybuns.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    public static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    // Endpoint to publish a new post with an image
    @PostMapping
    public Post createPost(
            @RequestPart("postRequest") PostRequest postRequest,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        // Find the user by ID
        User user = userRepository.findById(postRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            imagePath = imageRepository.saveImage(image);
        }

        // Create a new post
        Post post = new Post();
        post.setDescription(postRequest.getDescription());
        post.setAddress(postRequest.getAddress());
        post.setImagePath(imagePath);
        post.setUser(user);
        post.setLatitude(postRequest.getLatitude());
        post.setLongitude(postRequest.getLongitude());
        post.setCreatedAt(LocalDateTime.now());

        return postRepository.save(post);
    }


    // Fetch all posts (for verification/testing)
    @GetMapping
    public List<Post> getAllPosts() {
        System.out.println("SADASDASDADSASDASD");

        return postRepository.findAll();
    }
    @GetMapping("/{userId}")
    public List<Post> getAllByUserId(@PathVariable Long userId) {

        return postRepository.findByUserId(userId);
    }
}
