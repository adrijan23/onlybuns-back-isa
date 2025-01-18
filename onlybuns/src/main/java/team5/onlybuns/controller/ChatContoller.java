package team5.onlybuns.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import team5.onlybuns.model.ChatMessage;
import team5.onlybuns.model.ChatRoom;
import team5.onlybuns.service.ChatService;

import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin("http://localhost:3000")
public class ChatContoller {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/chat-room/{roomId}}")
    public ChatMessage sendMessage(@DestinationVariable String roomId, @Payload ChatMessage message) {
        message.setDate(new Date());
        chatService.saveMessage(message, roomId);
        return message;
    }

    @MessageMapping("/chat/{roomId}/history")
    @SendTo("/chat-room/{roomId}")
    public List<ChatMessage> getChatHistory(@DestinationVariable String roomId, @Payload Long userId) {
        return chatService.getLast10Messages(Long.parseLong(roomId), userId);
    }

    @GetMapping("/chat/chatrooms/{userId}")
    public List<ChatRoom> getChatRoomsForUser(@PathVariable Long userId) {
        return chatService.getChatRoomsForUser(userId);
    }
}
