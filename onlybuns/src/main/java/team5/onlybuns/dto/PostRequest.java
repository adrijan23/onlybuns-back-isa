package team5.onlybuns.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostRequest {
    private String description;
    private String address;
    private Long userId;
    private String imagePath; // Path to the image, if applicable
    private double longitude;
    private double latitude;
}

