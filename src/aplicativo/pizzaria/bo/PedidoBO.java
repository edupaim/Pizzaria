package aplicativo.pizzaria.bo;

import aplicativo.pizzaria.dao.PedidoDAO;
import aplicativo.pizzaria.dto.ClienteDTO;
import aplicativo.pizzaria.dto.ItemPedido;
import aplicativo.pizzaria.dto.PedidoDTO;
import aplicativo.pizzaria.exception.NegocioException;
import aplicativo.pizzaria.exception.PersistenciaException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author Edu
 */
public class PedidoBO {

    public boolean cadastrar(String data, String estado, ClienteDTO cliente, List<ItemPedido> listaItens) throws NegocioException {
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        boolean resul = false;
        try {
            if (cliente == null) {
                throw new NegocioException("Favor buscar um cliente.");
            } else if (listaItens == null) {
                throw new NegocioException("Favor selecionar os itens do pedido.");
            } else {
                PedidoDTO pedido = new PedidoDTO();
                pedido.setData(sdf.format(gc.getTime()));
                pedido.setEstado(estado);
                pedido.setCliente(cliente);
                pedido.setItens(listaItens);
                PedidoDAO pedidoDAO = new PedidoDAO();
                pedidoDAO.inserir(pedido);
                resul = true;
            }
        } catch (NegocioException | PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return resul;
    }

    public List<PedidoDTO> listar() throws NegocioException {
        PedidoDAO pedidoDao = new PedidoDAO();
        try {
            return pedidoDao.listarTodos();
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    public List<PedidoDTO> listarPendentes() throws NegocioException {
        PedidoDAO pedidoDao = new PedidoDAO();
        try {
            return pedidoDao.listarTodos("Pendente");
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    public List<PedidoDTO> listarFinalizados() throws NegocioException {
        PedidoDAO pedidoDao = new PedidoDAO();
        try {
            return pedidoDao.listarTodos("Finalizado");
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    public List<PedidoDTO> listarEntregues() throws NegocioException {
        PedidoDAO pedidoDao = new PedidoDAO();
        try {
            return pedidoDao.listarTodos("Entregue");
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    public boolean excluir(String id) throws NegocioException {
        PedidoDAO pedidoDao = new PedidoDAO();
        boolean resul = false;
        try {
            pedidoDao.deletar(Integer.parseInt(id));
            resul = true;
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return resul;
    }

    public PedidoDTO buscarPorID(Integer id) {
        PedidoDTO ped = new PedidoDTO();
        PedidoDAO predDao = new PedidoDAO();
        try {
            ped = predDao.buscarPorId(id);
        } catch (PersistenciaException ex) {
        }
        return ped;
    }

    public void finalizar(Integer id) {
        PedidoDAO ped = new PedidoDAO();
        try {
            ped.atualizarEstado(id, "Finalizado");
        } catch (PersistenciaException ex) {
            Logger.getLogger(PedidoBO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void entregar(Integer id) {
        PedidoDAO ped = new PedidoDAO();
        try {
            ped.atualizarEstado(id, "Entregue");
        } catch (PersistenciaException ex) {
            Logger.getLogger(PedidoBO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
