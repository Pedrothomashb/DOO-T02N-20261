package com.example;
import com.example.Interface.JanelaLogin;
import com.example.Interface.JanelaPrincipal;
import com.example.exception.SeriesException;
import com.example.model.Usuario;
import com.example.service.GerenciadorSeries;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        iniciarSistema();
    }

    public static void iniciarSistema() {
        GerenciadorSeries gerenciadorSeries = new GerenciadorSeries(new ObjectMapper());

        try {
            Usuario usuario = gerenciadorSeries.carregarDados();
            if (usuario.getNome() == null || usuario.getNome().isBlank()) {
                SwingUtilities.invokeLater(() -> {
                    new JanelaLogin().setVisible(true);
                });
            } else {
                SwingUtilities.invokeLater(() -> {
                    new JanelaPrincipal(gerenciadorSeries).setVisible(true);
                });
            }

        } catch (SeriesException e) {
            SwingUtilities.invokeLater(() -> {
                new JanelaLogin().setVisible(true);
            });
        }
    }
}