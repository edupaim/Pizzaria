package aplicativo.pizzaria.dto;

public class ItemPedido {

    private ProdutoDTO produto;
    private PedidoDTO pedido;
    private Integer Quantidade;

    public ItemPedido(ProdutoDTO produto, PedidoDTO pedido, Integer Quantidade) {
        this.produto = produto;
        this.pedido = pedido;
        this.Quantidade = Quantidade;
    }

    public ItemPedido() {
        this.produto = null;
        this.pedido = null;
        this.Quantidade = null;
    }

    public void setProduto(ProdutoDTO produto) {
        this.produto = produto;
    }

    public void setPedido(PedidoDTO pedido) {
        this.pedido = pedido;
    }

    public ProdutoDTO getProduto() {
        return produto;
    }

    public PedidoDTO getPedido() {
        return pedido;
    }

    public Integer getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(Integer Quantidade) {
        this.Quantidade = Quantidade;
    }

}
