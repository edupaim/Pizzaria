package aplicativo.pizzaria.dao;

import aplicativo.pizzaria.exception.PersistenciaException;
import java.util.List;

public interface GenericoDAO<DTO> {

    void inserir(DTO obj) throws PersistenciaException;

    void atualizar(DTO obj) throws PersistenciaException;

    void deletar(Integer id) throws PersistenciaException;

    List<DTO> listarTodos() throws PersistenciaException;

    DTO buscarPorId(Integer id) throws PersistenciaException;

}
