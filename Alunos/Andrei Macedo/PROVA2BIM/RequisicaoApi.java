package com.example.service;
import com.example.exception.SeriesException;
import com.example.model.Serie;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class RequisicaoApi {

    private static final String URL = "https://api.tvmaze.com/search/shows?q=";
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public RequisicaoApi() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<Serie> buscarSerie (String termoDeBusca) throws SeriesException {

        String serieDecodificada = URLEncoder.encode(termoDeBusca, StandardCharsets.UTF_8);
        String urlFinal = URL + serieDecodificada;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlFinal))
                .GET()
                .header("accept", "application/json")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (ConnectException e) {
            e.printStackTrace();
            throw new SeriesException(
                    "Sem conexão com a internet",
                    SeriesException.TipoErro.ERRO_CONEXAO_API
            );
        } catch (java.net.http.HttpConnectTimeoutException e) {
            e.printStackTrace();
            throw new SeriesException(
                    "Tempo limite de solicitação excedido. ",
                    SeriesException.TipoErro.TEMPO_EXCEDIDO
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new SeriesException(
                    "Falha na transmissão de dados com a API:" + e.getMessage(),
                    SeriesException.TipoErro.ERRO_CONEXAO_API
            );
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            throw new SeriesException(
                    "A requisição foi interrompida inesperadamente.",
                    SeriesException.TipoErro.ERRO_CONEXAO_API
            );
        }

        if (response.statusCode() == 404) {
            throw new SeriesException(
                    "Série não encontrada",
                    SeriesException.TipoErro.SERIE_NAO_ENCONTRADA
            );
        }

        if (response.statusCode() != 200) {
            throw new SeriesException(
                    "Erro no servidor do TVMaze. Status: " + response.statusCode(),
                    SeriesException.TipoErro.ERRO_CONEXAO_API
            );
        }

        try {
            JsonNode raiz = objectMapper.readTree(response.body());
            List<Serie> resultado = new ArrayList<>();

            for(JsonNode item: raiz) {
                JsonNode noShow = item.get("show");
                if (noShow != null) {
                    Serie serie = objectMapper.treeToValue(noShow, Serie.class);
                    resultado.add(serie);
                }
            }

            if (resultado.isEmpty()) {
                throw new SeriesException("Série não encontrada", 
                SeriesException.TipoErro.SERIE_NAO_ENCONTRADA);
            }

            return resultado;

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            e.printStackTrace();
            e.printStackTrace();
            throw new SeriesException(
                    "Erro ao converter dados JSON.",
                    SeriesException.TipoErro.FALHA_GRAVACAO
            );
        }
    }
}
