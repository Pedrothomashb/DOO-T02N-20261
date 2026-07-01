package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controller.AppController;
import model.enums.TipoLista;
import view.dialogs.ConfigUsuarioDialog;
import view.panels.BuscaPanel;
import view.panels.ListasPanel;

public class MainFrame extends JFrame {

    private final AppController controller;

    private JPanel painelConteudo;
    private JLabel lblSaudacao;
    private CardLayout cardLayout;

    private ListasPanel painelFavoritos;
    private ListasPanel painelAssistidas;
    private ListasPanel painelQuerAssistir;

    private JButton btnAtivo = null;

    public MainFrame(AppController controller) {
        this.controller = controller;
        inicializar();
    }

    private void inicializar() {
        setTitle("TV Tracker");
        setSize(950, 620);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                controller.salvar();
                dispose();
                System.exit(0);
            }
        });

        add(criarBarraTopo(),     BorderLayout.NORTH);
        add(criarMenuLateral(),   BorderLayout.WEST);
        add(criarPainelCentral(), BorderLayout.CENTER);
    }

    private JPanel criarBarraTopo() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(Color.BLACK);
        barra.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel lblLogo = new JLabel("Gerenciador de Séries");
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblLogo.setForeground(Color.WHITE);

        JPanel painelDireito = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        painelDireito.setBackground(Color.BLACK);

        lblSaudacao = new JLabel("Olá, " + controller.getNomeUsuario());
        lblSaudacao.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSaudacao.setForeground(Color.LIGHT_GRAY);

        JButton btnPerfil = new JButton("⚙ Perfil");
        btnPerfil.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnPerfil.setBackground(Color.DARK_GRAY);
        btnPerfil.setForeground(Color.WHITE);
        btnPerfil.setOpaque(true);
        btnPerfil.setFocusPainted(false);
        btnPerfil.setBorder(new EmptyBorder(6, 14, 6, 14));
        btnPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPerfil.addActionListener(e -> editarPerfil());

        painelDireito.add(lblSaudacao);
        painelDireito.add(btnPerfil);

        barra.add(lblLogo,        BorderLayout.WEST);
        barra.add(painelDireito,  BorderLayout.EAST);
        return barra;
    }

    private JPanel criarMenuLateral() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(new Color(245, 245, 245));
        menu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)),
                new EmptyBorder(15, 0, 15, 0)
        ));
        menu.setPreferredSize(new Dimension(190, 0));

        JButton btnBusca       = criarBotaoMenu("🔍  Buscar Séries",  "busca");
        JButton btnFavoritos   = criarBotaoMenu("Favoritos",           "favoritos");
        JButton btnAssistidas  = criarBotaoMenu("Já Assistidas",       "assistidas");
        JButton btnQuero       = criarBotaoMenu("Quero Assistir",      "queroAssistir");

        menu.add(Box.createVerticalStrut(5));
        menu.add(btnBusca);
        menu.add(Box.createVerticalStrut(5));
        menu.add(criarSeparadorMenu("MINHAS LISTAS"));
        menu.add(btnFavoritos);
        menu.add(Box.createVerticalStrut(5));
        menu.add(btnAssistidas);
        menu.add(Box.createVerticalStrut(5));
        menu.add(btnQuero);
        menu.add(Box.createVerticalGlue());

        ativarBotaoMenu(btnBusca);

        return menu;
    }

    private JButton criarBotaoMenu(String texto, String card) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setForeground(Color.DARK_GRAY);
        btn.setBackground(new Color(245, 245, 245));
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(8, 20, 8, 20));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn != btnAtivo) btn.setBackground(new Color(230, 230, 230));
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn != btnAtivo) btn.setBackground(new Color(245, 245, 245));
            }
        });

        btn.addActionListener(e -> {
            ativarBotaoMenu(btn);
            cardLayout.show(painelConteudo, card);
            if ("favoritos".equals(card))      painelFavoritos.carregarDados();
            if ("assistidas".equals(card))     painelAssistidas.carregarDados();
            if ("queroAssistir".equals(card))  painelQuerAssistir.carregarDados();
        });

        return btn;
    }

    private void ativarBotaoMenu(JButton btn) {
        if (btnAtivo != null) {
            btnAtivo.setBackground(new Color(245, 245, 245));
            btnAtivo.setForeground(Color.DARK_GRAY);
            btnAtivo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        }
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAtivo = btn;
    }

    private JLabel criarSeparadorMenu(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        lbl.setForeground(Color.GRAY);
        lbl.setBorder(new EmptyBorder(12, 20, 4, 20));
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return lbl;
    }

    private JPanel criarPainelCentral() {
        cardLayout      = new CardLayout();
        painelConteudo  = new JPanel(cardLayout);
        painelConteudo.setBackground(Color.WHITE);

        painelFavoritos    = new ListasPanel(this, controller, TipoLista.FAVORITOS);
        painelAssistidas   = new ListasPanel(this, controller, TipoLista.ASSISTIDAS);
        painelQuerAssistir = new ListasPanel(this, controller, TipoLista.QUERO_ASSISTIR);

        painelConteudo.add(new BuscaPanel(this, controller), "busca");
        painelConteudo.add(painelFavoritos,    "favoritos");
        painelConteudo.add(painelAssistidas,   "assistidas");
        painelConteudo.add(painelQuerAssistir, "queroAssistir");

        return painelConteudo;
    }

    private void editarPerfil() {
        ConfigUsuarioDialog dialog = new ConfigUsuarioDialog(
                this, controller.getNomeUsuario(), false);
        dialog.setVisible(true);
        if (dialog.isConfirmado()) {
            controller.atualizarNomeUsuario(dialog.getNome());
            lblSaudacao.setText("Olá, " + controller.getNomeUsuario());
        }
    }
}