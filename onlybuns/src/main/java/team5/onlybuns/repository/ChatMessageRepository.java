package team5.onlybuns.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team5.onlybuns.model.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Query to find the last 10 messages before a user's join time
    @Query("SELECT m FROM ChatMessage m WHERE m.chatRoom.id = :chatRoomId AND m.date <= :joinTime ORDER BY m.date DESC")
    List<ChatMessage> findMessagesBeforeJoin(@Param("chatRoomId") Long chatRoomId, @Param("joinTime") LocalDateTime joinTime, Pageable pageable);

    @Query("SELECT m FROM ChatMessage m WHERE m.chatRoom.id = :chatRoomId AND m.date > :joinTime ORDER BY m.date")
    List<ChatMessage> getMessages(@Param("chatRoomId") Long chatRoomId, @Param("joinTime") LocalDateTime joinTime);
}
