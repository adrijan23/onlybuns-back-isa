package team5.onlybuns.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team5.onlybuns.model.ChatMessage;
import team5.onlybuns.model.ChatRoom;
import team5.onlybuns.model.ChatUser;
import team5.onlybuns.model.User;
import team5.onlybuns.service.ChatService;
import team5.onlybuns.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin("http://localhost:3000")
public class ChatRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private ChatService chatService;

    @GetMapping("chatrooms/{userId}")
    public List<ChatRoom> getChatRoomsForUser(@PathVariable Long userId) {
        return chatService.getChatRoomsForUser(userId);
    }

    @GetMapping("room/{roomId}")
    public ChatRoom getChatRoom(@PathVariable Long roomId) {
        return chatService.getChatRoomById(roomId);
    }

    @PostMapping("create")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestParam Long userId, @RequestParam String roomName) {
        try {
            User user = userService.findById(userId);
            if(user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setName(roomName);
            chatRoom.setChatAdminId(userId);

            ChatRoom savedRoom = chatService.createChatRoom(chatRoom);

            chatService.addUserToChatRoom(savedRoom.getId(), userId);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("add-user")
    public ResponseEntity<ChatUser> addUserToRoom(@RequestParam Long userId, @RequestParam Long roomId) {
        try {
            ChatUser savedUser = chatService.addUserToChatRoom(roomId, userId);
            if (savedUser != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("delete-user")
    public ResponseEntity<String> deleteUserFromRoom(@RequestParam Long id) {
        chatService.removeUserFromChatRoom(id);
        return ResponseEntity.status(HttpStatus.OK).body("User successfully deleted from chat");
    }

    @GetMapping("messages/{roomId}/{userId}")
    public List<ChatMessage> getChatHistory(@PathVariable Long roomId, @PathVariable Long userId) {
        List<ChatMessage> messages = new ArrayList<>(chatService.getLast10Messages(roomId, userId));
        Collections.reverse(messages);
        messages.addAll(chatService.getMessages(roomId, userId));
        return messages;
    }

    @GetMapping("room/{roomId}/users")
    public List<ChatUser> getUsers(@PathVariable Long roomId) {
        return chatService.getUsers(roomId);
    }
}
