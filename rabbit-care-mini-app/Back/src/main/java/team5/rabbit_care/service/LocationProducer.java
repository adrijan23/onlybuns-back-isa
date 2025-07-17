package team5.rabbit_care.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LocationProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingKey;

    public void sendLocation(String name, Double longitude, Double latitude) {
        Map<String, String> message = new HashMap<>();
        message.put("name", name);
        message.put("longitude", String.valueOf(longitude));
        message.put("latitude", String.valueOf(latitude));

        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        System.out.println("Location sent: " + message);
    }
}
