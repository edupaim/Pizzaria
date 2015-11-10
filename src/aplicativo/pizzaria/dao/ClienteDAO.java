package aplicativo.pizzaria.dao;

import aplicativo.pizzaria.dto.ClienteDTO;
import aplicativo.pizzaria.dto.ProdutoDTO;
import aplicativo.pizzaria.exception.PersistenciaException;
import aplicativo.pizzaria.util.ConexaoUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * @author Edu
 */
public class ClienteDAO implements GenericoDAO<ClienteDTO> {

    @Override
    public void inserir(ClienteDTO cliente) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("Inserir");
        String sql = "insert into produto(nome, cpf, numero) values(?,?,?) ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.setString(3, cliente.getNumero());
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
    }

    @Override
    public void atualizar(Integer id, ClienteDTO cliente) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("Atualizar");
        String sql = "update cliente set nome = ?, ";
        sql += "cpf = ?, ";
        sql += "numero = ? ";
        sql += "where id_cliente = ? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.setString(3, cliente.getNumero());
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
        Connection con = ConexaoUtil.abrirConexao("Deletar");
        String sql = "delete from cliente where id_cliente = ? ";
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
    public List<ClienteDTO> listarTodos() throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("ListarTodos");
        List<ClienteDTO> lista = new ArrayList<>();
        String sql = "select * from cliente ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ClienteDTO cliente = new ClienteDTO();
                cliente.setId(rs.getInt(1));
                cliente.setNome(rs.getString(2));
                cliente.setCpf(rs.getString(3));
                cliente.setNumero(rs.getString(4));
                cliente.setEndereco(rs.getObject(sql, null));
                lista.add(cliente);
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
    public ClienteDTO buscarPorId(Integer id) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
