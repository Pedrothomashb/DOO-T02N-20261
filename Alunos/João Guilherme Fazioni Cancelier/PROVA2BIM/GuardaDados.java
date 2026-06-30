import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
* Componente responsável pela persistência de dados do sistema local.
* Gerencia a gravação e leitura da lista de usuários em formato JSON.
*/
public class GuardaDados {

    private static final String CAMINHO_ARQUIVO = "usuarios_sistema.json";
    private final ObjectMapper mapper;

    public GuardaDados() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
    * Serializa a lista de usuários e suas respectivas coleções de séries,
    * gravando o resultado de forma síncrona no armazenamento local.
    *
    * @param listaUsuarios Lista contendo todos os perfis cadastrados no sistema.
    */
    public void salvarTodosUsuarios(List<Usuario> listaUsuarios) {
        if (listaUsuarios == null) return;
        
        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            mapper.writeValue(arquivo, listaUsuarios);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados no arquivo JSON: " + e.getMessage());
        }
    }

    /**
    * Desserializa o arquivo físico JSON, reconstruindo as instâncias 
    * de objetos do tipo Usuario e suas listas associadas na memória.
    *
    * @return List de usuários carregados ou uma nova lista vazia em caso de ausência do arquivo.
    */
    public List<Usuario> carregarTodosUsuarios() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        
        try {
            return mapper.readValue(arquivo, new TypeReference<List<Usuario>>() {});
        } catch (IOException e) {
            System.err.println("Erro ao carregar dados do arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
