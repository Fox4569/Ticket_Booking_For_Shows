package pt.isec.g45.tp.model.fsm;

import pt.isec.g45.tp.model.ClientManager;
import pt.isec.g45.tp.model.data.AppData;
import pt.isec.g45.tp.model.data.Show;
import pt.isec.g45.tp.model.data.Sit;
import pt.isec.g45.tp.model.fsm.states.AdminPanelState;
import pt.isec.g45.tp.model.fsm.states.LoginState;
import pt.isec.g45.tp.model.fsm.states.MakeReservationState;
import pt.isec.g45.tp.utils.*;

import java.util.ArrayList;

public class AppContext {

    IAppState state;
    AppData data;
    ClientManager cManager;
    public AppContext(ClientManager cManager) {
        data = new AppData();
        state = new LoginState(this, data);
        this.cManager = cManager;
    }

    public ClientManager getcManager() {
        return cManager;
    }
    public AppData getData() {
        return data;
    }

    /**
     * Modifica o estado atual da aplicação.
     *
     * Esta função não retorna nada.
     * @see IAppState
     * @see AppState
     * */
    public void changeState(IAppState newState) {
        this.state = newState;
    }

    /**
     * Obtem o estado atual da aplicação
     *
     * @return AppState O estado atual da aplicação
     *  @see IAppState
     *  @see AppState
     * */
    public ArrayList<Sit> disposicao(int idEspetaculo){
        return state.disposicao(idEspetaculo);
    }
    public Show getShow(int idEspetaculo) {
        return data.getShow(idEspetaculo);
    }
    public ArrayList<Lugar> getLugares(int idEspetaculo, ArrayList <String> bts){
        ArrayList<Lugar> aux = new ArrayList<>();
        ArrayList<Lugar> sits = data.getShow(idEspetaculo).getLugares();
        for (String s: bts) {
            String fila = ""+s.charAt(0);
            String col = ""+s.charAt(2);
            for (Lugar e: sits) {
                if (fila.equals(e.getFila()) && col.equals(e.getAssento())) {
                    aux.add(e);
                    break;
                }
            }
        }
        return aux;
    }
    public void reserve(Show i, ArrayList<Lugar> lugares) {
        state.reserve(i,lugares);
    }
    public ArrayList<Sit> getSitsFila(int idEspetaculo, String fila) {
        return data.getShow(idEspetaculo).getSitsFila(fila);
    }

    public int[] getSize(int idEspetaculo) {
        return state.getSize(idEspetaculo);
    }
    public AppState getState() {
        if (this.state == null) {
            return null;
        }
        return state.getState();
    }
    public void logout() {
        state.logout();
    }
    public void consulta_menu() {
        state.consulta_menu();
    }
    public void selectShow() {
        state.selectShow();
    }
    public void makeReservation() {
        state.makeReservation();
    };
    public void login() {
        state.login();
    };
    public String begin_menu(Authentication file) {
        return state.begin_menu(file);
    }
    public void register() {
        state.register();
    }
    public void reporPw() {
        state.reporPw();
    }
    public void adminPanel() {
        state.adminPanel();
    }

    public void userDataEditing() {
        state.editData();
    }

    public ArrayList<Reserva> getNotPayedReservations(){
        return state.getNotPayedReservations();
    }


    public void insertShow(String filename) {
        state.insertShow(filename);
    }


    public void deleteShow(int espetaculoID) {
        state.deleteShow(espetaculoID);
    }

    public ArrayList<Reserva> getPayedReservations(){
        return state.getPayedReservations();
    }

    public void editData() {
        state.editData();
    }
    public void deletePayedReservation() {
        state.deletePayedReservation();
    }

    public ArrayList<Espetaculo> getFilteredShow(FilterShow filter, String nome){
       return state.getFilteredShow(filter, nome);
    }
    public void getSelectedShow(){
        state.getSelectedShow();
    }
    public void selectSits(){
        state.selectSits();
    }

    public void deleteNotPayedReservation(int espetaculoID){
        state.deleteNotPayedReservation(espetaculoID);
    }

    public void payReservation(){
        state.payReservation();
    }

    public ArrayList<Show> getShows() {
        return state.getShows();
    }

}
