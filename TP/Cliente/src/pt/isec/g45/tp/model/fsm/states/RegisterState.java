package pt.isec.g45.tp.model.fsm.states;

import pt.isec.g45.tp.model.data.AppData;
import pt.isec.g45.tp.model.fsm.AppContext;
import pt.isec.g45.tp.model.fsm.AppState;
import pt.isec.g45.tp.model.fsm.AppStateAdapter;
import pt.isec.g45.tp.utils.Authentication;

public class RegisterState extends AppStateAdapter {
    public RegisterState(AppContext context, AppData data) {
        super(context, data);
    }
    @Override
    public AppState getState() {
        return AppState.REGISTER;
    }

    /** AÇÕES POSSÍVEIS NO ESTADO DE «REGISTER»
     *    - Se a criação de conta correu bem, passa automaticamente para o «LOGIN_STATE»
     *    - Se não quiser criar uma conta, porque já tiver uma, pode voltar para o «LOGIN_STATE»
     */

    @Override
    public void login() {
        changeState(AppState.LOGIN_STATE);
    }

    @Override
    public String begin_menu(Authentication file) {
        if (getClientManager().authentication(file)){
            changeState(AppState.BEGIN_MENU);
            return "";
        }
        return "Dados Incorretos";

    }

}
