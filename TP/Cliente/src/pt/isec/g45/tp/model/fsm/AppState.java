package pt.isec.g45.tp.model.fsm;

import pt.isec.g45.tp.model.data.AppData;
import pt.isec.g45.tp.model.fsm.states.*;

public enum AppState {

    BEGIN_MENU,
    LOGIN_STATE,
    MAKE_RESERVATION,
    MENU_CONSULTA,
    REGISTER,
    REPOR_PW,
    SELECT_SHOW,
    ADMIN_PANEL_STATE,
    SHOW_INFO_STATE,
    USER_DATA_EDITING_STATE;

    public IAppState createState(AppContext context, AppData data) {

        return switch (this) {
            case BEGIN_MENU -> new BeginMenuState(context,data);
            case LOGIN_STATE -> new LoginState(context, data);
            case MAKE_RESERVATION -> new MakeReservationState(context,data);
            case MENU_CONSULTA -> new MenuConsulta(context,data);
            case REGISTER -> new RegisterState(context,data);
            case REPOR_PW -> new ReposicaoPwState(context, data);
            case SELECT_SHOW -> new SelectShowState(context, data);
            case ADMIN_PANEL_STATE -> new AdminPanelState(context, data);
            case SHOW_INFO_STATE -> new ShowInfoState(context,data);
            case USER_DATA_EDITING_STATE -> new UserDataEditingState(context, data);

        };
    }
}
