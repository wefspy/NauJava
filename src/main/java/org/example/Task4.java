package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class Task4 {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/user-agent"))
                .build();

        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        response.thenAccept(resp -> {
            try {
                UserAgent userAgent = mapper.readValue(resp.body(), UserAgent.class);
                System.out.println("Ваш UserAgent: " + userAgent.value());
            } catch (Exception e) {
                System.out.println("Ошибка обработки JSON: " + e.getMessage());
            }
        }).exceptionally(e -> {
            System.out.println("Ошибка запроса: " + e.getMessage());
            return null;
        }).join();
    }

    public record UserAgent(@JsonProperty("user-agent") String value) {
    }
}
