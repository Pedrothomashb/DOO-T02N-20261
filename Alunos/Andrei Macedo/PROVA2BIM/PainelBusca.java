package com.example.Interface;
import com.example.exception.SeriesException;
import com.example.model.Serie;
import com.example.service.GerenciadorSeries;
import com.example.service.RequisicaoApi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;

public class PainelBusca extends JPanel {
    private GerenciadorSeries gerenciador;
    private RequisicaoApi requisicaoApi;
    private JTextField txtBusca;
    private JButton botaoBusca;
    private JTable tabelaResultado;
    private DefaultTableModel modeloTabela;
    private JButton botaoFavoritar;
    private JButton jaAssistido;
    private JButton queroAssistir;
    private List<Serie> listaResultadosBusca = new ArrayList<>();
    private JLabel labelCapa;

    public PainelBusca(GerenciadorSeries gerenciador) {
        this.gerenciador = gerenciador;
        this.requisicaoApi = new RequisicaoApi();

        setLayout(new BorderLayout());
        Font fontePadrao = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel painelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBusca = new JTextField(30);
        botaoBusca = new JButton("Buscar");

        painelSuperior.setBackground(new Color(245,245,245));
        txtBusca.setBackground(Color.WHITE);
        txtBusca.setForeground(Color.DARK_GRAY);
        botaoBusca.setBackground(new Color(0,153,76));
        botaoBusca.setForeground(Color.WHITE);
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        txtBusca.setPreferredSize(new Dimension(250, 30));
        botaoBusca.setPreferredSize(new Dimension(100, 30));
        txtBusca.setFont(fontePadrao);
        botaoBusca.setFont(fontePadrao);

        painelSuperior.add(new JLabel("<html><b>Nome da Série de TV:</b></html>"));
        painelSuperior.add(txtBusca);
        painelSuperior.add(botaoBusca);
        add(painelSuperior, BorderLayout.NORTH);
        botaoBusca.setCursor(new Cursor(Cursor.HAND_CURSOR));

        String[] colunas = {"Nome", "Emissora", "Idioma", "Nota", "Status", "Estreia", "Término"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaResultado = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaResultado);
        add(scrollTabela, BorderLayout.CENTER);
        JPanel painelDireito = new JPanel(new BorderLayout());
        painelDireito.setBorder(BorderFactory.createTitledBorder("Capa da Série"));
        painelDireito.setPreferredSize(new Dimension(220, 0));
        labelCapa = new JLabel("Selecione uma série", SwingConstants.CENTER);
        labelCapa.setFont(fontePadrao);
        painelDireito.add(labelCapa, BorderLayout.CENTER);
        add(painelDireito, BorderLayout.EAST); // Coloca fixo no lado direito
        tabelaResultado.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarCapaSelecionada();
            }
        });
        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        botaoFavoritar = new JButton("Gostei");
        botaoFavoritar.setBackground(new Color(0,153,76));
        botaoFavoritar.setForeground(Color.WHITE);
        botaoFavoritar.setPreferredSize(new Dimension(140, 35));
        botaoFavoritar.setFont(fontePadrao);
        botaoFavoritar.setEnabled(false);
        botaoFavoritar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jaAssistido = new JButton("Já Assisti");
        jaAssistido.setBackground(new Color(0,153,76));
        jaAssistido.setForeground(Color.WHITE);
        jaAssistido.setEnabled(false);
        jaAssistido.setFont(fontePadrao);
        jaAssistido.setPreferredSize(new Dimension(140, 35));
        jaAssistido.setCursor(new Cursor(Cursor.HAND_CURSOR));
        queroAssistir = new JButton("Quero Assistir");
        queroAssistir.setPreferredSize(new Dimension(140, 35));
        queroAssistir.setBackground(new Color(0,153,76));
        queroAssistir.setForeground(Color.WHITE);
        queroAssistir.setEnabled(false);
        queroAssistir.setFont(fontePadrao);
        queroAssistir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        painelInferior.add(botaoFavoritar);
        painelInferior.add(jaAssistido);
        painelInferior.add(queroAssistir);
        add(painelInferior, BorderLayout.SOUTH);
        botaoBusca.addActionListener(e -> executarBusca());
        botaoFavoritar.addActionListener(e -> adicionarLista("favoritas"));
        jaAssistido.addActionListener(e -> adicionarLista("assistidas"));
        queroAssistir.addActionListener(e -> adicionarLista("assistir"));
        txtBusca.addActionListener(e -> executarBusca());
    }

    private void executarBusca() {
        String termoBusca = txtBusca.getText().trim();
        if (termoBusca.isBlank()) {
            JOptionPane.showMessageDialog(this, "Digite um termo válido de busca", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            botaoBusca.setEnabled(false);
            modeloTabela.setRowCount(0);
            labelCapa.setIcon(null);
            labelCapa.setText("Selecione uma série");
            botaoAtivo(false);

            listaResultadosBusca = requisicaoApi.buscarSerie(termoBusca);

           for (Serie serieAtual : listaResultadosBusca) {
                Object[] linha = {
                        serieAtual.getName(),
                        serieAtual.getNomeEmissora(),
                        serieAtual.getLanguage(),
                        serieAtual.getNotaFormatada(),
                        serieAtual.getStatus(),
                        serieAtual.getPremiered(),
                        serieAtual.getEnded()
                };
                modeloTabela.addRow(linha);
            }

            botaoAtivo(true);
            botaoBusca.setEnabled(true);

        } catch (SeriesException e) {
            botaoBusca.setEnabled(true);
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro na Busca",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarLista(String status) {

        int linhaSelecionada = tabelaResultado.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma série antes de adicionar!", 
            "Aviso", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Serie serieAtual = listaResultadosBusca.get(linhaSelecionada);

    
        if (serieAtual == null){
            return;
        }

        try {
            String msg = "";
            var usuario = gerenciador.getUsuarioAtual();

            switch (status) {
                case "favoritas":
                    usuario.getFavoritas().add(serieAtual);
                    msg = "Adicionada aos favoritos";
                    break;
                case "assistidas":
                    usuario.getAssistidas().add(serieAtual);
                    msg = "marcada como Já Assistida!";
                    break;
                case "assistir":
                    usuario.getDesejadas().add(serieAtual);
                    msg = "adicionada à lista Quero Assistir!";
                    break;
            }

            gerenciador.salvarDados();

            JOptionPane.showMessageDialog(this,
                    "\"" + serieAtual.getName() + "\" " + msg,
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (SeriesException e) {

            JOptionPane.showMessageDialog(this, "Erro ao salvar lista" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void botaoAtivo(boolean ativo) {
        botaoFavoritar.setEnabled(ativo);
        jaAssistido.setEnabled(ativo);
        queroAssistir.setEnabled(ativo);
    }

    private void carregarCapaSelecionada() {
        int linhaSelecionada = tabelaResultado.getSelectedRow();

        if (linhaSelecionada == -1) {
            labelCapa.setIcon(null);
            labelCapa.setText("Selecione uma série");
            return;
        }

        Serie serieSelecionada = listaResultadosBusca.get(linhaSelecionada);

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
