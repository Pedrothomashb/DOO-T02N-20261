import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pela integração direta com a API do TVMaze.
 * Realiza requisições HTTP para buscar séries com base em um termo de pesquisa.
 */
public class BuscaSerie {

    private static final String API_URL = "https://api.tvmaze.com/search/shows?q=";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    /**
     * Inicializa o cliente HTTP e o mapeador de JSON.
     */
    public BuscaSerie() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Busca séries na API externa com base em um texto.
     * 
     * @param termoBusca Texto para pesquisa.
     * @return Lista de séries mapeadas.
     * @throws IOException Erro de rede ou parsing.
     * @throws InterruptedException Requisição interrompida.
     */
    public List<Serie> pesquisarSeries(String termoBusca) throws IOException, InterruptedException {
        List<Serie> seriesEncontradas = new ArrayList<>();

        if (termoBusca == null || termoBusca.strip().isEmpty()) {
            return seriesEncontradas;
        }

        String termoCodificado = URLEncoder.encode(termoBusca.trim(), StandardCharsets.UTF_8);
        String urlCompleta = API_URL + termoCodificado;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlCompleta))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonNode rootNode = objectMapper.readTree(response.body());
            
            for (JsonNode node : rootNode) {
                JsonNode showNode = node.get("show");
                if (showNode != null) {
                    seriesEncontradas.add(mapearJsonParaSerie(showNode));
                }
            }
        }

        return seriesEncontradas;
    }

    /**
     * Mapeia os dados do nó JSON para o objeto Serie aplicando os termos exatos do edital.
     */
    private Serie mapearJsonParaSerie(JsonNode showNode) {
        Serie serie = new Serie();
        
        serie.setId(showNode.path("id").asInt());
        serie.setNome(showNode.path("name").asText("Sem Nome"));
        serie.setIdioma(showNode.path("language").asText("Desconhecido"));
        serie.setDataEstreia(showNode.path("premiered").asText(null));
        serie.setDataTermino(showNode.path("ended").asText(null));
        
        String statusApi = showNode.path("status").asText("Desconhecido");

        if (statusApi.equalsIgnoreCase("Running")) {
            serie.setEstado("Ainda transmitindo");
        } else if (statusApi.equalsIgnoreCase("Ended")) {
            serie.setEstado("Já concluída");
        } else if (statusApi.equalsIgnoreCase("Canceled")) {
            serie.setEstado("Cancelada");
        } else if (statusApi.equalsIgnoreCase("To Be Determined")) {
            serie.setEstado("A Ser Determinado");
        } else {
            serie.setEstado(statusApi);
        }

        JsonNode ratingNode = showNode.path("rating");
        serie.setNotaGeral(ratingNode.path("average").asDouble(0.0));

        String emissora = "Desconhecida";
        if (showNode.hasNonNull("network")) {
            emissora = showNode.path("network").path("name").asText();
        } else if (showNode.hasNonNull("webChannel")) {
            emissora = showNode.path("webChannel").path("name").asText();
        }
        serie.setEmissora(emissora);

        List<String> generos = new ArrayList<>();
        JsonNode genresNode = showNode.path("genres");
        if (genresNode.isArray()) {
            for (JsonNode generoNode : genresNode) {
                generos.add(generoNode.asText());
            }
        }
        serie.setGeneros(generos);
        
        return serie;
    }
}
