package aplicativo.pizzaria.util;

import java.sql.*;
import java.util.ResourceBundle;

public class ConexaoUtil {

    private static Connection con;
    private static ResourceBundle config;

    public static Connection abrirConexao(String s) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://localhost:3306/pizzaria";
            String user = "root";
            String senha = "root";
            con = DriverManager.getConnection(url, user, senha);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } catch (InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        System.out.println("Conexao aberta. [" + s + "]");
        return con;
    }

    public static void fecharConexao(Connection con) {
        try {
            con.close();
            System.out.println("Conexao fechada.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
