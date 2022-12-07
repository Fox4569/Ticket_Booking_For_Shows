package pt.isec.g45.tp.utils;

import java.io.Serializable;

public class Lugar implements Serializable {
    static final long serialVersionUID = 1234L;

    String assento, fila;
    int id, espetaculo_id;
    double preco;

    public Lugar(int id, String fila, String assento, double preco, int espetaculo_id) {

        this.preco = preco;
        this.id = id;
        this.fila = fila;
        this.assento = assento;
        this.espetaculo_id = espetaculo_id;

    }

    public String getAssento() {
        return assento;
    }

    public String getFila() {
        return fila;
    }

    public int getId() {
        return id;
    }

    public int getEspetaculo_id() {
        return espetaculo_id;
    }

    public double getPreco() {
        return preco;
    }

    @Override
    public String toString() {
        return String.format("Preco: %f\nEspetaculo_id: %d\n Id: %d\n Fila: %s\n Assento: %s",preco,espetaculo_id,id,fila,assento);
    }
}
