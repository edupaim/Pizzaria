package aplicativo.pizzaria.bo;

import aplicativo.pizzaria.dao.EnderecoDAO;
import aplicativo.pizzaria.dto.EnderecoDTO;
import aplicativo.pizzaria.exception.NegocioException;
import aplicativo.pizzaria.exception.PersistenciaException;
import java.util.List;

/*
 * @author Edu
 */
public class EnderecoBO {

    public List<EnderecoDTO> listar() throws NegocioException {
        EnderecoDAO endDao = new EnderecoDAO();
        try {
            return endDao.listarTodos();
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    public boolean excluir(String id) throws NegocioException {
        EnderecoDAO endDao = new EnderecoDAO();
        boolean resul = false;
        try {
            endDao.deletar(Integer.parseInt(id));
            resul = true;
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return resul;
    }

}
