package team5.onlybuns.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team5.onlybuns.model.Comment;
import team5.onlybuns.repository.CommentsRepository;
import team5.onlybuns.service.CommentsService;

import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentsRepository commentsRepository;

    public List<Comment> findCommentsByPostId(Long postId){
        return commentsRepository.findCommentsByPostId(postId);
    }
}
