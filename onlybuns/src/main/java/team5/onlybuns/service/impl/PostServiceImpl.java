package team5.onlybuns.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team5.onlybuns.model.Like;
import team5.onlybuns.model.Post;
import team5.onlybuns.model.User;
import team5.onlybuns.repository.PostRepository;
import team5.onlybuns.repository.UserRepository;
import team5.onlybuns.service.PostService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Override
    public void deletePost(Long id) {
        // Find the post by ID
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Delete the post
        postRepository.delete(post);
    }

    @Override
    public Post updatePost(Long id, Post updatedPost) {
        // Find the post by ID
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Update the fields of the post
        existingPost.setDescription(updatedPost.getDescription());
        existingPost.setAddress(updatedPost.getAddress());
        existingPost.setLatitude(updatedPost.getLatitude());
        existingPost.setLongitude(updatedPost.getLongitude());

        // If the image path needs to be updated, handle file upload (optional)
        if (updatedPost.getImagePath() != null) {
            existingPost.setImagePath(updatedPost.getImagePath());
        }

        // Save the updated post
        return postRepository.save(existingPost);
    }

    // Method to fetch posts by user ID
    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findByUserId(userId);
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> findByUserId(Long userId) {
        return postRepository.findByUserId(userId);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post addLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the like already exists
        boolean alreadyLiked = post.getLikes().stream()
                .anyMatch(like -> like.getUser().equals(user));
        if (alreadyLiked) {
            throw new RuntimeException("Post already liked by user");
        }

        // Create and save the like
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        post.getLikes().add(like);

        return postRepository.save(post);
    }

    public Post removeLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Find and remove the like
        post.getLikes().removeIf(like -> like.getUser().equals(user));
        return postRepository.save(post);
    }

    @Override
    public Set<User> getLikes(Long postId) {
        // Fetch the post by ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Extract the users who liked the post from the Like entities
        Set<User> usersWhoLiked = post.getLikes().stream()
                .map(Like::getUser)
                .collect(Collectors.toSet());

        return usersWhoLiked;
    }

    @Override
    public boolean hasUserLikedPost(Long postId, Long userId) {
        // Fetch the post by ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Check if the likes contain the current user
        return post.getLikes().stream()
                .anyMatch(like -> like.getUser().getId().equals(userId));
    }



    @Override
    public Integer getPostCount() {
        return (int)postRepository.count();
    }

    @Override
    public Integer getPostsInLastMonth() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return postRepository.countPostsInLastMonth(thirtyDaysAgo);
    }

    @Override
    @Cacheable("topPostsWeekly")
    public List<Post> getTopPostsLast7Days() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return postRepository.findTopPostsLast7Days(sevenDaysAgo, PageRequest.of(0, 5));
    }

    @Override
    @Cacheable("topPostsAllTime")
    public List<Post> getTopPostsAllTime() {
        return postRepository.findTopPostsAllTime(PageRequest.of(0, 10)); // Top 10 posts
    }
}
