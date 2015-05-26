package aplicativo.pizzaria.bo;

import aplicativo.pizzaria.dao.UserDAO;
import aplicativo.pizzaria.dto.UserDTO;
import aplicativo.pizzaria.exception.NegocioException;
import aplicativo.pizzaria.exception.PersistenciaException;
import java.util.ArrayList;
import java.util.List;

public class UserBO {

    public UserDTO logar(String login, String senha) throws NegocioException {
        UserDTO user = null;
        try {
            if (login == null || "".equals(login)) {
                throw new NegocioException("Login obrigatório.");
            } else if (senha == null || "".equals(senha)) {
                throw new NegocioException("Senha obrigatória.");
            } else {
                UserDAO userDAO = new UserDAO();
                user = userDAO.logar(login, senha);
            }
        } catch (NegocioException | PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return user;
    }

    public boolean cadastrar(String login, String senha, String senhar, String tipo) throws NegocioException {
        boolean resul = false;
        try {
            if (login == null || "".equals(login)) {
                throw new NegocioException("Login obrigatório.");
            } else if (senha == null || "".equals(senha)) {
                throw new NegocioException("Senha obrigatória.");
            } else if (tipo == null || "".equals(tipo)) {
                throw new NegocioException("Tipo obrigatório.");
            } else if (!senha.equals(senhar)) {
                throw new NegocioException("Repita a senha corretamente.");
            } else {
                UserDTO user = new UserDTO();
                user.setLogin(login);
                user.setSenha(senha);
                user.setTipo(Integer.parseInt(tipo));
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
            } else if (senha1 == null || "".equals(senha1)) {
                throw new NegocioException("Senha nova obrigatório");
            } else if (!senha1.equals(senha2)) {
                throw new NegocioException("Repita a senha corretamente.");
            } else {
                user.setSenha(senha1);
                UserDAO userDAO = new UserDAO();
                userDAO.alterarSenha(user);
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

    public boolean excluir(String login, String senha) throws NegocioException {
        UserDTO user = new UserDTO();
        UserDAO userDao = new UserDAO();
        boolean resul = false;
        try {
            user = logar(login, senha);
            if (user != null) {
                userDao.deletar(user.getId());
                resul = true;
            }
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return resul;
    }

    public List<UserDTO> listarFiltrada(String id, String login, String tipo) throws NegocioException {
        UserDTO user = new UserDTO();
        List<UserDTO> lista = new ArrayList<>();
        UserDAO userDao = new UserDAO();
        try {
            if (id != null && !"".equals(id)) {
                user.setId(Integer.parseInt(id));
            }
            if (login != null && !"".equals(login)) {
                user.setLogin(login);
            }
            if (tipo != null && !"".equals(tipo)) {
                user.setTipo(Integer.parseInt(tipo));
            }
            lista = userDao.listaFiltrar(user);
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return lista;
    }
}
