package team5.rabbit_care.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team5.rabbit_care.dto.LocationRequest;
import team5.rabbit_care.service.LocationProducer;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationProducer producer;

    @PostMapping("/send")
    public ResponseEntity<?> sendLocation(@RequestBody LocationRequest request) {
        producer.sendLocation(request.getName(), request.getLongitude(), request.getLatitude());
        return ResponseEntity.ok("Location sent successfully!");
    }
}
