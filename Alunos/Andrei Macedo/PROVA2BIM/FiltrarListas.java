package com.example.service;
import com.example.model.Serie;
import java.util.List;

public class FiltrarListas {

    public List<Serie> ordenarPorNome(List<Serie> series) {
        return series.stream()
                .sorted((s1, s2) -> {
                    if (s1.getName() == null) return 1;
                    if (s2.getName() == null) return -1;
                    return s1.getName().compareToIgnoreCase(s2.getName());
                })
                .toList();
    }

    public List<Serie> ordenarPorNota(List<Serie> series) {
        return series.stream()
                .sorted((s1, s2) -> Double.compare(s2.getRating().getAverage(), s1.getRating().getAverage()))
                .toList();
    }

    public List<Serie> odenarPorDataEstado(List<Serie> series) {
        return series.stream()
                .sorted((s1, s2) -> {
                    String statu1 = s1.getStatus() != null ? s1.getStatus() : "";
                    String statu2 = s2.getStatus() != null ? s2.getStatus() : "";
                    return statu1.compareToIgnoreCase(statu2);
                })
                .toList();
    }

    public List<Serie> ordenarPorDataEstreia(List<Serie> series) {
        return series.stream()
                .sorted((s1, s2) -> {
                    String data1 = s1.getPremiered() != null ? s1.getPremiered() : "9999-99-99";
                    String data2 = s2.getPremiered() != null ? s2.getPremiered() : "9999-99-99";
                    return data2.compareTo(data1);
                })
                .toList();
    }
}
