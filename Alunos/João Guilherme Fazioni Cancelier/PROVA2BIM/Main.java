import java.util.List;
import javax.swing.SwingUtilities;

/**
 * Ponto de entrada principal do sistema.
 * Gerencia o carregamento inicial da persistência JSON e decide o fluxo de telas.
 */
public class Main {
    public static void main(String[] args) {
        GuardaDados guardaDados = new GuardaDados();
        List<Usuario> todosUsuarios = guardaDados.carregarTodosUsuarios();

        Usuario usuarioComSessaoAtiva = null;
        for (Usuario u : todosUsuarios) {
            if (u.isSessaoAtiva()) {
                usuarioComSessaoAtiva = u;
                break;
            }
        }

        final Usuario usuarioLogado = usuarioComSessaoAtiva;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (usuarioLogado != null) {
                    System.out.println("Sessão ativa encontrada para: " + usuarioLogado.getNome());
                    new TelaPrincipal(usuarioLogado).setVisible(true);
                } else {
                    // SE NÃO HÁ SESSÃO: abre a tela de login normal exigindo senha
                    TelaLogin tela = new TelaLogin();
                    tela.setVisible(true);
                }
            }
        });
    }
}
