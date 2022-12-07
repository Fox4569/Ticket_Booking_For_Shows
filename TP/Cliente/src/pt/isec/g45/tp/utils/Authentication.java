package pt.isec.g45.tp.utils;

import java.io.Serializable;

public  class Authentication implements Serializable {
    static final long serialVersionUID = 1L;
    private final String password;
    private final String username;
    private final String name;
    AuthenticationEnum type;

    public Authentication(String username, String password, AuthenticationEnum type){
        this.username = username;
        this.password = password;
        this.type = type;
        this.name = null;
    }
    public Authentication(String username, String password, String name ,AuthenticationEnum type){
        this.username = username;
        this.password = password;
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public AuthenticationEnum getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("Username: %s\nPassword: %s", username, password);
    }
}
