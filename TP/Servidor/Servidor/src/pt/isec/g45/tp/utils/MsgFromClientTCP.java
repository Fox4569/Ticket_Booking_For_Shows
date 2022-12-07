package pt.isec.g45.tp.utils;

import java.io.Serializable;

public class MsgFromClientTCP implements Serializable {
    static final long serialVersionUID = 1234L;

    CodigosTCP codigo;
    String [] args; // argumentos caso seja necessário alteração (ex: no lado do cliente temos uma função do seguinte tipo:
    // )
    /*
    * def build_args(args) {
    *   return String.format("%s %s", args[0], args [1]) # sendo args[0] o velho username e args[1] o novo
    * }
    *
    * e depois basta fazer um algoritmo para obter cada um dos argumentos, ou ate mesmo fazer um array de strings
    *
    * */

    public MsgFromClientTCP(CodigosTCP codigo) {
        this.codigo = codigo;
        args = null;
    }
    public MsgFromClientTCP(CodigosTCP codigo, String [] args) {
        this.codigo = codigo;
        this.args = args;
    }

    public CodigosTCP getCodigo() {
        return codigo;
    }

    public void setCodigo(CodigosTCP codigo) {
        this.codigo = codigo;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
