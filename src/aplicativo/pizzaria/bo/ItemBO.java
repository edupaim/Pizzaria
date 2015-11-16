package aplicativo.pizzaria.bo;

import aplicativo.pizzaria.dao.ItemDAO;
import aplicativo.pizzaria.dto.ItemPedido;
import aplicativo.pizzaria.exception.PersistenciaException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author Edu
 */
public class ItemBO {

    public ItemPedido buscarPorId(Integer id) throws PersistenciaException {
        ItemDAO dao = new ItemDAO();
        ItemPedido item;
        item = dao.buscarPorId(id);
        return item;
    }

    public List<ItemPedido> buscarPorPedido(Integer id) throws PersistenciaException {
        ItemDAO dao = new ItemDAO();
        List<ItemPedido> item;

        item = dao.buscarPorPedido(id);
        return item;

    }

}
