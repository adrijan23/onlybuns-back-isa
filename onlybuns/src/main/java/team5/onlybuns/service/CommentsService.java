package team5.onlybuns.service;

import team5.onlybuns.model.Comment;

import java.io.IOException;
import java.util.List;

public interface CommentsService {
    List<Comment> findCommentsByPostId(Long postId);
    public Comment createComment(String content, Long postId, Long userId) throws IOException;

    public Long findCommentsCountFromLastHourForUser(Long userId);
    Integer countByPostId(Long postId);

    List<Integer> getAvailableYears();
    List<Integer> getAvailableMonths(Integer year);
    List<Object[]> getPerMonth(Integer year);
    List<Object[]> getPerDay(Integer year, Integer month);
    List<Object[]> getPerWeek(Integer year, Integer month, Integer week);
}
