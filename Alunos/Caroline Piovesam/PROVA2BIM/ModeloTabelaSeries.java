package com.caroline.prova2bim.telas;

import com.caroline.prova2bim.series.Serie;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ModeloTabelaSeries extends AbstractTableModel {

    private List<Serie> series;
    private final String[] colunas = { "Nome", "Nota", "Estado", "Estreia",
            "Término", "Gênero", "Emissora", "Idioma" };

    public ModeloTabelaSeries() {
        this.series = new ArrayList<>();
    }

    // aqui usa os recursos pra tabela no swing

    public void setSeries(List<Serie> series) {
        this.series = series;
        fireTableDataChanged();
    }

    public Serie getSerieAt(int linha) {
        return series.get(linha);
    }

    @Override
    public int getRowCount() {
        return series.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return colunas[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Serie serie = series.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return serie.getNome();
            case 1:
                return (serie.getAvaliacao() != null &&
                        serie.getAvaliacao().getMedia() != null)
                                ? serie.getAvaliacao().getMedia()
                                : "Não Disponível";
            case 2:
                String est = (serie.getEstado() != null) ? serie.getEstado() : "";
                if (est.equals("Ended"))
                    return "Concluída";
                if (est.equals("Running"))
                    return "Transmitindo";
                if (est.equals("To Be Determined"))
                    return "Indefinida";
                return est;
            case 3:
                return serie.getDataEstreia();
            case 4:
                return serie.getDataTermino();
            case 5:
                return (serie.getGeneros() != null && !serie.getGeneros().isEmpty())
                        ? String.join(", ", serie.getGeneros())
                        : "Não disponível";
            case 6:
                return (serie.getEmissora() != null && serie.getEmissora().getNome() != null)
                        ? serie.getEmissora().getNome()
                        : "Desconhecida";
            case 7:
                return serie.getIdioma();
            default:
                return null;
        }
    }
}