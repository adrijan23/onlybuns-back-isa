package team5.onlybuns.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team5.onlybuns.model.Post;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdMessageSender {

    private final AmqpTemplate amqpTemplate;
    private final FanoutExchange fanoutExchange;
    @Autowired
    private ObjectMapper objectMapper;

    public AdMessageSender(AmqpTemplate amqpTemplate, FanoutExchange fanoutExchange) {
        this.amqpTemplate = amqpTemplate;
        this.fanoutExchange = fanoutExchange;
    }

    public void sendAdMessage(Post post) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(Map.of(
                "description", post.getDescription(),
                "createdAt", post.getCreatedAt().toString(),
                "username", post.getUser().getUsername()
        ));

        amqpTemplate.convertAndSend(fanoutExchange.getName(), "", message);
    }
}

