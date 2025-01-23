package team5.onlybuns.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team5.onlybuns.model.ChatMessage;
import team5.onlybuns.model.ChatRoom;
import team5.onlybuns.model.ChatUser;
import team5.onlybuns.model.User;
import team5.onlybuns.repository.ChatMessageRepository;
import team5.onlybuns.repository.ChatRoomRepository;
import team5.onlybuns.repository.ChatUserRepository;
import team5.onlybuns.repository.UserRepository;
import team5.onlybuns.service.ChatService;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService{
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatUserRepository chatUserRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ChatRoom findChatRoomById(Long id) {
        return chatRoomRepository.findById(id).orElse(null);
    }

    @Override
    public void saveMessage(ChatMessage message, String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(roomId)).orElseThrow(() -> new EntityNotFoundException("ChatRoom not found"));
        message.setChatRoom(chatRoom);
        chatMessageRepository.save(message);
    }

    @Override
    public List<ChatMessage> getLast10Messages(Long chatRoomId, Long userId) {
        ChatUser chatUser = chatUserRepository.findByUserIdAndChatRoomId(userId, chatRoomId);
        if(chatUser != null) {
            Pageable pageable = PageRequest.of(0,10);
            return chatMessageRepository.findMessagesBeforeJoin(chatRoomId, chatUser.getJoinTime(), pageable);
        }
        return Collections.emptyList();
    }

    @Override
    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public ChatUser addUserToChatRoom(Long chatRoomId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(()-> new RuntimeException("ChatRoom not found"));

        ChatUser chatUser = new ChatUser();
        chatUser.setChatRoom(chatRoom);
        chatUser.setUser(user);
        return chatUserRepository.save(chatUser);
    }

    @Override
    public List<ChatRoom> getChatRoomsForUser(Long userId) {
        return chatUserRepository.findChatRoomsByUserId(userId);
    }
}
