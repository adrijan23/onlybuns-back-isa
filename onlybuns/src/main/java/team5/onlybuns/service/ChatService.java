package team5.onlybuns.service;

import team5.onlybuns.model.ChatMessage;
import team5.onlybuns.model.ChatRoom;
import team5.onlybuns.model.ChatUser;

import java.util.List;

public interface ChatService {
    void saveMessage(ChatMessage message, String roomId);
    List<ChatMessage> getLast10Messages(Long chatRoomId, Long userId);
    ChatRoom createChatRoom(ChatRoom chatRoom);
    ChatUser addUserToChatRoom(Long chatRoomId, Long userId);
    void removeUserFromChatRoom(Long id);
    List<ChatRoom> getChatRoomsForUser(Long userId);
    ChatRoom getChatRoomById(Long chatRoomId);
    List<ChatMessage> getMessages(Long chatRoomId, Long userId);
    List<ChatUser> getUsers(Long chatRoomId);
}
