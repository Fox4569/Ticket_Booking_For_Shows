package pt.isec.g45.tp.model.fsm.states;

import pt.isec.g45.tp.model.data.AppData;
import pt.isec.g45.tp.model.fsm.AppContext;
import pt.isec.g45.tp.model.fsm.AppState;
import pt.isec.g45.tp.model.fsm.AppStateAdapter;
import pt.isec.g45.tp.utils.Authentication;

public class UserDataEditingState extends AppStateAdapter {
    public UserDataEditingState(AppContext context, AppData data) {
        super(context,data);
    }
    @Override
    public AppState getState() {
        return AppState.USER_DATA_EDITING_STATE;
    }

    /** AÇÕES POSSÍVEIS NO ESTADO DE «USER_DATA_EDITING»
     *    - Se
     */


    @Override
    public void userDataEditing() {

    }


}
