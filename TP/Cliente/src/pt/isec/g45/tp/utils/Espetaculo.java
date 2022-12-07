package pt.isec.g45.tp.utils;

import java.io.Serializable;

public class Espetaculo implements Serializable {
    static final long serialVersionUID = 1234L;

    int id, duracao, visivel;
    String descricao, tipo, data_hora, local, localidade,pais,classificacao_etaria;
    public Espetaculo(int id, String descricao, String tipo, String data_hora, int duracao, String local, String localidade, String pais, String classificacao_etaria, int visivel) {
        this.descricao = descricao;
        this.tipo = tipo;
        this.data_hora = data_hora;
        this.local = local;
        this.localidade = localidade;
        this.pais = pais;
        this.classificacao_etaria = classificacao_etaria;
        this.id = id;
        this.duracao = duracao;
        this.visivel = visivel;

    }
    public Espetaculo(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public int getDuracao() {
        return duracao;
    }

    public int getVisivel() {
        return visivel;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getData_hora() {
        return data_hora;
    }

    public String getLocal() {
        return local;
    }

    public String getLocalidade() {
        return localidade;
    }

    public String getPais() {
        return pais;
    }

    public String getClassificacao_etaria() {
        return classificacao_etaria;
    }

    @Override
    public String toString() {
        return "Espetaculo{" +
                "id=" + id +
                ", duracao=" + duracao +
                ", visivel=" + visivel +
                ", descricao='" + descricao + '\'' +
                ", tipo='" + tipo + '\'' +
                ", data_hora='" + data_hora + '\'' +
                ", local='" + local + '\'' +
                ", localidade='" + localidade + '\'' +
                ", pais='" + pais + '\'' +
                ", classificacao_etaria='" + classificacao_etaria + '\'' +
                '}';
    }
}
