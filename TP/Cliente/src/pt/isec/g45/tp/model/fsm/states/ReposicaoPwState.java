package pt.isec.g45.tp.model.fsm.states;

import pt.isec.g45.tp.model.data.AppData;
import pt.isec.g45.tp.model.fsm.AppContext;
import pt.isec.g45.tp.model.fsm.AppState;
import pt.isec.g45.tp.model.fsm.AppStateAdapter;

public class ReposicaoPwState extends AppStateAdapter {
    public ReposicaoPwState(AppContext context, AppData data) {
        super(context, data);
    }
    @Override
    public AppState getState() {
        return AppState.REPOR_PW;
    }

    /** AÇÕES POSSÍVEIS NO ESTADO DE «REPOR_PW»
     *    - Se o username existir, neste mesmo estado aparece uma caixa de verificação com um código
     *    - Se o código de validação for o mesmo, aparecerá uma caixa para nova Password
     *    - Se a redefinição da password correu bem, o user é informado e haverá uma hiperligação de volta para o «LOGIN_STATE»
     *    - O utilizador pode ter recordado a password, por isso volta para o «LOGIN_STATE»
     */

    @Override
    public void login() {
        changeState(AppState.LOGIN_STATE);
    }

}
