package team5.onlybuns.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import team5.onlybuns.model.ChatMessage;

import java.util.Date;

@Controller
@CrossOrigin("http://localhost:3000")
public class ChatContoller {
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(@Payload ChatMessage message) {
        message.setDate(new Date());
        return message;
    }
}
