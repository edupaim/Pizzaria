package aplicativo.pizzaria.main;

import aplicativo.pizzaria.dto.UserDTO;
import aplicativo.pizzaria.gui.LoginFrame;

public class Main {

    public static UserDTO usuarioLogado = null;

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                System.out.println(info.getName());
                if ("GTK+".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    public static UserDTO getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(UserDTO usuarioLogado) {
        Main.usuarioLogado = usuarioLogado;
    }
}
