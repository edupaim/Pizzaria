/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicativo.pizzaria.util;

/**
 *
 * @author eduardo
 */
public class FuncoesUtil {

    public static String onlyNumbers(String str) {
        if (str != null) {
            return str.replaceAll("[^0123456789]", "");
        } else {
            return "";
        }
    }

}
