package pt.isec.g45.tp.ui.gui.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import pt.isec.g45.tp.model.AppManager;
import pt.isec.g45.tp.model.data.Sit;
import pt.isec.g45.tp.model.fsm.AppState;

import java.util.ArrayList;
import java.util.Arrays;

public class MakeReservationsUI extends BorderPane {

    AppManager appManager;

    private String cadeira;
    private ArrayList<String> lugaresSelecionados = new ArrayList<>();
    private int qntFilas = 6;
    private int lugaresFila = 10;
    Boolean passou = false;
    private ArrayList<String> letrasFilaArr = new ArrayList<>(
            Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R")
    );
    ArrayList<Sit> disposicao = new ArrayList<>();

    private Label titulo;
    private HBox nomeVBox;
    private Button confirmar, voltar;
    private VBox conjBotoesVBox;
    private VBox principalVBox;

    private HBox lugaresIndexHBox;
    private HBox numLugarHBox;
    private Label letraFila, numLugar;


    public MakeReservationsUI(AppManager appManager) {
        this.appManager = appManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        if(passou) {


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
            titulo.setText("Reserva de Lugar");
            titulo.setFont( new Font("Times New Roman", 30) );
            titulo.setAlignment(Pos.CENTER);
            titulo.setPadding(new Insets(35, 0, 10, 0));


            voltar = new Button("VOLTAR AO MENU PRINCIPAL");
            voltar.setPadding(new Insets(7.50));

            confirmar = new Button("CONFIRMAR");
            //confirmar.setMinWidth(75);
            //confirmar.setMaxWidth(75);
            //confirmar.setPrefWidth(75);
            confirmar.setPadding(new Insets(7.50));

            conjBotoesVBox = new VBox();
            conjBotoesVBox.setAlignment(Pos.CENTER);
            conjBotoesVBox.setSpacing(15);
            conjBotoesVBox.getChildren().addAll(confirmar, voltar);


            principalVBox = new VBox();
            //principalVBox.getChildren().add(titulo);
            principalVBox.setAlignment(Pos.CENTER);
            principalVBox.setSpacing(0);


            lugaresIndexHBox = new HBox();
            lugaresIndexHBox.setMinWidth(600);
            lugaresIndexHBox.setMaxWidth(600);
            lugaresIndexHBox.setAlignment(Pos.CENTER);
            lugaresIndexHBox.setSpacing(20);



            for (int k = 0; k < lugaresFila+1; k++) {

                if (k != 0) {
                    numLugarHBox = new HBox();
                    numLugarHBox.setMinWidth(35);
                    numLugarHBox.setMaxWidth(35);
                    numLugarHBox.setMinHeight(35);
                    numLugarHBox.setMaxHeight(35);
                    numLugarHBox.setAlignment(Pos.CENTER);

                    numLugar = new Label();
                    numLugar.setText(String.valueOf(k));
                    numLugar.setFont( new Font("Arial", 15) );
                    numLugar.setAlignment(Pos.CENTER);

                    numLugarHBox.getChildren().add(numLugar);
                }
                else {
                    numLugarHBox = new HBox();
                    numLugarHBox.setMinWidth(15);
                    numLugarHBox.setMaxWidth(15);
                    numLugarHBox.setMinHeight(15);
                    numLugarHBox.setMaxHeight(15);
                    numLugarHBox.setAlignment(Pos.CENTER);
                }


                lugaresIndexHBox.getChildren().add(numLugarHBox);
            }

            principalVBox.getChildren().add(lugaresIndexHBox);

            for (int i = 0; i < qntFilas; i++) {

                lugaresIndexHBox.setMinWidth(600);
                lugaresIndexHBox.setMaxWidth(600);

                letraFila = new Label();
                letraFila.setText(letrasFilaArr.get(i));
                letraFila.setFont( new Font("Arial", 15) );
                letraFila.setAlignment(Pos.CENTER);
                letraFila.setMinWidth(15);
                letraFila.setMaxWidth(15);
                letraFila.setMinHeight(15);
                letraFila.setMaxHeight(15);

                nomeVBox = new HBox();
                nomeVBox.setAlignment(Pos.CENTER);
                nomeVBox.setSpacing(20);
                nomeVBox.setMinWidth(600);
                nomeVBox.setMaxWidth(600);
                //nomeVBox.setPadding(new Insets(50, 0, 0, 0));
                nomeVBox.getChildren().add(letraFila);
                nomeVBox.setPadding(new Insets(0, 0, 20, 0));

              for (Sit s: appManager.getSitsFila(letraFila.getText())) {
                    //Button lugar = new Button(i + "/" + j);

                    Button lugar = new Button();
                    lugar.setText(""+s.getPreco());
                    lugar.setMinWidth(35);
                    lugar.setMaxWidth(35);
                    lugar.setMinHeight(35);
                    lugar.setMaxHeight(35);
                    lugar.setPadding(new Insets(10));
                    if (s.getOcupado()) {
                        lugar.setStyle("-fx-background-color: #ff0000; ");
                        lugar.setDisable(true);
                    }
                    else
                        lugar.setStyle("-fx-background-color: #eaeaea; ");
                    nomeVBox.getChildren().add(lugar);


                    lugar.setOnAction(evt -> {
                        System.out.println("Cliquei no botão " + letraFila.getText() + "/" + s.getAssento() );
                        String aux = lugar.getStyle().equals("-fx-background-color: #eaeaea; ")?"-fx-background-color: #00ff00; ":"-fx-background-color: #eaeaea; ";
                        //System.out.println(aux);
                        lugar.setStyle(aux);
                        if (lugar.getStyle().equals("-fx-background-color: #00ff00; "))
                            lugaresSelecionados.add(letraFila.getText() + "/" + s.getAssento());
                        else
                            lugaresSelecionados.remove(letraFila.getText() + "/" + s.getAssento());
                    });
                }

                principalVBox.getChildren().addAll(nomeVBox);

            }


            principalVBox.getChildren().add(conjBotoesVBox);

            this.setTop(principalVBox);
        }
    }

    private void registerHandlers() {
        appManager.addPropertyChangeListener(evt -> {
            update();
        });
        if (passou) {
            confirmar.setOnAction(event -> {
                System.out.println("Cliquei no botao «CONFIRMAR»");
                System.out.println("Os lugares selecionados são: " + lugaresSelecionados);
                appManager.reserve(lugaresSelecionados);
                //System.out.println("O lugar escolhido foi: " + cadeira);
            });

            voltar.setOnAction(event -> {
                appManager.begin_menu(null);
                lugaresSelecionados.clear();
            });
        }




    }

    private void update() {
        if (appManager.getState() != AppState.MAKE_RESERVATION) {
            this.setVisible(false);
            passou = false;
            return;
        }
        this.lugaresFila = appManager.getSize()[1];
        this.qntFilas = appManager.getSize()[0];
        disposicao = appManager.disposicao();
        passou =true;

        createViews();
        registerHandlers();
        this.setVisible(true);

    }
}
