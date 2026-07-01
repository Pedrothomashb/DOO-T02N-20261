package view.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ConfigUsuarioDialog extends JDialog {

    private JTextField campoNome;
    private boolean confirmado = false;
    private final boolean primeiroAcesso;

    public ConfigUsuarioDialog(JFrame parent, String nomeAtual, boolean primeiroAcesso) {
        super(parent, primeiroAcesso ? "Bem-vindo ao TV Tracker" : "Editar Perfil", true);
        this.primeiroAcesso = primeiroAcesso;
        inicializar(nomeAtual);
    }

    private void inicializar(String nomeAtual) {
        setSize(380, 200);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(primeiroAcesso ? DO_NOTHING_ON_CLOSE : DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(20, 25, 20, 25));
        painel.setBackground(Color.WHITE);

        String msg = primeiroAcesso
                ? "Para começar, como podemos te chamar?"
                : "Altere seu nome ou apelido:";
        JLabel lblMsg = new JLabel(msg);
        lblMsg.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblMsg.setForeground(Color.DARK_GRAY);
        painel.add(lblMsg, BorderLayout.NORTH);

        campoNome = new JTextField(nomeAtual != null ? nomeAtual : "");
        campoNome.setFont(new Font("SansSerif", Font.PLAIN, 15));
        campoNome.setBackground(Color.WHITE);
        campoNome.setForeground(Color.BLACK);
        campoNome.setCaretColor(Color.BLACK);
        campoNome.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        painel.add(campoNome, BorderLayout.CENTER);

        JButton btnConfirmar = new JButton(primeiroAcesso ? "Entrar" : "Salvar");
        btnConfirmar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnConfirmar.setBackground(Color.BLACK);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setOpaque(true);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmar.addActionListener(e -> confirmar());
        campoNome.addActionListener(e -> confirmar());

        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        painelBotao.setBackground(Color.WHITE);
        painelBotao.add(btnConfirmar);
        painel.add(painelBotao, BorderLayout.SOUTH);

        add(painel);
    }

    private void confirmar() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, informe um nome ou apelido.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        confirmado = true;
        dispose();
    }

    public String getNome() {
        return confirmado ? campoNome.getText().trim() : null;
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}