import java.util.Comparator;
import java.util.List;


public class Serie {
    private int id;
    private String nome;
    private String idioma;
    private List<String> generos;
    private double notaGeral;
    private String estado;
    private String dataEstreia;
    private String dataTermino;
    private String emissora;

    public Serie() {
        this.generos = new java.util.ArrayList<>();
    }

    public Serie(int id, String nome, String idioma, List<String> generos, double notaGeral, String estado, String dataEstreia, String dataTermino, String emissora) {
        this.id = id;
        this.nome = nome;
        this.idioma = idioma;
        this.generos = generos;
        this.notaGeral = notaGeral;
        this.estado = estado;
        this.dataEstreia = dataEstreia;
        this.dataTermino = dataTermino;
        this.emissora = emissora;
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

    public List<String> getGenero() { 
        return generos; 
    }
    public void setGeneros(List<String> generos) { 
        this.generos = generos; 
    }

    public double getNotaGeral() { 
        return notaGeral; 
    }
    public void setNotaGeral(double notaGeral) { 
        this.notaGeral = notaGeral; 
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

    /**
     * Critério de ordenação alfabética pelo nome da série de forma insensível a maiúsculas/minúsculas.
     */
    public static final Comparator<Serie> COMPARADOR_NOME = new Comparator<Serie>() {
        @Override
        public int compare(Serie s1, Serie s2) {
            return s1.getNome().compareToIgnoreCase(s2.getNome());
        }
    };

    /**
     * Critério de ordenação decrescente pela nota geral (da maior para a menor).
     */
    public static final Comparator<Serie> COMPARADOR_NOTA = new Comparator<Serie>() {
        @Override
        public int compare(Serie s1, Serie s2) {
            return Double.compare(s2.getNotaGeral(), s1.getNotaGeral());
        }
    };

    /**
     * Critério de ordenação alfabética baseada no estado atual de transmissão da série.
     */
    public static final Comparator<Serie> COMPARADOR_ESTADO = new Comparator<Serie>() {
        @Override
        public int compare(Serie s1, Serie s2) {
            return s1.getEstado().compareToIgnoreCase(s2.getEstado());
        }
    };

    public static final Comparator<Serie> COMPARADOR_ESTADO_TRANSMITIDO = new Comparator<Serie>() {
        @Override
        public int compare(Serie s1, Serie s2) {
            String e1 = s1.getEstado() != null ? s1.getEstado() : "";
            String e2 = s2.getEstado() != null ? s2.getEstado() : "";
            if (e1.equals("Ainda transmitindo") && !e2.equals("Ainda transmitindo")) return -1;
            if (!e1.equals("Ainda transmitindo") && e2.equals("Ainda transmitindo")) return 1;
            return e1.compareToIgnoreCase(e2);
        }
    };
    public static final Comparator<Serie> COMPARADOR_ESTADO_CONCLUIDO = new Comparator<Serie>() {
        @Override
        public int compare(Serie s1, Serie s2) {
            String e1 = s1.getEstado() != null ? s1.getEstado() : "";
            String e2 = s2.getEstado() != null ? s2.getEstado() : "";
            if (e1.equals("Já concluída") && !e2.equals("Já concluída")) return -1;
            if (!e1.equals("Já concluída") && e2.equals("Já concluída")) return 1;
            return e1.compareToIgnoreCase(e2);
        }
    };
    public static final Comparator<Serie> COMPARADOR_ESTADO_CANCELADA = new Comparator<Serie>() {
        @Override
        public int compare(Serie s1, Serie s2) {
            String e1 = s1.getEstado() != null ? s1.getEstado() : "";
            String e2 = s2.getEstado() != null ? s2.getEstado() : "";
            if (e1.equals("Cancelada") && !e2.equals("Cancelada")) return -1;
            if (!e1.equals("Cancelada") && e2.equals("Cancelada")) return 1;
            return e1.compareToIgnoreCase(e2);
        }
    };

    /**
     * Critério de ordenação cronológica crescente baseada na data de estreia.
     * Trata valores nulos movendo-os para o fim da estrutura de dados.
     */
    public static final Comparator<Serie> COMPARADOR_ESTREIA = new Comparator<Serie>() {
        @Override
        public int compare(Serie s1, Serie s2) {
            if (s1.getDataEstreia() == null) return 1;
            if (s2.getDataEstreia() == null) return -1;
            return s2.getDataEstreia().compareTo(s1.getDataEstreia());
        }
    };

    /**
     * Compara a igualdade entre duas instâncias de séries utilizando o identificador único da API.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Serie outra = (Serie) obj;
        return this.id == outra.id;
    }

    @Override
    public String toString() {
        return this.nome != null ? this.nome : "Série sem nome";
    }
}
