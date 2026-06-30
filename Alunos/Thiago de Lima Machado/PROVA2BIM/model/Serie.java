package com.ImdbTLM.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

/**
 * Classe que configura as séries da API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Serie {

    private int id;
    private String name;
    private String language;
    private List<String> genres;
    private String status;
    private String premiered;
    private String ended;

    // Nota da série
    private Double ratingAverage;

    // Emissora que lançou a série
    private String networkName;

    public Serie() {}

    public Serie(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Metodo que desserializa o JSON da nota
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rating {
        @JsonProperty("average")
        private Double average;

        public Double getAverage() { return average; }
        public void setAverage(Double average) { this.average = average; }
    }

    // Metodo que desserializa o JSON da emissora
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Network {
        @JsonProperty("name")
        private String name;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @JsonProperty("rating")
    public void setRatingFromApi(Rating rating) {
        if (rating != null) {
            this.ratingAverage = rating.getAverage();
        }
    }

    @JsonProperty("network")
    public void setNetworkFromApi(Network network) {
        if (network != null && network.getName() != null) {
            this.networkName = network.getName();
        }
    }

    @JsonProperty("webChannel")
    public void setWebChannelFromApi(Network webChannel) {
        if (webChannel != null && webChannel.getName() != null && this.networkName == null) {
            this.networkName = webChannel.getName();
        }
    }

    // Getters e Setters da API
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPremiered() { return premiered; }
    public void setPremiered(String premiered) { this.premiered = premiered; }

    public String getEnded() { return ended; }
    public void setEnded(String ended) { this.ended = ended; }

    public Double getRatingAverage() { return ratingAverage; }
    public void setRatingAverage(Double ratingAverage) { this.ratingAverage = ratingAverage; }

    public String getNetworkName() { return networkName; }
    public void setNetworkName(String networkName) { this.networkName = networkName; }

    public String getGenresFormatted() {
        if (genres == null || genres.isEmpty()) return "N/A";
        return String.join(", ", genres);
    }

    // Retorna a nota da série formatada
    public String getRatingFormatted() {
        if (ratingAverage == null) return "N/A";
        return String.format("%.1f", ratingAverage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Serie)) return false;
        Serie serie = (Serie) o;
        return id == serie.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name != null ? name : "Série #" + id;
    }
}
