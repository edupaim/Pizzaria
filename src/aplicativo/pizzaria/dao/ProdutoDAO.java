/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicativo.pizzaria.dao;

import aplicativo.pizzaria.dto.ProdutoDTO;
import aplicativo.pizzaria.exception.PersistenciaException;
import aplicativo.pizzaria.util.ConexaoUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eduardo
 */
public class ProdutoDAO implements GenericoDAO<ProdutoDTO> {

    @Override
    public void inserir(ProdutoDTO prod) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("Inserir");
        String sql = "insert into produto(nome, tamanho, descricao, valor, tipo) values(?,?,?,?,?) ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, prod.getNome());
            ps.setString(2, prod.getTamanho());
            ps.setString(3, prod.getDescricao());
            ps.setDouble(4, prod.getValor());
            ps.setString(5, prod.getTipo());
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
    }

    @Override
    public void atualizar(Integer id, ProdutoDTO prod) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("Atualizar");
        String sql = "update produto set nome = ?, ";
        sql += "tamanho = ?, ";
        sql += "descricao = ? ";
        sql += "valor = ? ";
        sql += "tipo = ? ";
        sql += "where id_produto = ? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, prod.getNome());
            ps.setString(2, prod.getTamanho());
            ps.setString(3, prod.getDescricao());
            ps.setDouble(4, prod.getValor());
            ps.setString(5, prod.getTipo());
            ps.setInt(6, id);
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
        String sql = "delete from produto where id_produto = ? ";
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
    public List<ProdutoDTO> listarTodos() throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("ListarTodos");
        List<ProdutoDTO> lista = new ArrayList<>();
        String sql = "select * from produto ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProdutoDTO prod = new ProdutoDTO();
                prod.setId(rs.getInt(1));
                prod.setNome(rs.getString(2));
                prod.setTamanho(rs.getString(3));
                prod.setDescricao(rs.getString(4));
                prod.setValor(rs.getDouble(5));
                prod.setTipo(rs.getString(6));
                lista.add(prod);
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
    public ProdutoDTO buscarPorId(Integer id) throws PersistenciaException {
        ProdutoDTO prod = null;
        Connection con = ConexaoUtil.abrirConexao("BuscarId");
        String sql = "select * from produto where id_user = ? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                prod = new ProdutoDTO();
                prod.setId(rs.getInt(1));
                prod.setNome(rs.getString(2));
                prod.setTamanho(rs.getString(3));
                prod.setDescricao(rs.getString(4));
                prod.setValor(rs.getDouble(5));
                prod.setTipo(rs.getString(6));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return prod;
    }
    
    public List<ProdutoDTO> listaFiltro(ProdutoDTO prod) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("ListaFiltro");
        List<ProdutoDTO> lista = new ArrayList<>();
        String sql = "select * from produto ";
        boolean ultimo = false;
        int cont = 0;
        if (prod.getId() != null) {
            sql += "where id_user like ? ";
            ultimo = true;
        }
        if (prod.getNome()!= null) {
            if (ultimo) {
                sql += "and ";
            } else {
                sql += "where ";
                ultimo = true;
            }
            sql += "nome like ? ";
        }
        if (prod.getTamanho()!= null) {
            if (ultimo) {
                sql += "and ";
            } else {
                sql += "where ";
                ultimo = true;
            }
            sql += "tamanho like ? ";
        }
        if (prod.getTipo()!= null) {
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
            if (prod.getId() != null) {
                ps.setInt(++cont, prod.getId());
            }
            if (prod.getNome()!= null) {
                ps.setString(++cont, "%" + prod.getNome()+ "%");
            }
            if (prod.getTamanho()!= null) {
                ps.setString(++cont, prod.getTamanho());
            }
            if (prod.getTipo()!= null) {
                ps.setString(++cont, "%" + prod.getTipo()+ "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProdutoDTO aux = new ProdutoDTO();
                aux.setId(rs.getInt(1));
                aux.setNome(rs.getString(2));
                aux.setTamanho(rs.getString(5));
                aux.setTipo(rs.getString(3));
                aux.setValor(rs.getDouble(sql));
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
