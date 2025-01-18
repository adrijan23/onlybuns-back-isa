package team5.onlybuns.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@Table(name="MESSAGES")
@AllArgsConstructor
@Data
@ToString
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String username;
    @Setter
    private String content;
    @Setter
    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    public ChatMessage() {
        this.date = new Date();
    }
}
