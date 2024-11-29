package team5.onlybuns.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CommentRequest {
    @NotNull()
    private Long userId;

    @NotNull()
    private Long postId;

    @NotBlank(message = "Content cannot be blank")
    @Size(max = 500, message = "Content cannot exceed 500 characters")
    private String content;
}
