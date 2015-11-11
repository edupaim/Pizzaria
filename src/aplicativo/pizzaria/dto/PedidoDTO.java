package aplicativo.pizzaria.dto;

import java.util.ArrayList;

public class PedidoDTO {

    private Integer id;
    private String data;
    private String estado;
    private ClienteDTO cliente;
    private ArrayList<ProdutoDTO> produtos;

    public PedidoDTO(Integer id, String data, String estado, ClienteDTO cliente, ArrayList<ProdutoDTO> produto) {
        this.id = id;
        this.data = data;
        this.estado = estado;
        this.cliente = cliente;
        this.produtos = produto;
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

    public void setProduto(ArrayList<ProdutoDTO> produto) {
        this.produtos = produto;
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

    public ArrayList<ProdutoDTO> getProduto() {
        return produtos;
    }

}
