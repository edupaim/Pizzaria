package aplicativo.pizzaria.bo;

import aplicativo.pizzaria.dao.UserDAO;
import aplicativo.pizzaria.dto.UserDTO;
import aplicativo.pizzaria.exception.NegocioException;
import aplicativo.pizzaria.exception.PersistenciaException;
import java.util.ArrayList;
import java.util.List;

public class UserBO {

    public boolean logar(UserDTO user) throws NegocioException {
        boolean resul = false;
        try {
            if (user.getLogin() == null || "".equals(user.getLogin())) {
                throw new NegocioException("Login Obrigatório");
            } else if (user.getSenha() == null || "".equals(user.getSenha())) {
                throw new NegocioException("Senha Obrigatório");
            } else {
                UserDAO userDAO = new UserDAO();
                resul = userDAO.logar(user);
            }
        } catch (NegocioException | PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return resul;
    }

    public boolean cadastrar(UserDTO user, String senha) throws NegocioException {
        boolean resul = false;
        try {
            if (user.getLogin() == null || "".equals(user.getLogin())) {
                throw new NegocioException("Login Obrigatório.");
            } else if (user.getSenha() == null || "".equals(user.getSenha())) {
                throw new NegocioException("Senha Obrigatório.");
            } else if (0 > user.getTipo()) {
                throw new NegocioException("Tipo deve ser maior que zero.");
            } else if (!senha.equals(user.getSenha())) {
                throw new NegocioException("Repita a senha corretamente.");
            } else {
                UserDAO userDAO = new UserDAO();
                userDAO.inserir(user);
                resul = true;
            }
        } catch (NegocioException | PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return resul;
    }

    public boolean alterarSenha(UserDTO user, String senha1, String senha2) throws NegocioException {
        boolean resul = false;
        try {
            if (user.getLogin() == null || "".equals(user.getLogin())) {
                throw new NegocioException("Login obrigatório");
            } else if (user.getSenha() == null || "".equals(user.getSenha())) {
                throw new NegocioException("Senha antiga obrigatório");
            } else if (!senha1.equals(senha2)) {
                throw new NegocioException("Repita a senha corretamente.");
            } else {
                UserDAO userDAO = new UserDAO();
                userDAO.alterar(user);
                resul = true;
            }
        } catch (NegocioException | PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return resul;
    }

    public List<UserDTO> listar() throws NegocioException {
        List<UserDTO> lista = new ArrayList<>();
        UserDAO userDao = new UserDAO();
        try {
            lista = userDao.listarTodos();
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return lista;
    }

    public boolean excluir(UserDTO user) throws NegocioException {
        UserDAO userDao = new UserDAO();
        try {
            userDao.deletar(user.getId());
            return true;
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    public List<UserDTO> listarFiltrada(int id, String login) throws NegocioException {
        UserDTO user = new UserDTO();
        List<UserDTO> lista = new ArrayList<>();
        UserDAO userDao = new UserDAO();
        try {
            if (id >= 0) {
                user.setId(id);
            }
            if (login != null || !"".equals(login)) {
                user.setLogin(login);
            }
            lista = userDao.listaFiltrar(user);
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return lista;
    }
}
