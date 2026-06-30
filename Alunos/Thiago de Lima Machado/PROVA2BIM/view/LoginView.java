package com.ImdbTLM.view;

import com.ImdbTLM.repository.UsuarioRepository;
import com.ImdbTLM.service.APIdeIMDB;
import com.ImdbTLM.service.UsuarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.List;

public class LoginView extends JFrame {

    private final UsuarioRepository repository;
    private final UsuarioService usuarioService;

    private JPanel painelCards;
    private DefaultListModel<String> modeloLista;
    private JList<String> listaPerfis;
    private JTextField campoNome;

    private static final String CARD_SELECAO = "SELECAO";
    private static final String CARD_NOVO    = "NOVO";

    public LoginView(UsuarioRepository repository) {
        this.repository = repository;
        this.usuarioService = new UsuarioService(repository);
        configurarJanela();
        construirInterface();
        carregarPerfis();
    }

    private void configurarJanela() {
        setTitle("IMDB TLM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(460, 520);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(AppTheme.BG_PRIMARY);
    }

    private void construirInterface() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG_PRIMARY);
        root.add(construirHeader(), BorderLayout.NORTH);

        painelCards = new JPanel(new CardLayout());
        painelCards.setBackground(AppTheme.BG_PRIMARY);
        painelCards.add(construirPainelSelecao(), CARD_SELECAO);
        painelCards.add(construirPainelNovoPerfil(), CARD_NOVO);
        root.add(painelCards, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel construirHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.BG_HEADER);
        header.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 3, 0, AppTheme.GOLD),
                new EmptyBorder(20, 28, 18, 28)));

        JLabel titulo = new JLabel("📺 IMDB - TLM", SwingConstants.CENTER);
        titulo.setFont(AppTheme.FONT_TITLE);
        titulo.setForeground(AppTheme.GOLD);

        JLabel sub = new JLabel("Selecione ou crie um perfil", SwingConstants.CENTER);
        sub.setFont(AppTheme.FONT_SMALL);
        sub.setForeground(AppTheme.FG_SECONDARY);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 6));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(sub);
        header.add(textos, BorderLayout.CENTER);
        return header;
    }

    private JPanel construirPainelSelecao() {
        JPanel painel = new JPanel(new BorderLayout(0, 12));
        painel.setBackground(AppTheme.BG_PRIMARY);
        painel.setBorder(new EmptyBorder(20, 28, 20, 28));

        JLabel instrucao = new JLabel("Perfis salvos:");
        instrucao.setFont(AppTheme.FONT_HEADING);
        instrucao.setForeground(AppTheme.FG_SECONDARY);

        modeloLista = new DefaultListModel<>();
        listaPerfis = new JList<>(modeloLista);
        listaPerfis.setFont(new Font("SansSerif", Font.PLAIN, 14));
        listaPerfis.setBackground(AppTheme.BG_SURFACE);
        listaPerfis.setForeground(AppTheme.FG_PRIMARY);
        listaPerfis.setSelectionBackground(AppTheme.TEAL);
        listaPerfis.setSelectionForeground(AppTheme.OFFWHITE);
        listaPerfis.setFixedCellHeight(38);
        listaPerfis.setBorder(new EmptyBorder(4, 12, 4, 12));
        listaPerfis.setCellRenderer(new PerfilListCellRenderer());
        listaPerfis.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) entrarComPerfilSelecionado();
            }
        });

        JScrollPane scroll = new JScrollPane(listaPerfis);
        scroll.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER, 1));
        scroll.getViewport().setBackground(AppTheme.BG_SURFACE);

        JButton btnEntrar = criarBotaoPrimario("→ Entrar");
        btnEntrar.addActionListener(e -> entrarComPerfilSelecionado());

        JButton btnNovo = criarBotaoSecundario("＋ Novo Perfil");
        btnNovo.addActionListener(e -> mostrarCard(CARD_NOVO));

        JButton btnDeletar = new JButton("🗑 Deletar Perfil");
        btnDeletar.setFont(AppTheme.FONT_HEADING);
        btnDeletar.setBackground(AppTheme.BTN_DANGER);
        btnDeletar.setForeground(AppTheme.OFFWHITE);
        btnDeletar.setFocusPainted(false);
        btnDeletar.setOpaque(true);
        btnDeletar.setBorder(new EmptyBorder(10, 12, 10, 12));
        btnDeletar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDeletar.addActionListener(e -> deletarPerfilSelecionado());

        // Linha superior: Novo + Entrar
        JPanel linhaTop = new JPanel(new GridLayout(1, 2, 10, 0));
        linhaTop.setOpaque(false);
        linhaTop.add(btnNovo);
        linhaTop.add(btnEntrar);

        // Linha inferior: Deletar (largura total, destaque vermelho)
        JPanel linhaBottom = new JPanel(new BorderLayout());
        linhaBottom.setOpaque(false);
        linhaBottom.add(btnDeletar, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new GridLayout(2, 1, 0, 8));
        botoes.setOpaque(false);
        botoes.add(linhaTop);
        botoes.add(linhaBottom);
        // ─────────────────────────────────────────────────────

        painel.add(instrucao, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        painel.add(botoes, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel construirPainelNovoPerfil() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(AppTheme.BG_PRIMARY);
        painel.setBorder(new EmptyBorder(28, 36, 28, 36));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; gbc.insets = new Insets(8, 0, 8, 0);

        JLabel lblNome = new JLabel("Nome ou apelido:");
        lblNome.setFont(AppTheme.FONT_HEADING);
        lblNome.setForeground(AppTheme.FG_SECONDARY);
        gbc.gridy = 0;
        painel.add(lblNome, gbc);

        campoNome = new JTextField();
        campoNome.setFont(new Font("SansSerif", Font.PLAIN, 15));
        campoNome.setBackground(AppTheme.BG_INPUT);
        campoNome.setForeground(AppTheme.FG_PRIMARY);
        campoNome.setCaretColor(AppTheme.GOLD);
        campoNome.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)));
        campoNome.addActionListener(e -> criarPerfil());
        gbc.gridy = 1;
        painel.add(campoNome, gbc);

        JButton btnCriar = criarBotaoPrimario("✔ Criar e Entrar");
        btnCriar.addActionListener(e -> criarPerfil());
        gbc.gridy = 2; gbc.insets = new Insets(16, 0, 8, 0);
        painel.add(btnCriar, gbc);

        JButton btnVoltar = new JButton("← Voltar para a lista");
        btnVoltar.setFont(AppTheme.FONT_SMALL);
        btnVoltar.setForeground(AppTheme.TEAL);
        btnVoltar.setBackground(AppTheme.BG_PRIMARY);
        btnVoltar.setBorderPainted(false);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setOpaque(false);
        btnVoltar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e -> mostrarCard(CARD_SELECAO));
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 0, 0);
        painel.add(btnVoltar, gbc);

        return painel;
    }

    private void carregarPerfis() {
        List<String> perfis = repository.listarPerfis();
        if (perfis.isEmpty()) {
            mostrarCard(CARD_NOVO);
        } else {
            modeloLista.clear();
            perfis.forEach(modeloLista::addElement);
            listaPerfis.setSelectedIndex(0);
            mostrarCard(CARD_SELECAO);
        }
    }

    private void entrarComPerfilSelecionado() {
        String nome = listaPerfis.getSelectedValue();
        if (nome == null) {
            JOptionPane.showMessageDialog(this, "Selecione um perfil na lista.",
                    "Nenhum perfil selecionado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        usuarioService.inicializar(nome);
        abrirDashboard();
    }

    /**
     * Deleta o perfil selecionado após confirmação dupla.
     * Operação irreversível: exibe aviso explícito antes de prosseguir.
     */
    private void deletarPerfilSelecionado() {
        String nome = listaPerfis.getSelectedValue();
        if (nome == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um perfil na lista para deletar.",
                    "Nenhum perfil selecionado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Confirmação com aviso de irreversibilidade
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "⚠ Tem certeza que deseja deletar o perfil \"" + nome + "\"?\n"
                        + "Todas as listas (Favoritos, Já Assistidas, Deseja Assistir) serão perdidas.\n"
                        + "Esta ação não pode ser desfeita.",
                "Deletar perfil",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacao != JOptionPane.YES_OPTION) return;

        boolean deletado = repository.deletarPerfil(nome);
        if (deletado) {
            JOptionPane.showMessageDialog(this,
                    "Perfil \"" + nome + "\" deletado com sucesso.",
                    "Perfil deletado", JOptionPane.INFORMATION_MESSAGE);
            carregarPerfis(); // Recarrega a lista (pode ir para CARD_NOVO se era o último)
        } else {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível deletar o perfil \"" + nome + "\".\n"
                            + "Verifique se o arquivo existe e se você tem permissão de escrita.",
                    "Erro ao deletar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void criarPerfil() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe um nome para o perfil.",
                    "Campo obrigatório", JOptionPane.WARNING_MESSAGE);
            campoNome.requestFocus();
            return;
        }
        if (nome.length() > 50) {
            JOptionPane.showMessageDialog(this, "Nome muito longo. Máximo 50 caracteres.",
                    "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean jaExiste = repository.listarPerfis().stream()
                .anyMatch(p -> p.equalsIgnoreCase(nome));
        if (jaExiste) {
            int op = JOptionPane.showConfirmDialog(this,
                    "Já existe um perfil \"" + nome + "\". Deseja carregá-lo?",
                    "Perfil existente", JOptionPane.YES_NO_OPTION);
            if (op != JOptionPane.YES_OPTION) { campoNome.selectAll(); return; }
        }
        usuarioService.inicializar(nome);
        abrirDashboard();
    }

    private void abrirDashboard() {
        new DashboardView(usuarioService, new APIdeIMDB()).setVisible(true);
        dispose();
    }

    private void mostrarCard(String card) {
        ((CardLayout) painelCards.getLayout()).show(painelCards, card);
        if (CARD_NOVO.equals(card)) { campoNome.setText(""); campoNome.requestFocusInWindow(); }
    }

    private JButton criarBotaoPrimario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(AppTheme.FONT_HEADING);
        btn.setBackground(AppTheme.BTN_PRIMARY);
        btn.setForeground(AppTheme.BTN_PRIMARY_FG);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton criarBotaoSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(AppTheme.FONT_HEADING);
        btn.setBackground(AppTheme.BTN_SECONDARY);
        btn.setForeground(AppTheme.BTN_SECONDARY_FG);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER, 1),
                new EmptyBorder(9, 18, 9, 18)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private static class PerfilListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            JLabel lbl = (JLabel) super.getListCellRendererComponent(
                    list, "👤  " + value, index, isSelected, cellHasFocus);
            lbl.setBorder(new EmptyBorder(5, 12, 5, 12));
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            if (!isSelected) {
                lbl.setBackground(index % 2 == 0 ? AppTheme.BG_ROW_EVEN : AppTheme.BG_ROW_ODD);
                lbl.setForeground(AppTheme.FG_PRIMARY);
            } else {
                lbl.setBackground(AppTheme.TEAL);
                lbl.setForeground(AppTheme.OFFWHITE);
            }
            return lbl;
        }
    }
}
