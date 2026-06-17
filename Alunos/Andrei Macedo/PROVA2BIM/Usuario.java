package com.example.model;

import java.util.HashSet;
import java.util.Set;

public class Usuario {
    private String nome;
    private Set<Serie> favoritas = new  HashSet<>();
    private Set<Serie> assistidas = new  HashSet<>();
    private Set<Serie> desejadas = new  HashSet<>();

    public Usuario() {
    }

    public Usuario(String nome) {
        this.nome = nome;
    }

    public Set<Serie> getDesejadas() {
        return desejadas;
    }

    public void setDesejadas(Set<Serie> desejadas) {
        this.desejadas = desejadas;
    }

    public Set<Serie> getAssistidas() {
        return assistidas;
    }

    public void setAssistidas(Set<Serie> assistidas) {
        this.assistidas = assistidas;
    }

    public Set<Serie> getFavoritas() {
        return favoritas;
    }

    public void setFavoritas(Set<Serie> favoritas) {
        this.favoritas = favoritas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
