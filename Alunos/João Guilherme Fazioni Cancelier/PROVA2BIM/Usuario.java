import java.util.List;

public class Usuario {
    private String nome;
    private String senha;
    private boolean sessaoAtiva;
    private List<Serie> favoritos;
    private List<Serie> assistidas;
    private List<Serie> queroAssistir;

    public Usuario() {
        this.sessaoAtiva = false;
        this.favoritos = new java.util.ArrayList<>();
        this.assistidas = new java.util.ArrayList<>();
        this.queroAssistir = new java.util.ArrayList<>();
    }

    public Usuario(String nome, String senha, List<Serie> favoritos, List<Serie> assistidas, List<Serie> queroAssistir) {
        this.nome = nome;
        this.senha = senha;
        this.sessaoAtiva = false;
        this.favoritos = favoritos;
        this.assistidas = assistidas;
        this.queroAssistir = queroAssistir;
    }

    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isSessaoAtiva() {
        return sessaoAtiva;
    }

    public void setSessaoAtiva(boolean sessaoAtiva) {
        this.sessaoAtiva = sessaoAtiva;
    }

    public List<Serie> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<Serie> favoritos) {
        this.favoritos = favoritos;
    }

    public List<Serie> getAssistidas() {
        return assistidas;
    }

    public void setAssistidas(List<Serie> assistidas) {
        this.assistidas = assistidas;
    }

    public List<Serie> getQueroAssistir() {
        return queroAssistir;
    }

    public void setQueroAssistir(List<Serie> queroAssistir) {
        this.queroAssistir = queroAssistir;
    }

    /**
     * Adiciona uma série à coleção de favoritos, evitando duplicidade de registros.
     */
    public void adicionarFavorito(Serie serie) {
        if (!this.favoritos.contains(serie)) {
            this.favoritos.add(serie);
        }
    }

    /**
     * Remove uma série da coleção de favoritos caso ela esteja presente.
     */
    public void removerFavorito(Serie serie) {
        this.favoritos.remove(serie);
    }

    /**
     * Adiciona uma série à coleção de assistidas, evitando duplicidade de registros.
     */
    public void adicionarAssistida(Serie serie) {
        if (!this.assistidas.contains(serie)) {
            this.assistidas.add(serie);
        }
    }

    /**
     * Remove uma série da coleção de assistidas caso ela esteja presente.
     */
    public void removerAssistida(Serie serie) {
        this.assistidas.remove(serie);
    }

    /**
     * Adiciona uma série à coleção de interesse, evitando duplicidade de registros.
     */
    public void adicionarQueroAssistir(Serie serie) {
        if (!this.queroAssistir.contains(serie)) {
            this.queroAssistir.add(serie);
        }
    }

    /**
     * Remove uma série da coleção de interesse caso ela esteja presente.
     */
    public void removerQueroAssistir(Serie serie) {
        this.queroAssistir.remove(serie);
    }
}
