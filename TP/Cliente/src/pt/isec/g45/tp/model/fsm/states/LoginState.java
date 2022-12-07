package pt.isec.g45.tp.model.fsm.states;

import pt.isec.g45.tp.model.data.AppData;
import pt.isec.g45.tp.model.fsm.AppContext;
import pt.isec.g45.tp.model.fsm.AppState;
import pt.isec.g45.tp.model.fsm.AppStateAdapter;
import pt.isec.g45.tp.utils.Authentication;

public class LoginState extends AppStateAdapter {
    AppData data;
    public LoginState(AppContext context, AppData data) {
        super(context,data);
        this.data = data;
    }
    @Override
    public AppState getState() {
        return AppState.LOGIN_STATE;
    }

    /** AÇÕES POSSÍVEIS NO ESTADO DE «LOGIN»
     *    - Se a autenticação correu bem, passa para o «BEGIN_MENU»
     *    - Se precisar de repor a PW, passa para a «REPOR_PW»
     *    - Se quiser criar uma conta, passa para a «REGISTER»
     */

    @Override
    public String begin_menu(Authentication file) {
        if (getClientManager().authentication(file)){
            System.out.println(data.getUser().getAdmin() + " is Admin");
            if (data.getUser().getAdmin())changeState(AppState.ADMIN_PANEL_STATE);
            else changeState(AppState.BEGIN_MENU);

            return "";
        }
        return "Dados Incorretos";

    }
    @Override
    public void reporPw() {
        changeState(AppState.REPOR_PW);
    }
    @Override
    public void register() {
        changeState(AppState.REGISTER);
    }

}
