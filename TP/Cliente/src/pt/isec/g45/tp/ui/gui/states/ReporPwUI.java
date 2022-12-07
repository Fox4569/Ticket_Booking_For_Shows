package pt.isec.g45.tp.ui.gui.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import pt.isec.g45.tp.model.AppManager;
import pt.isec.g45.tp.model.fsm.AppState;

public class ReporPwUI extends BorderPane {

    AppManager appManager;

    private Label titulo;
    private Label nomeUserLabel;
    private TextField nomeUserTField;
    private VBox nomeVBox;
    private Button autenticar;
    private Hyperlink lembrouPass;
    private VBox infoHBox;
    private VBox loginVBox;


    public ReporPwUI(AppManager appManager) {
        this.appManager = appManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        this.setBackground(
                new Background(
                        new BackgroundFill(
                                Paint.valueOf("#F0FFF0"),
                                CornerRadii.EMPTY,
                                Insets.EMPTY
                        )
                )
        );

        titulo = new Label();
        titulo.setText("REPOSIÇÃO DA PALAVRA-PASSE");
        titulo.setFont( new Font("Times New Roman", 30) );
        titulo.setAlignment(Pos.CENTER);
        titulo.setPadding(new Insets(50, 0, 75, 0));

        nomeUserLabel = new Label();
        nomeUserLabel.setText("Nome de Utilizador");
        nomeUserLabel.setFont( new Font("Arial", 15) );
        nomeUserLabel.setMinWidth(45);
        nomeUserLabel.setMinHeight(45);
        //nomeUserLabel.setAlignment(Pos.CENTER);
        //nomeUserLabel.setTextFill(Color.WHITE);

        nomeUserTField = new TextField();
        nomeUserTField.setPromptText("Indique o nome");
        nomeUserTField.setPrefColumnCount(10);
        nomeUserTField.setMinWidth(220);
        nomeUserTField.setMaxWidth(220);
        //nomeUserTField.getText();

        nomeVBox = new VBox();
        nomeVBox.setAlignment(Pos.CENTER);
        //nomeVBox.setSpacing(10);
        nomeVBox.setMinWidth(275);
        nomeVBox.setMaxWidth(275);
        nomeVBox.setPadding(new Insets(0, 0, 25, 0));
        //nomeVBox.setPadding(new Insets(50, 0, 0, 0));
        nomeVBox.getChildren().addAll(nomeUserLabel, nomeUserTField);



        autenticar = new Button("ENVIAR");
        //autenticar.setMinWidth(75);
        //autenticar.setMaxWidth(75);
        //autenticar.setPrefWidth(75);
        autenticar.setPadding(new Insets(10));

        lembrouPass = new Hyperlink();
        lembrouPass.setText("Lembrou-se da palavra-passe? Faça login...");
        lembrouPass.setFont( new Font("Arial", 15) );
        lembrouPass.setMinWidth(45);
        lembrouPass.setMinHeight(45);

        infoHBox = new VBox();
        infoHBox.getChildren().addAll(lembrouPass);
        infoHBox.setAlignment(Pos.CENTER);
        infoHBox.setPadding(new Insets(15, 0, 0, 0));
        infoHBox.setSpacing(-15);

        loginVBox = new VBox();
        loginVBox.getChildren().addAll(titulo, nomeVBox, autenticar, infoHBox);
        loginVBox.setAlignment(Pos.CENTER);
        loginVBox.setSpacing(10);

        this.setTop(loginVBox);

    }

    private void registerHandlers() {

        appManager.addPropertyChangeListener(evt -> {
            update();
        });

        autenticar.setOnAction(event -> {
            appManager.verificaUser(nomeUserTField.getText());
        });

        lembrouPass.setOnAction(event -> {
            //System.out.println("O utilizador lembrou-se da passe...");
            nomeUserTField.clear();
            appManager.login();
        });

    }

    private void update() {
        if (appManager.getState() != AppState.REPOR_PW) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}
