package view.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import controller.AppController;
import model.Serie;
import view.dialogs.DetalhesSerieDialog;

public class BuscaPanel extends JPanel {

    private final AppController controller;
    private final JFrame janelaPai;

    private JTextField campoBusca;
    private JButton btnBuscar;
    private JPanel painelResultados;
    private JLabel lblStatus;

    public BuscaPanel(JFrame janelaPai, AppController controller) {
        this.janelaPai  = janelaPai;
        this.controller = controller;
        setLayout(new BorderLayout(0, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        inicializar();
    }

    private void inicializar() {
        JPanel painelTopo = new JPanel(new BorderLayout(10, 0));
        painelTopo.setBackground(Color.WHITE);

        campoBusca = new JTextField();
        campoBusca.setFont(new Font("SansSerif", Font.PLAIN, 15));
        campoBusca.setBackground(Color.WHITE);
        campoBusca.setForeground(Color.BLACK);
        campoBusca.setCaretColor(Color.BLACK);
        campoBusca.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));

        btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBuscar.setBackground(Color.BLACK);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setOpaque(true);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.setBorder(new EmptyBorder(10, 20, 10, 20));

        painelTopo.add(campoBusca, BorderLayout.CENTER);
        painelTopo.add(btnBuscar,  BorderLayout.EAST);

        lblStatus = new JLabel("Digite o nome de uma série para buscar.");
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblStatus.setForeground(Color.GRAY);

        JPanel painelNorte = new JPanel(new BorderLayout(0, 6));
        painelNorte.setBackground(Color.WHITE);
        painelNorte.add(painelTopo, BorderLayout.CENTER);
        painelNorte.add(lblStatus,  BorderLayout.SOUTH);
        add(painelNorte, BorderLayout.NORTH);

        painelResultados = new JPanel();
        painelResultados.setLayout(new BoxLayout(painelResultados, BoxLayout.Y_AXIS));
        painelResultados.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(painelResultados);
        scroll.setBorder(null);
        scroll.setBackground(Color.WHITE);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> realizarBusca());
        campoBusca.addActionListener(e -> realizarBusca());
    }

    private void realizarBusca() {
        String termo = campoBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(janelaPai,
                    "Digite o nome de uma série.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnBuscar.setEnabled(false);
        lblStatus.setText("Buscando...");
        painelResultados.removeAll();
        painelResultados.revalidate();
        painelResultados.repaint();

        new Thread(() -> {
            try {
                List<Serie> series = controller.buscarSeries(termo);
                SwingUtilities.invokeLater(() -> exibirResultados(series));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText("Erro ao buscar. Verifique sua conexão.");
                    btnBuscar.setEnabled(true);
                    JOptionPane.showMessageDialog(janelaPai,
                            "Não foi possível conectar à API.\n" + ex.getMessage(),
                            "Erro de conexão", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void exibirResultados(List<Serie> series) {
        painelResultados.removeAll();
        btnBuscar.setEnabled(true);

        if (series.isEmpty()) {
            lblStatus.setText("Nenhuma série encontrada.");
        } else {
            lblStatus.setText(series.size() + " resultado(s) encontrado(s). Clique para ver detalhes.");
            for (Serie s : series) {
                painelResultados.add(criarCardSerie(s));
                painelResultados.add(Box.createVerticalStrut(8));
            }
        }

        painelResultados.revalidate();
        painelResultados.repaint();
    }

    private JPanel criarCardSerie(Serie serie) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel esquerda = new JPanel(new GridLayout(3, 1, 0, 3));
        esquerda.setBackground(Color.WHITE);

        JLabel lblNome = new JLabel(serie.getNome());
        lblNome.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblNome.setForeground(Color.BLACK);

        JLabel lblGenero = new JLabel(serie.getGenerosFormatados() + " • " + serie.getIdioma());
        lblGenero.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblGenero.setForeground(Color.GRAY);

        JLabel lblEmissora = new JLabel(serie.getEmissora() + "  |  " + serie.getDataEstreiaFormatada());
        lblEmissora.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblEmissora.setForeground(Color.GRAY);

        esquerda.add(lblNome);
        esquerda.add(lblGenero);
        esquerda.add(lblEmissora);

        JPanel direita = new JPanel(new GridLayout(2, 1, 0, 4));
        direita.setBackground(Color.WHITE);

        JLabel lblNota = new JLabel(serie.getNotaFormatada(), SwingConstants.RIGHT);
        lblNota.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNota.setForeground(Color.BLACK);

        JLabel lblStatus = new JLabel(serie.getStatus().getDescricao(), SwingConstants.RIGHT);
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblStatus.setForeground(statusColor(serie));

        direita.add(lblNota);
        direita.add(lblStatus);

        card.add(esquerda, BorderLayout.CENTER);
        card.add(direita,  BorderLayout.EAST);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new DetalhesSerieDialog(janelaPai, serie, controller).setVisible(true);
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(245, 245, 245));
                esquerda.setBackground(new Color(245, 245, 245));
                direita.setBackground(new Color(245, 245, 245));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
                esquerda.setBackground(Color.WHITE);
                direita.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    private Color statusColor(Serie serie) {
        return switch (serie.getStatus()) {
            case RUNNING  -> new Color(0, 150, 80);
            case ENDED    -> Color.GRAY;
            case CANCELED -> new Color(180, 0, 0);
            default       -> new Color(150, 120, 0);
        };
    }
}