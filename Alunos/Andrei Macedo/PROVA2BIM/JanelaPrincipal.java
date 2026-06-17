package com.example.Interface;

import com.example.service.GerenciadorSeries;

import javax.swing.*;
import java.awt.*;

public class JanelaPrincipal extends JFrame {

    private GerenciadorSeries gerenciador;

    public JanelaPrincipal(GerenciadorSeries gerenciadorSeries) {
        this.gerenciador = gerenciadorSeries;

        setTitle("Gerenciador de Series - Usuário: " + gerenciador.getUsuarioAtual().getNome());
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(new Font("Segoe-UI", Font.PLAIN, 20));
        abas.setBackground(Color.WHITE);
        abas.setForeground(Color.DARK_GRAY);
        PainelBusca abaBusca = new PainelBusca(gerenciador);
        PaineListas abaListas = new PaineListas(gerenciador);

        abas.addTab("Procurar Séries de TV", abaBusca);
        abas.addTab("Minhas Listas", abaListas);

        add(abas);
    }
}
