package aplicativo.pizzaria.dao;

import aplicativo.pizzaria.util.ConexaoUtil;
import aplicativo.pizzaria.dto.UserDTO;
import aplicativo.pizzaria.exception.PersistenciaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements GenericoDAO<UserDTO> {

    public UserDTO logar(String login, String senha) throws PersistenciaException {
        UserDTO user = null;
        Connection con = ConexaoUtil.abrirConexao();
        String sql = "select * from user ";
        sql += "where login = ? ";
        sql += "and senha = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, login);
            ps.setString(2, senha);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new UserDTO();
                user.setId(rs.getInt(1));
                user.setLogin(rs.getString(2));
                user.setSenha(rs.getString(3));
                user.setTipo(rs.getInt(4));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return user;
    }

    @Override
    public void inserir(UserDTO userDto) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao();
        String sql = "insert into user(login, senha, tipo) values(?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, userDto.getLogin());
            ps.setString(2, userDto.getSenha());
            ps.setInt(3, userDto.getTipo());
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
    }

    @Override
    public void atualizar(Integer id, UserDTO user) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao();
        String sql = "update user set login = ?";
        sql += "set senha = ?";
        sql += "set tipo = ?";
        sql += "where id_user = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getSenha());
            ps.setInt(3, user.getTipo());
            ps.setInt(4, id);
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
    }

    @Override
    public void deletar(Integer id) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao();
        String sql = "delete from user where id_user = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
    }

    @Override
    public List<UserDTO> listarTodos() throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao();
        List<UserDTO> lista = new ArrayList<>();
        String sql = "select * from user";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UserDTO user = new UserDTO();
                user.setId(rs.getInt(1));
                user.setLogin(rs.getString(2));
                user.setSenha(rs.getString(3));
                user.setTipo(rs.getInt(4));
                lista.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return lista;
    }

    @Override
    public UserDTO buscarPorId(Integer id) throws PersistenciaException {
        UserDTO user = null;
        Connection con = ConexaoUtil.abrirConexao();
        String sql = "select * from user where id_user = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                user = new UserDTO();
                user.setId(rs.getInt(1));
                user.setLogin(rs.getString(2));
                user.setSenha(rs.getString(3));
                user.setTipo(rs.getInt(4));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PersistenciaException(ex.getMessage(), ex);
        }
        return user;
    }

    public boolean alterarSenha(UserDTO user) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao();
        String sql = "update user set senha = ?";
        sql += " where id_user = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getSenha());
            ps.setInt(2, user.getId());
            if (ps.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PersistenciaException(ex.getMessage(), ex);
        }
        return false;
    }

    public List<UserDTO> listaFiltrar(UserDTO user) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao();
        List<UserDTO> lista = new ArrayList<>();
        String sql = "select * from user ";
        boolean ultimo = false;
        int cont = 0;
        if (user.getId() != null) {
            sql += "where id_user like ? ";
            ultimo = true;
        }
        if (user.getLogin() != null) {
            if (ultimo) {
                sql += "and ";
            } else {
                sql += "where ";
                ultimo = true;
            }
            sql += "login like ? ";
        }
        if (user.getTipo() != null) {
            if (ultimo) {
                sql += "and ";
            } else {
                sql += "where ";
                ultimo = true;
            }
            sql += "tipo like ? ";
        }
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            if (user.getId() != null) {
                ps.setInt(++cont, user.getId());
            }
            if (user.getLogin() != null) {
                ps.setString(++cont, user.getLogin());
            }
            if (user.getTipo() != null) {
                ps.setString(++cont, user.getLogin());
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UserDTO aux = new UserDTO();
                aux.setId(rs.getInt(1));
                aux.setLogin(rs.getString(2));
                aux.setSenha(rs.getString(3));
                aux.setTipo(rs.getInt(4));
                lista.add(aux);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return lista;
    }
}
