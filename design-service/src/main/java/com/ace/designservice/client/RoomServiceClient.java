package com.ace.designservice.client;


import com.ace.designservice.dto.RoomSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RoomServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public RoomServiceClient(RestTemplate restTemplate,
                             @Value("${room.service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public RoomSummary getRoomById(String roomId) {
        String url = baseUrl + "/rooms/" + roomId;
        return restTemplate.getForObject(url, RoomSummary.class);
    }
}
