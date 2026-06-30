package model.enums;

public enum StatusSerie {
    RUNNING("Em Exibição"),
    ENDED("Encerrada"),
    CANCELED("Cancelada"),
    TBD("Indefinido"),
    UNKNOWN("Desconhecido");

    private final String descricao;

    StatusSerie(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusSerie fromString(String valor) {
        if (valor == null) return UNKNOWN;
        return switch (valor.toLowerCase()) {
            case "running"    -> RUNNING;
            case "ended"      -> ENDED;
            case "canceled"   -> CANCELED;
            case "to be determined", "tbd" -> TBD;
            default           -> UNKNOWN;
        };
    }

    @Override
    public String toString() {
        return descricao;
    }
}
