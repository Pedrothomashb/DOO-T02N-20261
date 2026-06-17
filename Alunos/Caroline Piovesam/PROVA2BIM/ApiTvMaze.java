package com.caroline.prova2bim.api;

import com.fasterxml.jackson.core.type.TypeReference; // pra evitar o apagamento de tipos da list
import com.fasterxml.jackson.databind.ObjectMapper;
import com.caroline.prova2bim.erros.FalhasSistemaExceptions;
import com.caroline.prova2bim.series.Serie;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ApiTvMaze {

    private final HttpClient clienteHttp;
    private final ObjectMapper mapeadorJson;

    public ApiTvMaze() {
        this.clienteHttp = HttpClient.newHttpClient();
        this.mapeadorJson = new ObjectMapper();
    }

    public List<Serie> buscarSeries(String nomeDigitado) throws FalhasSistemaExceptions {
        try {
            String nomeFormatado = URLEncoder.encode(nomeDigitado, StandardCharsets.UTF_8); // pra ç e acentos
            String enderecoApi = "https://api.tvmaze.com/search/shows?q=" + nomeFormatado;

            HttpRequest requisicao = HttpRequest.newBuilder()
                    .uri(URI.create(enderecoApi))
                    .GET()
                    .build();

            HttpResponse<String> respostaHttp = clienteHttp.send(requisicao,
                    HttpResponse.BodyHandlers.ofString());

            if (respostaHttp.statusCode() != 200) {
                throw new FalhasSistemaExceptions("A API retornou um erro: " + respostaHttp.statusCode());
            }

            List<RespostaTvMaze> listaRespostas = mapeadorJson.readValue( // desserialização
                    respostaHttp.body(),
                    new TypeReference<List<RespostaTvMaze>>() {
                    });

            List<Serie> seriesEncontradas = new ArrayList<>();
            for (RespostaTvMaze resposta : listaRespostas) {
                if (resposta.getSerie() != null) {
                    seriesEncontradas.add(resposta.getSerie());
                }
            }
            return seriesEncontradas;

        } catch (Exception erro) {
            throw new FalhasSistemaExceptions("Não foi possível processar os dados!", erro);
        }
    }

    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class RespostaTvMaze {

        @com.fasterxml.jackson.annotation.JsonProperty("show")
        private Serie serie;

        public RespostaTvMaze() {
        }

        public Serie getSerie() {
            return serie;
        }

        public void setSerie(Serie serie) {
            this.serie = serie;
        }
    }
}
