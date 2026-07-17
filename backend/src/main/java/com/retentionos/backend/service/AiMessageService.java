package com.retentionos.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.retentionos.backend.entity.BusinessType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AiMessageService {

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    public AiMessageService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    public String generateRetentionMessage(String customerName, String businessName, BusinessType businessType, long daysInactive) {
        GeminiRequest requestBody = new GeminiRequest(List.of(
                new GeminiRequest.Content(List.of(
                        new GeminiRequest.Part(buildPrompt(customerName, businessName, businessType, daysInactive))
                ))
        ));

        try {
            GeminiResponse response = webClient.post()
                    .uri("/v1beta/models/gemini-flash-latest:generateContent")
                    .header("x-goog-api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(GeminiResponse.class)
                    .block();

            String text = extractText(response);
            return (text == null || text.isBlank()) ? null : text.trim();
        } catch (WebClientResponseException e) {
            log.error("Gemini API call failed with status {}: {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            return null;
        } catch (Exception e) {
            log.error("Gemini API call failed", e);
            return null;
        }
    }

    private String buildPrompt(String customerName, String businessName, BusinessType businessType, long daysInactive) {
        return "Write a short, warm WhatsApp message for a customer named " + customerName
                + " who has not visited " + businessName + " (a " + businessType.name().toLowerCase()
                + " business) in " + daysInactive + " days. "
                + "Write it in a natural mix of Hindi and English (Hinglish), the way small business "
                + "owners in India message their regular customers. Keep it friendly, concise (2-3 sentences), "
                + "and encourage them to visit again. Address them by name and return only the message text, "
                + "with no greeting placeholders or quotation marks.";
    }

    private String extractText(GeminiResponse response) {
        if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
            return null;
        }
        GeminiResponse.Content content = response.candidates().get(0).content();
        if (content == null || content.parts() == null || content.parts().isEmpty()) {
            return null;
        }
        return content.parts().get(0).text();
    }

    private record GeminiRequest(List<Content> contents) {
        private record Content(List<Part> parts) {
        }

        private record Part(String text) {
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record GeminiResponse(List<Candidate> candidates) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        private record Candidate(Content content) {
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        private record Content(List<Part> parts) {
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        private record Part(String text) {
        }
    }
}
