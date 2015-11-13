package aplicativo.pizzaria.dto;

import java.util.List;

public class PedidoDTO {

    private Integer id;
    private String data;
    private String estado;
    private ClienteDTO cliente;
    private List<ItemPedido> itens;

    public PedidoDTO(Integer id, String data, String estado, ClienteDTO cliente, List<ItemPedido> itens) {
        this.id = id;
        this.data = data;
        this.estado = estado;
        this.cliente = cliente;
        this.itens = itens;
    }

    public PedidoDTO() {
        this.id = null;
        this.data = null;
        this.estado = null;
        this.cliente = null;
        this.itens = null;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public Integer getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

}
