package com.example.Interface;
import com.example.model.Serie;
import com.example.service.FiltrarListas;
import com.example.service.GerenciadorSeries;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class PaineListas extends JPanel {
    private GerenciadorSeries gerenciador;
    private FiltrarListas aplicarFiltro;
    private JComboBox<String> comboOrdenacao;
    private JTable tabelaListas;
    private DefaultTableModel modeloTabela;
    private JButton botaoFavoritas;
    private JButton botaoAssistidas;
    private JButton botaoDesejadas;
    private JButton botaoRemover;
    private String listaAtualExibida = "favoritas";
    private List<Serie> listaLocalAtual = new ArrayList<>();
    private JLabel labelCapa;


    public PaineListas(GerenciadorSeries gerenciador) {
        this.gerenciador = gerenciador;
        this.aplicarFiltro = new FiltrarListas();
        Font fontePadrao = new Font("Segoe UI", Font.PLAIN, 14);

        setLayout(new BorderLayout());
        JPanel painelSuperior = new JPanel(new BorderLayout(5, 5));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botaoFavoritas = new JButton("Favoritas");
        botaoFavoritas.setPreferredSize(new Dimension(100, 30));
        botaoFavoritas.setBackground(new Color(0,153,76));
        botaoFavoritas.setForeground(Color.WHITE);
        botaoFavoritas.setEnabled(true);
        botaoFavoritas.setFont(fontePadrao);
        botaoFavoritas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoAssistidas = new JButton("Assistidas");
        botaoAssistidas.setPreferredSize(new Dimension(100, 30));
        botaoAssistidas.setBackground(new Color(0,153,76));
        botaoAssistidas.setForeground(Color.WHITE);
        botaoAssistidas.setEnabled(true);
        botaoAssistidas.setFont(fontePadrao);
        botaoAssistidas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoDesejadas = new JButton("Assistir");
        botaoDesejadas.setPreferredSize(new Dimension(100, 30));
        botaoDesejadas.setBackground(new Color(0,153,76));
        botaoDesejadas.setForeground(Color.WHITE);
        botaoDesejadas.setEnabled(true);
        botaoDesejadas.setFont(fontePadrao);
        botaoDesejadas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoRemover = new JButton("Remover");
        botaoRemover.setPreferredSize(new Dimension(100, 30));
        botaoRemover.setBackground(new Color(204, 0, 0));
        botaoRemover.setForeground(Color.WHITE);
        botaoRemover.setFont(fontePadrao);
        botaoRemover.setCursor(new Cursor(Cursor.HAND_CURSOR));

        painelBotoes.add(botaoFavoritas);
        painelBotoes.add(botaoAssistidas);
        painelBotoes.add(botaoDesejadas);
        painelBotoes.add(botaoRemover);

        JPanel painelOrdenacaoConfig = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        String[] opcoes = {"Ordem Alfabética", "Nota Geral", "Data de Estreia"};
        comboOrdenacao = new JComboBox<>(opcoes);

        painelSuperior.setBackground(new Color(245,245,245));
        comboOrdenacao.setBackground(Color.WHITE);
        comboOrdenacao.setForeground(Color.DARK_GRAY);
        comboOrdenacao.setPreferredSize(new Dimension(180, 30));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        comboOrdenacao.setFont(fontePadrao);
        comboOrdenacao.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        painelOrdenacaoConfig.add(new JLabel("<html><b>Ordenar favoritos por:</b></html>"));
        painelOrdenacaoConfig.add(comboOrdenacao);
        painelSuperior.add(painelOrdenacaoConfig, BorderLayout.NORTH);
        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);

        add(painelSuperior, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome", "Emissora", "Idioma", "Estreia", "Nota"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaListas = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaListas);
        add(scrollTabela, BorderLayout.CENTER);

        JPanel painelDireito = new JPanel(new BorderLayout());
        painelDireito.setBorder(BorderFactory.createTitledBorder("Capa da Série"));
        painelDireito.setPreferredSize(new Dimension(220, 0));

        labelCapa = new JLabel("Selecione uma série", SwingConstants.CENTER);
        labelCapa.setFont(fontePadrao);
        painelDireito.add(labelCapa, BorderLayout.CENTER);
        add(painelDireito, BorderLayout.EAST); // Encaixa no lado direito

        tabelaListas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarCapaSerieSelecionada();
            }
        });

        botaoFavoritas.addActionListener(e -> {listaAtualExibida = "favoritas"; atualizarTabela();});
        botaoAssistidas.addActionListener(e -> {listaAtualExibida = "assistidas"; atualizarTabela();});
        botaoDesejadas.addActionListener(e -> {listaAtualExibida = "desejadas"; atualizarTabela();});
        botaoRemover.addActionListener(e -> removerSerieSelecionada());
        comboOrdenacao.addActionListener(e -> atualizarTabela());

        atualizarTabela();
    }

    public void atualizarTabela() {
        modeloTabela.setRowCount(0);

        if (labelCapa != null) {
            labelCapa.setIcon(null);
            labelCapa.setText("Selecione uma série");
        }

        var usuario = gerenciador.getUsuarioAtual();
        if (usuario == null) {
            return;
        }

        List<Serie> listaFiltrada = new ArrayList<>();
        switch (listaAtualExibida) {
            case "favoritas":
                listaFiltrada = new ArrayList<>(usuario.getFavoritas());
                break;
            case "assistidas":
                listaFiltrada = new ArrayList<>(usuario.getAssistidas());
                break;
            case "desejadas":
                listaFiltrada = new ArrayList<>(usuario.getDesejadas());
                break;
        }

        String criterioSelecao = (String) comboOrdenacao.getSelectedItem();
        if (criterioSelecao != null && !listaFiltrada.isEmpty()) {
            if (criterioSelecao.equals("Ordem Alfabética")) {
                listaFiltrada = aplicarFiltro.ordenarPorNome(listaFiltrada);
            } else if (criterioSelecao.equals("Nota Geral")) {
                listaFiltrada = aplicarFiltro.ordenarPorNota(listaFiltrada);
            } else {
                listaFiltrada = aplicarFiltro.ordenarPorDataEstreia(listaFiltrada);
            }
        }

        listaLocalAtual = listaFiltrada;

        for (Serie s: listaFiltrada) {
            Object[] linha = {
                    s.getId(),
                    s.getName(),
                    s.getNomeEmissora(),
                    s.getLanguage(),
                    s.getPremiered(),
                    s.getNotaFormatada()
            };
            modeloTabela.addRow(linha);
        }
    }

    private void removerSerieSelecionada() {
        int linhaSelecionada = tabelaListas.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma série na tabela para remover!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Serie serieParaRemover = listaLocalAtual.get(linhaSelecionada);
        var usuario = gerenciador.getUsuarioAtual();

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja remover \"" + serieParaRemover.getName() + "\" desta lista?",
                "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                switch (listaAtualExibida) {
                    case "favoritas":
                        usuario.getFavoritas().remove(serieParaRemover);
                        break;
                    case "assistidas":
                        usuario.getAssistidas().remove(serieParaRemover);
                        break;
                    case "desejadas":
                        usuario.getDesejadas().remove(serieParaRemover);
                        break;
                }

                gerenciador.salvarDados();
                atualizarTabela();
                JOptionPane.showMessageDialog(this, "Série removida com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar alterações no arquivo: "
                        + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarCapaSerieSelecionada() {
        int linhaSelecionada = tabelaListas.getSelectedRow();

        if (linhaSelecionada == -1) {
            labelCapa.setIcon(null);
            labelCapa.setText("Selecione uma série");
            return;
        }

        Serie serieSelecionada = listaLocalAtual.get(linhaSelecionada);

        if (serieSelecionada.getImage() != null && serieSelecionada.getImage().getMedium() != null) {
            labelCapa.setText("Carregando capa...");
            labelCapa.setIcon(null);

            SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
                @Override
                protected ImageIcon doInBackground() throws Exception {
                    URL url = new URL(serieSelecionada.getImage().getMedium());
                    return new ImageIcon(url);
                }

                @Override
                protected void done() {
                    try {
                        ImageIcon icone = get();
                        labelCapa.setIcon(icone);
                        labelCapa.setText("");
                    } catch (Exception ex) {
                        labelCapa.setIcon(null);
                        labelCapa.setText("Erro ao carregar imagem");
                    }
                }
            };
            worker.execute();
        } else {
            labelCapa.setIcon(null);
            labelCapa.setText("Sem capa disponível");
        }
    }
}

