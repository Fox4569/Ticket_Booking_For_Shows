package pt.isec.g45.tp.ui.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.g45.tp.model.AppManager;


import java.util.List;
import java.util.Map;

import static pt.isec.g45.tp.utils.UtilsUI.windowHeight;
import static pt.isec.g45.tp.utils.UtilsUI.windowWidth;


public class MainJFX extends Application {
    AppManager manager;

    @Override
    public void init() throws Exception {
        super.init();
        Parameters parameters = getParameters ();

        Map<String, String> namedParameters = parameters.getNamed ();
        List<String> rawArguments = parameters.getRaw ();


        manager = new AppManager(rawArguments);
    }

    @Override
    public void start(Stage stage) throws Exception {

        try {
            RootPane root = new RootPane(manager);
            Scene scene = new Scene(root, windowWidth, windowHeight);
            stage.setScene(scene);
            stage.setTitle("@shows_PD");
            stage.setMinWidth(1000);
            stage.setMinHeight(600);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
