package team5.onlybuns.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "CHAT_ROOMS")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_admin_id")
    @Setter
    private Long chatAdminId;

    @Column(name = "created_at")
    @Setter
    private LocalDateTime createdAt;

    @Column(name = "name")
    @Setter
    private String name;

    @Setter
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> messages = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatUser> chatUsers = new ArrayList<>();

    public ChatRoom() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters, setters, and other methods (e.g., adding messages, users) go here
}

