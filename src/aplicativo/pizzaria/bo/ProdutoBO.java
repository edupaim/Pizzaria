package aplicativo.pizzaria.bo;

import aplicativo.pizzaria.dao.ProdutoDAO;
import aplicativo.pizzaria.dto.ProdutoDTO;
import aplicativo.pizzaria.exception.NegocioException;
import aplicativo.pizzaria.exception.PersistenciaException;
import static aplicativo.pizzaria.util.FuncoesUtil.onlyNumbers;

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

}
