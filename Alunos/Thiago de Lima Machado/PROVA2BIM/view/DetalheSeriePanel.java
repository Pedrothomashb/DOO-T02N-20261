package com.ImdbTLM.view;

import com.ImdbTLM.model.Serie;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class DetalheSeriePanel extends JPanel {

    private JLabel lblNomeValor, lblIdiomaValor, lblGenerosValor;
    private JLabel lblNotaValor, lblStatusValor, lblEstreiaValor;
    private JLabel lblTerminoValor, lblEmissoraValor;
    private JLabel lblVazio;
    private JPanel painelCampos;

    /**
     * Executa a criação do painel
     */

    public DetalheSeriePanel() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_SURFACE);
        setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 2, 0, 0, AppTheme.GOLD),
                new EmptyBorder(0, 0, 0, 0)));
        setPreferredSize(new Dimension(300, 0));
        construirComponentes();
        exibirEstadoVazio();
    }

    /**
     * Cria o campo do painel lateral para os detalhes da série
     */

    private void construirComponentes() {
        // Título do painel
        JPanel tituloBar = new JPanel(new BorderLayout());
        tituloBar.setBackground(AppTheme.BG_HEADER);
        tituloBar.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 2, 0, AppTheme.GOLD),
                new EmptyBorder(10, 16, 10, 16)));
        JLabel lblTitulo = new JLabel("Detalhes");
        lblTitulo.setFont(AppTheme.FONT_HEADING);
        lblTitulo.setForeground(AppTheme.GOLD);
        tituloBar.add(lblTitulo, BorderLayout.WEST);
        add(tituloBar, BorderLayout.NORTH);

        // Campos
        painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBackground(AppTheme.BG_SURFACE);
        painelCampos.setBorder(new EmptyBorder(14, 16, 14, 16));

        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.anchor = GridBagConstraints.NORTHWEST;
        gbcL.insets = new Insets(6, 0, 6, 10);
        gbcL.gridx = 0;

        GridBagConstraints gbcV = new GridBagConstraints();
        gbcV.anchor = GridBagConstraints.NORTHWEST;
        gbcV.fill = GridBagConstraints.HORIZONTAL;
        gbcV.weightx = 1.0;
        gbcV.insets = new Insets(6, 0, 6, 0);
        gbcV.gridx = 1;

        lblNomeValor    = addField(painelCampos, gbcL, gbcV, 0, "Nome");
        lblIdiomaValor  = addField(painelCampos, gbcL, gbcV, 1, "Idioma");
        lblGenerosValor = addField(painelCampos, gbcL, gbcV, 2, "Gêneros");
        lblNotaValor    = addField(painelCampos, gbcL, gbcV, 3, "Nota");
        lblStatusValor  = addField(painelCampos, gbcL, gbcV, 4, "Status");
        lblEstreiaValor = addField(painelCampos, gbcL, gbcV, 5, "Estreia");
        lblTerminoValor = addField(painelCampos, gbcL, gbcV, 6, "Término");
        lblEmissoraValor= addField(painelCampos, gbcL, gbcV, 7, "Emissora");

        // Mensagem de detalhe antes da busca
        lblVazio = new JLabel("<html><center>Selecione uma série<br>para ver os detalhes</center></html>",
                SwingConstants.CENTER);
        lblVazio.setFont(new Font("SansSerif", Font.ITALIC, 13));
        lblVazio.setForeground(AppTheme.FG_MUTED);

        add(painelCampos, BorderLayout.NORTH);
        add(lblVazio, BorderLayout.CENTER);
    }

    /**
     * Cria a linha que será exibido os detalhes da série
     */

    private JLabel addField(JPanel p, GridBagConstraints gbcL, GridBagConstraints gbcV,
                             int row, String rotulo) {
        gbcL.gridy = row;
        gbcV.gridy = row;

        JLabel lbl = new JLabel(rotulo);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(AppTheme.GOLD);
        p.add(lbl, gbcL);

        JLabel val = new JLabel("");
        val.setFont(AppTheme.FONT_BODY);
        val.setForeground(AppTheme.FG_PRIMARY);
        p.add(val, gbcV);

        if (row < 7) {
            GridBagConstraints gbcSep = new GridBagConstraints();
            gbcSep.gridx = 0; gbcSep.gridy = row;
            gbcSep.gridwidth = 2;
            gbcSep.fill = GridBagConstraints.HORIZONTAL;
            gbcSep.insets = new Insets(0, 0, 0, 0);
            gbcSep.anchor = GridBagConstraints.SOUTH;
        }

        return val;
    }

    /**
     * Exibição das informações da serie detalhada
     */

    public void exibir(Serie s) {
        if (s == null) { exibirEstadoVazio(); return; }

        lblNomeValor.setText("<html><b>" + nvl(s.getName()) + "</b></html>");
        lblIdiomaValor.setText(nvl(s.getLanguage()));
        lblGenerosValor.setText("<html>" + s.getGenresFormatted() + "</html>");
        lblNotaValor.setText(formatarNota(s));
        lblStatusValor.setText(formatarStatus(s.getStatus()));
        lblEstreiaValor.setText(nvl(s.getPremiered()));
        lblTerminoValor.setText(nvl(s.getEnded()));
        lblEmissoraValor.setText(nvl(s.getNetworkName()));

        lblVazio.setVisible(false);
        painelCampos.setVisible(true);
        revalidate(); repaint();
    }

    private void exibirEstadoVazio() {
        lblVazio.setVisible(true);
        painelCampos.setVisible(false);
    }

    /**
     * Formatadores das informações no Painel lateral da serie
     */

    private String nvl(String v) {
        return (v != null && !v.isBlank()) ? v : "N/A";
    }

    private String formatarNota(Serie s) {
        if (s.getRatingAverage() == null) return "N/A";
        double v = s.getRatingAverage();
        String hex = v >= 8.0 ? "#C9A227" : (v >= 6.0 ? "#1D8FAB" : "#CC4444");
        return "<html><font color='" + hex + "'><b>" + s.getRatingFormatted() + "</b></font> / 10</html>";
    }

    private String formatarStatus(String status) {
        if (status == null) return "N/A";
        switch (status.toLowerCase()) {
            case "running": return "● Em andamento";
            case "ended":   return "○ Finalizada";
            default:        return "◐ " + status;
        }
    }
}
