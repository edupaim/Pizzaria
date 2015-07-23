package aplicativo.pizzaria.dto;

public class UserDTO extends PessoaDTO{

    private Integer id;
    private String login;
    private String senha;
    private Integer tipo;
    
    public UserDTO() {
        super();
        this.id = null;
        this.login = null;
        this.senha = null;
        this.tipo = null;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
