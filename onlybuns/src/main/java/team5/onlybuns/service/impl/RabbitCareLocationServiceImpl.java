package team5.onlybuns.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team5.onlybuns.model.RabbitCareLocation;
import team5.onlybuns.repository.RabbitCareLocationRepository;
import team5.onlybuns.service.RabbitCareLocationService;

import java.util.List;

@Service
public class RabbitCareLocationServiceImpl implements RabbitCareLocationService {

    @Autowired
    private RabbitCareLocationRepository rabbitCareLocationRepository;


    @Override
    public List<RabbitCareLocation> getAllLocations() {
        return rabbitCareLocationRepository.findAll();
    }
}
