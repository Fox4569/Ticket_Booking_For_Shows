package pt.isec.g45.tp.model.fsm.states;

import pt.isec.g45.tp.model.data.AppData;
import pt.isec.g45.tp.model.data.Show;
import pt.isec.g45.tp.model.fsm.AppContext;
import pt.isec.g45.tp.model.fsm.AppState;
import pt.isec.g45.tp.model.fsm.AppStateAdapter;
import pt.isec.g45.tp.utils.Espetaculo;
import pt.isec.g45.tp.utils.Reserva;

import java.util.ArrayList;

public class AdminPanelState extends AppStateAdapter {
    AppData data;
    public AdminPanelState(AppContext context, AppData data) {
        super(context, data);
        this.data = data;
    }

    @Override
    public AppState getState() {
        return AppState.ADMIN_PANEL_STATE;
    }

    @Override
    public ArrayList<Show> getShows() {
        if (!data.getEspetaculos().isEmpty()){  data.getEspetaculos().clear();}
        ArrayList<Espetaculo> espetaculos = getClientManager().getClientTCP().getShows();
        if (espetaculos == null) return null;
        int i = 0;
        for (Espetaculo e: espetaculos) {
            //System.out.println(e);
            data.addEspetaculo(e);
            System.out.println("LUGARES -> " + getClientManager().getClientTCP().getSits(e.getId()));

            data.getEspetaculos().get(i).setLugares(getClientManager().getClientTCP().getSits(e.getId()));
            data.getEspetaculos().get(i).setLugaresDisponiveis(getClientManager().getClientTCP().getAvailableSits(e.getId()));
            System.out.println("LUGARES -> " + data.getEspetaculos().get(i).getLugares());
            System.out.println( "LUGARES_DISPONIVEIS -> " + data.getEspetaculos().get(i).getLugaresDisponiveis());
            data.getEspetaculos().get(i).buildDisposicao();
            System.out.println(data.getEspetaculos().get(i).getDisposicao());
            i++;      }
        System.out.println(data.getEspetaculos());
        return data.getEspetaculos();
    }

    @Override
    public ArrayList<Reserva> getNotPayedReservations() {
        return getClientManager().getClientTCP().getNotPayedReservations();
    }

    @Override
    public ArrayList<Reserva> getPayedReservations() {
        ArrayList<Reserva> reservas = getClientManager().getClientTCP().getPayedReservations();
        System.out.println("DEBIG___" + reservas);
        return reservas;
    }

    @Override
    public void deleteNotPayedReservation(int espetaculoID) {
        getClientManager().getClientTCP().deleteNotPayedReservation(""+espetaculoID);
    }

    @Override
    public void insertShow(String filename) {
        getClientManager().getClientTCP().insertShow(filename);
    }


    @Override
    public void deleteShow(int espetaculoID) {
        getClientManager().getClientTCP().deleteShow(espetaculoID);
        data.removeEspetaculo(espetaculoID);
        //super.deleteShow(espetaculoID);
    }

    @Override
    public void logout() {
        changeState(AppState.LOGIN_STATE);
        getClientManager().getClientTCP().logout(data.getUser().getUsername());
    }
}
