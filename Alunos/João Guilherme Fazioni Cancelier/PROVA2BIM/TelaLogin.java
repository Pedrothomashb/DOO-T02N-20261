import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
* Interface gráfica responsável pela autenticação e cadastro de usuários.
* Modificada para apresentar um visual moderno, limpo e atraente.
*/
public class TelaLogin extends JFrame {

    private JTextField txtNome;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnCadastrar;
    private GuardaDados guardaDados;
    private List<Usuario> todosUsuarios;

    public TelaLogin() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Aviso: Não foi possível definir o tema Nimbus, usando o padrão do sistema.");
        }

        this.guardaDados = new GuardaDados();
        this.todosUsuarios = guardaDados.carregarTodosUsuarios();

        setTitle("Tela de autenticação");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        try {
            java.net.URL urlIcone = getClass().getResource("/icone.png");
            if (urlIcone != null) {
                setIconImage(Toolkit.getDefaultToolkit().getImage(urlIcone));
            }
        } catch (Exception e) {
            System.out.println("Aviso: Ícone não encontrado, usando o padrão do sistema.");
        }

        JPanel painelPrincipal = new JPanel(new BorderLayout(15, 15));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painelPrincipal.setBackground(new Color(245, 245, 247));

        JLabel lblTitulo = new JLabel("BEM-VINDO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(51, 51, 51));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel painelFormulario = new JPanel(new GridLayout(2, 1, 10, 10));
        painelFormulario.setOpaque(false);

        JPanel blocoNome = new JPanel(new BorderLayout(5, 5));
        blocoNome.setOpaque(false);
        JLabel lblNome = new JLabel("Nome do Usuário");
        lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtNome = new JTextField();
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNome.setPreferredSize(new Dimension(0, 30));
        blocoNome.add(lblNome, BorderLayout.NORTH);
        blocoNome.add(txtNome, BorderLayout.CENTER);

        JPanel blocoSenha = new JPanel(new BorderLayout(5, 5));
        blocoSenha.setOpaque(false);
        JLabel lblSenha = new JLabel("Senha de Acesso");
        lblSenha.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtSenha = new JPasswordField();
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSenha.setPreferredSize(new Dimension(0, 30));
        blocoSenha.add(lblSenha, BorderLayout.NORTH);
        blocoSenha.add(txtSenha, BorderLayout.CENTER);

        painelFormulario.add(blocoNome);
        painelFormulario.add(blocoSenha);
        painelPrincipal.add(painelFormulario, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new GridLayout(1, 2, 15, 0));
        painelBotoes.setOpaque(false);

        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEntrar.setBackground(new Color(0, 122, 255));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCadastrar.setBackground(new Color(230, 230, 235)); 
        btnCadastrar.setForeground(new Color(51, 51, 51));
        btnCadastrar.setFocusPainted(false);
        btnCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnEntrar);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);

        btnEntrar.addActionListener(e -> {
            try { executarLogin(); 

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCadastrar.addActionListener(e -> {
            try { executarCadastro();
                
             } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void executarLogin() {
        String nome = txtNome.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (nome.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos antes de continuar!", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioLogado = null;
        for (Usuario u : todosUsuarios) {
            if (u.getNome().equalsIgnoreCase(nome) && u.getSenha().equals(senha)) {
                usuarioLogado = u;
                break;
            }
        }

        if (usuarioLogado != null) {
            usuarioLogado.setSessaoAtiva(true);
            guardaDados.salvarTodosUsuarios(todosUsuarios);
            JOptionPane.showMessageDialog(this, "Autenticação bem-sucedida!\nBem-vindo(a), " + usuarioLogado.getNome() + "!", "Acesso Permitido", JOptionPane.INFORMATION_MESSAGE);
            new TelaPrincipal(usuarioLogado).setVisible(true);
            this.dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Usuário não encontrado ou senha incorreta.", "Falha na Autenticação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executarCadastro() {
        String nome = txtNome.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (nome.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o Nome e a Senha que deseja cadastrar!", "Campos Vazios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (Usuario u : todosUsuarios) {
            if (u.getNome().equalsIgnoreCase(nome)) {
                JOptionPane.showMessageDialog(this, "Este nome de usuário já está cadastrado!", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Usuario novoUsuario = new Usuario(nome, senha, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        todosUsuarios.add(novoUsuario);
        guardaDados.salvarTodosUsuarios(todosUsuarios);
        JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
}