package team5.onlybuns.model;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="COMMENTS")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private LocalDateTime createdAt;

    @ManyToOne
    private User user;

    @ManyToOne
    private Post post;

    public Comment() {
        this.createdAt = LocalDateTime.now();
    }
}
