package aplicativo.pizzaria.bo;

import aplicativo.pizzaria.dao.ProdutoDAO;
import aplicativo.pizzaria.dto.ProdutoDTO;
import aplicativo.pizzaria.exception.NegocioException;
import aplicativo.pizzaria.exception.PersistenciaException;
import static aplicativo.pizzaria.util.FuncoesUtil.onlyNumbers;
import java.util.ArrayList;
import java.util.List;

public class ProdutoBO {

    public boolean cadastrar(String nome, String tam, String descricao, String valor, String tipo) throws NegocioException {
        boolean resul = false;
        try {
            if ("".equals(nome)) {
                throw new NegocioException("Nome obrigatório.");
            } else if ("".equals(valor)) {
                throw new NegocioException("Valor obrigatório.");
            } else {
                ProdutoDTO produto = new ProdutoDTO();
                produto.setNome(nome.trim());
                produto.setTamanho(tam);
                produto.setDescricao(descricao.trim());
                produto.setTipo(tipo);
                produto.setValor(Double.parseDouble(onlyNumbers(valor)));
                ProdutoDAO prodDAO = new ProdutoDAO();
                prodDAO.inserir(produto);
                resul = true;
            }
        } catch (NegocioException | PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return resul;
    }

    public List<ProdutoDTO> listar() throws NegocioException {
        ProdutoDAO prodDao = new ProdutoDAO();
        try {
            return prodDao.listarTodos();
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }
    
    public boolean excluir(String id) throws NegocioException {
        ProdutoDAO prodDao = new ProdutoDAO();
        boolean resul = false;
        try {
            prodDao.deletar(Integer.parseInt(id));
            resul = true;
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return resul;
    }
    
    public List<ProdutoDTO> busca(String id, String nome, String tamanho, String tipo) throws NegocioException {
        ProdutoDTO prod = new ProdutoDTO();
        List<ProdutoDTO> lista = new ArrayList<>();
        ProdutoDAO prodDao = new ProdutoDAO();
        try {
            if (id != null && !"".equals(id)) {
                prod.setId(Integer.parseInt(id));
            }
            if (nome != null && !"".equals(nome)) {
                prod.setNome(nome.trim());
            }
            if (tamanho != null && !"".equals(tamanho)) {
                prod.setTamanho(tamanho.trim());
            }
            if (tipo != null && !"".equals(tipo)) {
                prod.setTipo(tipo.trim());
            }
            lista = prodDao.listaFiltro(prod);
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        return lista;
    }
}
