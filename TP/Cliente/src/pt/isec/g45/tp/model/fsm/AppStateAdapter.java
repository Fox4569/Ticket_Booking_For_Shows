package pt.isec.g45.tp.model.fsm;

import pt.isec.g45.tp.model.ClientManager;
import pt.isec.g45.tp.model.data.AppData;
import pt.isec.g45.tp.model.data.Show;
import pt.isec.g45.tp.model.data.Sit;
import pt.isec.g45.tp.utils.*;

import java.util.ArrayList;

public class AppStateAdapter implements IAppState{

    AppContext context;
    AppData data;

    public AppStateAdapter(AppContext context, AppData data) {
        this.context = context;
        this.data = data;
    }

    protected void changeState(AppState newState) {
        context.changeState(newState.createState(context,data));
    }
    protected ClientManager getClientManager() {
        return context.getcManager();
    }
    @Override
    public AppState getState() {
        return null;
    }

    @Override
    public String begin_menu(Authentication file) {
        return "";
    }

    @Override
    public void insertShow(String filename) {

    }

    @Override
    public void reserve(Show i, ArrayList<Lugar> lugares) {

    }

    @Override
    public void deleteShow(int espetaculoID) {

    }

    @Override
    public void adminPanel() {

    }

    @Override
    public int[] getSize(int idEspetaculo) {
        return new int[0];
    }

    @Override
    public ArrayList<Sit> disposicao(int idEspetaculo) {
        return null;
    }

    @Override
    public ArrayList<Reserva> getNotPayedReservations() {
        return null;
    }

    @Override
    public ArrayList<Reserva> getPayedReservations() {
        return null;
    }

    @Override
    public void editData() {

    }

    @Override
    public void deletePayedReservation() {

    }

    @Override
    public ArrayList<Espetaculo> getFilteredShow(FilterShow filter, String nome) {
        return null;
    }

    @Override
    public void getSelectedShow() {

    }

    @Override
    public void selectSits() {

    }

    @Override
    public void deleteNotPayedReservation(int espetaculoID) {

    }

    @Override
    public ArrayList<Show> getShows() {
        return null;
    }

    @Override
    public void payReservation() {

    }

    @Override
    public void login() {

    }
    @Override
    public void makeReservation() {

    }
    @Override
    public void consulta_menu() {

    }
    @Override
    public void register() {

    }
    @Override
    public void reporPw() {

    }
    @Override
    public void selectShow() {

    }
    @Override
    public void logout() {

    }

    @Override
    public void userDataEditing() {

    }
}
