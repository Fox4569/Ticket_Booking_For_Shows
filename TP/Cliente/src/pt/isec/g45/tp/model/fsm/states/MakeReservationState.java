package pt.isec.g45.tp.model.fsm.states;

import pt.isec.g45.tp.model.data.AppData;
import pt.isec.g45.tp.model.data.Show;
import pt.isec.g45.tp.model.data.Sit;
import pt.isec.g45.tp.model.fsm.AppContext;
import pt.isec.g45.tp.model.fsm.AppState;
import pt.isec.g45.tp.model.fsm.AppStateAdapter;
import pt.isec.g45.tp.utils.Authentication;
import pt.isec.g45.tp.utils.Lugar;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MakeReservationState  extends AppStateAdapter {
    AppData data;
    public MakeReservationState(AppContext context, AppData data) {
        super(context, data);
        this.data = data;
    }
    @Override
    public AppState getState() {
        return AppState.MAKE_RESERVATION;
    }

    @Override
    public String begin_menu(Authentication file) {
        System.out.println("CHEGUEI AQUI!!!!!!!");
        changeState(AppState.BEGIN_MENU);
        return "";
    }

    /**
     *
     */

    @Override
    public void logout() {
        changeState(AppState.LOGIN_STATE);
        getClientManager().getClientTCP().logout(data.getUser().getUsername());
    }
    @Override
    public ArrayList<Sit> disposicao(int idEspetaculo){
        return data.getShow(idEspetaculo).getDisposicao();
    }
    @Override
    public int[] getSize(int idEspetaculo) {
        return new int[]{data.getShow(idEspetaculo).getLinhas(),data.getShow(idEspetaculo).getColunas() }; // 0 -> x, 1 -> y
    }

    @Override
    public void reserve(Show i, ArrayList<Lugar> lugares) {
        getClientManager().getClientTCP().makeReservation(i.getData_hora(), data.getUser().getUsername(),""+i.getId(), lugares);
        changeState(AppState.BEGIN_MENU);
    }
}
