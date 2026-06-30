package com.ImdbTLM.service;

import com.ImdbTLM.model.Serie;
import java.util.Comparator;

public final class SerieComparators {

    public enum Criterio {
        ORDEM_ALFABETICA("Nome (A-Z)"),
        NOTA_DECRESCENTE("Nota (maior primeiro)"),
        STATUS("Status"),
        DATA_ESTREIA("Data de Estreia");

        private final String label;

        Criterio(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    // Construtor que irá receber a comparação
    private SerieComparators() {}
    public static Comparator<Serie> porCriterio(Criterio criterio) {
        switch (criterio) {
            case ORDEM_ALFABETICA: return porNome();
            case NOTA_DECRESCENTE: return porNotaDecrescente();
            case STATUS:           return porStatus();
            case DATA_ESTREIA:     return porDataEstreia();
            default:               return porNome();
        }
    }

    // Ordena alfabeticamente por nome.
    public static Comparator<Serie> porNome() {
        return Comparator.comparing(
                Serie::getName,
                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
        );
    }

    // Ordena por nota de forma decrescente
    public static Comparator<Serie> porNotaDecrescente() {
        return Comparator.comparing(
                Serie::getRatingAverage,
                Comparator.nullsLast(Comparator.reverseOrder())
        );
    }

    // Ordena pelo status da série: "Em andamento" > "Em desenvolvimento" > "A ser lançada" > "Finalizada".
    // Séries em andamento aparecem no topo.
    public static Comparator<Serie> porStatus() {
        return Comparator.comparing(
                Serie::getStatus,
                Comparator.nullsLast((s1, s2) -> {
                    int p1 = prioridadeStatus(s1);
                    int p2 = prioridadeStatus(s2);
                    if (p1 != p2) return Integer.compare(p1, p2);
                    return s1.compareToIgnoreCase(s2);
                })
        );
    }

    // Ordena pela data de estreia (do mais antigo ao mais recente).
    public static Comparator<Serie> porDataEstreia() {
        return Comparator.comparing(
                Serie::getPremiered,
                Comparator.nullsLast(String::compareTo)
        );
    }

    // Metodo para definir prioridade (usado no porStatus)
    private static int prioridadeStatus(String status) {
        if (status == null) return 99;
        switch (status.toLowerCase()) {
            case "Em andamento":           return 1;
            case "Em desenvolvimento":    return 2;
            case "A ser lançada":  return 3;
            case "Finalizada":             return 4;
            default:                  return 5;
        }
    }
}
