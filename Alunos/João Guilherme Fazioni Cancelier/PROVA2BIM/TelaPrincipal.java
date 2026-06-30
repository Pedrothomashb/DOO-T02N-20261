import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Interface gráfica principal do sistema em Java Swing.
 */
public class TelaPrincipal extends JFrame {
    private JScrollPane scrollTabela;
    private final Usuario usuarioLogado;
    private final BuscaSerie buscaSerieAPI;

    private List<Serie> listaResultadosBusca;
    private List<Serie> listaExibicaoAtual;

    private JTextField txtPesquisa;
    private JTable tabelaSeries;
    private DefaultTableModel modeloTabela;
    private JLabel lblStatusUsuario;
    private JTabbedPane abasNavegacao;

    private JButton btnAddFavoritos;
    private JButton btnAddAssistidos;
    private JButton btnAddQueroAssistir;
    private JButton btnRemoverItem;

    public TelaPrincipal(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("O usuário logado não pode ser nulo.");
        }
        this.usuarioLogado = usuario;
        this.buscaSerieAPI = new BuscaSerie();
        this.listaResultadosBusca = new ArrayList<>();
        this.listaExibicaoAtual = this.listaResultadosBusca;

        configurarJanela();
        inicializarComponentes();
        configurarEventosJanela();
    }

    private void configurarJanela() {
        setTitle("Biblioseres - Gerenciador de Séries");
        setSize(950, 650);
        setMinimumSize(new Dimension(850, 550));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(215, 235, 241));
        try {
            java.net.URL urlIcone = getClass().getResource("/icone.png");
            if (urlIcone != null) {
                setIconImage(Toolkit.getDefaultToolkit().getImage(urlIcone));
            }
        } catch (Exception e) {
            System.out.println("Aviso: Ícone não encontrado, usando o padrão do sistema.");
        }
    }

    private void inicializarComponentes() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuSessao = new JMenu("Sessão");
        JMenuItem itemLogout = new JMenuItem("Deslogar da Conta");
        itemLogout.addActionListener(e -> executarLogout());
        menuSessao.add(itemLogout);
        menuBar.add(menuSessao);
        setJMenuBar(menuBar);

        Color azulTexto = new Color(43, 87, 154);
        Color azulGeloFundo = new Color(225, 235, 245);

        JPanel painelSuperior = new JPanel(new BorderLayout(5, 5));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        painelSuperior.setBackground(azulGeloFundo);

        lblStatusUsuario = new JLabel("Bem vindo ao sistema usuário: " + usuarioLogado.getNome());
        lblStatusUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblStatusUsuario.setForeground(azulTexto);
        painelSuperior.add(lblStatusUsuario, BorderLayout.NORTH);

        JPanel painelBuscaInput = new JPanel(new BorderLayout(8, 5));
        painelBuscaInput.setOpaque(false);
        txtPesquisa = new JTextField();
        txtPesquisa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton btnBuscar = new JButton("Buscar");
        
        painelBuscaInput.add(txtPesquisa, BorderLayout.CENTER);
        painelBuscaInput.add(btnBuscar, BorderLayout.EAST);
        painelSuperior.add(painelBuscaInput, BorderLayout.SOUTH);

        add(painelSuperior, BorderLayout.NORTH);

        JPanel p1 = new JPanel(); p1.setBackground(azulGeloFundo);
        JPanel p2 = new JPanel(); p2.setBackground(azulGeloFundo);
        JPanel p3 = new JPanel(); p3.setBackground(azulGeloFundo);
        JPanel p4 = new JPanel(); p4.setBackground(azulGeloFundo);

        abasNavegacao = new JTabbedPane();
        abasNavegacao.addTab("Resultados", p1);
        abasNavegacao.addTab("Favoritos (" + usuarioLogado.getFavoritos().size() + ")", p2);
        abasNavegacao.addTab("Já Assistidas (" + usuarioLogado.getAssistidas().size() + ")", p3);
        abasNavegacao.addTab("Quero Assistir (" + usuarioLogado.getQueroAssistir().size() + ")", p4);
        abasNavegacao.addChangeListener(e -> alternarAbasExibicao());

        String[] colunas = {"Nome da Série", "Nota Geral", "Gênero", "Idioma", "Estado", "Emissora", "Estreia / Término"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaSeries = new JTable(modeloTabela);
        tabelaSeries.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollTabela = new JScrollPane(tabelaSeries);

        tabelaSeries.setBackground(azulGeloFundo);
        if (scrollTabela != null) {
            scrollTabela.getViewport().setBackground(azulGeloFundo);
        }

        tabelaSeries.getTableHeader().setBackground(azulGeloFundo);
        tabelaSeries.getTableHeader().setOpaque(false);
        tabelaSeries.getTableHeader().setBorder(BorderFactory.createEmptyBorder());

        scrollTabela.setBorder(BorderFactory.createEmptyBorder());
        scrollTabela.setViewportBorder(BorderFactory.createEmptyBorder());

        JPanel painelCentralUnificado = new JPanel(new BorderLayout());
        painelCentralUnificado.setBackground(azulGeloFundo);
        painelCentralUnificado.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        painelCentralUnificado.add(abasNavegacao, BorderLayout.NORTH);
        painelCentralUnificado.add(scrollTabela, BorderLayout.CENTER);
        add(painelCentralUnificado, BorderLayout.CENTER);

        JPanel painelInferior = new JPanel(new BorderLayout(5, 5));
        painelInferior.setBackground(azulGeloFundo);

        JPanel painelOrdenacao = new JPanel(new BorderLayout(0, 5)); 
        painelOrdenacao.setBackground(azulGeloFundo);

        JPanel linhaSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        linhaSuperior.setBackground(azulGeloFundo);
        
        JLabel lblOrdenarSUpText = new JLabel("Ordenar Lista:");
        lblOrdenarSUpText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblOrdenarSUpText.setForeground(azulTexto);

        JButton btnOrdenarNome = new JButton("Por Nome");
        JButton btnOrdenarNota = new JButton("Melhores Notas");
        JButton btnOrdenarEstreia = new JButton("Por Estreia");
        
        linhaSuperior.add(lblOrdenarSUpText);
        linhaSuperior.add(btnOrdenarNome);
        linhaSuperior.add(btnOrdenarNota);
        linhaSuperior.add(btnOrdenarEstreia);

        JPanel linhaInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        linhaInferior.setBackground(azulGeloFundo);
        
        JLabel lblOrdenarInfText = new JLabel("Por Estado:"); 
        lblOrdenarInfText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblOrdenarInfText.setForeground(azulTexto);

        JButton btnEstadoTransmitindo = new JButton("Em andamento");
        JButton btnEstadoConcluido = new JButton("Concluídas");
        JButton btnEstadoCancelada = new JButton("Canceladas");

        linhaInferior.add(lblOrdenarInfText);
        linhaInferior.add(btnEstadoTransmitindo);
        linhaInferior.add(btnEstadoConcluido);
        linhaInferior.add(btnEstadoCancelada);

        painelOrdenacao.add(linhaSuperior, BorderLayout.NORTH);
        painelOrdenacao.add(linhaInferior, BorderLayout.SOUTH);

        JPanel painelListasAcao = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelListasAcao.setBackground(azulGeloFundo);
        btnAddFavoritos = new JButton("+ Favoritos");
        btnAddAssistidos = new JButton("+ Já Assistidas");
        btnAddQueroAssistir = new JButton("+ Quero Assistir");
        btnRemoverItem = new JButton("❌ Remover da Lista");
        
        painelListasAcao.add(btnAddFavoritos);
        painelListasAcao.add(btnAddAssistidos);
        painelListasAcao.add(btnAddQueroAssistir);
        painelListasAcao.add(btnRemoverItem);

        painelInferior.add(painelOrdenacao, BorderLayout.NORTH);
        painelInferior.add(painelListasAcao, BorderLayout.SOUTH);
        add(painelInferior, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> executarBuscaAPI());
        txtPesquisa.addActionListener(e -> executarBuscaAPI());

        btnOrdenarNome.addActionListener(e -> ordenarDados(Serie.COMPARADOR_NOME));
        btnOrdenarNota.addActionListener(e -> ordenarDados(Serie.COMPARADOR_NOTA));
        btnOrdenarEstreia.addActionListener(e -> ordenarDados(Serie.COMPARADOR_ESTREIA));

        btnEstadoTransmitindo.addActionListener(e -> ordenarDados(Serie.COMPARADOR_ESTADO_TRANSMITIDO));

        btnEstadoConcluido.addActionListener(e -> ordenarDados(Serie.COMPARADOR_ESTADO_CONCLUIDO));

        btnEstadoCancelada.addActionListener(e -> ordenarDados(Serie.COMPARADOR_ESTADO_CANCELADA));

        btnAddFavoritos.addActionListener(e -> gerenciarInclusaoLista(usuarioLogado.getFavoritos(), "Favoritos"));
        btnAddAssistidos.addActionListener(e -> gerenciarInclusaoLista(usuarioLogado.getAssistidas(), "Já Assistidas"));
        btnAddQueroAssistir.addActionListener(e -> gerenciarInclusaoLista(usuarioLogado.getQueroAssistir(), "Quero Assistir"));
        btnRemoverItem.addActionListener(e -> executarRemocaoLista());

        // CONFIGURAÇÃO VISUAL DINÂMICA (HOVER)
        aplicarEfeitoDinamicoBotao(btnBuscar);
        aplicarEfeitoDinamicoBotao(btnOrdenarNome);
        aplicarEfeitoDinamicoBotao(btnOrdenarNota);
        aplicarEfeitoDinamicoBotao(btnEstadoTransmitindo);
        aplicarEfeitoDinamicoBotao(btnEstadoConcluido);
        aplicarEfeitoDinamicoBotao(btnEstadoCancelada);
        aplicarEfeitoDinamicoBotao(btnOrdenarEstreia);
        aplicarEfeitoDinamicoBotao(btnAddFavoritos);
        aplicarEfeitoDinamicoBotao(btnAddAssistidos);
        aplicarEfeitoDinamicoBotao(btnAddQueroAssistir);

        // CONFIGURAÇÃO VISUAL ESPECIAL PARA O BOTÃO DE REMOÇÃO (HOVER)
        btnRemoverItem.setContentAreaFilled(false);
        btnRemoverItem.setOpaque(true);
        btnRemoverItem.setBackground(Color.WHITE);
        btnRemoverItem.setForeground(Color.RED);
        btnRemoverItem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRemoverItem.setFocusPainted(false);
        btnRemoverItem.setVisible(false);
        btnRemoverItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRemoverItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRemoverItem.setBackground(new Color(255, 230, 230));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRemoverItem.setBackground(Color.WHITE);
            }
        });
    }

    private void configurarEventosJanela() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    System.out.println("Janela fechando... Forçando sincronização final de segurança.");
                    sincronizarPersistenciaLocal();
                } catch (Exception ex) {
                    System.out.println("Aviso no fechamento: " + ex.getMessage());
                } finally {
                    System.exit(0); 
                }
            }
        });
    }


    private void executarBuscaAPI() {
        String termo = txtPesquisa.getText();
        if (termo == null || termo.strip().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, digite o nome de uma série.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            abasNavegacao.setSelectedIndex(0);
            this.listaResultadosBusca = buscaSerieAPI.pesquisarSeries(termo);
            this.listaExibicaoAtual = this.listaResultadosBusca;
            atualizarTabelaVisual();

            if (listaResultadosBusca.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma série correspondente encontrada.", "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro de Conexão com TVMaze: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alternarAbasExibicao() {
        int indiceAba = abasNavegacao.getSelectedIndex();

        if (indiceAba == 0) {
            listaExibicaoAtual = listaResultadosBusca;
            btnRemoverItem.setVisible(false);
            btnAddFavoritos.setVisible(true);
            btnAddAssistidos.setVisible(true);
            btnAddQueroAssistir.setVisible(true);
        } else {
            btnRemoverItem.setVisible(true);
            
            btnAddFavoritos.setVisible(false);
            btnAddAssistidos.setVisible(false);
            btnAddQueroAssistir.setVisible(false);

            if (indiceAba == 1) listaExibicaoAtual = usuarioLogado.getFavoritos();
            else if (indiceAba == 2) listaExibicaoAtual = usuarioLogado.getAssistidas();
            else if (indiceAba == 3) listaExibicaoAtual = usuarioLogado.getQueroAssistir();
        }
        
        atualizarTabelaVisual();
    }



    private void atualizarTabelaVisual() {
        modeloTabela.setRowCount(0); 
        
        Color azulGeloFundo = new Color(225, 235, 245);

        if (listaExibicaoAtual == null || listaExibicaoAtual.isEmpty()) {
            tabelaSeries.setBackground(azulGeloFundo);
            if (scrollTabela != null) {
                scrollTabela.getViewport().setBackground(azulGeloFundo);
            }
        } else {
            tabelaSeries.setBackground(Color.WHITE);
            if (scrollTabela != null) {
                scrollTabela.getViewport().setBackground(Color.WHITE);
            }

            for (Serie serie : listaExibicaoAtual) {

                String estreiaOriginal = serie.getDataEstreia();
                String terminoOriginal = serie.getDataTermino();
                
                String estreiaBR = formatarDataParaExibicao(estreiaOriginal);
                String terminoBR = formatarDataParaExibicao(terminoOriginal);

                String estreiaExibicao = estreiaBR != null ? estreiaBR : "Início N/A";
                String terminoExibicao = terminoBR != null ? terminoBR : "Até o momento";
                
                Object[] linha = {
                    serie.getNome(),
                    serie.getNotaGeral() == 0.0 ? "N/A" : serie.getNotaGeral(),
                    serie.getGenero() == null || serie.getGenero().isEmpty() ? "N/A" : String.join(", ", serie.getGenero()),
                    serie.getIdioma(),
                    serie.getEstado(),
                    serie.getEmissora(),
                    estreiaExibicao + " / " + terminoExibicao // Junta os dois textos já em formato DD/MM/AAAA
                };
                modeloTabela.addRow(linha);
            }

        }
    
        abasNavegacao.setTitleAt(1, "Favoritos (" + usuarioLogado.getFavoritos().size() + ")");
        abasNavegacao.setTitleAt(2, "Já Assistidas (" + usuarioLogado.getAssistidas().size() + ")");
        abasNavegacao.setTitleAt(3, "Quero Assistir (" + usuarioLogado.getQueroAssistir().size() + ")");
    }

    private void ordenarDados(java.util.Comparator<Serie> comparador) {
        if (listaExibicaoAtual == null || listaExibicaoAtual.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Esta lista está vazia. Não há dados para ordenar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        listaExibicaoAtual.sort(comparador);
        atualizarTabelaVisual();
    }

    private void gerenciarInclusaoLista(List<Serie> listaDestino, String nomeLista) {
        int linha = tabelaSeries.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma série na tabela para incluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Serie serie = listaExibicaoAtual.get(linha);

        // Se a série já está na lista que o usuário clicou, apenas avisa e para
        if (listaDestino.contains(serie)) {
            JOptionPane.showMessageDialog(this, "Esta série já está incluída na lista de " + nomeLista + ".", "Item Duplicado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // --- VALIDAÇÃO E PEDIDO DE CONFIRMAÇÃO DO USUÁRIO ---
        StringBuilder perguntaRegra = new StringBuilder();
        boolean precisaConfirmar = false;

        if (nomeLista.equals("Favoritos")) {
            // Regra: Se favoritou, vai para 'Já Assistidas' e sai de 'Quero Assistir'
            if (!usuarioLogado.getAssistidas().contains(serie)) {
                perguntaRegra.append("- Adicionar automaticamente em 'Já Assistidas'\n");
                precisaConfirmar = true;
            }
            if (usuarioLogado.getQueroAssistir().contains(serie)) {
                perguntaRegra.append("- Remover de 'Quero Assistir'\n");
                precisaConfirmar = true;
            }
        } else if (nomeLista.equals("Já Assistidas")) {
            // Regra: Se já assistiu, remove de 'Quero Assistir'
            if (usuarioLogado.getQueroAssistir().contains(serie)) {
                perguntaRegra.append("- Remover da sua lista de 'Quero Assistir'\n");
                precisaConfirmar = true;
            }
        } else if (nomeLista.equals("Quero Assistir")) {
            // Regra: Se quer assistir, remove de 'Já Assistidas' e 'Favoritos'
            if (usuarioLogado.getAssistidas().contains(serie)) {
                perguntaRegra.append("- Remover da sua lista de 'Já Assistidas'\n");
                precisaConfirmar = true;
            }
            if (usuarioLogado.getFavoritos().contains(serie)) {
                perguntaRegra.append("- Remover da sua lista de 'Favoritos'\n");
                precisaConfirmar = true;
            }
        }

        if (precisaConfirmar) {
            String mensagemConfirmacao = "Para mover '" + serie.getNome() + "' para " + nomeLista + ",\no sistema precisará realizar as seguintes ações:\n\n" 
                    + perguntaRegra.toString() + "\nDeseja prosseguir com a organização?";
            
            int resposta = JOptionPane.showConfirmDialog(this, mensagemConfirmacao, "Confirmar Movimentação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            if (resposta != JOptionPane.YES_OPTION) {
                return; 
            }
        }

        // EXECUÇÃO DAS AÇÕES DE MOVIMENTAÇÃO
        StringBuilder avisoRegra = new StringBuilder();

        if (nomeLista.equals("Favoritos")) {
            listaDestino.add(serie);
            if (!usuarioLogado.getAssistidas().contains(serie)) {
                usuarioLogado.getAssistidas().add(serie);
                avisoRegra.append("- Série adicionada em 'Já Assistidas'.\n");
            }
            if (usuarioLogado.getQueroAssistir().contains(serie)) {
                usuarioLogado.getQueroAssistir().remove(serie);
                avisoRegra.append("- Série removida de 'Quero Assistir'.\n");
            }
        } else if (nomeLista.equals("Já Assistidas")) {
            listaDestino.add(serie);
            if (usuarioLogado.getQueroAssistir().contains(serie)) {
                usuarioLogado.getQueroAssistir().remove(serie);
                avisoRegra.append("- Série removida de 'Quero Assistir'.\n");
            }
        } else if (nomeLista.equals("Quero Assistir")) {
            listaDestino.add(serie);
            if (usuarioLogado.getAssistidas().contains(serie)) {
                usuarioLogado.getAssistidas().remove(serie);
                avisoRegra.append("- Série removida de 'Já Assistidas'.\n");
            }
            if (usuarioLogado.getFavoritos().contains(serie)) {
                usuarioLogado.getFavoritos().remove(serie);
                avisoRegra.append("- Série removida de 'Favoritos'.\n");
            }
        }

        sincronizarPersistenciaLocal();
        atualizarTabelaVisual();

        String mensagemSucesso = "Série incluída em " + nomeLista + " com sucesso!\n";
        if (avisoRegra.length() > 0) {
            mensagemSucesso += "\nAções aplicadas:\n" + avisoRegra.toString();
        }
        JOptionPane.showMessageDialog(this, mensagemSucesso, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void executarRemocaoLista() {
        int linha = tabelaSeries.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma série para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Serie serie = listaExibicaoAtual.get(linha);
        listaExibicaoAtual.remove(serie);
        sincronizarPersistenciaLocal();
        atualizarTabelaVisual();
        JOptionPane.showMessageDialog(this, "Série removida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void executarLogout() {
        usuarioLogado.setSessaoAtiva(false);
        sincronizarPersistenciaLocal();
        this.dispose();

        java.awt.EventQueue.invokeLater(() -> {
            new TelaLogin().setVisible(true);
        });
    }

    private void sincronizarPersistenciaLocal() {
        try {
            GuardaDados guardaDados = new GuardaDados();
            List<Usuario> todosUsuarios = guardaDados.carregarTodosUsuarios();

            for (int i = 0; i < todosUsuarios.size(); i++) {
                if (todosUsuarios.get(i).getNome().equalsIgnoreCase(usuarioLogado.getNome())) {
                    todosUsuarios.set(i, usuarioLogado);
                    break;
                }
            }

            guardaDados.salvarTodosUsuarios(todosUsuarios);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro de Salvamento local JSON: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatarDataParaExibicao(String dataCrua) {
        if (dataCrua == null || dataCrua.strip().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter inputFormato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataObjeto = LocalDate.parse(dataCrua, inputFormato);
            DateTimeFormatter outputFormato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return dataObjeto.format(outputFormato);

        } catch (Exception e) {
            return dataCrua;
        }
    }
    /**
    * Adiciona efeitos dinâmicos (Hover) com cores firmes e sem filtros esbranquiçados do sistema.
    */
    private void aplicarEfeitoDinamicoBotao(JButton botao) {
        
        Color azulOriginal = new Color(43, 87, 154);
        Color azulClaroHover = new Color(70, 120, 195);
        Color azulEscuroClick = new Color(25, 55, 100);

        botao.setContentAreaFilled(false);
        botao.setOpaque(true);

        botao.setBackground(azulOriginal);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Monitor de eventos físicos do mouse
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(azulClaroHover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(azulOriginal);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botao.setBackground(azulEscuroClick);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                if (botao.getBounds().contains(evt.getPoint())) {
                    botao.setBackground(azulClaroHover);
                } else {
                    botao.setBackground(azulOriginal);
                }
            }
        });
    }
}