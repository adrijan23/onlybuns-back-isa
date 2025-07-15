package team5.onlybuns.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "street", nullable = false)
    private String street;

    @Getter
    @Setter
    @Column(name = "street_number")
    private String streetNumber;

    @Getter
    @Setter
    @Column(name = "city", nullable = false)
    private String city;

    @Getter
    @Setter
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Getter
    @Setter
    @Column(name = "longitude", nullable = false)
    private Double longitude;

}
