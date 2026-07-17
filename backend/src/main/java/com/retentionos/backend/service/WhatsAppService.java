package com.retentionos.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WhatsAppService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${gupshup.api.key}")
    private String apiKey;

    @Value("${gupshup.sender.number}")
    private String senderNumber;

    public WhatsAppService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.gupshup.io")
                .build();
        this.objectMapper = objectMapper;
    }

    public boolean sendTextMessage(String destinationNumber, String message) {
        try {
            String messagePayload = objectMapper.writeValueAsString(new TextMessage("text", message));

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("channel", "whatsapp");
            formData.add("source", senderNumber);
            formData.add("destination", destinationNumber);
            formData.add("message", messagePayload);

            webClient.post()
                    .uri("/wa/api/v1/msg")
                    .header("apikey", apiKey)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            return true;
        } catch (WebClientResponseException e) {
            log.error("Gupshup API call failed with status {}: {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            return false;
        } catch (Exception e) {
            log.error("Failed to send WhatsApp message via Gupshup", e);
            return false;
        }
    }

    private record TextMessage(String type, String text) {
    }
}
