package pt.isec.g45.tp.ui.gui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import pt.isec.g45.tp.model.AppManager;
import pt.isec.g45.tp.ui.gui.states.*;

public class RootPane extends BorderPane {
    AppManager appManager;

    public RootPane(AppManager appManager) {
        this.appManager = appManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        StackPane stackPane = new StackPane(
                new AdminPanelUI(appManager),
                new BeginMenuUI(appManager),
                new LoginUI(appManager),
                new MakeReservationsUI(appManager),
                new MenuConsultaUI(appManager),
                new RegisterUI(appManager),
                new ReporPwUI(appManager),
                new SelectShowUI(appManager),
                new ShowInfoUI(appManager),
                new UserDataEditingUI(appManager)
        );

        this.setCenter(stackPane);

    }

    private void registerHandlers() {

        appManager.addPropertyChangeListener(evt -> {
            update();
        });

    }

    private void update() {

    }
}
