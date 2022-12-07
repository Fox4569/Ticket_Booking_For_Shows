package pt.isec.g45.tp.model.fsm.states;

import pt.isec.g45.tp.model.data.AppData;
import pt.isec.g45.tp.model.fsm.AppContext;
import pt.isec.g45.tp.model.fsm.AppState;
import pt.isec.g45.tp.model.fsm.AppStateAdapter;

public class ShowInfoState extends AppStateAdapter {
    public ShowInfoState(AppContext context, AppData data) {
        super(context, data);
    }

    @Override
    public AppState getState() {
        return AppState.SHOW_INFO_STATE;
    }
}
