package team5.onlybuns.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "CHAT_USERS")
public class ChatUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Setter
    @Column(name = "join_time")
    private LocalDateTime joinTime;

    public ChatUser() {
        this.joinTime = LocalDateTime.now();
    }

    // Getters, setters, and other methods (e.g., adding join time) go here
}

