package aplicativo.pizzaria.main;

import aplicativo.pizzaria.dto.UserDTO;
import aplicativo.pizzaria.gui.LoginFrame;

public class Main {
    public static UserDTO usuarioLogado = null;

    public static void main(String args[]) {
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
