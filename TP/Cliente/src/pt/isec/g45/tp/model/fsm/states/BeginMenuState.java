package pt.isec.g45.tp.model.fsm.states;

import pt.isec.g45.tp.model.data.AppData;
import pt.isec.g45.tp.model.data.Show;
import pt.isec.g45.tp.model.fsm.AppContext;
import pt.isec.g45.tp.model.fsm.AppState;
import pt.isec.g45.tp.model.fsm.AppStateAdapter;
import pt.isec.g45.tp.utils.Espetaculo;
import pt.isec.g45.tp.utils.FilterShow;

import java.util.ArrayList;
import java.util.logging.Filter;

public class BeginMenuState  extends AppStateAdapter {
    AppData data;
    public BeginMenuState(AppContext context, AppData data) {

        super(context, data);
        this.data = data;
    }
    @Override
    public AppState getState() {
        return AppState.BEGIN_MENU;
    }


    @Override
    public ArrayList<Show> getShows() {
        if (!data.getEspetaculos().isEmpty()) return data.getEspetaculos();
        ArrayList<Espetaculo> espetaculos = getClientManager().getClientTCP().getShows();
        if (espetaculos == null) return null;
        int i = 0;
        for (Espetaculo e: espetaculos) {
            System.out.println(e);
            data.addEspetaculo(e);
            data.getEspetaculos().get(i).setLugares(getClientManager().getClientTCP().getSits(e.getId()));
            data.getEspetaculos().get(i).setLugaresDisponiveis(getClientManager().getClientTCP().getAvailableSits(e.getId()));
            data.getEspetaculos().get(i).buildDisposicao();
            i++;
        }
        System.out.println(data.getEspetaculos());
        return data.getEspetaculos();
    }
    /**
     *
     */


    @Override
    public void consulta_menu() {
        changeState(AppState.MENU_CONSULTA);
    }

    @Override
    public void makeReservation() {
        changeState(AppState.MAKE_RESERVATION);
    }

    @Override
    public void logout() {
        changeState(AppState.LOGIN_STATE);
        getClientManager().getClientTCP().logout(data.getUser().getUsername());
    }

    @Override
    public void editData() {
        changeState(AppState.USER_DATA_EDITING_STATE);
    }

    @Override
    public ArrayList<Espetaculo> getFilteredShow(FilterShow filter, String nome) {
        return getClientManager().getClientTCP().getFilteredShow(filter,nome);
    }


}
