package pt.isec.g45.tp.ui.gui.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import pt.isec.g45.tp.model.AppManager;
import pt.isec.g45.tp.model.fsm.AppState;
import pt.isec.g45.tp.utils.UtilsUI;

import java.util.Optional;

public class AdminPanelUI extends BorderPane {

    AppManager appManager;

    private Label titulo;

    private Label esquerdaLabel, direitaLabel;

    private Button reservasSemPagamentoButton, reservasPagasButton, eliminarReservaButton, insercaoEspetaculoButton;
    private Button eliminarEspetaculoButton, alterarEstadoEspetaculoButton, obterEspetaculos;

    private Button logout;

    private TextArea direitaTArea;

    private VBox esquerdaVBox, direitaVBox;
    private HBox conjuntoHBox;
    private VBox principalVBox;


    public AdminPanelUI(AppManager appManager) {
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
        titulo.setText("PAINEL DE ADMINISTRAÇÃO");
        titulo.setFont( new Font("Times New Roman", 30) );
        titulo.setAlignment(Pos.CENTER);
        titulo.setPadding(new Insets(50, 0, 50, 0));



        esquerdaLabel = new Label();
        esquerdaLabel.setText("AÇÕES POSSÍVEIS");
        esquerdaLabel.setFont( new Font(25) );
        esquerdaLabel.setAlignment(Pos.CENTER);
        esquerdaLabel.setPadding(new Insets(0, 0, 10, 0));

        reservasSemPagamentoButton = new Button();
        reservasSemPagamentoButton.setText("Reservas não pagas");
        reservasSemPagamentoButton.setMinWidth(175);
        reservasSemPagamentoButton.setMaxWidth(175);
        reservasSemPagamentoButton.setMinHeight(30);
        reservasSemPagamentoButton.setMaxHeight(30);
        reservasPagasButton = new Button();
        reservasPagasButton.setText("Reservas pagas");
        reservasPagasButton.setMinWidth(175);
        reservasPagasButton.setMaxWidth(175);
        reservasPagasButton.setMinHeight(30);
        reservasPagasButton.setMaxHeight(30);
        eliminarReservaButton = new Button();
        eliminarReservaButton.setText("Eliminar reserva não paga");
        eliminarReservaButton.setMinWidth(175);
        eliminarReservaButton.setMaxWidth(175);
        eliminarReservaButton.setMinHeight(30);
        eliminarReservaButton.setMaxHeight(30);
        insercaoEspetaculoButton = new Button();
        insercaoEspetaculoButton.setText("Inserir espetáculo");
        insercaoEspetaculoButton.setMinWidth(175);
        insercaoEspetaculoButton.setMaxWidth(175);
        insercaoEspetaculoButton.setMinHeight(30);
        insercaoEspetaculoButton.setMaxHeight(30);
        eliminarEspetaculoButton = new Button();
        eliminarEspetaculoButton.setText("Eliminar espetáculo");
        eliminarEspetaculoButton.setMinWidth(175);
        eliminarEspetaculoButton.setMaxWidth(175);
        eliminarEspetaculoButton.setMinHeight(30);
        eliminarEspetaculoButton.setMaxHeight(30);
        alterarEstadoEspetaculoButton = new Button();
        alterarEstadoEspetaculoButton.setText("Alterar estado espetáculo");
        alterarEstadoEspetaculoButton.setMinWidth(175);
        alterarEstadoEspetaculoButton.setMaxWidth(175);
        alterarEstadoEspetaculoButton.setMinHeight(30);
        alterarEstadoEspetaculoButton.setMaxHeight(30);
        obterEspetaculos = new Button();
        obterEspetaculos.setText("Obter Espetaculos");
        obterEspetaculos.setMinWidth(175);
        obterEspetaculos.setMaxWidth(175);
        obterEspetaculos.setMinHeight(30);
        obterEspetaculos.setMaxHeight(30);

        logout = new Button();
        logout.setText("Logout");
        logout.setMinWidth(175);
        logout.setMaxWidth(175);
        logout.setMinHeight(30);
        logout.setMaxHeight(30);


        esquerdaVBox = new VBox();
        esquerdaVBox.setAlignment(Pos.TOP_CENTER);
        esquerdaVBox.getChildren().add(esquerdaLabel);
        esquerdaVBox.getChildren().add(reservasSemPagamentoButton);
        esquerdaVBox.getChildren().add(reservasPagasButton);
        esquerdaVBox.getChildren().add(eliminarReservaButton);
        esquerdaVBox.getChildren().add(insercaoEspetaculoButton);
        esquerdaVBox.getChildren().add(eliminarEspetaculoButton);
        esquerdaVBox.getChildren().add(alterarEstadoEspetaculoButton);
        esquerdaVBox.getChildren().add(obterEspetaculos);
        esquerdaVBox.getChildren().add(logout);

        esquerdaVBox.setMinWidth(UtilsUI.windowWidth * (1.0/3));
        esquerdaVBox.setMaxWidth(UtilsUI.windowWidth * (1.0/3));
        esquerdaVBox.setSpacing(15);


        direitaLabel = new Label();
        direitaLabel.setText("RESULTADO");
        direitaLabel.setFont( new Font(25) );
        direitaLabel.setAlignment(Pos.CENTER);

        direitaTArea = new TextArea();
        direitaTArea.setText(appManager.getShows());
        direitaTArea.setEditable(false);
        direitaTArea.setMinWidth(UtilsUI.windowWidth * (2.0/3) -75);
        direitaTArea.setMaxWidth(UtilsUI.windowWidth * (2.0/3) -75);
        direitaTArea.setMinHeight(UtilsUI.windowHeight -250);
        direitaTArea.setMaxHeight(UtilsUI.windowHeight -250);


        direitaVBox = new VBox();
        direitaVBox.setAlignment(Pos.TOP_CENTER);
        direitaVBox.getChildren().addAll(direitaLabel, direitaTArea);
        direitaVBox.setMinWidth(UtilsUI.windowWidth * (2.0/3));
        direitaVBox.setMaxWidth(UtilsUI.windowWidth * (2.0/3));
        direitaVBox.setSpacing(25);

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
        obterEspetaculos.setOnAction(event -> {
            direitaTArea.setText(appManager.getShows());
        });

        logout.setOnAction(event -> {
            direitaTArea.clear();
            appManager.logout();
        });
        reservasPagasButton.setOnAction(event -> {
            direitaTArea.clear();
            direitaTArea.setText(appManager.getPayedReservations());

        });
        reservasSemPagamentoButton.setOnAction(event -> {
            direitaTArea.clear();
            direitaTArea.setText(appManager.getNotPayedReservations());

        });

        insercaoEspetaculoButton.setOnAction(event -> {
            TextInputDialog tid = new TextInputDialog();
            tid.setTitle("Inserir espetáculo");
            tid.setHeaderText("Indique o nome do ficheiro");
            tid.setContentText("filename");
            Optional<String> result = tid.showAndWait();
            result.ifPresent(s-> {
                System.out.println("filename: " + s);
                appManager.insertShow(s);
            });
        });

        eliminarEspetaculoButton.setOnAction(event -> {
            TextInputDialog tid = new TextInputDialog();
            tid.setTitle("Eliminar espetáculo");
            tid.setHeaderText("Indique o espetaculo_ID");
            tid.setContentText("ID");
            Optional<String> result = tid.showAndWait();


            result.ifPresent(s-> {
                System.out.println("Espetaculo_ID: " + Integer.parseInt(s));
                appManager.deleteShow(Integer.parseInt(s));
            });

        });

        alterarEstadoEspetaculoButton.setOnAction(event -> {
            TextInputDialog tid = new TextInputDialog();
            tid.setTitle("Alterar estado espetáculo");
            tid.setHeaderText("Indique o espetaculo_ID");
            tid.setContentText("ID");
            Optional<String> result = tid.showAndWait();

            System.out.println("espetaculo_ID: " + result.toString());
        });

        eliminarReservaButton.setOnAction(event -> {
            TextInputDialog tid = new TextInputDialog();
            tid.setTitle("Eliminar reserva não paga");
            tid.setHeaderText("Indique a reserva_ID");
            tid.setContentText("ID");
            Optional<String> result = tid.showAndWait();
            result.ifPresent(s-> {
                System.out.println("Espetaculo_ID: " + Integer.parseInt(s));
                appManager.deleteNotPayedReservation(Integer.parseInt(s));
            });
            System.out.println("reserva_ID: " + result.toString());
        });

    }

    private void update() {
        if (appManager.getState() != AppState.ADMIN_PANEL_STATE) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}
