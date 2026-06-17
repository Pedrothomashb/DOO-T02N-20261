package com.example.Interface;
import com.example.exception.SeriesException;
import com.example.model.Usuario;
import com.example.service.GerenciadorSeries;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.swing.*;
import java.awt.*;

public class JanelaLogin extends JFrame {
    private JTextField txtNome;
    private JButton botaoLogin;
    private GerenciadorSeries gerenciadorSeries;

    public JanelaLogin() {
        this.gerenciadorSeries = new GerenciadorSeries(new ObjectMapper());

        setTitle("Login");
        setSize(400,220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3,1,10,10));


        getContentPane().setBackground(Color.WHITE);
        JLabel labelMsg = new JLabel("Digite um nome ou apelido para entrar.", SwingConstants.CENTER);
        labelMsg.setFont(new Font("Arial", Font.PLAIN, 14));
        labelMsg.setBackground(Color.WHITE);
        labelMsg.setForeground(Color.BLACK);
        labelMsg.setOpaque(true);
        add(labelMsg);

        txtNome = new JTextField();
        txtNome.setHorizontalAlignment(SwingConstants.CENTER);
        txtNome.setBackground(Color.WHITE);
        txtNome.setForeground(Color.BLACK);
        txtNome.setFont(new Font("Arial", Font.PLAIN, 14));
        add(txtNome);

        botaoLogin = new JButton("Entrar");
        botaoLogin.setFont(new Font("Arial", Font.PLAIN, 18));
        botaoLogin.setBackground(new Color(0,153,76));
        botaoLogin.setForeground(Color.WHITE);
        add(botaoLogin);

        botaoLogin.addActionListener(e -> logar());
        txtNome.addActionListener(e -> logar());
    }

    private void logar() {
        String nomeUsuario = txtNome.getText().trim();

        if (nomeUsuario.isBlank()) {
            JOptionPane.showMessageDialog(this, "Por favor, digite um nome válido!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario usuario = gerenciadorSeries.carregarDados();

            if (usuario.getNome() == null || usuario.getNome().isBlank()) {
                usuario.setNome(nomeUsuario);
                gerenciadorSeries.salvarDados();
            }

            new JanelaPrincipal(gerenciadorSeries).setVisible(true);
            this.dispose();

        } catch (SeriesException ex) {
            Usuario usuario = new Usuario(nomeUsuario);
            gerenciadorSeries.setUsuarioAtual(usuario);

            try {
                gerenciadorSeries.salvarDados();
                new JanelaPrincipal(gerenciadorSeries).setVisible(true);
                this.dispose();
            } catch (SeriesException e) {
                JOptionPane.showMessageDialog(this, "Erro de dados: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
