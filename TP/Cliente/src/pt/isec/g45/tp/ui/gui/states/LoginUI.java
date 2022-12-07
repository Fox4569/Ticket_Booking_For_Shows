package pt.isec.g45.tp.ui.gui.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import pt.isec.g45.tp.model.AppManager;
import pt.isec.g45.tp.model.fsm.AppState;
import pt.isec.g45.tp.utils.Authentication;

public class LoginUI extends BorderPane {

    AppManager appManager;

    private Label titulo;
    private Label nomeUserLabel, pwUserLabel;
    private TextField nomeUserTField, pwUserTField;
    private VBox nomeVBox, pwVBox;
    private Button autenticar;
    private Hyperlink esqueceuPass, criarConta;
    private VBox infoHBox;
    private VBox loginVBox;
    private Authentication file;

    public LoginUI(AppManager appManager) {
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
        titulo.setText("LOGIN");
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
        //nomeVBox.setPadding(new Insets(50, 0, 0, 0));
        nomeVBox.getChildren().addAll(nomeUserLabel, nomeUserTField);


        pwUserLabel = new Label();
        pwUserLabel.setText("Palavra-Passe");
        pwUserLabel.setFont( new Font("Arial", 15) );
        pwUserLabel.setMinWidth(45);
        pwUserLabel.setMinHeight(45);
        //nomeUserLabel.setAlignment(Pos.CENTER);
        //nomeUserLabel.setTextFill(Color.WHITE);

        pwUserTField = new PasswordField();
        pwUserTField.setPromptText("Indique a palavra-passe");
        pwUserTField.setPrefColumnCount(10);
        pwUserTField.setMinWidth(220);
        pwUserTField.setMaxWidth(220);
        //pwUserTField.getText();

        pwVBox = new VBox();
        pwVBox.setAlignment(Pos.CENTER);
        //pwVBox.setSpacing(10);
        pwVBox.setMinWidth(275);
        pwVBox.setMaxWidth(275);
        pwVBox.setPadding(new Insets(0, 0, 25, 0));
        pwVBox.getChildren().addAll(pwUserLabel, pwUserTField);

        autenticar = new Button("AUTENTICAR");
        //autenticar.setMinWidth(75);
        //autenticar.setMaxWidth(75);
        //autenticar.setPrefWidth(75);
        autenticar.setPadding(new Insets(10));

        esqueceuPass = new Hyperlink();
        esqueceuPass.setText("Esqueceu-se da palavra-passe? Recupere-a...");
        esqueceuPass.setFont( new Font("Arial", 15) );
        esqueceuPass.setMinWidth(45);
        esqueceuPass.setMinHeight(45);

        criarConta = new Hyperlink();
        criarConta.setText("Não tem conta? Crie uma...");
        criarConta.setFont( new Font("Arial", 15) );
        criarConta.setMinWidth(45);
        criarConta.setMinHeight(45);

        infoHBox = new VBox();
        infoHBox.getChildren().addAll(esqueceuPass, criarConta);
        infoHBox.setAlignment(Pos.CENTER);
        infoHBox.setPadding(new Insets(15, 0, 0, 0));
        infoHBox.setSpacing(-15);

        loginVBox = new VBox();
        loginVBox.getChildren().addAll(titulo, nomeVBox, pwVBox, autenticar, infoHBox);
        loginVBox.setAlignment(Pos.CENTER);
        loginVBox.setSpacing(10);

        this.setTop(loginVBox);

    }

    private void registerHandlers() {

        appManager.addPropertyChangeListener(evt -> {
            update();
        });

        autenticar.setOnAction(event -> {
            String auxUser, auxPass;
            auxUser = nomeUserTField.getText();
            auxPass = pwUserTField.getText();
            nomeUserTField.clear();
            pwUserTField.clear();
            System.out.println(appManager.autenticar(auxUser, auxPass));
        });

        esqueceuPass.setOnAction(event -> {
            //System.out.println("O utilizador esqueceu-se da passe...");
            nomeUserTField.clear();
            pwUserTField.clear();
            appManager.reporPw();
        });

        criarConta.setOnAction(event -> {
            //System.out.println("O utilizador deseja criar uma conta nova...");
            nomeUserTField.clear();
            pwUserTField.clear();
            appManager.register();
        });

    }

    private void update() {
        if (appManager.getState() != AppState.LOGIN_STATE) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}
