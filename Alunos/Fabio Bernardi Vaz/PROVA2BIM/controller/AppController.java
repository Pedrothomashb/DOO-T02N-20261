package controller;

import java.util.List;

import model.ListaUsuario;
import model.Serie;
import model.Usuario;
import model.enums.TipoLista;
import service.PersistenciaService;
import service.TVMazeService;

public class AppController {

    private Usuario usuario;
    private final PersistenciaService persistencia;
    private final TVMazeService tvMazeService;

    public AppController() {
        this.persistencia   = new PersistenciaService();
        this.tvMazeService  = new TVMazeService();
    }

    public boolean inicializar() {
        usuario = persistencia.carregar();
        return usuario != null;
    }

    public void configurarNovoUsuario(String nome) {
        usuario = persistencia.criarDadosIniciais(nome);
    }

    public void atualizarNomeUsuario(String nome) {
        usuario.setNome(nome);
        salvar();
    }

    //bate na api
    public List<Serie> buscarSeries(String termo) throws Exception {
        return tvMazeService.buscarPorNome(termo);
    }

    //listas, Ordenando, adicionando etc...

    public boolean adicionarNaLista(Serie serie, TipoLista tipo) {
        boolean resultado = getLista(tipo).adicionar(serie);
        if (resultado) salvar();
        return resultado;
    }

    public boolean removerDaLista(Serie serie, TipoLista tipo) {
        boolean resultado = getLista(tipo).remover(serie);
        if (resultado) salvar();
        return resultado;
    }

    public boolean estaEmLista(Serie serie, TipoLista tipo) {
        return getLista(tipo).contem(serie);
    }

    public List<Serie> getSeriesOrdenadas(TipoLista tipo, String criterio) {
        ListaUsuario lista = getLista(tipo);
        return switch (criterio) {
            case "Nome"         -> lista.ordenarPorNome();
            case "Nota"         -> lista.ordenarPorNota();
            case "Status"       -> lista.ordenarPorStatus();
            case "Data Estreia" -> lista.ordenarPorDataEstreia();
            default             -> lista.getSeries();
        };
    }

    private ListaUsuario getLista(TipoLista tipo) {
        return switch (tipo) {
            case FAVORITOS     -> usuario.getFavoritos();
            case ASSISTIDAS    -> usuario.getAssistidas();
            case QUERO_ASSISTIR -> usuario.getQuerAssistir();
        };
    }

    //salvando usuario
    public void salvar() {
        persistencia.salvar(usuario);
    }

    //getters

    public Usuario getUsuario() { 
        return usuario; 
    }

    public String getNomeUsuario() {
        return usuario != null ? usuario.getNome() : "";
    }
}
