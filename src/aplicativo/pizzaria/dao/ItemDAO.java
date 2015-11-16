package aplicativo.pizzaria.dao;

import aplicativo.pizzaria.dto.ItemPedido;
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
public class ItemDAO implements GenericoDAO<ItemPedido> {

    @Override
    public void inserir(ItemPedido obj) throws PersistenciaException {
    }

    @Override
    public void atualizar(Integer id, ItemPedido obj) throws PersistenciaException {
    }

    @Override
    public void deletar(Integer id) throws PersistenciaException {
    }

    @Override
    public List<ItemPedido> listarTodos() throws PersistenciaException {
        List<ItemPedido> lista = new ArrayList<>();
        ItemPedido ped;
        Connection con = ConexaoUtil.abrirConexao("BuscarId");
        String sql = "select * from produto_pedido";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ped = new ItemPedido();
                ProdutoDTO produto = new ProdutoDAO().buscarPorId(rs.getInt(2));
                ped.setProduto(produto);
                ped.setQuantidade(rs.getInt(4));
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
    public ItemPedido buscarPorId(Integer id) throws PersistenciaException {
        ItemPedido ped = null;
        Connection con = ConexaoUtil.abrirConexao("BuscarId");
        String sql = "select * from produto_pedido where id = ? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ped = new ItemPedido();
                ped.setPedido(new PedidoDAO().buscarPorId(rs.getInt(2)));
                ped.setProduto(new ProdutoDAO().buscarPorId(rs.getInt(3)));
                ped.setQuantidade(rs.getInt(4));
            }
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return ped;
    }
    
    public List<ItemPedido> buscarPorPedido(Integer id) throws PersistenciaException {
        List<ItemPedido> lista = new ArrayList<>();
        ItemPedido ped;
        Connection con = ConexaoUtil.abrirConexao("BuscarId");
        String sql = "select * from produto_pedido where id_pedido = ? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ped = new ItemPedido();
                ProdutoDTO produto = new ProdutoDAO().buscarPorId(rs.getInt(2));
                ped.setProduto(produto);
                ped.setQuantidade(rs.getInt(4));
                lista.add(ped);
            }
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return lista;
    }

}
