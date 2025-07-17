package team5.onlybuns.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team5.onlybuns.model.RabbitCareLocation;
import team5.onlybuns.service.RabbitCareLocationService;

import java.util.List;

@RestController
@RequestMapping("api/care-locations")
public class RabbitCareLocationController {
    @Autowired
    private RabbitCareLocationService service;

    @GetMapping
    public List<RabbitCareLocation> getLocations() {
        return service.getAllLocations();
    }
}
