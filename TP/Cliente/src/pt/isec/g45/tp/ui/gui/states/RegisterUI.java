package pt.isec.g45.tp.ui.gui.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import pt.isec.g45.tp.model.AppManager;
import pt.isec.g45.tp.model.fsm.AppState;

public class RegisterUI extends BorderPane {

    AppManager appManager;

    private Label titulo;
    private Label nomeLabel, nomeUserLabel, pwUserLabel, pwUserRepLabel;
    private TextField nomeTField, nomeUserTField, pwUserTField, pwUserRepTField;
    private VBox nomeVBox, nomeUserVBox, pwVBox, pwRepVBox;
    private Button criar;
    private Hyperlink logConta;
    private VBox infoHBox;
    private VBox loginVBox;

    public RegisterUI(AppManager appManager) {
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
        titulo.setText("REGISTO");
        titulo.setFont( new Font("Times New Roman", 30) );
        titulo.setAlignment(Pos.CENTER);
        titulo.setPadding(new Insets(50, 0, 25, 0));








        nomeLabel = new Label();
        nomeLabel.setText("1.º e Último Nomes");
        nomeLabel.setFont( new Font("Arial", 15) );
        nomeLabel.setMinWidth(45);
        nomeLabel.setMinHeight(45);
        //nomeLabel.setAlignment(Pos.CENTER);
        //nomeLabel.setTextFill(Color.WHITE);

        nomeTField = new TextField();
        nomeTField.setPromptText("Indique o nome");
        nomeTField.setPrefColumnCount(10);
        nomeTField.setMinWidth(220);
        nomeTField.setMaxWidth(220);
        //nomeTField.getText();

        nomeVBox = new VBox();
        nomeVBox.setAlignment(Pos.CENTER);
        //nomeVBox.setSpacing(10);
        nomeVBox.setMinWidth(275);
        nomeVBox.setMaxWidth(275);
        //nomeVBox.setPadding(new Insets(50, 0, 0, 0));
        nomeVBox.getChildren().addAll(nomeLabel, nomeTField);













        nomeUserLabel = new Label();
        nomeUserLabel.setText("Nome de Utilizador");
        nomeUserLabel.setFont( new Font("Arial", 15) );
        nomeUserLabel.setMinWidth(45);
        nomeUserLabel.setMinHeight(45);
        //nomeUserLabel.setAlignment(Pos.CENTER);
        //nomeUserLabel.setTextFill(Color.WHITE);

        nomeUserTField = new TextField();
        nomeUserTField.setPromptText("Indique o nome de utilizador");
        nomeUserTField.setPrefColumnCount(10);
        nomeUserTField.setMinWidth(220);
        nomeUserTField.setMaxWidth(220);
        //nomeUserTField.getText();

        nomeUserVBox = new VBox();
        nomeUserVBox.setAlignment(Pos.CENTER);
        //nomeVBox.setSpacing(10);
        nomeUserVBox.setMinWidth(275);
        nomeUserVBox.setMaxWidth(275);
        //nomeVBox.setPadding(new Insets(50, 0, 0, 0));
        nomeUserVBox.getChildren().addAll(nomeUserLabel, nomeUserTField);


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
        pwVBox.getChildren().addAll(pwUserLabel, pwUserTField);

        pwUserRepLabel = new Label();
        pwUserRepLabel.setText("Confirmação da Palavra-Passe");
        pwUserRepLabel.setFont( new Font("Arial", 15) );
        pwUserRepLabel.setMinWidth(45);
        pwUserRepLabel.setMinHeight(45);
        //nomeUserLabel.setAlignment(Pos.CENTER);
        //nomeUserLabel.setTextFill(Color.WHITE);

        pwUserRepTField = new PasswordField();
        pwUserRepTField.setPromptText("Repita a palavra-passe");
        pwUserRepTField.setPrefColumnCount(10);
        pwUserRepTField.setMinWidth(220);
        pwUserRepTField.setMaxWidth(220);
        //pwUserTField.getText();

        pwRepVBox = new VBox();
        pwRepVBox.setAlignment(Pos.CENTER);
        //pwVBox.setSpacing(10);
        pwRepVBox.setMinWidth(275);
        pwRepVBox.setMaxWidth(275);
        pwRepVBox.setPadding(new Insets(0, 0, 25, 0));
        pwRepVBox.getChildren().addAll(pwUserRepLabel, pwUserRepTField);

        criar = new Button("CRIAR CONTA");
        //autenticar.setMinWidth(75);
        //autenticar.setMaxWidth(75);
        //autenticar.setPrefWidth(75);
        criar.setPadding(new Insets(10));

        logConta = new Hyperlink();
        logConta.setText("Já tem uma conta? Faça login...");
        logConta.setFont( new Font("Arial", 15) );
        logConta.setMinWidth(45);
        logConta.setMinHeight(45);

        infoHBox = new VBox();
        infoHBox.getChildren().addAll(logConta);
        infoHBox.setAlignment(Pos.CENTER);
        infoHBox.setPadding(new Insets(15, 0, 0, 0));
        infoHBox.setSpacing(-15);

        loginVBox = new VBox();
        loginVBox.getChildren().addAll(titulo, nomeVBox, nomeUserVBox, pwVBox, pwRepVBox, criar, infoHBox);
        loginVBox.setAlignment(Pos.CENTER);
        loginVBox.setSpacing(10);

        this.setTop(loginVBox);

    }

    private void registerHandlers() {

        appManager.addPropertyChangeListener(evt -> {
            update();
        });

        criar.setOnAction(event -> {
            appManager.registar(nomeTField.getText(), nomeUserTField.getText(), pwUserTField.getText(), pwUserRepTField.getText());
        });


        logConta.setOnAction(event -> {
            //System.out.println("O utilizador já tem uma conta...");
            nomeTField.clear();
            nomeUserTField.clear();
            pwUserTField.clear();
            pwUserRepTField.clear();
            appManager.login();
        });

    }

    private void update() {
        if (appManager.getState() != AppState.REGISTER) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}
