package pt.isec.g45.tp.model;

import pt.isec.g45.tp.model.data.Show;
import pt.isec.g45.tp.model.data.Sit;
import pt.isec.g45.tp.model.fsm.AppContext;
import pt.isec.g45.tp.model.fsm.AppState;
import pt.isec.g45.tp.utils.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class AppManager {

    private AppContext fsm;

    PropertyChangeSupport pcs;
    ClientManager cManager;
    int aux = 0;

    public AppManager(List<String> args) {
        this.cManager= new ClientManager(args.get(0), args.get(1), this);
        this.fsm = new AppContext(cManager);
        this.pcs = new PropertyChangeSupport(this);
        this.cManager.start();
    }

    public AppContext getFsm() {
        return fsm;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
    public AppState getState() {
        //System.out.println("ESTADO ATUAL: " + fsm.getState());
        return fsm.getState();
    }
    public void admin_Panel() {
        fsm.adminPanel();
        pcs.firePropertyChange(null, null, null);
    }
    public String begin_menu(Authentication file) {
        String aux;
        aux = fsm.begin_menu(file);
        pcs.firePropertyChange(null,null,null);
        return aux;
    }
    public void login() {
        fsm.login();
        pcs.firePropertyChange(null,null,null);
    }
    public void makeReservation(int idEspetaculo) {
        aux = idEspetaculo;
        fsm.makeReservation();
        pcs.firePropertyChange(null,null,null);
    }
    public void consulta_menu() {
        fsm.consulta_menu();
        pcs.firePropertyChange(null,null,null);
    }
    public void register() {
        fsm.register();
        pcs.firePropertyChange(null,null,null);
    }
    public void reporPw() {
        fsm.reporPw();
        pcs.firePropertyChange(null, null, null);
    }
    public void selectShow() {
        fsm.selectShow();
        pcs.firePropertyChange(null,null,null);
    }

    public void logout() {
        fsm.logout();
        pcs.firePropertyChange(null,null,null);

    }

    public String autenticar(String nome, String password) {
        System.out.println("Nome: " + nome + "\nPassword: " + password);
        Authentication file = new Authentication(nome, password, AuthenticationEnum.LOGIN);
        return begin_menu(file);
    }
    public void verificaUser(String nome) {
        System.out.println("Nome: " + nome);
    }
    public void registar(String nome, String nomeUser, String password, String passwordRep) {
        System.out.println("Nome: " + nome + "\nUsername: " + nomeUser + "\nPassword: " + password + "\nPassword_REP: " + passwordRep);
        Authentication file = new Authentication(nomeUser, password, nome, AuthenticationEnum.REGISTER);
        begin_menu(file);
    }
    public String getNotPayedReservations(){
        ArrayList<Reserva> s = fsm.getNotPayedReservations();
        String v = s == null? "":s.toString();
        System.out.println(v);
        pcs.firePropertyChange(null,null,null);
        return v;
    }

    public String getPayedReservations(){
        ArrayList<Reserva> s = fsm.getPayedReservations();
        String v = s == null? "":s.toString();
        System.out.println(v);
        pcs.firePropertyChange(null,null,null);
        return v;
    }

    public void editData() {
        fsm.editData();
        pcs.firePropertyChange(null,null,null);
    }
    public void deletePayedReservation() {
        fsm.deletePayedReservation();
        pcs.firePropertyChange(null,null,null);
    }

    public String getFilteredShow(FilterShow fs, String nome){
        ArrayList<Espetaculo> s = fsm.getFilteredShow(fs,nome);

        String v = s == null? "":s.toString();
        System.out.println(v);
        pcs.firePropertyChange(null,null,null);
        return v;
    }
    public void getSelectedShow(){
        fsm.getSelectedShow();
        pcs.firePropertyChange(null,null,null);
    }
    public void selectSits(){
        fsm.selectSits();
        pcs.firePropertyChange(null,null,null);
    }

    public void deleteNotPayedReservation(int espetaculoID){
        fsm.deleteNotPayedReservation(espetaculoID);
        pcs.firePropertyChange(null,null,null);
    }

   public void payReservation(){
        fsm.payReservation();
        pcs.firePropertyChange(null,null,null);
   }
    public String getShows() {
        ArrayList<Show> s = fsm.getShows();
        String v = s == null? "":s.toString();
        System.out.println(v);
        pcs.firePropertyChange(null,null,null);

        return v;
    }


    public void insertShow(String filename) {
        fsm.insertShow(filename);
        pcs.firePropertyChange(null,null,null);

    }


    public void deleteShow(int espetaculoID) {
        fsm.deleteShow(espetaculoID);
        pcs.firePropertyChange(null,null,null);

    }

    public ArrayList<Sit> disposicao(){
        return fsm.disposicao(aux);
    }

    public int[] getSize() {
        return fsm.getSize(aux);
    }


    public int getAux() {
        return aux;
    }

    public void setAux(int aux) {
        this.aux = aux;
    }
    public void reserve(ArrayList<String> buttons) {
        Show i = fsm.getShow(aux);
        ArrayList <Lugar> lugares = fsm.getLugares(aux, buttons);

        fsm.reserve(i,lugares);
        pcs.firePropertyChange(null, null, null);
    }

    public ArrayList<Sit> getSitsFila(String fila) {
        return fsm.getShow(aux).getSitsFila(fila);
    }
}
