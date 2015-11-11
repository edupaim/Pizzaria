package aplicativo.pizzaria.dto;

public class ClienteDTO extends PessoaDTO {

    private Integer id;
    private String cpf;
    private String numero;
    private EnderecoDTO enderecos;

    public ClienteDTO() {
        super();
        this.cpf = null;
        this.numero = null;
        this.enderecos = null;
    }
    
    public void setEndereco(EnderecoDTO end) {
        this.enderecos = end;
    }

    public EnderecoDTO getEndereco() {
        return enderecos;
    }

    public Integer getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNumero() {
        return numero;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

}
