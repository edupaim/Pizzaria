/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicativo.pizzaria.util;

import aplicativo.pizzaria.gui.MainFrame;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eduardo
 */
public class HorarioUtil implements Runnable {

    private SimpleDateFormat sdf = null;

    public HorarioUtil() {
        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(HorarioUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
