package com.ImdbTLM.view;

import com.ImdbTLM.model.Serie;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;


/** Modelo de tabela customizado para exibição das listas com as {@link Serie}.
 *
 */

public class SerieTableModel extends AbstractTableModel {

    private static final String[] COLUNAS = {
            "Nome", "Status", "Nota", "Estreia", "Emissora", "Idioma"
    };

    private List<Serie> series;

    public SerieTableModel() {
        this.series = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return series.size();
    }

    @Override
    public int getColumnCount() {
        return COLUNAS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUNAS[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= series.size()) return null;
        Serie s = series.get(rowIndex);

        switch (columnIndex) {
            case 0:  return s.getName() != null ? s.getName() : "";
            case 1:  return s.getStatus() != null ? s.getStatus() : "N/A";
            case 2:  return s.getRatingFormatted();
            case 3:  return s.getPremiered() != null ? s.getPremiered() : "N/A";
            case 4:  return s.getNetworkName() != null ? s.getNetworkName() : "N/A";
            case 5:  return s.getLanguage() != null ? s.getLanguage() : "N/A";
            default: return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * Troca todos os dados da tabela e envia para a View.
     */
    public void setSeries(List<Serie> novasSeries) {
        this.series = new ArrayList<>(novasSeries != null ? novasSeries : new ArrayList<>());
        fireTableDataChanged();
    }

    /**
     * Retorna a série na linha indicada.
     */
    public Serie getSerieAt(int rowIndex) {
        return series.get(rowIndex);
    }

    /**
     * Limpa todos os dados da tabela.
     */
    public void limpar() {
        series.clear();
        fireTableDataChanged();
    }
}
