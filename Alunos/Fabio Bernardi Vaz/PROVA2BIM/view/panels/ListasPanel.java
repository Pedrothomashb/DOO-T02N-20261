package view.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.AppController;
import model.Serie;
import model.enums.TipoLista;
import view.dialogs.DetalhesSerieDialog;

public class ListasPanel extends JPanel {

    private final AppController controller;
    private final JFrame janelaPai;
    private final TipoLista tipo;

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JComboBox<String> cbOrdenacao;
    private JLabel lblContador;

    private static final String[] COLUNAS = {"Nome", "Nota", "Status", "Estreia", "Idioma", "Emissora"};

    public ListasPanel(JFrame janelaPai, AppController controller, TipoLista tipo) {
        this.janelaPai  = janelaPai;
        this.controller = controller;
        this.tipo       = tipo;
        setLayout(new BorderLayout(0, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        inicializar();
    }

    private void inicializar() {
        JPanel painelTopo = new JPanel(new BorderLayout(10, 0));
        painelTopo.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel(tipo.getDescricao());
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(Color.BLACK);

        lblContador = new JLabel();
        lblContador.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblContador.setForeground(Color.GRAY);

        JPanel painelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        painelTitulo.setBackground(Color.WHITE);
        painelTitulo.add(lblTitulo);
        painelTitulo.add(Box.createHorizontalStrut(12));
        painelTitulo.add(lblContador);

        JPanel painelOrdenacao = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        painelOrdenacao.setBackground(Color.WHITE);

        JLabel lblOrdenar = new JLabel("Ordenar por:");
        lblOrdenar.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblOrdenar.setForeground(Color.GRAY);

        cbOrdenacao = new JComboBox<>(new String[]{"Padrão", "Nome", "Nota", "Status", "Data Estreia"});
        cbOrdenacao.setFont(new Font("SansSerif", Font.PLAIN, 13));

        painelOrdenacao.add(lblOrdenar);
        painelOrdenacao.add(cbOrdenacao);

        painelTopo.add(painelTitulo,    BorderLayout.WEST);
        painelTopo.add(painelOrdenacao, BorderLayout.EAST);
        add(painelTopo, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(COLUNAS, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        tabela = new JTable(modeloTabela);
        tabela.setBackground(Color.WHITE);
        tabela.setForeground(Color.BLACK);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabela.setRowHeight(32);
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0, 0));
        tabela.getTableHeader().setBackground(new Color(240, 240, 240));
        tabela.getTableHeader().setForeground(Color.DARK_GRAY);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tabela.getTableHeader().setBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        tabela.setSelectionBackground(new Color(220, 220, 220));
        tabela.setSelectionForeground(Color.BLACK);
        tabela.setFocusable(false);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(90);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(130);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                if (!sel) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    setForeground(Color.BLACK);
                }
                return this;
            }
        };
        for (int i = 0; i < COLUNAS.length; i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        scroll.setBackground(Color.WHITE);
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        painelBotoes.setBackground(Color.WHITE);
        painelBotoes.setBorder(new EmptyBorder(8, 0, 0, 0));

        JButton btnDetalhes = criarBotao("Ver Detalhes", Color.BLACK, Color.WHITE);
        JButton btnRemover  = criarBotao("Remover da Lista", new Color(180, 0, 0), Color.WHITE);

        btnDetalhes.addActionListener(e -> abrirDetalhes());
        btnRemover.addActionListener(e -> removerSelecionada());

        painelBotoes.add(btnRemover);
        painelBotoes.add(btnDetalhes);
        add(painelBotoes, BorderLayout.SOUTH);

        cbOrdenacao.addActionListener(e -> carregarDados());
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) abrirDetalhes();
            }
        });

        carregarDados();
    }

    public void carregarDados() {
        String criterio = (String) cbOrdenacao.getSelectedItem();
        if ("Padrão".equals(criterio)) criterio = "";

        List<Serie> series = controller.getSeriesOrdenadas(tipo, criterio != null ? criterio : "");
        modeloTabela.setRowCount(0);

        for (Serie s : series) {
            modeloTabela.addRow(new Object[]{
                    s.getNome(),
                    s.getNotaFormatada(),
                    s.getStatus().getDescricao(),
                    s.getDataEstreiaFormatada(),
                    s.getIdioma(),
                    s.getEmissora()
            });
        }

        int total = series.size();
        lblContador.setText("(" + total + " série" + (total != 1 ? "s" : "") + ")");
    }

    private void abrirDetalhes() {
        Serie selecionada = getSeriesSelecionada();
        if (selecionada == null) {
            JOptionPane.showMessageDialog(janelaPai,
                    "Selecione uma série na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new DetalhesSerieDialog(janelaPai, selecionada, controller).setVisible(true);
        carregarDados();
    }

    private void removerSelecionada() {
        Serie selecionada = getSeriesSelecionada();
        if (selecionada == null) {
            JOptionPane.showMessageDialog(janelaPai,
                    "Selecione uma série para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirma = JOptionPane.showConfirmDialog(janelaPai,
                "Remover \"" + selecionada.getNome() + "\" de " + tipo.getDescricao() + "?",
                "Confirmar remoção", JOptionPane.YES_NO_OPTION);

        if (confirma == JOptionPane.YES_OPTION) {
            controller.removerDaLista(selecionada, tipo);
            carregarDados();
        }
    }

    private Serie getSeriesSelecionada() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return null;

        String criterio = (String) cbOrdenacao.getSelectedItem();
        if ("Padrão".equals(criterio)) criterio = "";
        List<Serie> series = controller.getSeriesOrdenadas(tipo, criterio != null ? criterio : "");

        if (linha < series.size()) return series.get(linha);
        return null;
    }

    private JButton criarBotao(String texto, Color fundo, Color fonte) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(fundo);
        btn.setForeground(fonte);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(9, 18, 9, 18));
        return btn;
    }
}