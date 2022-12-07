package pt.isec.g45.tp.utils;

import java.io.Serializable;

public class Reserva implements Serializable {
    static final long serialVersionUID = 1234L;

    int id, pago, id_utilizador, id_espetaculo;
    String data_hora, descricao;

    public Reserva(int id, int pago, int id_utilizador, int id_espetaculo, String data_hora, String descricao) {
        this.id = id;
        this.pago = pago;
        this.id_utilizador = id_utilizador;
        this.id_espetaculo = id_espetaculo;
        this.data_hora = data_hora;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public int getPago() {
        return pago;
    }

    public int getId_utilizador() {
        return id_utilizador;
    }

    public int getId_espetaculo() {
        return id_espetaculo;
    }

    public String getData_hora() {
        return data_hora;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return String.format("\nid: %d\ndescricao: %s\ndata: %s\nid_utilizador: %d\n id_espetaculo: %d",id,descricao,data_hora,id_utilizador,id_espetaculo);
    }
}
