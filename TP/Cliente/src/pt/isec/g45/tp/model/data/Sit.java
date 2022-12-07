package pt.isec.g45.tp.model.data;

public class Sit {

    String assento, fila;
    int id, espetaculo_id;
    double preco;
    Boolean ocupado;

    public Sit(int id, String fila, String assento, double preco, int espetaculo_id, Boolean ocupado) {

        this.preco = preco;
        this.id = id;
        this.fila = fila;
        this.assento = assento;

        this.espetaculo_id = espetaculo_id;

        this.ocupado = ocupado;

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

    public Boolean getOcupado() {
        return ocupado;
    }

    public void setOcupado(Boolean ocupado) {
        this.ocupado = ocupado;
    }

    @Override
    public String toString() {
        return "Sit{" +
                "assento='" + assento + '\'' +
                ", fila='" + fila + '\'' +
                ", id=" + id +
                ", espetaculo_id=" + espetaculo_id +
                ", preco=" + preco +
                ", ocupado=" + ocupado +
                '}';
    }
}
