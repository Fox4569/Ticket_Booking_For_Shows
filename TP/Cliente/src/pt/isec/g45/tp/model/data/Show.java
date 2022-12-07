package pt.isec.g45.tp.model.data;

import pt.isec.g45.tp.utils.Lugar;

import java.util.ArrayList;

public class Show {
    int id, duracao, visivel;
    String descricao, tipo, data_hora, local, localidade,pais,classificacao_etaria;
    int linhas;
    int colunas;

    ArrayList<Lugar> lugaresDisponiveis;
    ArrayList<Lugar> lugares;
    ArrayList<Sit> disposicao;
    public Show(pt.isec.g45.tp.utils.Espetaculo e) {
        this.descricao = e.getDescricao();
        this.tipo = e.getTipo();
        this.data_hora = e.getData_hora();
        this.local = e.getLocal();
        this.localidade = e.getLocalidade();
        this.pais = e.getPais();
        this.classificacao_etaria = e.getClassificacao_etaria();
        this.id = e.getId();
        this.duracao = e.getDuracao();
        this.visivel = e.getVisivel();
        this.lugaresDisponiveis = new ArrayList<>();
        this.lugares = new ArrayList<>();
        this.disposicao = new ArrayList<>();
        this.linhas = 0;
        this.colunas = 0;

    }
    public Boolean ocupado(Lugar e) {
        for(Lugar l: lugaresDisponiveis) {
            if (l.getId() == e.getId()) return false;
        }
        return true;
    }
    public void buildDisposicao() {
        Boolean isFila = false;
        Boolean isASsento = false;
        ArrayList<String> filas = new ArrayList<>();
        ArrayList<String> assento = new ArrayList<>();

        for (Lugar l: lugares) {
            disposicao.add(new Sit(l.getId(), l.getFila(), l.getAssento(),l.getPreco(),l.getEspetaculo_id(),ocupado(l)));

            for(String s: filas) {
                if (s.equals(l.getFila())) {
                    isFila = true;
                    break;
                }

            }
            if (!isFila) {
                filas.add(l.getFila());
            }
            else isFila = false;
            for(String s: assento) {
                if (s.equals(l.getAssento())) {
                    isASsento = true;
                    break;
                }

            }
            if (!isASsento) {
                assento.add(l.getAssento());
            }
            else isASsento = false;
        }
        this.linhas= filas.size();
        this.colunas = Integer.parseInt(assento.get(assento.size()-1));
        System.out.println("Linhas: " + linhas + "\nColunas: " + colunas);
    }
    public ArrayList<Lugar> getLugaresDisponiveis() {
        return lugaresDisponiveis;
    }
    public void setLugaresDisponiveis(ArrayList<Lugar> aux) {

        lugaresDisponiveis = aux;
    }
    public ArrayList<Lugar> getLugares() {
        return lugares;
    }
    public void setLugares(ArrayList<Lugar> aux) {

        lugares = aux;
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

    public ArrayList<Sit> getDisposicao() {
        return disposicao;
    }

    public int getLinhas() {
        return linhas;
    }

    public void setLinhas(int linhas) {
        this.linhas = linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public void setColunas(int colunas) {
        this.colunas = colunas;
    }

    public ArrayList<Sit> getSitsFila(String fila) {
        ArrayList<Sit> bancos = new ArrayList<>();

        for (Sit s: disposicao) {
            if (s.getFila().equals(fila)) {
                bancos.add(s);
            }
        }
        return bancos;
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
                '}'+"\n";
    }
}
