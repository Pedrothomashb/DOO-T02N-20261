package service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Serie;
import model.enums.StatusSerie;

public class TVMazeService {

    private static final String BASE_URL = "https://api.tvmaze.com";
    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public TVMazeService() {
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public List<Serie> buscarPorNome(String nome) throws Exception {
        String encoded = URLEncoder.encode(nome, StandardCharsets.UTF_8);
        String url = BASE_URL + "/search/shows?q=" + encoded;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Erro na API TVMaze. Código: " + response.statusCode());
        }

        JsonNode root = mapper.readTree(response.body());
        List<Serie> resultados = new ArrayList<>();

        for (JsonNode item : root) {
            JsonNode showNode = item.get("show");
            if (showNode != null) {
                resultados.add(parseSerie(showNode));
            }
        }

        return resultados;
    }

    private Serie parseSerie(JsonNode node) {
        int id = node.path("id").asInt(0);
        String nome = node.path("name").asText("Sem título");
        String idioma = node.path("language").asText("Não informado");
        String statusStr = node.path("status").asText("");
        StatusSerie status = StatusSerie.fromString(statusStr);

        String dataEstreia = node.path("premiered").asText("");
        String dataTermino = node.path("ended").asText("");

        double nota = 0.0;
        JsonNode ratingNode = node.path("rating");
        if (!ratingNode.isMissingNode() && !ratingNode.path("average").isNull()) {
            nota = ratingNode.path("average").asDouble(0.0);
        }

        List<String> generos = new ArrayList<>();
        JsonNode generosNode = node.path("genres");
        if (generosNode.isArray()) {
            for (JsonNode g : generosNode) {
                generos.add(g.asText());
            }
        }

        String emissora = "Não informada";
        JsonNode networkNode = node.path("network");
        if (!networkNode.isMissingNode() && !networkNode.isNull()) {
            emissora = networkNode.path("name").asText("Não informada");
        } else {
            JsonNode webNode = node.path("webChannel");
            if (!webNode.isMissingNode() && !webNode.isNull()) {
                emissora = webNode.path("name").asText("Não informada");
            }
        }

        String imagemUrl = "";
        JsonNode imageNode = node.path("image");
        if (!imageNode.isMissingNode() && !imageNode.isNull()) {
            imagemUrl = imageNode.path("medium").asText("");
        }

        //remove tags HTML básicas
        String resumo = node.path("summary").asText("Sem resumo disponível.");
        resumo = resumo.replaceAll("<[^>]*>", "").trim();

        return new Serie(id, nome, idioma, generos, nota, status,
                dataEstreia, dataTermino, emissora, imagemUrl, resumo);
    }
}
