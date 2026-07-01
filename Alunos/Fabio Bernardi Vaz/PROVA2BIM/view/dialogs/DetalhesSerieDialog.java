package view.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import controller.AppController;
import model.Serie;
import model.enums.TipoLista;

public class DetalhesSerieDialog extends JDialog {

    private final Serie serie;
    private final AppController controller;

    public DetalhesSerieDialog(JFrame parent, Serie serie, AppController controller) {
        super(parent, serie.getNome(), true);
        this.serie      = serie;
        this.controller = controller;
        inicializar();
    }

    private void inicializar() {
        setSize(520, 560);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel painelRaiz = new JPanel(new BorderLayout(0, 10));
        painelRaiz.setBackground(Color.WHITE);
        painelRaiz.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel cabecalho = new JPanel(new BorderLayout(0, 4));
        cabecalho.setBackground(Color.WHITE);

        JLabel lblNome = new JLabel(serie.getNome());
        lblNome.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblNome.setForeground(Color.BLACK);

        JLabel lblEmissora = new JLabel(serie.getEmissora());
        lblEmissora.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblEmissora.setForeground(Color.GRAY);

        cabecalho.add(lblNome,     BorderLayout.NORTH);
        cabecalho.add(lblEmissora, BorderLayout.SOUTH);
        painelRaiz.add(cabecalho,  BorderLayout.NORTH);

        JPanel painelInfo = new JPanel(new GridLayout(0, 1, 0, 8));
        painelInfo.setBackground(Color.WHITE);
        painelInfo.setBorder(new EmptyBorder(10, 0, 10, 0));

        adicionarInfo(painelInfo, "Idioma",   serie.getIdioma());
        adicionarInfo(painelInfo, "Generos",  serie.getGenerosFormatados());
        adicionarInfo(painelInfo, "Nota",     serie.getNotaFormatada());
        adicionarInfo(painelInfo, "Status",   serie.getStatus().getDescricao());
        adicionarInfo(painelInfo, "Estreia",  serie.getDataEstreiaFormatada());
        adicionarInfo(painelInfo, "Termino",  serie.getDataTerminoFormatada());
        adicionarInfo(painelInfo, "Emissora", serie.getEmissora());

        painelRaiz.add(painelInfo, BorderLayout.CENTER);

        JPanel painelResumo = new JPanel(new BorderLayout(0, 5));
        painelResumo.setBackground(Color.WHITE);

        JLabel lblTituloResumo = new JLabel("Resumo");
        lblTituloResumo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTituloResumo.setForeground(Color.GRAY);

        JTextArea areaResumo = new JTextArea(serie.getResumo());
        areaResumo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        areaResumo.setForeground(Color.BLACK);
        areaResumo.setBackground(new Color(248, 248, 248));
        areaResumo.setLineWrap(true);
        areaResumo.setWrapStyleWord(true);
        areaResumo.setEditable(false);
        areaResumo.setBorder(new EmptyBorder(10, 12, 10, 12));

        JScrollPane scroll = new JScrollPane(areaResumo);
        scroll.setPreferredSize(new Dimension(0, 90));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

        painelResumo.add(lblTituloResumo, BorderLayout.NORTH);
        painelResumo.add(scroll,          BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new GridLayout(1, 3, 8, 0));
        painelBotoes.setBackground(Color.WHITE);
        painelBotoes.setBorder(new EmptyBorder(12, 0, 0, 0));

        painelBotoes.add(criarBotaoLista("Favoritos",    TipoLista.FAVORITOS));
        painelBotoes.add(criarBotaoLista("Assistida",    TipoLista.ASSISTIDAS));
        painelBotoes.add(criarBotaoLista("Quero Ver",    TipoLista.QUERO_ASSISTIR));

        JPanel painelSul = new JPanel(new BorderLayout(0, 8));
        painelSul.setBackground(Color.WHITE);
        painelSul.add(painelResumo, BorderLayout.CENTER);
        painelSul.add(painelBotoes, BorderLayout.SOUTH);

        painelRaiz.add(painelSul, BorderLayout.SOUTH);

        add(painelRaiz);
    }

    private void adicionarInfo(JPanel painel, String titulo, String valor) {
        JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        linha.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel(titulo + ":  ");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTitulo.setForeground(Color.GRAY);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblValor.setForeground(Color.BLACK);

        linha.add(lblTitulo);
        linha.add(lblValor);
        painel.add(linha);
    }

    private JButton criarBotaoLista(String texto, TipoLista tipo) {
        boolean jaEsta = controller.estaEmLista(serie, tipo);
        JButton btn = new JButton(jaEsta ? "Remover de " + texto : "Adicionar a " + texto);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 10, 8, 10));
        btn.setOpaque(true);

        atualizarEstiloBotao(btn, jaEsta);

        btn.addActionListener(e -> {
            boolean estaAtualmente = controller.estaEmLista(serie, tipo);
            if (estaAtualmente) {
                controller.removerDaLista(serie, tipo);
                btn.setText("Adicionar a " + texto);
                atualizarEstiloBotao(btn, false);
            } else {
                controller.adicionarNaLista(serie, tipo);
                btn.setText("Remover de " + texto);
                atualizarEstiloBotao(btn, true);
            }
            btn.revalidate();
            btn.repaint();
        });

        return btn;
    }

    private void atualizarEstiloBotao(JButton btn, boolean ativo) {
        if (ativo) {
            btn.setBackground(new Color(180, 0, 0));
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(Color.BLACK);
            btn.setForeground(Color.WHITE);
        }
    }
}