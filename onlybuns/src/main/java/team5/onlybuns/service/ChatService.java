package team5.onlybuns.service;

import team5.onlybuns.model.ChatMessage;
import team5.onlybuns.model.ChatRoom;
import team5.onlybuns.model.ChatUser;

import java.util.List;

public interface ChatService {
    void saveMessage(ChatMessage message, String roomId);
    List<ChatMessage> getLast10Messages(Long chatRoomId, Long userId);
    ChatRoom createChatRoom(Long adminId);
    ChatUser joinChatRoom(Long chatRoomId, Long userId);
    List<ChatRoom> getChatRoomsForUser(Long userId);
}
