package com.caroline.prova2bim.dados;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.caroline.prova2bim.series.ContaUsuario;

import java.io.File;

public class DadosUsuario {

    private final ObjectMapper mapeadorJson;

    public DadosUsuario() {
        this.mapeadorJson = new ObjectMapper();
    }

    public void salvarDados(ContaUsuario conta) {
        try {
            String nomeArquivo = "conta-" + conta.getApelido().toLowerCase() + ".json";
            File arquivo = new File(nomeArquivo);

            mapeadorJson.writerWithDefaultPrettyPrinter().writeValue(arquivo, conta); // serialização
        } catch (Exception erro) {
            System.out.println("Falha ao salvar o arquivo:" + erro.getMessage());
        }
    }

    public ContaUsuario carregarDados(String apelidoDigitado) {
        try {
            String nomeArquivo = "conta-" + apelidoDigitado.toLowerCase() + ".json";
            File arquivo = new File(nomeArquivo);

            if (arquivo.exists()) {
                return mapeadorJson.readValue(arquivo, ContaUsuario.class); // desserialização
            }
        } catch (Exception erro) {
            System.out.println("Falha ao carregar o arquivo:" + erro.getMessage());
        }
        return new ContaUsuario(apelidoDigitado);
    }

}