package team5.onlybuns.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team5.onlybuns.model.RabbitCareLocation;
import team5.onlybuns.repository.RabbitCareLocationRepository;
import team5.onlybuns.service.LocationConsumer;

import java.util.Map;

@Service
public class LocationConsumerImpl implements LocationConsumer {

    @Autowired
    private RabbitCareLocationRepository rabbitCareLocationRepository;


    @RabbitListener(queues = "rabbit-care-locations-queue")
    public void receiveLocation(Map<String, String> message) {
        System.out.println("Received Rabbit Care location: " + message);

        // Parse the message and map it to RabbitCareLocation
        String name = message.get("name");
        double latitude = Double.parseDouble(message.get("latitude"));
        double longitude = Double.parseDouble(message.get("longitude"));

        RabbitCareLocation location = new RabbitCareLocation();
        location.setName(name);
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        rabbitCareLocationRepository.save(location);
        System.out.println("Location saved to database: " + location);
    }
}
