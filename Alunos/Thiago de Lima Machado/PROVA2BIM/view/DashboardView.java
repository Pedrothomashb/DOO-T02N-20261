package com.ImdbTLM.view;

import com.ImdbTLM.model.Serie;
import com.ImdbTLM.service.SerieComparators;
import com.ImdbTLM.service.APIdeIMDB;
import com.ImdbTLM.service.UsuarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class DashboardView extends JFrame {

    private final UsuarioService usuarioService;
    private final APIdeIMDB APIdeIMDB;

    private JTextField campoBusca;
    private JButton btnBuscar;
    private SerieTableModel modeloResultados;
    private JTable tabelaResultados;
    private JLabel lblStatusBusca;

    private JTabbedPane abaListas;
    private SerieTableModel[] modelosListas;
    private JTable[] tabelasListas;
    private JComboBox<SerieComparators.Criterio>[] combosOrdenacao;

    private DetalheSeriePanel painelDetalhes;

    private static final UsuarioService.Lista[] LISTAS = UsuarioService.Lista.values();

    // View do Dashboard com as informações já puxadas da API
    @SuppressWarnings("unchecked")
    public DashboardView(UsuarioService usuarioService, APIdeIMDB APIdeIMDB) {
        this.usuarioService = usuarioService;
        this.APIdeIMDB = APIdeIMDB;
        modelosListas = new SerieTableModel[3];
        tabelasListas = new JTable[3];
        combosOrdenacao = new JComboBox[3];

        configurarJanela();
        construirInterface();
        carregarTodasAsListas();
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                tentarSalvarAoFechar();
            }
        });
    }

    private void configurarJanela() {
        setTitle("IMDB TLM — " + usuarioService.getUsuario().getNome());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1120, 740);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppTheme.BG_PRIMARY);
    }

    private void tentarSalvarAoFechar() {
        try { usuarioService.salvar(); }
        catch (IOException ex) {
            int op = JOptionPane.showConfirmDialog(this,
                    "Não foi possível salvar os dados.\n" + ex.getMessage() + "\n\nFechar mesmo assim?",
                    "Erro ao salvar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (op != JOptionPane.YES_OPTION) return;
        }
        dispose();
    }

    // Layout principal do dashboard
    private void construirInterface() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(AppTheme.BG_PRIMARY);
        root.add(construirHeader(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                construirPainelEsquerdo(), construirPainelDetalhes());
        split.setResizeWeight(0.68);
        split.setDividerSize(4);
        split.setBackground(AppTheme.GOLD);
        root.add(split, BorderLayout.CENTER);
        setContentPane(root);
    }

    /**
     * Header do dashboard com título e perfil do API
     */

    private JPanel construirHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(AppTheme.BG_HEADER);
        h.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 3, 0, AppTheme.GOLD),
                new EmptyBorder(12, 22, 12, 22)));

        JLabel titulo = new JLabel("📺 IMDB - TLM");
        titulo.setFont(AppTheme.FONT_TITLE);
        titulo.setForeground(AppTheme.GOLD);

        JLabel usuario = new JLabel("👤 " + usuarioService.getUsuario().getNome());
        usuario.setFont(AppTheme.FONT_BODY);
        usuario.setForeground(AppTheme.FG_SECONDARY);

        // Botão de voltar para a tela de login
        JButton btnVoltar = new JButton("← Trocar Perfil");
        btnVoltar.setFont(AppTheme.FONT_SMALL);
        btnVoltar.setBackground(AppTheme.BTN_SECONDARY);
        btnVoltar.setForeground(AppTheme.FG_SECONDARY);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setOpaque(true);
        btnVoltar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER, 1),
                new EmptyBorder(5, 12, 5, 12)));
        btnVoltar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e -> voltarParaLogin());

        // Layout perfil/troca canto direito superior

        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        direita.setOpaque(false);
        direita.add(usuario);
        direita.add(btnVoltar);

        h.add(titulo, BorderLayout.WEST);
        h.add(direita, BorderLayout.EAST);
        return h;
    }

    // Barra lateral esquerda

    private JPanel construirPainelEsquerdo() {
        JPanel p = new JPanel(new BorderLayout(0, 0));
        p.setBackground(AppTheme.BG_PRIMARY);
        p.add(construirPainelBusca(), BorderLayout.NORTH);
        p.add(construirAbaListas(), BorderLayout.CENTER);
        return p;
    }

    /**
     * Painel de busca no dashboard, para adicionar a serie buscada na lista desejada
     */

    private JPanel construirPainelBusca() {
        JPanel painel = new JPanel(new BorderLayout(0, 0));
        painel.setBackground(AppTheme.BG_CARD);
        painel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 2, 0, AppTheme.GOLD),
                new EmptyBorder(12, 14, 12, 14)));

        // Barra de busca
        JPanel linhaBusca = new JPanel(new BorderLayout(8, 0));
        linhaBusca.setOpaque(false);

        campoBusca = new JTextField();
        campoBusca.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campoBusca.setBackground(AppTheme.BG_INPUT);
        campoBusca.setForeground(AppTheme.FG_PRIMARY);
        campoBusca.setCaretColor(AppTheme.GOLD);
        campoBusca.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER, 1),
                new EmptyBorder(7, 10, 7, 10)));
        campoBusca.addActionListener(e -> executarBusca());

        btnBuscar = criarBotaoPrimario("🔍 Buscar");
        btnBuscar.addActionListener(e -> executarBusca());

        linhaBusca.add(campoBusca, BorderLayout.CENTER);
        linhaBusca.add(btnBuscar, BorderLayout.EAST);

        // Status
        lblStatusBusca = new JLabel("  Digite um nome e pressione Buscar");
        lblStatusBusca.setFont(AppTheme.FONT_SMALL);
        lblStatusBusca.setForeground(AppTheme.FG_MUTED);

        // Tabela de resultados
        modeloResultados = new SerieTableModel();
        tabelaResultados = criarTabela(modeloResultados);
        tabelaResultados.getSelectionModel().addListSelectionListener(
                e -> aoSelecionarNaTabela(tabelaResultados, modeloResultados, e));
        JScrollPane scroll = new JScrollPane(tabelaResultados);
        estilizarScroll(scroll);
        scroll.setPreferredSize(new Dimension(0, 175));

        // Botões adicionar
        JPanel botoesAdicionar = construirBotoesAdicionar();

        JPanel corpo = new JPanel(new BorderLayout(0, 6));
        corpo.setOpaque(false);
        corpo.setBorder(new EmptyBorder(8, 0, 0, 0));
        corpo.add(lblStatusBusca, BorderLayout.NORTH);
        corpo.add(scroll, BorderLayout.CENTER);
        corpo.add(botoesAdicionar, BorderLayout.SOUTH);

        painel.add(linhaBusca, BorderLayout.NORTH);
        painel.add(corpo, BorderLayout.CENTER);
        return painel;
    }

    private JPanel construirBotoesAdicionar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        p.setOpaque(false);

        JLabel lbl = new JLabel("Adicionar à:");
        lbl.setFont(AppTheme.FONT_SMALL);
        lbl.setForeground(AppTheme.FG_SECONDARY);
        p.add(lbl);

        for (UsuarioService.Lista lista : UsuarioService.Lista.values()) {
            JButton btn = criarBotaoSecundario(lista.toString());
            btn.setFont(AppTheme.FONT_SMALL);
            final UsuarioService.Lista l = lista;
            btn.addActionListener(e -> adicionarDeResultados(l));
            p.add(btn);
        }
        return p;
    }

    /**
     * Reponsável por criar a sessão de lista das series (favoritas/já assistidas/Deseja assistir)
     */
    private JTabbedPane construirAbaListas() {
        abaListas = new JTabbedPane(JTabbedPane.TOP);
        abaListas.setBackground(AppTheme.BG_CARD);
        abaListas.setForeground(AppTheme.FG_PRIMARY);
        abaListas.setFont(AppTheme.FONT_HEADING);

        String[] icones = {"⭐ ", "✅ ", "📋 "};
        for (int i = 0; i < 3; i++) {
            modelosListas[i] = new SerieTableModel();
            tabelasListas[i] = criarTabela(modelosListas[i]);
            final int idx = i;
            tabelasListas[i].getSelectionModel().addListSelectionListener(
                    e -> aoSelecionarNaTabela(tabelasListas[idx], modelosListas[idx], e));
            abaListas.addTab(icones[i] + LISTAS[i].toString(), construirConteudoAba(i));
        }
        return abaListas;
    }

    private JPanel construirConteudoAba(int i) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBackground(AppTheme.BG_PRIMARY);
        p.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Barra superior
        JPanel top = new JPanel(new BorderLayout(8, 0));
        top.setOpaque(false);

        JPanel esq = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        esq.setOpaque(false);
        JLabel lblOrd = new JLabel("Ordenar:");
        lblOrd.setFont(AppTheme.FONT_SMALL);
        lblOrd.setForeground(AppTheme.FG_SECONDARY);
        combosOrdenacao[i] = new JComboBox<>(SerieComparators.Criterio.values());
        combosOrdenacao[i].setFont(AppTheme.FONT_SMALL);
        combosOrdenacao[i].setBackground(AppTheme.BG_CARD);
        combosOrdenacao[i].setForeground(AppTheme.FG_PRIMARY);
        final int idx = i;
        combosOrdenacao[i].addActionListener(e -> recarregarLista(idx));
        esq.add(lblOrd);
        esq.add(combosOrdenacao[i]);

        JButton btnRem = criarBotaoPerigo("🗑 Remover");
        btnRem.setFont(AppTheme.FONT_SMALL);
        btnRem.addActionListener(e -> removerDaLista(i));

        top.add(esq, BorderLayout.WEST);
        top.add(btnRem, BorderLayout.EAST);

        JScrollPane scroll = new JScrollPane(tabelasListas[i]);
        estilizarScroll(scroll);

        p.add(top, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    /**
     * Painel de detalhes com as informações da série
     */
    private DetalheSeriePanel construirPainelDetalhes() {
        painelDetalhes = new DetalheSeriePanel();
        return painelDetalhes;
    }

    /**
     * Lógica / mecanismo de Busca das séries.
     * Faz a busca na API e retorna informativos
     */
    private void executarBusca() {
        String termo = campoBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome de uma série para buscar.",
                    "Campo vazio", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        btnBuscar.setEnabled(false);
        lblStatusBusca.setText("  ⏳ Buscando \"" + termo + "\"...");
        modeloResultados.limpar();
        painelDetalhes.exibir(null);

        new SwingWorker<List<Serie>, Void>() {
            @Override protected List<Serie> doInBackground() throws Exception {
                return APIdeIMDB.buscarPorNome(termo);
            }
            @Override protected void done() {
                btnBuscar.setEnabled(true);
                try {
                    List<Serie> res = get();
                    if (res.isEmpty()) {
                        lblStatusBusca.setText("  Nenhuma série encontrada para \"" + termo + "\".");
                    } else {
                        modeloResultados.setSeries(res);
                        lblStatusBusca.setText("  " + res.size() + " série(s) encontrada(s).");
                    }
                } catch (java.util.concurrent.ExecutionException ex) {
                    Throwable causa = ex.getCause();
                    lblStatusBusca.setText("  ❌ Erro na busca.");
                    JOptionPane.showMessageDialog(DashboardView.this,
                            "Falha na busca:\n" + causa.getMessage(),
                            "Erro de Busca", JOptionPane.ERROR_MESSAGE);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    lblStatusBusca.setText("  Busca cancelada.");
                }
            }
        }.execute();
    }

    // Método que adiciona a serie a lista seleciona e exibe mensagem de confirmação
    private void adicionarDeResultados(UsuarioService.Lista lista) {
        int linha = tabelaResultados.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma série nos resultados.",
                    "Nenhuma série selecionada", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Serie s = modeloResultados.getSerieAt(linha);
        try {
            usuarioService.adicionarSerie(s, lista);
            recarregarTodasAsListas();
            JOptionPane.showMessageDialog(this,
                    "\"" + s.getName() + "\" adicionada a " + lista + "!",
                    "Adicionada", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Série já adicionada", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Retira uma serie da lista escolhida
    private void removerDaLista(int i) {
        int linha = tabelasListas[i].getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma série para remover.",
                    "Nenhuma série selecionada", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Serie s = modelosListas[i].getSerieAt(linha);
        int op = JOptionPane.showConfirmDialog(this,
                "Remover \"" + s.getName() + "\" de " + LISTAS[i] + "?",
                "Confirmar remoção", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            try {
                usuarioService.removerSerie(s, LISTAS[i]);
                recarregarLista(i);
                painelDetalhes.exibir(null);
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método que exibe a tabela selecionada
    private void aoSelecionarNaTabela(JTable tabela, SerieTableModel modelo, ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int linha = tabela.getSelectedRow();
        if (linha >= 0 && linha < modelo.getRowCount()) {
            painelDetalhes.exibir(modelo.getSerieAt(linha));
            if (tabela != tabelaResultados) tabelaResultados.clearSelection();
            for (int i = 0; i < tabelasListas.length; i++)
                if (tabelasListas[i] != tabela) tabelasListas[i].clearSelection();
        }
    }

    // Métodos que carregam a lista de serie salvas
    private void carregarTodasAsListas()   { for (int i = 0; i < 3; i++) recarregarLista(i); }
    private void recarregarTodasAsListas() { carregarTodasAsListas(); }
    private void recarregarLista(int i) {
        SerieComparators.Criterio c = (SerieComparators.Criterio) combosOrdenacao[i].getSelectedItem();
        modelosListas[i].setSeries(usuarioService.getListaOrdenada(LISTAS[i], c));
        String[] icones = {"⭐ ", "✅ ", "📋 "};
        abaListas.setTitleAt(i, icones[i] + LISTAS[i] + " (" + modelosListas[i].getRowCount() + ")");
    }

    /**
     * Cria todos os elementos gráficos que aparecem no dashboard (Botões, tabelas, scroll)
     */

    private JTable criarTabela(SerieTableModel modelo) {
        JTable t = new JTable(modelo);
        t.setFont(AppTheme.FONT_BODY);
        t.setBackground(AppTheme.BG_SURFACE);
        t.setForeground(AppTheme.FG_PRIMARY);
        t.setGridColor(AppTheme.BORDER);
        t.setSelectionBackground(AppTheme.TEAL);
        t.setSelectionForeground(AppTheme.OFFWHITE);
        t.setRowHeight(27);
        t.setShowGrid(true);
        t.setFillsViewportHeight(true);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setAutoCreateRowSorter(false);

        t.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        t.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(AppTheme.BG_HEADER);   // ← troque pela cor que quiser
                lbl.setForeground(AppTheme.GOLD);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
                lbl.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 1, AppTheme.BORDER),
                        new EmptyBorder(4, 8, 4, 8)));
                lbl.setOpaque(true);
                return lbl;
            }
        });

        // Renderer com linhas zebradas
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? AppTheme.BG_ROW_EVEN : AppTheme.BG_ROW_ODD);
                    setForeground(AppTheme.FG_PRIMARY);
                } else {
                    setBackground(AppTheme.TEAL);
                    setForeground(AppTheme.OFFWHITE);
                }
                setBorder(new EmptyBorder(2, 8, 2, 8));
                return this;
            }
        });

        int[] larguras = {220, 90, 55, 90, 110, 75};
        for (int i = 0; i < larguras.length && i < t.getColumnCount(); i++)
            t.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);

        return t;
    }

    private JButton criarBotaoPrimario(String texto) {
        JButton b = new JButton(texto);
        b.setFont(AppTheme.FONT_HEADING);
        b.setBackground(AppTheme.BTN_PRIMARY);
        b.setForeground(AppTheme.BTN_PRIMARY_FG);
        b.setFocusPainted(false); b.setOpaque(true);
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton criarBotaoSecundario(String texto) {
        JButton b = new JButton(texto);
        b.setFont(AppTheme.FONT_SMALL);
        b.setBackground(AppTheme.BTN_SECONDARY);
        b.setForeground(AppTheme.FG_PRIMARY);
        b.setFocusPainted(false); b.setOpaque(true);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER, 1),
                new EmptyBorder(5, 10, 5, 10)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton criarBotaoPerigo(String texto) {
        JButton b = new JButton(texto);
        b.setFont(AppTheme.FONT_SMALL);
        b.setBackground(AppTheme.BTN_DANGER);
        b.setForeground(AppTheme.OFFWHITE);
        b.setFocusPainted(false); b.setOpaque(true);
        b.setBorder(new EmptyBorder(5, 10, 5, 10));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void estilizarScroll(JScrollPane s) {
        s.getViewport().setBackground(AppTheme.BG_SURFACE);
        s.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER, 1));
        s.getVerticalScrollBar().setBackground(AppTheme.BG_SURFACE);
    }

    // Método para voltar a tela de login
    private void voltarParaLogin() {
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja voltar para a seleção de perfis?\nOs dados serão salvos automaticamente.",
                "Trocar perfil",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacao != JOptionPane.YES_OPTION) return;

        tentarSalvarAoFechar(); //

        new LoginView(usuarioService.getRepository()).setVisible(true);
        dispose();
    }
}
