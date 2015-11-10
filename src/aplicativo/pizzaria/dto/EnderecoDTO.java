package aplicativo.pizzaria.dto;

/*
 * @author Edu
 */
public class EnderecoDTO {
    private Integer id;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private Integer idCliente;

    public EnderecoDTO(String rua, String bairro, String cidade, String estado, Integer idCliente) {
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.idCliente = idCliente;
    }

    public EnderecoDTO() {
        this.rua = null;
        this.bairro = null;
        this.cidade = null;
        this.estado = null;
        this.idCliente = null;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRua() {
        return rua;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

}
