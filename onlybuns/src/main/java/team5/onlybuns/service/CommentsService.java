package team5.onlybuns.service;

import team5.onlybuns.model.Comment;

import java.io.IOException;
import java.util.List;

public interface CommentsService {
    List<Comment> findCommentsByPostId(Long postId);
    public Comment createComment(String content, Long postId, Long userId) throws IOException;
}
