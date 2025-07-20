package team5.onlybuns.service;

import java.util.Map;

public interface LocationConsumer {
    void receiveLocation(Map<String, String> message);
}
