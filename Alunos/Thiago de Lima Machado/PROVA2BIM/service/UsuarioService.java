package com.ImdbTLM.service;

import com.ImdbTLM.model.Serie;
import com.ImdbTLM.model.Usuario;
import com.ImdbTLM.repository.UsuarioRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço de negócio para operações sobre o {@link Usuario} e suas listas.
 *
 * <p>Esta camada isola as regras de negócio da View, garantindo que:</p>
 * <ul>
 *   <li>Uma série não seja duplicada dentro de uma mesma lista</li>
 *   <li>Uma série possa existir em apenas uma lista por vez (comportamento configurável)</li>
 *   <li>A persistência seja disparada automaticamente após cada mutação</li>
 * </ul>
 */
public class UsuarioService {

    /**
     * Enum tipado representando as listas do usuário.
     * Usar enum ao invés de strings evita erros de digitação na View.
     */
    public enum Lista {
        FAVORITOS("Favoritos"),
        JA_ASSISTIDAS("Já Assistidas"),
        DESEJA_ASSISTIR("Deseja Assistir");

        private final String label;
        Lista(String label) { this.label = label; }

        @Override
        public String toString() { return label; }
    }

    private Usuario usuario;
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    // =========================================================
    // Inicialização
    // =========================================================

    /**
     * Carrega o usuário persistido com o nome fornecido, ou cria um novo perfil.
     */
    public Usuario inicializar(String nome) {
        this.usuario = repository.carregar(nome);
        if (this.usuario == null) {
            this.usuario = new Usuario(nome);
        }
        return this.usuario;
    }

    public Usuario getUsuario() { return usuario; }

    public void setNomeUsuario(String nome) {
        usuario.setNome(nome);
        salvarSilencioso();
    }

    // =========================================================
    // Operações de lista
    // =========================================================

    /**
     * Adiciona uma série à lista especificada.
     * Remove automaticamente de outras listas para evitar inconsistência.
     *
     * @param serie série a adicionar
     * @param lista lista destino
     * @throws IllegalStateException se a série já estiver na lista destino
     */
    public void adicionarSerie(Serie serie, Lista lista) {
        List<Serie> listaDestino = resolverLista(lista);

        if (listaDestino.contains(serie)) {
            throw new IllegalStateException(
                    "\"" + serie.getName() + "\" já está em " + lista + "."
            );
        }

        // Remove de outras listas (regra de negócio: série em uma lista por vez)
        removerDeOutrasListas(serie, lista);

        listaDestino.add(serie);
        salvarSilencioso();
    }

    /**
     * Remove uma série da lista especificada.
     *
     * @param serie série a remover
     * @param lista lista de origem
     * @throws IllegalStateException se a série não estiver na lista
     */
    public void removerSerie(Serie serie, Lista lista) {
        List<Serie> listaOrigem = resolverLista(lista);

        if (!listaOrigem.remove(serie)) {
            throw new IllegalStateException(
                    "\"" + serie.getName() + "\" não encontrada em " + lista + "."
            );
        }

        salvarSilencioso();
    }

    /**
     * Retorna uma cópia da lista, opcionalmente ordenada por um comparador.
     * Retorna cópia para evitar que a View modifique a lista original diretamente.
     */
    public List<Serie> getLista(Lista lista) {
        return new ArrayList<>(resolverLista(lista));
    }

    /**
     * Retorna uma cópia da lista ordenada pelo critério informado.
     */
    public List<Serie> getListaOrdenada(Lista lista, SerieComparators.Criterio criterio) {
        List<Serie> copia = getLista(lista);
        copia.sort(SerieComparators.porCriterio(criterio));
        return copia;
    }

    /**
     * Verifica em qual lista uma série se encontra, ou null se não estiver em nenhuma.
     */
    public Lista verificarListaDeSerie(Serie serie) {
        if (usuario.getFavoritos().contains(serie)) return Lista.FAVORITOS;
        if (usuario.getJaAssistidas().contains(serie)) return Lista.JA_ASSISTIDAS;
        if (usuario.getDesejaAssistir().contains(serie)) return Lista.DESEJA_ASSISTIR;
        return null;
    }

    // =========================================================
    // Persistência
    // =========================================================

    /**
     * Persiste os dados do usuário. Lança IOException para tratamento pela View.
     */
    public void salvar() throws IOException {
        repository.salvar(usuario);
    }

    /**
     * Persiste silenciosamente (sem propagar exceção).
     * Adequado para operações CRUD onde falha de persistência não deve
     * reverter a operação em memória, mas deve ser logada.
     */
    private void salvarSilencioso() {
        try {
            repository.salvar(usuario);
        } catch (IOException e) {
            System.err.println("[ERROR] Falha ao salvar automaticamente: " + e.getMessage());
        }
    }

    // =========================================================
    // Helpers privados
    // =========================================================

    private List<Serie> resolverLista(Lista lista) {
        switch (lista) {
            case FAVORITOS:      return usuario.getFavoritos();
            case JA_ASSISTIDAS:  return usuario.getJaAssistidas();
            case DESEJA_ASSISTIR:return usuario.getDesejaAssistir();
            default:             return usuario.getFavoritos();
        }
    }

    private void removerDeOutrasListas(Serie serie, Lista listaDestino) {
        for (Lista l : Lista.values()) {
            if (l != listaDestino) {
                resolverLista(l).remove(serie);
            }
        }
    }

    public UsuarioRepository getRepository() {
        return repository;
    }
}
