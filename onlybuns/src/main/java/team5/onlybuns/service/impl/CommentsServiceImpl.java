package team5.onlybuns.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team5.onlybuns.model.Comment;
import team5.onlybuns.model.Post;
import team5.onlybuns.model.User;
import team5.onlybuns.repository.CommentsRepository;
import team5.onlybuns.repository.PostRepository;
import team5.onlybuns.repository.UserRepository;
import team5.onlybuns.service.CommentsService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Comment> findCommentsByPostId(Long postId){
        return commentsRepository.findCommentsByPostId(postId);
    }

    public Comment createComment(String content, Long postId, Long userId) throws IOException {
        // Find the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create a new post
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());

        // Save the post to the database
        return commentsRepository.save(comment);
    }

    public Comment updateComment(Long commentId, String newContent) {
        Optional<Comment> existingComment = commentsRepository.findById(commentId);

        if (existingComment.isPresent()) {
            Comment comment = existingComment.get();
            comment.setContent(newContent);
            return commentsRepository.save(comment);
        } else {
            throw new RuntimeException("Comment not found");
        }
    }

    public void deleteComment(Long commentId) {
        if (commentsRepository.existsById(commentId)) {
            commentsRepository.deleteById(commentId);
        } else {
            throw new RuntimeException("Comment not found");
        }
    }
}
