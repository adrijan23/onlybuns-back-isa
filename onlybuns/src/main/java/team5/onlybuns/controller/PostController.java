package team5.onlybuns.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team5.onlybuns.dto.PostRequest;
import team5.onlybuns.model.Comment;
import team5.onlybuns.model.Post;
import team5.onlybuns.model.User;
import team5.onlybuns.repository.ImageRepository;
import team5.onlybuns.repository.PostRepository;
import team5.onlybuns.repository.UserRepository;
import team5.onlybuns.service.CommentsService;
import team5.onlybuns.service.PostService;
import team5.onlybuns.service.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentsService commentsService;

    public static final String UPLOAD_DIR = "uploads/";

    /**
     * Create a new post with an optional image.
     */
    @PostMapping
    public ResponseEntity<Post> createPost(
            @Valid @RequestPart("postRequest") PostRequest postRequest,
            @RequestPart(value = "image", required = true) MultipartFile image) {

        try {
            // Fetch the user by ID
            User user = userService.findById(postRequest.getUserId());

            // Save the image if provided
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

            Post savedPost = postService.save(post);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("id/{postId}")
    public Post getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.findAll();
        return ResponseEntity.ok(posts);
    }
  
    @GetMapping("/{userId}")
    public ResponseEntity<List<Post>> getAllByUserId(@PathVariable Long userId) {
        List<Post> posts = postService.findByUserId(userId);
        if (posts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
        List<Comment> comments = commentsService.findCommentsByPostId(postId);

        // Check if comments exist for the given post
        if (comments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comments);
    }
}
