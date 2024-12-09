package team5.onlybuns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team5.onlybuns.model.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Integer countByPostId(Long postId);
}
