package pt.isec.g45.tp.ui.gui.states;

import javafx.scene.layout.BorderPane;
import pt.isec.g45.tp.model.AppManager;
import pt.isec.g45.tp.model.fsm.AppState;

public class MenuConsultaUI extends BorderPane {

    AppManager appManager;

    public MenuConsultaUI(AppManager appManager) {
        this.appManager = appManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {

    }

    private void registerHandlers() {
        appManager.addPropertyChangeListener(evt -> {
            update();
        });

    }

    private void update() {
        if (appManager.getState() != AppState.MENU_CONSULTA) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}
