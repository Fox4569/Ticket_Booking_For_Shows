package pt.isec.g45.tp.model.data;

import pt.isec.g45.tp.utils.Espetaculo;

import java.util.ArrayList;

public class AppData {

    Utilizador user;

    ArrayList<Show> shows ;

    public AppData() {
        user = new Utilizador();
        shows = new ArrayList<>();
    }

    public Utilizador getUser() {
        return user;
    }

    public void setUser(Utilizador user) {
        this.user = user;
    }

    public ArrayList<Show> getEspetaculos() {
        return shows;
    }

    public void setEspetaculos(ArrayList<Show> shows) {
        this.shows = shows;
    }
    public void removeEspetaculo(int id) {
        getEspetaculos().removeIf(d -> d.getId() == id);
    }
    public void addEspetaculo(pt.isec.g45.tp.utils.Espetaculo e) {
        System.out.println(e);
        if (e == null) return;
        removeEspetaculo(e.getId());
        shows.add(new Show(e));
    }
    public Show getShow(int idEspetaculo) {

        for (Show s: shows) {
            if (s.id == idEspetaculo) return s;
        }
        System.out.println("CHEGOU AQUI");
        return null;
    }
}
