package com.example.service;
import com.example.exception.SeriesException;
import com.example.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class GerenciadorSeries {

    private Usuario usuarioAtual;
    private ObjectMapper objectMapper;
    private static final String CAMINHO_ARQUIVO = "dados_usuario.json";

    public GerenciadorSeries(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void salvarDados() throws SeriesException {
        if (usuarioAtual == null || usuarioAtual.getNome().isBlank()) {
            throw new SeriesException("Usuário não encontrado!", SeriesException.TipoErro.USUARIO_NAO_ENCONTADO);
        }

        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            objectMapper.writeValue(arquivo, usuarioAtual);
        } catch (IOException e) {
            throw new SeriesException("Erro ao salvar dados.", SeriesException.TipoErro.FALHA_GRAVACAO);
        }
    }

    public Usuario carregarDados() throws SeriesException {
        File arquivo = new File(CAMINHO_ARQUIVO);

        if(!arquivo.exists()) {
            throw new SeriesException("Arquivo JSON não encontrado", SeriesException.TipoErro.USUARIO_NAO_ENCONTADO);
        }

        try {
            this.usuarioAtual = objectMapper.readValue(arquivo, Usuario.class);
            return usuarioAtual;
        } catch (IOException e) {
            throw new SeriesException("Usuário não encontrado.", SeriesException.TipoErro.USUARIO_NAO_ENCONTADO);
        }
    }

    public Usuario getUsuarioAtual() {
        return usuarioAtual;
    }

    public void setUsuarioAtual(Usuario usuarioAtual) {
        this.usuarioAtual = usuarioAtual;
    }
}
