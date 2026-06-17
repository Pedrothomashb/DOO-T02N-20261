package com.caroline.prova2bim.series;

import java.util.HashSet;
import java.util.Set;

public class ContaUsuario {

    private String apelido;
    private Set<Serie> favoritas;
    private Set<Serie> jaAssistidas;
    private Set<Serie> desejaAssistir;

    public ContaUsuario(String apelido) {
        this.apelido = apelido;
        this.favoritas = new HashSet<>();
        this.jaAssistidas = new HashSet<>();
        this.desejaAssistir = new HashSet<>();
    }

    public ContaUsuario() { // para o Jackson conseguir ler o arquivo JSON depois de fechado
        // e aberto novamente
        this.favoritas = new HashSet<>();
        this.jaAssistidas = new HashSet<>();
        this.desejaAssistir = new HashSet<>();

    }

    public void adicionarFavorita(Serie serie) {
        this.favoritas.add(serie);
    }

    public void removerFavorita(Serie serie) {
        this.favoritas.remove(serie);
    }

    public void adicionarJaAssistida(Serie serie) {
        this.jaAssistidas.add(serie);
    }

    public void removerJaAssistida(Serie serie) {
        this.jaAssistidas.remove(serie);
    }

    public void adicionarDesejaAssistir(Serie serie) {
        this.desejaAssistir.add(serie);
    }

    public void removerDesejaAssistir(Serie serie) {
        this.desejaAssistir.remove(serie);
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public Set<Serie> getFavoritas() {
        return favoritas;
    }

    public void setFavoritas(Set<Serie> favoritas) {
        this.favoritas = favoritas;
    }

    public Set<Serie> getJaAssistidas() {
        return jaAssistidas;
    }

    public void setJaAssistidas(Set<Serie> jaAssistidas) {
        this.jaAssistidas = jaAssistidas;
    }

    public Set<Serie> getDesejaAssistir() {
        return desejaAssistir;
    }

    public void setDesejaAssistir(Set<Serie> desejaAssistir) {
        this.desejaAssistir = desejaAssistir;
    }

}