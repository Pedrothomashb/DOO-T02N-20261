package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import model.enums.TipoLista;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario {

    private String nome;
    private ListaUsuario favoritos;
    private ListaUsuario assistidas;
    private ListaUsuario querAssistir;

    public Usuario() {
        this.favoritos    = new ListaUsuario(TipoLista.FAVORITOS);
        this.assistidas   = new ListaUsuario(TipoLista.ASSISTIDAS);
        this.querAssistir = new ListaUsuario(TipoLista.QUERO_ASSISTIR);
    }

    public Usuario(String nome) {
        this.nome         = nome;
        this.favoritos    = new ListaUsuario(TipoLista.FAVORITOS);
        this.assistidas   = new ListaUsuario(TipoLista.ASSISTIDAS);
        this.querAssistir = new ListaUsuario(TipoLista.QUERO_ASSISTIR);
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public ListaUsuario getFavoritos() { return favoritos; }
    public void setFavoritos(ListaUsuario favoritos) { this.favoritos = favoritos; }

    public ListaUsuario getAssistidas() { return assistidas; }
    public void setAssistidas(ListaUsuario assistidas) { this.assistidas = assistidas; }

    public ListaUsuario getQuerAssistir() { return querAssistir; }
    public void setQuerAssistir(ListaUsuario querAssistir) { this.querAssistir = querAssistir; }
}
