package team5.onlybuns.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithStatsDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Integer followersCount;
    private Integer followingCount;
    private Integer postsCount;
}
