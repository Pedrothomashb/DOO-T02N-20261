package com.ImdbTLM.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/** Classe que configura o perfil local dos usuários cadastrados
*
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario {

    private String nome;
    private List<Serie> favoritos;
    private List<Serie> jaAssistidas;
    private List<Serie> desejaAssistir;

    public Usuario() {
        this.favoritos = new ArrayList<>();
        this.jaAssistidas = new ArrayList<>();
        this.desejaAssistir = new ArrayList<>();
    }

    public Usuario(String nome) {
        this();
        this.nome = nome;
    }

    // Método que verifica a existencia da serie em alguma lista
    public boolean serieJaAdicionada(Serie serie) {
        return favoritos.contains(serie)
                || jaAssistidas.contains(serie)
                || desejaAssistir.contains(serie);
    }

    // Remove a serie selecionada das listas
    public void removerDeTodas(Serie serie) {
        favoritos.remove(serie);
        jaAssistidas.remove(serie);
        desejaAssistir.remove(serie);
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    // Métodos setters para direcionar a série selecionada para a lista escolhida
    public List<Serie> getFavoritos() { return favoritos; }
    public void setFavoritos(List<Serie> favoritos) {
        this.favoritos = favoritos != null ? favoritos : new ArrayList<>();
    }

    public List<Serie> getJaAssistidas() { return jaAssistidas; }
    public void setJaAssistidas(List<Serie> jaAssistidas) {
        this.jaAssistidas = jaAssistidas != null ? jaAssistidas : new ArrayList<>();
    }

    public List<Serie> getDesejaAssistir() { return desejaAssistir; }
    public void setDesejaAssistir(List<Serie> desejaAssistir) {
        this.desejaAssistir = desejaAssistir != null ? desejaAssistir : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Usuario{nome='" + nome + '\''
                + ", favoritos=" + favoritos.size()
                + ", jaAssistidas=" + jaAssistidas.size()
                + ", desejaAssistir=" + desejaAssistir.size() + '}';
    }
}
