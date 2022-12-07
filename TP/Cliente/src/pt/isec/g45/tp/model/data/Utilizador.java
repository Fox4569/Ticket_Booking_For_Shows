package pt.isec.g45.tp.model.data;

public class Utilizador {
    String username;
    String password;
    Boolean isAdmin;
    String nome;

    public Utilizador() {
        this.username = null;
        this.password = null;
        this.isAdmin = null;
        this.nome = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
