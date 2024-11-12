package team5.onlybuns.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "POSTS")
public class Post {

    @Getter
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private String imagePath; // Store the file path instead of base64 or BLOB
    @Getter
    @Setter
    private String address;
    @Getter
    @Setter
    private Double latitude;
    @Getter
    @Setter
    private Double longitude;
    @Getter
    @Setter
    private LocalDateTime createdAt;

    @ManyToOne
    @Getter
    @Setter
    private User user;

    @Setter
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany
    @Getter
    private Set<User> likes = new HashSet<>();

    public Post() {
        this.createdAt = LocalDateTime.now();
    }
}
