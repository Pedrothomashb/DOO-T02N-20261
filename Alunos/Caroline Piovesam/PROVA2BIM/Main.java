package com.caroline.prova2bim;

import com.caroline.prova2bim.telas.TelaInicial;
import com.caroline.prova2bim.telas.TelaPrincipal;
import com.caroline.prova2bim.dados.DadosUsuario;
import com.caroline.prova2bim.series.ContaUsuario;
import java.util.prefs.Preferences;

public class Main {
    public static void main(String[] args) {
        // acessa a "memória" do sistema para a inicial
        Preferences memoriaSessao = Preferences.userNodeForPackage(TelaInicial.class);
        String ultimoApelido = memoriaSessao.get("ultimoUsuarioLogado", "");

        if (!ultimoApelido.trim().isEmpty()) {
            // se tiver usuário salvo, carrega p principal
            ContaUsuario conta = new DadosUsuario().carregarDados(ultimoApelido);
            new TelaPrincipal(conta).setVisible(true);
            return;
        }

        // se não tiver ninguém, carrega p inicial
        new TelaInicial().setVisible(true);
    }
}