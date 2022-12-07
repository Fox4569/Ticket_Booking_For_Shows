package pt.isec.g45.tp.model.fsm;


import pt.isec.g45.tp.model.data.Show;
import pt.isec.g45.tp.model.data.Sit;
import pt.isec.g45.tp.utils.*;

import java.util.ArrayList;

public interface IAppState {
    AppState getState();

    String begin_menu(Authentication file);
    void login();
    void makeReservation();
    void consulta_menu();
    void register();
    void reporPw();
    void selectShow();
    void logout();
    void adminPanel();

    void userDataEditing();


    ArrayList<Reserva> getNotPayedReservations();

    ArrayList<Reserva>  getPayedReservations();

    void insertShow(String filename);
    void deleteShow(int espetaculoID);

    void editData() ;
    void deletePayedReservation();
    ArrayList<Espetaculo> getFilteredShow(FilterShow filter, String nome);
    void getSelectedShow();
    void selectSits();

    void deleteNotPayedReservation(int esptaculoid);
    void payReservation();

     ArrayList<Show> getShows() ;

     ArrayList<Sit> disposicao(int idEspetaculo);

     int[] getSize(int idEspetaculo);
     void reserve(Show i, ArrayList<Lugar> lugares);
}
