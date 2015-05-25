/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicativo.pizzaria.util;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author eduardo
 */
public class MensagensUtil {

    public static void addMsg(Component comp, String msg) {
        JOptionPane.showMessageDialog(comp, msg);
    }
}
