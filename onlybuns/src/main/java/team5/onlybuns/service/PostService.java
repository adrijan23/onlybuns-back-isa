package team5.onlybuns.service;

import org.springframework.web.multipart.MultipartFile;
import team5.onlybuns.model.Post;

import java.io.IOException;
import java.util.List;

public interface PostService {

    Post createPost(String description, String address, Long userId, MultipartFile file) throws IOException;
    List<Post> getPostsByUser(Long userId);

    Post save(Post post);

    List<Post> findByUserId(Long userId);

    List<Post> findAll();
}
