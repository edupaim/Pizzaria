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
        Connection con = ConexaoUtil.abrirConexao("Login");
        String sql = "select * from user ";
        sql += "where login = ? ";
        sql += "and senha = ? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, login);
            ps.setString(2, senha);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new UserDTO();
                user.setId(rs.getInt(1));
                user.setLogin(rs.getString(2));
                user.setNome(rs.getString(5));
                user.setSenha(rs.getString(3));
                user.setTipo(rs.getInt(4));
            }
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return user;
    }

    @Override
    public void inserir(UserDTO user) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("Inserir");
        String sql = "insert into user(login, senha, tipo, nome) values(?,?,?,?) ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getSenha());
            ps.setInt(3, user.getTipo());
            ps.setString(4, user.getNome());
            ps.execute();
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
    }

    @Override
    public void atualizar(Integer id, UserDTO user) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("Atualizar");
        String sql = "update user set login = ?, ";
        sql += "tipo = ?, ";
        sql += "nome = ? ";
        sql += "where id_user = ? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getLogin());
            ps.setInt(2, user.getTipo());
            ps.setString(3, user.getNome());
            ps.setInt(4, id);
            ps.execute();
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
    }

    @Override
    public void deletar(Integer id) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("Deletar");
        String sql = "delete from user where id_user = ? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
    }

    @Override
    public List<UserDTO> listarTodos() throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("ListarTodos");
        List<UserDTO> lista = new ArrayList<>();
        String sql = "select * from user ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UserDTO user = new UserDTO();
                user.setId(rs.getInt(1));
                user.setLogin(rs.getString(2));
                user.setNome(rs.getString(5));
                user.setSenha(rs.getString(3));
                user.setTipo(rs.getInt(4));
                lista.add(user);
            }
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return lista;
    }

    @Override
    public UserDTO buscarPorId(Integer id) throws PersistenciaException {
        UserDTO user = null;
        Connection con = ConexaoUtil.abrirConexao("BuscarId");
        String sql = "select * from user where id_user = ? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new UserDTO();
                user.setId(rs.getInt(1));
                user.setLogin(rs.getString(2));
                user.setNome(rs.getString(5));
                user.setSenha(rs.getString(3));
                user.setTipo(rs.getInt(4));
            }
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return user;
    }

    public Integer buscarPorLogin(String login) throws PersistenciaException {
        Integer resul = null;
        Connection con = ConexaoUtil.abrirConexao("BuscaLogin");
        String sql = "select * from user where login = ? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resul = rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return resul;
    }

    public boolean alterarSenha(UserDTO user) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("AlterarSenha");
        String sql = "update user set senha = ? ";
        sql += "where id_user = ? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getSenha());
            ps.setInt(2, user.getId());
            if (ps.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return false;
    }

    public List<UserDTO> listaFiltro(UserDTO user) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("ListaFiltro");
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
        if (user.getNome() != null) {
            if (ultimo) {
                sql += "and ";
            } else {
                sql += "where ";
            }
            sql += "nome like ? ";
        }
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            if (user.getId() != null) {
                ps.setInt(++cont, user.getId());
            }
            if (user.getLogin() != null) {
                ps.setString(++cont, "%" + user.getLogin() + "%");
            }
            if (user.getTipo() != null) {
                ps.setInt(++cont, user.getTipo());
            }
            if (user.getNome() != null) {
                ps.setString(++cont, "%" + user.getNome() + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UserDTO aux = new UserDTO();
                aux.setId(rs.getInt(1));
                aux.setLogin(rs.getString(2));
                aux.setNome(rs.getString(5));
                aux.setSenha(rs.getString(3));
                aux.setTipo(rs.getInt(4));
                lista.add(aux);
            }
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return lista;
    }
}
