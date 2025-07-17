package team5.rabbit_care.dto;

import lombok.Getter;
import lombok.Setter;

public class LocationRequest {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Double latitude;

    @Getter
    @Setter
    private Double longitude;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


}
