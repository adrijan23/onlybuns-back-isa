package team5.onlybuns.service;

import org.springframework.web.multipart.MultipartFile;
import team5.onlybuns.model.Post;
import team5.onlybuns.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface PostService {

    Post createPost(String description, String address, Long userId, MultipartFile file) throws IOException;
    List<Post> getPostsByUser(Long userId);

    Post save(Post post);

    List<Post> findByUserId(Long userId);

    List<Post> findAll();
    Post getPost(Long id);
    void deletePost(Long id);
    Post updatePost(Long id, Post post);
    Post addLike(Long postId, Long userId);
    Post removeLike(Long postId, Long userId);
    Set<User> getLikes(Long postId);
    Integer getPostCount();
    Integer getPostsInLastMonth();
    List<Post> getTopPostsAllTime();
    List<Post> getTopPostsLast7Days();
    boolean hasUserLikedPost(Long postId, Long userId);
}
