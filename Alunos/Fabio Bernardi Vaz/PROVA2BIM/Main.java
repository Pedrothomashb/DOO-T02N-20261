import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import controller.AppController;
import view.MainFrame;
import view.dialogs.ConfigUsuarioDialog;

public class Main {

    public static void main(String[] args) {
        

        SwingUtilities.invokeLater(() -> {
            try {
                AppController controller = new AppController();
                boolean dadosExistem = controller.inicializar();

                if (!dadosExistem) {
                    ConfigUsuarioDialog dialog = new ConfigUsuarioDialog(null, null, true);
                    dialog.setVisible(true);

                    if (!dialog.isConfirmado()) {
                        System.exit(0);
                        return;
                    }

                    controller.configurarNovoUsuario(dialog.getNome());
                }

                MainFrame frame = new MainFrame(controller);
                frame.setVisible(true);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Erro inesperado ao iniciar o aplicativo:\n" + e.getMessage(),
                        "Erro Fatal", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}
