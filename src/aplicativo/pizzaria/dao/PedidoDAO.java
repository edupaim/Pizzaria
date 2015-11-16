package aplicativo.pizzaria.dao;

import aplicativo.pizzaria.dto.PedidoDTO;
import aplicativo.pizzaria.exception.PersistenciaException;
import aplicativo.pizzaria.util.ConexaoUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*
 * @author Edu
 */
public class PedidoDAO implements GenericoDAO<PedidoDTO> {

    @Override
    public void inserir(PedidoDTO ped) throws PersistenciaException {
        int generatedkey = 0;
        Connection con = ConexaoUtil.abrirConexao("Inserir");
        String sql = "insert into pedido(data, estado, id_cliente) values(?,?,?) ";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, ped.getData());
            ps.setString(2, ped.getEstado());
            ps.setInt(3, ped.getCliente().getId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedkey = rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            try {
                for (int i = 0; i < ped.getItens().size(); i++) {
                    sql = "insert into produto_pedido(id_produto, id_pedido, quantidade) values(?,?,?) ";
                    PreparedStatement ps;
                    ps = con.prepareStatement(sql);
                    ps.setInt(1, ped.getItens().get(i).getProduto().getId());
                    ps.setInt(2, generatedkey);
                    ps.setInt(3, ped.getItens().get(i).getQuantidade());
                    ps.execute();
                }
            } catch (SQLException ex) {
                throw new PersistenciaException(ex.getMessage(), ex);
            } finally {
                ConexaoUtil.fecharConexao(con);
            }
        }
    }

    public void atualizarEstado(Integer id, String est) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("Atualizar");
        String sql = "update pedido set estado = ? ";
        sql += "where id_pedido = ? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, est);
            ps.setInt(2, id);
            ps.execute();
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
    }

    @Override
    public void deletar(Integer id) throws PersistenciaException {

    }

    @Override
    public List<PedidoDTO> listarTodos() throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("ListarTodos");
        List<PedidoDTO> lista = new ArrayList<>();
        String sql = "select * from pedido order by data desc";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PedidoDTO ped = new PedidoDTO();
                ped.setId(rs.getInt(1));
                ped.setData(rs.getString(2));
                ped.setEstado(rs.getString(3));
                lista.add(ped);
            }
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return lista;
    }
    
    public List<PedidoDTO> listarTodos(String est) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("ListarTodos");
        List<PedidoDTO> lista = new ArrayList<>();
        String sql = "select * from pedido where estado = ?  order by data desc";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, est);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PedidoDTO ped = new PedidoDTO();
                ped.setId(rs.getInt(1));
                ped.setData(rs.getString(2));
                ped.setEstado(rs.getString(3));
                lista.add(ped);
            }
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return lista;
    }

    @Override
    public PedidoDTO buscarPorId(Integer id) throws PersistenciaException {
        PedidoDTO ped = new PedidoDTO();
        Connection con = ConexaoUtil.abrirConexao("BuscarId");
        String sql = "select * from pedido where id_pedido = ? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ped.setId(rs.getInt(1));
                ped.setData(rs.getString(2));
                ped.setEstado(rs.getString(3));
                ped.setCliente(new ClienteDAO().buscarPorId(rs.getInt(4)));
            }
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return ped;
    }

    @Override
    public void atualizar(Integer id, PedidoDTO obj) throws PersistenciaException {
    }
}
