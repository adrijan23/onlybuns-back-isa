package team5.onlybuns.service;

import team5.onlybuns.model.Comment;

import java.util.List;

public interface CommentsService {
    List<Comment> findCommentsByPostId(Long postId);
}
