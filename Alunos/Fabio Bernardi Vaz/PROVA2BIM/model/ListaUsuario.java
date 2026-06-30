package com.tvtracker.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tvtracker.model.enums.TipoLista;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListaUsuario {

    private TipoLista tipo;
    private List<Serie> series;

    public ListaUsuario() {
        this.series = new ArrayList<>();
    }

    public ListaUsuario(TipoLista tipo) {
        this.tipo = tipo;
        this.series = new ArrayList<>();
    }

    public boolean adicionar(Serie serie) {
        if (contem(serie)) return false;
        series.add(serie);
        return true;
    }

    public boolean remover(Serie serie) {
        return series.removeIf(s -> s.getId() == serie.getId());
    }

    public boolean contem(Serie serie) {
        return series.stream().anyMatch(s -> s.getId() == serie.getId());
    }

    public List<Serie> ordenarPorNome() {
        List<Serie> ordenada = new ArrayList<>(series);
        ordenada.sort(Comparator.comparing(s -> s.getNome().toLowerCase()));
        return ordenada;
    }

    public List<Serie> ordenarPorNota() {
        List<Serie> ordenada = new ArrayList<>(series);
        ordenada.sort(Comparator.comparingDouble(Serie::getNota).reversed());
        return ordenada;
    }

    public List<Serie> ordenarPorStatus() {
        List<Serie> ordenada = new ArrayList<>(series);
        ordenada.sort(Comparator.comparing(s -> s.getStatus().getDescricao()));
        return ordenada;
    }

    public List<Serie> ordenarPorDataEstreia() {
        List<Serie> ordenada = new ArrayList<>(series);
        ordenada.sort(Comparator.comparing(s -> {
            String d = s.getDataEstreia();
            return (d == null || d.isBlank()) ? "" : d;
        }));
        return ordenada;
    }

    public TipoLista getTipo() { 
        return tipo; 
    }
    
    public void setTipo(TipoLista tipo) { 
        this.tipo = tipo; 
    }

    public List<Serie> getSeries() { 
        return series; 
    }

    public void setSeries(List<Serie> series) { 
        this.series = series; 
    }
    
    public int getTamanho() { 
        return series.size(); 
    }
}
