package aplicativo.pizzaria.dao;

import aplicativo.pizzaria.dto.ClienteDTO;
import aplicativo.pizzaria.dto.EnderecoDTO;
import aplicativo.pizzaria.exception.PersistenciaException;
import aplicativo.pizzaria.util.ConexaoUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements GenericoDAO<ClienteDTO> {

    @Override
    public void inserir(ClienteDTO cliente) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("Inserir");
        String sql = "insert into cliente(nome, cpf, numero, rua, bairro, cidade) values(?,?,?,?,?,?) ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.setString(3, cliente.getNumero());
            ps.setString(4, cliente.getEndereco().getRua());
            ps.setString(5, cliente.getEndereco().getBairro());
            ps.setString(6, cliente.getEndereco().getCidade());
            ps.execute();
        } catch (SQLException ex) {
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
            ps.setString(4, cliente.getEndereco().getRua());
            ps.setString(5, cliente.getEndereco().getBairro());
            ps.setString(6, cliente.getEndereco().getCidade());
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
        String sql = "delete from cliente where id_cliente = ? ";
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
                cliente.setEndereco(new EnderecoDTO(rs.getString(5), rs.getString(6), rs.getString(7)));
                lista.add(cliente);
            }
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return lista;
    }

    @Override
    public ClienteDTO buscarPorId(Integer id) throws PersistenciaException {
        ClienteDTO cliente = null;
        Connection con = ConexaoUtil.abrirConexao("BuscarId");
        String sql = "select * from cliente where id_cliente = ? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cliente = new ClienteDTO();
                cliente.setId(rs.getInt(1));
                cliente.setNome(rs.getString(2));
                cliente.setCpf(rs.getString(3));
                cliente.setNumero(rs.getString(4));
                cliente.setEndereco(new EnderecoDTO(rs.getString(5), rs.getString(6), rs.getString(7)));
            }
        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage(), ex);
        } finally {
            ConexaoUtil.fecharConexao(con);
        }
        return cliente;
    }
    
    public List<ClienteDTO> listaFiltro(ClienteDTO cliente) throws PersistenciaException {
        Connection con = ConexaoUtil.abrirConexao("ListaFiltro");
        List<ClienteDTO> lista = new ArrayList<>();
        String sql = "select * from cliente ";
        boolean ultimo = false;
        int cont = 0;
        if (cliente.getId() != null) {
            sql += "where id_cliente like ? ";
            ultimo = true;
        }
        if (cliente.getNome() != null) {
            if (ultimo) {
                sql += "and ";
            } else {
                sql += "where ";
                ultimo = true;
            }
            sql += "nome like ? ";
        }
        if (cliente.getNumero()!= null) {
            if (ultimo) {
                sql += "and ";
            } else {
                sql += "where ";
            }
            sql += "numero like ? ";
        }
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            if (cliente.getId() != null) {
                ps.setInt(++cont, cliente.getId());
            }
            if (cliente.getNome()!= null) {
                ps.setString(++cont, "%" + cliente.getNome()+ "%");
            }
            if (cliente.getNumero()!= null) {
                ps.setString(++cont, "%" + cliente.getNumero()+ "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ClienteDTO aux = new ClienteDTO();
                aux.setId(rs.getInt(1));
                aux.setNome(rs.getString(2));
                aux.setCpf(rs.getString(3));
                aux.setNumero(rs.getString(4));
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
