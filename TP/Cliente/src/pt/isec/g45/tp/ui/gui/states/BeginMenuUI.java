package pt.isec.g45.tp.ui.gui.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import pt.isec.g45.tp.model.AppManager;
import pt.isec.g45.tp.model.fsm.AppState;
import pt.isec.g45.tp.utils.FilterShow;
import pt.isec.g45.tp.utils.UtilsUI;

import java.util.List;
import java.util.Optional;

public class BeginMenuUI extends BorderPane {

    AppManager appManager;

    private Label titulo;

    private Label esquerdaLabel, direitaLabel;

    private Button editarDadosRegisto, consultarEspetaculosFiltros;
    private Button visualizarLugares;
    private Button logout;

    private TextArea direitaTArea;

    private VBox esquerdaVBox, direitaVBox;
    private HBox conjuntoHBox;
    private VBox principalVBox;


    public BeginMenuUI(AppManager appManager) {
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
        titulo.setText("MENU PRINCIPAL");
        titulo.setFont( new Font("Times New Roman", 30) );
        titulo.setAlignment(Pos.CENTER);
        titulo.setPadding(new Insets(50, 0, 50, 0));



        esquerdaLabel = new Label();
        esquerdaLabel.setText("AÇÕES POSSÍVEIS");
        esquerdaLabel.setFont( new Font(25) );
        esquerdaLabel.setAlignment(Pos.CENTER);
        esquerdaLabel.setPadding(new Insets(0, 0, 10, 0));

        editarDadosRegisto = new Button();
        editarDadosRegisto.setText("Editar dados de registo");
        editarDadosRegisto.setMinWidth(175);
        editarDadosRegisto.setMaxWidth(175);
        editarDadosRegisto.setMinHeight(30);
        editarDadosRegisto.setMaxHeight(30);

        consultarEspetaculosFiltros = new Button();
        consultarEspetaculosFiltros.setText("Consultar espetáculos");
        consultarEspetaculosFiltros.setMinWidth(175);
        consultarEspetaculosFiltros.setMaxWidth(175);
        consultarEspetaculosFiltros.setMinHeight(30);
        consultarEspetaculosFiltros.setMaxHeight(30);

        visualizarLugares = new Button();
        visualizarLugares.setText("Visualizar lugares");
        visualizarLugares.setMinWidth(175);
        visualizarLugares.setMaxWidth(175);
        visualizarLugares.setMinHeight(30);
        visualizarLugares.setMaxHeight(30);

        logout = new Button();
        logout.setText("Logout");
        logout.setMinWidth(175);
        logout.setMaxWidth(175);
        logout.setMinHeight(30);
        logout.setMaxHeight(30);


        esquerdaVBox = new VBox();
        esquerdaVBox.setAlignment(Pos.TOP_CENTER);
        esquerdaVBox.getChildren().add(esquerdaLabel);
        esquerdaVBox.getChildren().add(editarDadosRegisto);
        esquerdaVBox.getChildren().add(consultarEspetaculosFiltros);
        esquerdaVBox.getChildren().add(visualizarLugares);

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

        editarDadosRegisto.setOnAction(event -> {
            appManager.editData();
        });

        logout.setOnAction(event -> {
            direitaTArea.clear();
            appManager.logout();
        });

        consultarEspetaculosFiltros.setOnAction(event -> {

            List<FilterShow> list = List.of(FilterShow.NOME, FilterShow.LOCALIDADE, FilterShow.GENERO, FilterShow.DATA);
            ChoiceDialog<FilterShow> cd = new ChoiceDialog<>(list.get(0), list);
            cd.setTitle("Consultar espetáculo");
            cd.setHeaderText("Indique o tipo de filtro");
            cd.setContentText("Filtro:");

            cd.showAndWait().ifPresent(response -> {
                TextInputDialog tid = new TextInputDialog();
                tid.setTitle("Consultar espetáculo");
                tid.setHeaderText(response.toString());
                tid.setContentText("valor: ");
                Optional<String> result = tid.showAndWait();

                result.ifPresent(r -> direitaTArea.setText(appManager.getFilteredShow(response, r)));
            });

        });

        visualizarLugares.setOnAction(event -> {

            //direitaTArea.setText();
            direitaTArea.setText(appManager.getShows());
            TextInputDialog tid = new TextInputDialog();
            tid.setTitle("Visualizar lugares");
            tid.setHeaderText("Indique o espetaculo_ID");
            tid.setContentText("ID");
            Optional<String> result = tid.showAndWait();


            result.ifPresent(s-> {
                System.out.println("Espetaculo_ID: " + Integer.parseInt(s));
                appManager.makeReservation(Integer.parseInt(s));

            });
        });

    }

    private void update() {
        if (appManager.getState() != AppState.BEGIN_MENU) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}
