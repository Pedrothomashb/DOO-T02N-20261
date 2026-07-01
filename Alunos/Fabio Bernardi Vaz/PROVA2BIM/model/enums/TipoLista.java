package model.enums;

public enum TipoLista {
    FAVORITOS("Favoritos"),
    ASSISTIDAS("Já Assistidas"),
    QUERO_ASSISTIR("Quero Assistir");

    private final String descricao;

    TipoLista(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
