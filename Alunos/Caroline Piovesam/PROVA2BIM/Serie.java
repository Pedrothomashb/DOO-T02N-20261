package com.caroline.prova2bim.series;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Serie {

    public static final String Concluida = "Ended";
    public static final String Transmitindo = "Running";
    public static final String Indefinida = "To Be Determined";

    @JsonProperty("name")
    private String nome;

    @JsonProperty("language")
    private String idioma;

    @JsonProperty("genres")
    private List<String> generos;

    @JsonProperty("status")
    private String estado;

    @JsonProperty("premiered")
    private String dataEstreia;

    @JsonProperty("ended")
    private String dataTermino;

    @JsonProperty("network")
    private Emissora emissora;

    @JsonProperty("rating")
    private Avaliacao avaliacao;

    @JsonProperty("image")
    private Imagem imagem;

    public Serie() {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Emissora {

        @JsonProperty("name")
        private String nome;

        public Emissora() {
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public List<String> getGeneros() {
        return generos;
    }

    public void setGeneros(List<String> generos) {
        this.generos = generos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDataEstreia() {
        return dataEstreia;
    }

    public void setDataEstreia(String dataEstreia) {
        this.dataEstreia = dataEstreia;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(String dataTermino) {
        this.dataTermino = dataTermino;
    }

    public Emissora getEmissora() {
        return emissora;
    }

    public void setEmissora(Emissora emissora) {
        this.emissora = emissora;
    }

    public Imagem getImagem() {
        return imagem;
    }

    public void setImagem(Imagem imagem) {
        this.imagem = imagem;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Avaliacao {
        @JsonProperty("average")
        private Double media;

        public Avaliacao() {
        }

        public Double getMedia() {
            return media;
        }

        public void setMedia(Double media) {
            this.media = media;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Imagem {
        @JsonProperty("medium")
        private String urlMedia;

        public Imagem() {
        }

        public String getUrlMedia() {
            return urlMedia;
        }

        public void setUrlMedia(String urlMedia) {
            this.urlMedia = urlMedia;
        }
    }
    // pra mesma série não entrar duas vezes (HashSet)

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Serie serie = (Serie) o;
        return Objects.equals(nome, serie.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }
}