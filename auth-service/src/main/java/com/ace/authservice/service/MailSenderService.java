package com.ace.authservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class MailSenderService {

    private final RestTemplate rest = new RestTemplate();

    public void sendVerificationEmail(String to, String link) {
        String url = "http://localhost:5678/webhook/email-verify";

        Map<String, String> body = new HashMap<>();
        body.put("to", to);
        body.put("link", link);

        try {
            rest.postForObject(url, body, String.class);
        } catch (Exception e) {
            System.out.println("Impossible d'envoyer le mail : " + e.getMessage());
        }
    }
}


