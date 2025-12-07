package com.ace.room_service.client;

import com.ace.room_service.dto.AiPredictionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AiRoomServiceClient {

    private static final Logger log = LoggerFactory.getLogger(AiRoomServiceClient.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AiRoomServiceClient(RestTemplate restTemplate,
                               @Value("${ai.room.service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public AiPredictionResponse predictRoomType(String imageUrl) {
        String url = baseUrl + "/predict";

        Map<String, String> body = Map.of("imageUrl", imageUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        log.info("Calling AI room service at {} with imageUrl={}", url, imageUrl);

        ResponseEntity<AiPredictionResponse> response = restTemplate.postForEntity(
                url,
                requestEntity,
                AiPredictionResponse.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }

        throw new RuntimeException("AI room service error: " + response.getStatusCode());
    }
}
