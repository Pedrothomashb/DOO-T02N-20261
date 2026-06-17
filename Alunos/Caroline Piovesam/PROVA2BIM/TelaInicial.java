package com.caroline.prova2bim.telas;

import java.util.prefs.Preferences;
import com.caroline.prova2bim.dados.DadosUsuario;
import com.caroline.prova2bim.series.ContaUsuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaInicial extends JFrame {

    private JTextField campoApelido;
    private JButton botaoEntrar;

    private final Color COR_FUNDO = new Color(30, 30, 30); // cinza escuro
    private final Color COR_TEXTO = new Color(220, 220, 220); // cinza claro
    private final Color COR_CAMPO = new Color(45, 45, 45);// cinza escuro p campo texto
    private final Color COR_BOTAO = new Color(0, 120, 215); // azul
    private final Font FONTE_PADRAO = new Font("Arial", Font.PLAIN, 16);
    private final Font FONTE_BOTAO = new Font("Arial", Font.BOLD, 14);

    public TelaInicial() {
        setTitle("Login - Gerenciador de Séries");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COR_FUNDO);

        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));
        painelCentral.setBackground(COR_FUNDO);
        painelCentral.setBorder(new EmptyBorder(40, 50,
                40, 50));

        JLabel labelMensagem = new JLabel("Digite seu apelido:");
        labelMensagem.setForeground(COR_TEXTO);
        labelMensagem.setFont(FONTE_PADRAO);
        labelMensagem.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoApelido = new JTextField(15);
        campoApelido.setMaximumSize(new Dimension(300, 35));
        campoApelido.setBackground(COR_CAMPO);
        campoApelido.setForeground(Color.WHITE);
        campoApelido.setCaretColor(Color.WHITE);
        campoApelido.setFont(FONTE_PADRAO);
        campoApelido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                BorderFactory.createEmptyBorder(5, 10,
                        5, 10)));
        campoApelido.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoApelido.setFocusable(true);
        campoApelido.addActionListener(e -> realizarLogin());

        botaoEntrar = new JButton("Entrar");
        botaoEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoEntrar.setBackground(COR_BOTAO);
        botaoEntrar.setForeground(Color.WHITE);
        botaoEntrar.setFont(FONTE_BOTAO);
        botaoEntrar.setFocusPainted(false);
        botaoEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoEntrar.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        botaoEntrar.addActionListener(e -> realizarLogin());

        painelCentral.add(labelMensagem);
        painelCentral.add(Box.createRigidArea(new Dimension(0, 15)));
        painelCentral.add(campoApelido);
        painelCentral.add(Box.createRigidArea(new Dimension(0, 25)));
        painelCentral.add(botaoEntrar);

        add(painelCentral, BorderLayout.CENTER);
    }

    public void setApelidoUsuario(String apelido) {
        if (campoApelido != null) {
            campoApelido.setText(apelido);
        }
    }

    private void realizarLogin() {
        String apelido = campoApelido.getText().trim();

        if (apelido.isEmpty()) {
            UIManager.put("OptionPane.background", COR_FUNDO);
            UIManager.put("Panel.background", COR_FUNDO);
            UIManager.put("OptionPane.messageForeground", COR_TEXTO);
            JOptionPane.showMessageDialog(this, "O apelido não pode ser vazio!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // salva a sessão
        Preferences memoriaSessao = Preferences.userNodeForPackage(TelaInicial.class);
        memoriaSessao.put("ultimoUsuarioLogado", apelido);

        DadosUsuario dadosUsuario = new DadosUsuario();
        ContaUsuario contaCarregada = dadosUsuario.carregarDados(apelido);

        TelaPrincipal telaPrincipal = new TelaPrincipal(contaCarregada);
        telaPrincipal.setVisible(true);
        dispose();
    }
}