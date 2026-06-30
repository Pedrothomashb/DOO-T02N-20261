package service;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Serie;
import model.Usuario;
import model.enums.StatusSerie;

public class PersistenciaService {

    private static final String CAMINHO_ARQUIVO = "dados.json";
    private final ObjectMapper mapper;

    public PersistenciaService() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void salvar(Usuario usuario) {
        try {
            mapper.writeValue(new File(CAMINHO_ARQUIVO), usuario);
        } catch (Exception e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    public Usuario carregar() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) {
            return null;
        }
        try {
            return mapper.readValue(arquivo, Usuario.class);
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            return null;
        }
    }

    public Usuario criarDadosIniciais(String nomeUsuario) {
        Usuario usuario = new Usuario(nomeUsuario);

        //pré-setando séries
        Serie breakingBad = new Serie(169, "Breaking Bad", "English",
                List.of("Drama", "Crime", "Thriller"), 9.2, StatusSerie.ENDED,
                "2008-01-20", "2013-09-29", "AMC",
                "", "Walter White, a chemistry teacher diagnosed with terminal cancer, becomes a methamphetamine producer.");

        Serie gameOfThrones = new Serie(82, "Game of Thrones", "English",
                List.of("Drama", "Adventure", "Fantasy"), 9.3, StatusSerie.ENDED,
                "2011-04-17", "2019-05-19", "HBO",
                "", "Noble families fight for control of the Iron Throne of Westeros.");

        Serie theOffice = new Serie(526, "The Office", "English",
                List.of("Comedy"), 8.8, StatusSerie.ENDED,
                "2005-03-24", "2013-05-16", "NBC",
                "", "A mockumentary about the employees of a paper company office in Scranton, Pennsylvania.");

        Serie strangerthings = new Serie(305, "Stranger Things", "English",
                List.of("Drama", "Fantasy", "Horror"), 8.8, StatusSerie.ENDED,
                "2016-07-15", "2025-11-21", "Netflix",
                "", "Children discover supernatural forces and secret government experiments in their town.");

        Serie theWitcher = new Serie(40008, "The Witcher", "English",
                List.of("Drama", "Fantasy", "Action"), 8.0, StatusSerie.RUNNING,
                "2019-12-20", "", "Netflix",
                "", "Geralt of Rivia, a monster hunter, navigates a dangerous world filled with magic.");

        Serie darkSerie = new Serie(17861, "Dark", "German",
                List.of("Drama", "Thriller", "Mystery"), 8.8, StatusSerie.ENDED,
                "2017-12-01", "2020-06-27", "Netflix",
                "", "A time-travel conspiracy connects four families in a small German town.");

        Serie chernobyl = new Serie(37136, "Chernobyl", "English",
                List.of("Drama", "History", "Thriller"), 9.4, StatusSerie.ENDED,
                "2019-05-06", "2019-06-03", "HBO",
                "", "Miniseries about the 1986 Chernobyl nuclear disaster and its aftermath.");

        Serie theLastOfUs = new Serie(63639, "The Last of Us", "English",
                List.of("Drama", "Action", "Horror"), 8.9, StatusSerie.RUNNING,
                "2023-01-15", "", "HBO",
                "", "In a post-apocalyptic world, Joel must smuggle Ellie across a devastated country.");

        Serie blackMirror = new Serie(305, "Black Mirror", "English",
                List.of("Drama", "Science-Fiction", "Thriller"), 8.3, StatusSerie.RUNNING,
                "2011-12-04", "", "Netflix",
                "", "An anthology exploring the dark side of technology in near and parallel futures.");

        Serie fleabag = new Serie(31801, "Fleabag", "English",
                List.of("Comedy", "Drama"), 8.7, StatusSerie.ENDED,
                "2016-07-21", "2019-04-08", "BBC Three",
                "", "A woman tries to navigate modern life in London in a totally irreverent way.");

        usuario.getFavoritos().adicionar(breakingBad);
        usuario.getFavoritos().adicionar(gameOfThrones);
        usuario.getFavoritos().adicionar(chernobyl);

        usuario.getAssistidas().adicionar(breakingBad);
        usuario.getAssistidas().adicionar(theOffice);
        usuario.getAssistidas().adicionar(darkSerie);
        usuario.getAssistidas().adicionar(chernobyl);
        usuario.getAssistidas().adicionar(fleabag);

        usuario.getQuerAssistir().adicionar(strangerthings);
        usuario.getQuerAssistir().adicionar(theWitcher);
        usuario.getQuerAssistir().adicionar(theLastOfUs);
        usuario.getQuerAssistir().adicionar(blackMirror);
        usuario.getQuerAssistir().adicionar(gameOfThrones);

        salvar(usuario);
        return usuario;
    }
}
