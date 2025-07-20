package team5.onlybuns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team5.onlybuns.model.ChatRoom;
import team5.onlybuns.model.ChatUser;

import java.util.List;
import java.util.Optional;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    @Query("SELECT cu FROM ChatUser cu WHERE cu.user.id = :userId AND cu.chatRoom.id = :chatRoomId")
    Optional<ChatUser> findByUserIdAndChatRoomId(@Param("userId") Long userId, @Param("chatRoomId") Long chatRoomId);

    @Query("SELECT cu.chatRoom FROM ChatUser cu WHERE cu.user.id = :userId")
    List<ChatRoom> findChatRoomsByUserId(@Param("userId") Long userId);

    List<ChatUser> findByChatRoomId(Long chatRoomId);
}
