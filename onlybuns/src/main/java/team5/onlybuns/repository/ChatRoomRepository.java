package team5.onlybuns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team5.onlybuns.model.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
