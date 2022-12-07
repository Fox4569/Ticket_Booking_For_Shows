package pt.isec.g45.tp.ui.gui.states;

import javafx.scene.layout.BorderPane;
import pt.isec.g45.tp.model.AppManager;
import pt.isec.g45.tp.model.fsm.AppState;

public class SelectShowUI extends BorderPane {

    AppManager appManager;

    public SelectShowUI(AppManager appManager) {
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
        if (appManager.getState() != AppState.SELECT_SHOW) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}
