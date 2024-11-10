package team5.onlybuns.dto;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class PostRequest {
    @NotNull()
    private Long userId;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotNull()
    private Double latitude;

    @NotNull()
    private Double longitude;
}

