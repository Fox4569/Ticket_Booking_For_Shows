package pt.isec.g45.tp.ui.gui.states;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import pt.isec.g45.tp.model.AppManager;
import pt.isec.g45.tp.model.fsm.AppState;

public class ShowInfoUI extends BorderPane {

    AppManager appManager;

    public ShowInfoUI(AppManager appManager) {
        this.appManager = appManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {

        this.setBackground(
                new Background(
                        new BackgroundFill(
                                Paint.valueOf("#7dcfe2"),
                                CornerRadii.EMPTY,
                                Insets.EMPTY
                        )
                )
        );

    }

    private void registerHandlers() {
        appManager.addPropertyChangeListener(evt -> {
            update();
        });

    }

    private void update() {
        if (appManager.getState() != AppState.SHOW_INFO_STATE) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}
