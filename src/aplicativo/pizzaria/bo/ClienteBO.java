package aplicativo.pizzaria.bo;

import aplicativo.pizzaria.dao.ClienteDAO;
import aplicativo.pizzaria.dao.EnderecoDAO;
import aplicativo.pizzaria.dto.ClienteDTO;
import aplicativo.pizzaria.dto.EnderecoDTO;
import aplicativo.pizzaria.exception.NegocioException;
import aplicativo.pizzaria.exception.PersistenciaException;
import java.util.List;

/*
 * @author Edu
 */
public class ClienteBO {

    public boolean cadastrar(String nome, String cpf, String numero) throws NegocioException {
        boolean resul = false;
        try {
            if ("".equals(nome)) {
                throw new NegocioException("Nome obrigatório.");
            } else if ("".equals(cpf)) {
                throw new NegocioException("CPF obrigatório.");
            } else if ("".equals(numero)) {
                throw new NegocioException("Numéro obrigatório.");
            } else {
                ClienteDTO cliente = new ClienteDTO();
                cliente.setNome(nome.trim());
                cliente.setCpf(cpf.trim());
                cliente.setNumero(numero.trim());
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

    public boolean cadastrarEndereco(String rua, String bairro, String cidade, String estado, ClienteDTO cliente) throws NegocioException {
        boolean resul = false;
        try {
            if ("".equals(rua)) {
                throw new NegocioException("Rua obrigatória.");
            } else if ("".equals(bairro)) {
                throw new NegocioException("Bairro obrigatório.");
            } else if ("".equals(cidade)) {
                throw new NegocioException("Cidade obrigatória.");
            } else if ("".equals(estado)) {
                throw new NegocioException("Estado obrigatório.");
            } else {
                EnderecoDTO end = new EnderecoDTO();
                end.setRua(rua.trim());
                end.setBairro(bairro.trim());
                end.setCidade(cidade.trim());
                end.setEstado(estado.trim());
                end.setIdCliente(cliente.getId());
                EnderecoDAO prodDAO = new EnderecoDAO();
                prodDAO.inserir(end);
                resul = true;
            }
        } catch (NegocioException | PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return resul;
    }

}
