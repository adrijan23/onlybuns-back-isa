package team5.onlybuns.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@Table(name="MESSAGES")
@NoArgsConstructor
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
    private Date date;
}
