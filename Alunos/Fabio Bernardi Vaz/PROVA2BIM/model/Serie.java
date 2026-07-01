package model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import model.enums.StatusSerie;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Serie {

    private int id;
    private String nome;
    private String idioma;
    private List<String> generos;
    private double nota;
    private StatusSerie status;
    private String dataEstreia;
    private String dataTermino;
    private String emissora;
    private String imagemUrl;
    private String resumo;

    public Serie() {}

    public Serie(int id, String nome, String idioma, List<String> generos,
                 double nota, StatusSerie status, String dataEstreia,
                 String dataTermino, String emissora, String imagemUrl, String resumo) {
        this.id = id;
        this.nome = nome;
        this.idioma = idioma;
        this.generos = generos;
        this.nota = nota;
        this.status = status;
        this.dataEstreia = dataEstreia;
        this.dataTermino = dataTermino;
        this.emissora = emissora;
        this.imagemUrl = imagemUrl;
        this.resumo = resumo;
    }

    public int getId() { 
        return id; 
    }

    public void setId(int id) { 
        this.id = id; 
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

    public double getNota() { 
        return nota; 
    }

    public void setNota(double nota) { 
        this.nota = nota; 
    }

    public StatusSerie getStatus() { 
        return status; 
    }

    public void setStatus(StatusSerie status) { 
        this.status = status; 
    }

    public String getDataEstreia() { 
        return dataEstreia; 
    }
    
    public void setDataEstreia(String dataEstreia) { 
        this.dataEstreia = dataEstreia; 
    }

    public String getDataTermino() { 
        return dataTermino; 
    }
    
    public void setDataTermino(String dataTermino) { 
        this.dataTermino = dataTermino; 
    }

    public String getEmissora() { 
        return emissora; 
    }
    public void setEmissora(String emissora) { 
        this.emissora = emissora; 
    }

    public String getImagemUrl() { 
        return imagemUrl; 
    }

    public void setImagemUrl(String imagemUrl) { 
        this.imagemUrl = imagemUrl; 
    }

    public String getResumo() { 
        return resumo; 
    }
    
    public void setResumo(String resumo) { 
        this.resumo = resumo; 
    }

    public String getGenerosFormatados() {
        if (generos == null || generos.isEmpty()) return "Não informado";
        return String.join(", ", generos);
    }

    public String getNotaFormatada() {
        if (nota <= 0) return "Sem avaliação";
        return String.format("%.1f", nota);
    }

    public String getDataEstreiaFormatada() {
        if (dataEstreia == null || dataEstreia.isBlank()) return "Não informada";
        return dataEstreia;
    }

    public String getDataTerminoFormatada() {
        if (dataTermino == null || dataTermino.isBlank()) return "—";
        return dataTermino;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Serie outro)) return false;
        return this.id == outro.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return nome;
    }
}
