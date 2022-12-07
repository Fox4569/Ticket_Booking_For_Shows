package pt.isec.g45.tp.model.fsm.states;

import pt.isec.g45.tp.model.data.AppData;
import pt.isec.g45.tp.model.fsm.AppContext;
import pt.isec.g45.tp.model.fsm.AppState;
import pt.isec.g45.tp.model.fsm.AppStateAdapter;

public class MenuConsulta extends AppStateAdapter {
    AppData data;
    public MenuConsulta(AppContext context, AppData data) {
        super(context, data);
        this.data = data;
    }
    @Override
    public AppState getState() {
        return AppState.MENU_CONSULTA;
    }

    /**
     *
     */

    @Override
    public void logout() {
        changeState(AppState.LOGIN_STATE);
        getClientManager().getClientTCP().logout(data.getUser().getUsername());
    }
}
