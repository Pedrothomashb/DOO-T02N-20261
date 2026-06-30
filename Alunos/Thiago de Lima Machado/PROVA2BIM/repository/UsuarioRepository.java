package com.ImdbTLM.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ImdbTLM.model.Usuario;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Responsável pela save de perfis de usuários em arquivos JSON locais.
public class UsuarioRepository {

    private static final String PREFIXO_ARQUIVO = "perfil_";
    private static final String SUFIXO_ARQUIVO  = ".json";

    private final ObjectMapper objectMapper;

    public UsuarioRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // Busca o nome de todos os perfis salvos, e mostra apenas o campo "nome" do arquivo.
    public List<String> listarPerfis() {
        List<String> nomes = new ArrayList<>();
        File dir = new File(".");
        File[] arquivos = dir.listFiles(
                (d, name) -> name.startsWith(PREFIXO_ARQUIVO) && name.endsWith(SUFIXO_ARQUIVO)
        );
        if (arquivos == null) return nomes;

        for (File f : arquivos) {
            try {
                Usuario u = objectMapper.readValue(f, Usuario.class);
                if (u.getNome() != null && !u.getNome().isBlank()) {
                    nomes.add(u.getNome());
                }
            } catch (IOException e) {
                System.err.println("Ignorando arquivo corrompido: " + f.getName());
            }
        }
        return nomes;
    }

    // Carrega o perfil do usuário com o nome informado.
    public Usuario carregar(String nome) {
        File arquivo = resolverArquivo(nome);
        if (!arquivo.exists()) {
            System.out.println("[INFO] Nenhum perfil encontrado para: " + nome);
            return null;
        }
        try {
            Usuario usuario = objectMapper.readValue(arquivo, Usuario.class);
            System.out.println("Perfil carregado: " + usuario);
            return usuario;
        } catch (IOException e) {
            System.err.println("Falha ao carregar perfil '" + nome + "': " + e.getMessage());
            return null;
        }
    }

    // Salva o perfil do usuário após encerrar.
    public void salvar(Usuario usuario) throws IOException {
        if (usuario == null || usuario.getNome() == null) {
            throw new IllegalArgumentException("Usuário ou nome não pode ser nulo.");
        }
        File arquivo = resolverArquivo(usuario.getNome());
        File arquivoTemp = new File(arquivo.getPath() + ".tmp");
        try {
            objectMapper.writeValue(arquivoTemp, usuario);
            if (arquivo.exists()) arquivo.delete();
            if (!arquivoTemp.renameTo(arquivo)) {
                objectMapper.writeValue(arquivo, usuario);
                arquivoTemp.delete();
            }
            System.out.println("Perfil salvo: " + arquivo.getName());
        } catch (IOException e) {
            arquivoTemp.delete();
            throw e;
        }
    }

    // Deleta o arquivo de perfil de um usuário específico.
    public boolean deletarPerfil(String nome) {
        return resolverArquivo(nome).delete();
    }


    // Metodo para ajustar arquivo do perfil, deixando apenas caracteres válidos
    private File resolverArquivo(String nome) {
        String nomeSanitizado = nome.trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9_\\-]", "_"); // apenas alfanumérico + _ -
        return new File(PREFIXO_ARQUIVO + nomeSanitizado + SUFIXO_ARQUIVO);
    }
}
