package com.ImdbTLM.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ImdbTLM.model.Serie;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

// Service que realiza o link com a api da TvMaze (fazendo as requisições e retornos)
public class APIdeIMDB {
    private static final String BASE_URL = "https://api.tvmaze.com";
    private static final int TIMEOUT_SEGUNDOS = 15;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public APIdeIMDB() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SEGUNDOS))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    // Buscader de serie na API
    public List<Serie> buscarPorNome(String nomeSerie) throws IOException, InterruptedException {
        if (nomeSerie == null || nomeSerie.isBlank()) {
            throw new IllegalArgumentException("O nome da série não pode ser vazio.");
        }

        String query = URLEncoder.encode(nomeSerie.trim(), StandardCharsets.UTF_8);
        String url = BASE_URL + "/search/shows?q=" + query;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(TIMEOUT_SEGUNDOS))
                .header("Accept", "application/json")
                .header("User-Agent", "TVTracker-App/1.0")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Retorno de Erro na API
        int statusCode = response.statusCode();
        if (statusCode == 429) {
            throw new IOException("Taxa de requisições excedida.");
        }
        if (statusCode != 200) {
            throw new IOException("API TVMaze retornou status inesperado:" + statusCode);
        }
        return parsearResultadoBusca(response.body());
    }

    // Deserializa o JSON de resposta da busca.
    private List<Serie> parsearResultadoBusca(String jsonBody) throws IOException {
        List<Serie> series = new ArrayList<>();
        JsonNode raiz = objectMapper.readTree(jsonBody);

        if (!raiz.isArray()) {
            throw new IOException("Resposta inesperada da API: esperava um JSON.");
        }

        for (JsonNode item : raiz) {
            JsonNode showNode = item.get("show");
            if (showNode != null && !showNode.isNull()) {
                try {
                    Serie serie = objectMapper.treeToValue(showNode, Serie.class);
                    series.add(serie);
                } catch (Exception e) {
                    // Ignora séries malformadas individualmente, não aborta a lista inteira
                    System.err.println("Falha ao parsear série individual: " + e.getMessage());
                }
            }
        }

        return series;
    }
}
