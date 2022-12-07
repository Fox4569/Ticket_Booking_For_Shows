package pt.isec.g45.tp.model.fsm.states;

import pt.isec.g45.tp.model.data.AppData;
import pt.isec.g45.tp.model.fsm.AppContext;
import pt.isec.g45.tp.model.fsm.AppState;
import pt.isec.g45.tp.model.fsm.AppStateAdapter;

public class SelectShowState  extends AppStateAdapter {
    public SelectShowState(AppContext context, AppData data) {
        super(context, data);
    }
    @Override
    public AppState getState() {
        return AppState.SELECT_SHOW;
    }

    /**
     *
     */

    @Override
    public void logout() {
        changeState(AppState.LOGIN_STATE);
    }

    @Override
    public void getSelectedShow() {
        getClientManager().getClientTCP().getSelectedShow("1"); // TODO MODIFICAR PARA LEVAR ARGUMENTOS
        changeState(AppState.SELECT_SHOW); // TODO VERIFICA ESTE EXEMPL
    }
}
