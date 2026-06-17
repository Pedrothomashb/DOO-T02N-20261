package com.caroline.prova2bim.telas;

import com.caroline.prova2bim.api.ApiTvMaze;
import com.caroline.prova2bim.dados.DadosUsuario;
import com.caroline.prova2bim.series.ContaUsuario;
import com.caroline.prova2bim.series.Serie;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class TelaPrincipal extends JFrame {

    private ContaUsuario contaLogada;
    private List<Serie> listaSendoExibidaAgora;

    private final Color COR_FUNDO = new Color(30, 30, 30);
    private final Color COR_TEXTO = new Color(220, 220, 220);
    private final Color COR_CAMPO = new Color(45, 45, 45);
    private final Color COR_BOTAO = new Color(0, 120, 215);
    private final Font FONTE_PADRAO = new Font("Arial", Font.PLAIN, 14);
    private final Font FONTE_BOTAO = new Font("Arial", Font.BOLD, 12);

    private JTable tabelaSeries;
    private ModeloTabelaSeries modeloTabela;
    private JLabel labelImagemCapa;

    private JTextField campoBusca;
    private JButton btnBuscar;

    public TelaPrincipal(ContaUsuario conta) {
        this.contaLogada = conta;
        this.listaSendoExibidaAgora = new ArrayList<>();

        setTitle("Gerenciador de Séries - Usuário: " + conta.getApelido());
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(COR_FUNDO);
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10,
                10, 10));

        // pra fechar o programa todo no x
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarESair(true);
            }
        });

        JTabbedPane abasControles = new JTabbedPane();
        abasControles.setFont(FONTE_BOTAO);

        // tela 1
        // pesquisa
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        painelPesquisa.setBackground(COR_FUNDO);

        campoBusca = new JTextField(20);
        estilizarCampo(campoBusca);
        btnBuscar = criarBotao("Pesquisar", COR_BOTAO);

        campoBusca.addActionListener(e -> buscarNaApi());
        btnBuscar.addActionListener(e -> buscarNaApi());

        JComboBox<String> comboAdicionar = new JComboBox<>(
                new String[] { "Adicionar a...", "Favoritos", "Assistidas", "Deseja Assistir" });
        comboAdicionar.addActionListener(e -> {
            int idx = comboAdicionar.getSelectedIndex();
            if (idx == 1)
                adicionarSerieNaLista("FAVORITOS");
            else if (idx == 2)
                adicionarSerieNaLista("ASSISTIDAS");
            else if (idx == 3)
                adicionarSerieNaLista("DESEJA");
            comboAdicionar.setSelectedIndex(0);
        });

        painelPesquisa.add(criarLabel("Nome da Série:"));
        painelPesquisa.add(campoBusca);
        painelPesquisa.add(btnBuscar);
        painelPesquisa.add(new JSeparator(SwingConstants.VERTICAL));
        painelPesquisa.add(comboAdicionar);

        // tela 2 de minhas listas

        JPanel painelListas = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        painelListas.setBackground(COR_FUNDO);

        JComboBox<String> comboVerListas = new JComboBox<>(
                new String[] { "Ver lista...", "Favoritas", "Assistidas", "Deseja Assistir" });
        comboVerListas.addActionListener(e -> {
            int idx = comboVerListas.getSelectedIndex();
            if (idx == 1)
                exibirListaSalva(new ArrayList<>(contaLogada.getFavoritas()), "Favoritas");
            else if (idx == 2)
                exibirListaSalva(new ArrayList<>(contaLogada.getJaAssistidas()), "Assistidas");
            else if (idx == 3)
                exibirListaSalva(new ArrayList<>(contaLogada.getDesejaAssistir()), "Deseja Assistir");
            comboVerListas.setSelectedIndex(0);
        });

        JButton btnOrdNome = criarBotao("Ord: Nome", new Color(80, 80, 80));
        JButton btnOrdNota = criarBotao("Ord: Nota", new Color(80, 80, 80));
        JButton btnOrdEstado = criarBotao("Ord: Estado", new Color(80, 80, 80));
        JButton btnOrdData = criarBotao("Ord: Data", new Color(80, 80, 80));
        JButton btnRemover = criarBotao("Remover Selecionada", new Color(200, 50, 50));

        btnOrdNome.addActionListener(
                e -> ordenarLista(Comparator.comparing(s -> s.getNome() == null ? "" : s.getNome())));
        btnOrdNota.addActionListener(e -> ordenarLista(Comparator
                .comparingDouble((Serie s) -> s.getAvaliacao() == null || s.getAvaliacao().getMedia() == null ? 0.0
                        : s.getAvaliacao().getMedia())
                .reversed()));
        btnOrdEstado.addActionListener(
                e -> ordenarLista(Comparator.comparing(s -> s.getEstado() == null ? "" : s.getEstado())));
        btnOrdData.addActionListener(
                e -> ordenarLista(Comparator.comparing(s -> s.getDataEstreia() == null ? "" : s.getDataEstreia())));
        btnRemover.addActionListener(e -> removerSerieDaLista());

        painelListas.add(comboVerListas);
        painelListas.add(btnOrdNome);
        painelListas.add(btnOrdNota);
        painelListas.add(btnOrdEstado);
        painelListas.add(btnOrdData);
        painelListas.add(new JSeparator(SwingConstants.VERTICAL));
        painelListas.add(btnRemover);

        abasControles.addTab("Pesquisar Séries", painelPesquisa);
        abasControles.addTab("Gerenciar Minhas Listas", painelListas);

        add(abasControles, BorderLayout.NORTH);

        // tabela
        modeloTabela = new ModeloTabelaSeries();
        tabelaSeries = new JTable(modeloTabela);
        configurarEstiloTabela();

        JScrollPane scrollTabela = new JScrollPane(tabelaSeries);
        scrollTabela.getViewport().setBackground(COR_FUNDO);
        scrollTabela.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        add(scrollTabela, BorderLayout.CENTER);

        // pra pegar a imagem das capas
        JPanel painelImagem = new JPanel(new BorderLayout());
        painelImagem.setBackground(COR_FUNDO);
        painelImagem.setPreferredSize(new Dimension(230, 0));

        labelImagemCapa = new JLabel("Nenhuma série", SwingConstants.CENTER);
        labelImagemCapa.setForeground(COR_TEXTO);
        labelImagemCapa.setFont(FONTE_PADRAO);

        TitledBorder bordaCapa = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)),
                "Capa Oficial");
        bordaCapa.setTitleColor(COR_TEXTO);
        painelImagem.setBorder(bordaCapa);
        painelImagem.add(labelImagemCapa, BorderLayout.CENTER);
        add(painelImagem, BorderLayout.EAST);

        // btn sair
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelRodape.setBackground(COR_FUNDO);
        JButton btnSair = criarBotao("Sair / Trocar Usuário", new Color(200, 50, 50));
        btnSair.addActionListener(e -> confirmarESair(false));
        painelRodape.add(btnSair);
        add(painelRodape, BorderLayout.SOUTH);
    }

    private void buscarNaApi() {
        String nome = campoBusca.getText().trim();
        if (nome.isEmpty()) {
            exibirMensagem("Digite o nome de uma série para pesquisar!");
            return;
        }
        // tenta evitar mais requisições
        btnBuscar.setEnabled(false);
        labelImagemCapa.setIcon(null);
        labelImagemCapa.setText("Buscando...");

        new SwingWorker<List<Serie>, Void>() {
            @Override
            protected List<Serie> doInBackground() throws Exception {
                return new ApiTvMaze().buscarSeries(nome);
            }

            @Override
            protected void done() {
                try {
                    listaSendoExibidaAgora = get();
                    if (listaSendoExibidaAgora.isEmpty()) {
                        exibirMensagem("Nenhuma série encontrada.");
                    }
                    modeloTabela.setSeries(listaSendoExibidaAgora);
                } catch (Exception ex) {
                    exibirMensagem("Erro ao buscar: " + ex.getMessage());
                } finally {
                    // libera o botão novamente
                    btnBuscar.setEnabled(true);
                    labelImagemCapa.setText("Pronto");
                }
            }
        }.execute();
    }

    private void removerSerieDaLista() {
        int linha = tabelaSeries.getSelectedRow();
        if (linha == -1) {
            exibirMensagem("Selecione uma série na tabela para remover!");
            return;
        }

        Serie selecionada = modeloTabela.getSerieAt(linha);

        // remove de todas as listas
        contaLogada.getFavoritas().remove(selecionada);
        contaLogada.getJaAssistidas().remove(selecionada);
        contaLogada.getDesejaAssistir().remove(selecionada);

        // remove da visualização atual
        if (listaSendoExibidaAgora != null) {
            listaSendoExibidaAgora.remove(selecionada);
            modeloTabela.setSeries(listaSendoExibidaAgora);
        }

        exibirMensagem(selecionada.getNome() + " foi removida das suas listas!");
    }

    private void ordenarLista(Comparator<Serie> comparador) {
        if (listaSendoExibidaAgora == null || listaSendoExibidaAgora.isEmpty()) {
            exibirMensagem("A tabela está vazia. Não há séries para ordenar!");
            return;
        }
        listaSendoExibidaAgora = listaSendoExibidaAgora.stream().sorted(comparador)
                .collect(Collectors.toList());
        modeloTabela.setSeries(listaSendoExibidaAgora);
    }

    private void exibirListaSalva(List<Serie> lista, String nomeDaLista) {
        if (lista == null || lista.isEmpty()) {
            exibirMensagem("Sua lista de " + nomeDaLista + " está vazia!");
            modeloTabela.setSeries(new ArrayList<>()); // limpa a tabela
        } else {
            listaSendoExibidaAgora = new ArrayList<>(lista);
            modeloTabela.setSeries(listaSendoExibidaAgora);
        }
    }

    private void adicionarSerieNaLista(String tipoLista) {
        int linha = tabelaSeries.getSelectedRow();
        if (linha == -1) {
            exibirMensagem("Selecione uma série na tabela primeiro!");
            return;
        }
        Serie selecionada = modeloTabela.getSerieAt(linha);

        if (tipoLista.equals("FAVORITOS"))
            contaLogada.adicionarFavorita(selecionada);
        else if (tipoLista.equals("ASSISTIDAS"))
            contaLogada.adicionarJaAssistida(selecionada);
        else if (tipoLista.equals("DESEJA"))
            contaLogada.adicionarDesejaAssistir(selecionada);

        exibirMensagem(selecionada.getNome() + " adicionada com sucesso!");
    }

    private void confirmarESair(boolean fecharOProgramaTodo) {
        UIManager.put("OptionPane.background", COR_FUNDO);
        UIManager.put("Panel.background", COR_FUNDO);
        UIManager.put("OptionPane.messageForeground", COR_TEXTO);

        Object[] options = { "Sim", "Não" };
        int resposta = JOptionPane.showOptionDialog(this, "Deseja realmente sair?",
                "Confirmar Saída",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                options, options[0]);

        if (resposta == 0) {
            salvarESair(fecharOProgramaTodo);
        }
    }

    private void salvarESair(boolean fecharOProgramaTodo) {
        new DadosUsuario().salvarDados(contaLogada);

        if (fecharOProgramaTodo) {
            // fechar no x fica salvo o usuário na memória
            System.exit(0);
        } else {
            // clicar em "sair" limpa a memória para pedir o usuário de novo
            Preferences memoriaSessao = Preferences.userNodeForPackage(TelaInicial.class);
            memoriaSessao.remove("ultimoUsuarioLogado");

            TelaInicial telaLogin = new TelaInicial();
            telaLogin.setVisible(true);
            this.dispose();
        }
    }

    private void configurarEstiloTabela() {
        tabelaSeries.setBackground(COR_CAMPO);
        tabelaSeries.setForeground(COR_TEXTO);
        tabelaSeries.setGridColor(new Color(70, 70, 70)); // cinza escuro
        tabelaSeries.setFont(FONTE_PADRAO);
        tabelaSeries.setRowHeight(25);
        tabelaSeries.setSelectionBackground(new Color(0, 120, 215, 100)); // azul
        tabelaSeries.setSelectionForeground(Color.WHITE);
        tabelaSeries.getTableHeader().setBackground(new Color(20, 20, 20)); // cinza
        tabelaSeries.getTableHeader().setForeground(Color.WHITE);
        tabelaSeries.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        tabelaSeries.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaSeries.getSelectedRow() != -1) {
                baixarEExibirCapa(modeloTabela.getSerieAt(tabelaSeries.getSelectedRow()));
            }
        });
    }

    private void baixarEExibirCapa(Serie serie) {
        labelImagemCapa.setIcon(null);
        if (serie.getImagem() == null || serie.getImagem().getUrlMedia() == null) {
            labelImagemCapa.setText("Série sem capa.");
            return;
        }

        labelImagemCapa.setText("Baixando capa...");
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                URL url = new URL(serie.getImagem().getUrlMedia());
                Image imagemRedimensionada = ImageIO.read(url).getScaledInstance(210, 295,
                        Image.SCALE_SMOOTH);
                return new ImageIcon(imagemRedimensionada);
            }

            @Override
            protected void done() {
                try {
                    labelImagemCapa.setText("");
                    labelImagemCapa.setIcon(get());
                } catch (Exception e) {
                    labelImagemCapa.setText("Erro ao carregar.");
                }
            }
        }.execute();
    }

    private JButton criarBotao(String texto, Color corFundo) {
        JButton btn = new JButton(texto);
        btn.setBackground(corFundo);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONTE_BOTAO);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return btn;
    }

    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(COR_TEXTO);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        return lbl;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setBackground(COR_CAMPO);
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setFont(FONTE_PADRAO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }

    private void exibirMensagem(String msg) {
        UIManager.put("OptionPane.background", COR_FUNDO);
        UIManager.put("Panel.background", COR_FUNDO);
        UIManager.put("OptionPane.messageForeground", COR_TEXTO);
        JOptionPane.showMessageDialog(this, msg);
    }
}