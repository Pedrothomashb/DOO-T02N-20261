package com.example.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Serie {
    private int id;
    private String name;
    private String language;
    private List<String> genres;
    private String status;
    private String premiered;
    private String ended;
    private Rating rating;
    private Network network;
    private Network webChannel;
    private ImagemLinks image;

    public Serie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPremiered() {
        return premiered;
    }

    public void setPremiered(String premiered) {
        this.premiered = premiered;
    }

    public String getEnded() {
        return ended;
    }

    public void setEnded(String ended) {
        this.ended = ended;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public Network getWebChannel() {
        return webChannel;
    }

    public void setWebChannel(Network webChannel) {
        this.webChannel = webChannel;
    }

    public ImagemLinks getImage() {
        return image;
    }

    public void setImage(ImagemLinks image) {
        this.image = image;
    }

    public String getNomeEmissora() {
        if (this.network != null && this.network.getName() != null) {
            return this.network.getName();
        }
        if (this.webChannel != null && this.webChannel.getName() != null) {
            return this.webChannel.getName();
        }
        return "Desconhecida";
    }

    public String getNotaFormatada() {
        if (this.rating != null) {
            return String.valueOf(this.rating.getAverage());
        }
        return "N/A";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Serie serie = (Serie) o;
        return id == serie.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
