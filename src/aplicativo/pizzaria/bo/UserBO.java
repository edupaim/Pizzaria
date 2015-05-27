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
        Integer tip = null;
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
                tip = escolherTipo(tipo);
                UserDTO user = new UserDTO();
                user.setLogin(login);
                user.setSenha(senha);
                user.setTipo(tip);
                UserDAO userDAO = new UserDAO();
                userDAO.inserir(user);
                resul = true;
            }
        } catch (NegocioException | PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return resul;
    }

    public boolean alterar(String id, String login, String senha, String tipo, String s1, String s2) throws NegocioException {
        UserDTO user = null;
        boolean resul = false;
        Integer tip = escolherTipo(tipo);
        try {
            if ("".equals(id)) {
                throw new NegocioException("Selecione um usuário na lista.");
            } else if ("".equals(login)) {
                throw new NegocioException("Login obrigatório.");
            } else if ("".equals(senha)) {
                throw new NegocioException("Senha antiga obrigatória.");
            } else {
                user = new UserDTO();
                user.setLogin(login);
                user.setTipo(tip);
                UserDAO userDAO = new UserDAO();
                if (!"".equals(s1) || !"".equals(s2)) {
                    if (!s1.equals(s2)) {
                        throw new NegocioException("Repita a senha corretamente.");
                    } else {
                        user.setSenha(s1);
                        userDAO.alterarSenha(user);
                        resul = true;
                    }
                } else {
                    user.setSenha(senha);
                    userDAO.atualizar(Integer.parseInt(id), user);
                    resul = true;
                }
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

    public List<UserDTO> listarFiltrada(String id, String login, Integer tipo) throws NegocioException {
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
            user.setTipo(tipo);
            lista = userDao.listaFiltrar(user);
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return lista;
    }

    public Integer escolherTipo(String tipo) throws NegocioException {
        Integer resul = null;
        switch (tipo) {
            case "Administrador":
                resul = 0;
                break;
            case "Gerente":
                resul = 1;
                break;
            case "Atendente":
                resul = 2;
                break;
            case "Pizzaiolo":
                resul = 3;
                break;
            case "Garçom":
                resul = 4;
                break;
            default:
                throw new NegocioException("Tipo incorreto.");
        }
        return resul;
    }
}
