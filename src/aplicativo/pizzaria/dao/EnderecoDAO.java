package aplicativo.pizzaria.dao;

import aplicativo.pizzaria.dto.EnderecoDTO;
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
public class EnderecoDAO implements GenericoDAO<EnderecoDTO> {

    @Override
    public void inserir(EnderecoDTO endereco) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("Inserir");
        String sql = "insert into endereco(rua, bairro, cidade, estado, cliente_id) values(?,?,?,?,?) ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, endereco.getRua());
            ps.setString(2, endereco.getBairro());
            ps.setString(3, endereco.getCidade());
            ps.setString(4, endereco.getEstado());
            ps.setInt(5, endereco.getIdCliente());
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
    }

    @Override
    public void atualizar(Integer id, EnderecoDTO end) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("Atualizar");
        String sql = "update endereco set rua = ?, ";
        sql += "bairro = ?, ";
        sql += "cidade = ? ";
        sql += "estado = ? ";
        sql += "where id_endereco = ? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, end.getRua());
            ps.setString(2, end.getBairro());
            ps.setString(3, end.getCidade());
            ps.setString(4, end.getEstado());
            ps.setInt(5, id);
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
        String sql = "delete from endereco where id_endereco = ? ";
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
    public List<EnderecoDTO> listarTodos() throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("ListarTodos");
        List<EnderecoDTO> lista = new ArrayList<>();
        String sql = "select * from endereco ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EnderecoDTO end = new EnderecoDTO();
                end.setId(rs.getInt(1));
                end.setRua(rs.getString(2));
                end.setBairro(rs.getString(3));
                end.setCidade(rs.getString(4));
                end.setEstado(rs.getString(5));
                end.setIdCliente(rs.getInt(5));
                lista.add(end);
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
    public EnderecoDTO buscarPorId(Integer id) throws PersistenciaException {
        EnderecoDTO end = null;
        Connection con = ConexaoUtil.abrirConexao("BuscarId");
        String sql = "select * from cliente where id_cliente = ? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                end = new EnderecoDTO();
                end.setId(rs.getInt(1));
                end.setRua(rs.getString(2));
                end.setBairro(rs.getString(3));
                end.setCidade(rs.getString(4));
                end.setEstado(rs.getString(5));
                end.setIdCliente(rs.getInt(5));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return end;
    }

}
