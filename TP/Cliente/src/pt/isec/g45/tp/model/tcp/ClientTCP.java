package pt.isec.g45.tp.model.tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.isec.g45.tp.model.ClientManager;
import pt.isec.g45.tp.utils.*;

import static pt.isec.g45.tp.utils.CodigosTCP.*;

public class ClientTCP extends  Thread{
    HashMap<Integer, HashMap<InetAddress, Integer>> listaServers;
    boolean res;
    Socket socket;
    InetAddress addr;
    int port;
    ClientManager cmanager;
    ObjectOutputStream out;
    ObjectInputStream in;
    public ClientTCP(HashMap<Integer,HashMap<InetAddress, Integer>> listaServers, ClientManager cmanager) {
        this.listaServers = listaServers;
        socket = null;
        this.cmanager = cmanager;
    }

    public ArrayList<Reserva> getNotPayedReservations(){ // FEITO
        try{

            MsgFromClientTCP toSV = new MsgFromClientTCP(GET_NOT_PAYED_RESERVATIONS);
            out.writeObject(toSV);
            out.flush();
            ArrayList<Reserva> reservas = new ArrayList<>();
            Reserva aux;
            do {
                aux = (Reserva) in.readObject();
                if (aux.getId() != -1)
                    reservas.add(aux);
            }while(aux.getId() != -1);

            System.out.println(reservas);
            return reservas;

        } catch (IOException | ClassNotFoundException e) {
            return null;
        }

    }
    public ArrayList<Lugar> getAvailableSits(int espetaculoID){ // FEITO
        try{
            String [] args = {String.valueOf(espetaculoID)};
            MsgFromClientTCP toSV = new MsgFromClientTCP(SELECT_AVAILABLE_SITS_FROM_SHOW,args);
            out.writeObject(toSV);
            out.flush();
            ArrayList<Lugar> reservas = new ArrayList<>();
            Lugar aux;
            do {
                aux = (Lugar) in.readObject();
                if (aux.getId() != -1){
                    reservas.add(aux);
                    System.out.println("ESTOU AQUI -> " + aux);
                }

            }while(aux.getId() != -1);

            System.out.println("MASTER_DEUBFG "+reservas);

        return reservas;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public ArrayList<Lugar> getSits(int espetaculoID){ // FEITO
        try{
            //String [] args
            String [] args = {String.valueOf(espetaculoID)};

            MsgFromClientTCP toSV = new MsgFromClientTCP(ASK_FOR_SITS,args);
            out.writeObject(toSV);
            out.flush();
            ArrayList<Lugar> reservas = new ArrayList<>();
            Lugar aux;
            do {
                aux = (Lugar) in.readObject();
                System.out.println(aux);
                if (aux.getId() != -1)
                    reservas.add(aux);
            }while(aux.getId() != -1);

            System.out.println("MASTER_DEBIIIG: " + reservas);
        return reservas;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean insertShow(String filename){ // FEITO
        try{
            String [] args= {filename};
            MsgFromClientTCP toSV = new MsgFromClientTCP(INSERT_SHOW,args);
            out.writeObject(toSV);
            out.flush();
            String aux;


            aux = (String) in.readObject();


            System.out.println(aux);
            return true;

        } catch (IOException | ClassNotFoundException e) {
            return false;
        }

    }
    public boolean deleteShow(int idEspetaculo){ // FEITO
        try{
            String [] args= {""+idEspetaculo};
            MsgFromClientTCP toSV = new MsgFromClientTCP(DELETE_SHOW,args);
            out.writeObject(toSV);
            out.flush();
            String aux;


            aux = (String) in.readObject();


            System.out.println(aux);
            return true;

        } catch (IOException | ClassNotFoundException e) {
            return false;
        }

    }

    public ArrayList<Reserva> getPayedReservations(){ // FEITO
        try{

            MsgFromClientTCP toSV = new MsgFromClientTCP(GET_PAYED_RESERVATION);
            out.writeObject(toSV);
            out.flush();
            ArrayList<Reserva> reservas = new ArrayList<>();
            Reserva aux;
            do {
                aux = (Reserva) in.readObject();
                System.out.println("DEBIG:::"+aux);
                if (aux.getId() != -1)
                    reservas.add(aux);
            }while(aux.getId() != -1);

            System.out.println(reservas);
            return reservas;

        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public boolean editData(String campo, String username, String replacement) { // FEITO
        try{

            MsgFromClientTCP toSV = new MsgFromClientTCP(EDITAR_DADOS_REGISTO, new String[]{username, campo, replacement});
            out.writeObject(toSV);
            out.flush();
            String aux;

            aux = (String) in.readObject();

            System.out.println(aux);


        } catch (IOException  e) {
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return  true;
    }
    public boolean deleteNotPayedReservation(String id) { // FEITO
        try{

            MsgFromClientTCP toSV = new MsgFromClientTCP(DELETE_NOT_PAYED_RESERVATION, new String[]{id});
            out.writeObject(toSV);
            out.flush();
            String aux;

            aux = (String) in.readObject();
            System.out.println(aux);


        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
        return  true;
    }

    public ArrayList<Espetaculo> getFilteredShow(FilterShow filter, String nome){ // FEITO
        try{
            System.out.println("DEBUG #### FILTER: " + filter.label);
            MsgFromClientTCP toSV = new MsgFromClientTCP(GET_FILTERED_SHOW, new String[]{filter.label, nome});
            out.writeObject(toSV);
            out.flush();
            ArrayList<Espetaculo> espetaculos = new ArrayList<>();
            Espetaculo aux;
            do {
                aux = (Espetaculo) in.readObject();
                if (aux.getId() != -1)
                    espetaculos.add(aux);
            }while(aux.getId() != -1);

            System.out.println(espetaculos);

            return espetaculos;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
    public boolean getSelectedShow(String id){ //FEITO
        try{

            MsgFromClientTCP toSV = new MsgFromClientTCP(CONSULTA_ESPETACULO, new String[]{id});
            out.writeObject(toSV);
            out.flush();
            Espetaculo aux;
            aux = (Espetaculo) in.readObject();

            System.out.println(aux);


        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
        return  true;
    }
    public boolean makeReservation(String data_hora, String id_utilizador, String id_espetaculo, ArrayList<Lugar> lugares){ // DONE
        try{

            MsgFromClientTCP toSV = new MsgFromClientTCP(MAKE_RESERVATION, new String[]{data_hora, id_espetaculo, id_utilizador});
            out.writeObject(toSV);
            out.flush();

            String aux;
            aux = (String) in.readObject();
            System.out.println(aux);

            MsgFromClientTCP toSV2 = new MsgFromClientTCP(RESERVE_SITS, new String[]{id_espetaculo, id_utilizador}, lugares);
            out.writeObject(toSV2);
            out.flush();

            
            aux = (String) in.readObject();
            System.out.println(aux);



        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
        return  true;
    }

    public void logout(String usernameID){ // DONE
        try{

            MsgFromClientTCP toSV = new MsgFromClientTCP(LOGOUT, new String[]{usernameID});
            out.writeObject(toSV);
            out.flush();
            String aux;
            aux = (String) in.readObject();


            System.out.println(aux);
            this.interrupt();
            cmanager.getuTCP().setRunning(false);

        } catch (IOException | ClassNotFoundException e) {
            this.interrupt();
           cmanager.getuTCP().setRunning(false);


        }
    }

    public boolean payReservation(String reservID){ // DONE
        try{

            MsgFromClientTCP toSV = new MsgFromClientTCP(PAY_RESERVATION, new String[]{reservID});
            out.writeObject(toSV);
            out.flush();
            String aux;
            aux = (String) in.readObject();


            System.out.println(aux);


        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
        return  true;
    }
    public ArrayList<Espetaculo> getShows() { // FEITO
        try{

            MsgFromClientTCP toSV = new MsgFromClientTCP(CONSULTA_ESPETACULOS);
            out.writeObject(toSV);
            out.flush();
            ArrayList<Espetaculo> espetaculos = new ArrayList<>();
            Espetaculo aux;
            do {
                aux = (Espetaculo) in.readObject();
                if (aux.getId() != -1)
                    espetaculos.add(aux);
            }while(aux.getId() != -1);

            System.out.println(espetaculos);
            return espetaculos;

        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }



    @Override
    public void run() {
        //Tenta estabelecer ligação TCP
        estabeleceLigacao();
        //comunication();
        //Envia pedido TCP
    }

    private void estabeleceLigacao(){
        //TODO Quando o cliente perde ligaçao reestablecer usando este loop again
        for(int aux = 0; aux < listaServers.size() || !res; aux++) {
           // if (listaServers.get(aux) == null) break;
            for (Map.Entry<InetAddress, Integer> m : listaServers.get(aux).entrySet()) {
                addr = m.getKey();
                port = m.getValue();
            }
            try {
                socket = new Socket(addr, port);
                socket.setSoTimeout(utils.TIMEOUT_TCP*1000);
                res = true;
                out = new ObjectOutputStream(socket.getOutputStream());
                 in = new ObjectInputStream(socket.getInputStream());
            }catch (Exception e){

                res = false;
            }
        }
    }
}
