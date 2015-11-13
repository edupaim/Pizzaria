package aplicativo.pizzaria.bo;

import aplicativo.pizzaria.dao.ClienteDAO;
import aplicativo.pizzaria.dto.ClienteDTO;
import aplicativo.pizzaria.dto.EnderecoDTO;
import aplicativo.pizzaria.exception.NegocioException;
import aplicativo.pizzaria.exception.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author Edu
 */
public class ClienteBO {

    public boolean cadastrar(String nome, String cpf, String numero, EnderecoDTO end) throws NegocioException {
        boolean resul = false;
        try {
            if ("".equals(nome)) {
                throw new NegocioException("Nome obrigatório.");
            } else if ("".equals(cpf)) {
                throw new NegocioException("CPF obrigatório.");
            } else if ("".equals(numero)) {
                throw new NegocioException("Número obrigatório.");
            } else if (end == null) {
                throw new NegocioException("Endereço obrigatório.");
            } else {
                ClienteDTO cliente = new ClienteDTO();
                cliente.setNome(nome.trim());
                cliente.setCpf(cpf.trim());
                cliente.setNumero(numero.trim());
                cliente.setEndereco(end);
                ClienteDAO clienteDAO = new ClienteDAO();
                clienteDAO.inserir(cliente);
                resul = true;
            }
        } catch (NegocioException | PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return resul;
    }

    public List<ClienteDTO> listar() throws NegocioException {
        ClienteDAO clienteDao = new ClienteDAO();
        try {
            return clienteDao.listarTodos();
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    public boolean excluir(String id) throws NegocioException {
        ClienteDAO clienteDao = new ClienteDAO();
        boolean resul = false;
        try {
            clienteDao.deletar(Integer.parseInt(id));
            resul = true;
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return resul;
    }

    public List<ClienteDTO> buscar(String nome, String num) {
        ClienteDTO cliente = new ClienteDTO();
        List<ClienteDTO> lista = new ArrayList<>();
        ClienteDAO clienteDao = new ClienteDAO();
        if (nome != null && !"".equals(nome)) {
            cliente.setNome(nome.trim());
        }
        if (num != null && !"".equals(num)) {
            cliente.setNumero(num.trim());
        }
        try {
            lista = clienteDao.listaFiltro(cliente);
        } catch (PersistenciaException ex) {

        }
        return lista;
    }
    
    public ClienteDTO buscarPorID(Integer id) {
        ClienteDTO cliente = new ClienteDTO();
        ClienteDAO clienteDao = new ClienteDAO();
        try {
            cliente = clienteDao.buscarPorId(id);
        } catch (PersistenciaException ex) {

        }
        return cliente;
    }

}
