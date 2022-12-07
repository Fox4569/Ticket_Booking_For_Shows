package pt.isec.g45.tp.ui.gui.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import pt.isec.g45.tp.model.AppManager;
import pt.isec.g45.tp.model.fsm.AppState;
import pt.isec.g45.tp.utils.UtilsUI;

public class UserDataEditingUI extends BorderPane {

    AppManager appManager;

    private Label titulo;
    private Label esquerdaLabel, direitaLabel;
    private HBox conjuntoHBox;

    private Label nomeAtualLabel, usernameAtualLabel, passwordAtualLabel;
    private Label nomeAtualTField, usernameAtualTField, passwordAtualTFiel;
    private VBox nomeAtualVBox, usernameAtualVBox, passwordAtualVBox;
    private Label novoNomeLabel, novoUsernameLabel, novaPasswordLabel, novaPasswordRepLabel;
    private TextField novoNomeTField, novoUsernameTField, novaPasswordTField, novaPassordRepTField;
    private VBox novoNomeVBox, novoUsernameVBox, novaPasswordVBox, novaPasswordRepVBox;

    private VBox dadosAtuaisVBox, novosDadosVBox;
    private VBox esquerdaVBox, direitaVBox;

    private VBox principalVBox;



    public UserDataEditingUI(AppManager appManager) {
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
        titulo.setText("EDITAR DADOS DO UTILIZADOR");
        titulo.setFont( new Font("Times New Roman", 30) );
        titulo.setAlignment(Pos.TOP_CENTER);
        titulo.setPadding(new Insets(50, 0, 50, 0));













        esquerdaLabel = new Label();
        esquerdaLabel.setText("DADOS ATUAIS");
        esquerdaLabel.setFont( new Font(25) );
        esquerdaLabel.setAlignment(Pos.CENTER);

        nomeAtualLabel = new Label();
        nomeAtualLabel.setText("Nome");
        nomeAtualLabel.setAlignment(Pos.CENTER);
        nomeAtualLabel.setFont(new Font(20));
        nomeAtualTField = new Label();
        nomeAtualTField.setText("Nome..");
        nomeAtualTField.setAlignment(Pos.CENTER);
        nomeAtualTField.setFont(new Font(15));
        nomeAtualVBox = new VBox();
        nomeAtualVBox.getChildren().addAll(nomeAtualLabel, nomeAtualTField);
        nomeAtualVBox.setAlignment(Pos.CENTER);
        nomeAtualVBox.setPadding(new Insets(10));



        usernameAtualLabel = new Label();
        usernameAtualLabel.setText("Nome de Utilizador");
        usernameAtualLabel.setAlignment(Pos.CENTER);
        usernameAtualLabel.setFont(new Font(20));
        usernameAtualTField = new Label();
        usernameAtualTField.setText("Nome...");
        usernameAtualTField.setAlignment(Pos.CENTER);
        usernameAtualTField.setFont(new Font(15));
        usernameAtualVBox = new VBox();
        usernameAtualVBox.getChildren().addAll(usernameAtualLabel, usernameAtualTField);
        usernameAtualVBox.setAlignment(Pos.CENTER);
        usernameAtualVBox.setPadding(new Insets(10));


        passwordAtualLabel = new Label();
        passwordAtualLabel.setText("Palavra-passe");
        passwordAtualLabel.setAlignment(Pos.CENTER);
        passwordAtualLabel.setFont(new Font(20));
        passwordAtualTFiel = new Label();
        passwordAtualTFiel.setText("****");
        passwordAtualTFiel.setAlignment(Pos.CENTER);
        passwordAtualTFiel.setFont(new Font(15));
        passwordAtualVBox = new VBox();
        passwordAtualVBox.getChildren().addAll(passwordAtualLabel, passwordAtualTFiel);
        passwordAtualVBox.setAlignment(Pos.CENTER);
        passwordAtualVBox.setPadding(new Insets(10));

        esquerdaVBox = new VBox();
        esquerdaVBox.setAlignment(Pos.TOP_CENTER);
        esquerdaVBox.getChildren().addAll(esquerdaLabel, nomeAtualVBox, usernameAtualVBox, passwordAtualVBox);
        esquerdaVBox.setMinWidth(UtilsUI.windowWidth /2.0);
        esquerdaVBox.setMaxWidth(UtilsUI.windowWidth /2.0);














        direitaLabel = new Label();
        direitaLabel.setText("DADOS NOVOS");
        direitaLabel.setFont( new Font(25) );
        direitaLabel.setAlignment(Pos.CENTER);

        direitaVBox = new VBox();
        direitaVBox.setAlignment(Pos.TOP_CENTER);
        direitaVBox.getChildren().add(direitaLabel);
        direitaVBox.setMinWidth(UtilsUI.windowWidth /2.0);
        direitaVBox.setMaxWidth(UtilsUI.windowWidth /2.0);













        conjuntoHBox = new HBox();
        conjuntoHBox.getChildren().addAll(esquerdaVBox, direitaVBox);
        conjuntoHBox.setAlignment(Pos.CENTER);
        conjuntoHBox.setMinWidth(UtilsUI.windowWidth);
        conjuntoHBox.setMaxWidth(UtilsUI.windowWidth);










        principalVBox = new VBox();
        principalVBox.getChildren().addAll(titulo, conjuntoHBox);
        principalVBox.setAlignment(Pos.TOP_CENTER);
        principalVBox.setMinWidth(UtilsUI.windowWidth);
        principalVBox.setMaxWidth(UtilsUI.windowWidth);
        principalVBox.setMinHeight(UtilsUI.windowHeight);
        principalVBox.setMaxHeight(UtilsUI.windowHeight);

        this.setTop(principalVBox);

    }

    private void registerHandlers() {
        appManager.addPropertyChangeListener(evt -> {
            update();
        });

    }

    private void update() {
        if (appManager.getState() != AppState.USER_DATA_EDITING_STATE) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}
