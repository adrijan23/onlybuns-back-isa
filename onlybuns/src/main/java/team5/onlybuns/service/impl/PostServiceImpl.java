package team5.onlybuns.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team5.onlybuns.model.Post;
import team5.onlybuns.model.User;
import team5.onlybuns.repository.PostRepository;
import team5.onlybuns.repository.UserRepository;
import team5.onlybuns.service.PostService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    // Method to create a new post
    public Post createPost(String description, String address, Long userId, MultipartFile file) throws IOException {
        // Find the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Save the uploaded file to the filesystem
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = Paths.get(UPLOAD_DIR, filename).toString();
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        file.transferTo(new File(filePath));

        // Create a new post
        Post post = new Post();
        post.setDescription(description);
        post.setAddress(address);
        post.setImagePath(filePath);
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());

        // Save the post to the database
        return postRepository.save(post);
    }

    @Override
    public Post getPost(Long id) {
        return postRepository.findPostById(id);
    }

    // Method to fetch posts by user ID
    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findByUserId(userId);
    }
}
