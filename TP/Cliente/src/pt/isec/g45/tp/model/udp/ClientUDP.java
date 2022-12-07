package pt.isec.g45.tp.model.udp;

import pt.isec.g45.tp.utils.Authentication;
import pt.isec.g45.tp.utils.ServerInfoToClient;
import pt.isec.g45.tp.utils.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.HashMap;

public class ClientUDP {
    InetAddress ip;
    int port, id = 0;
    DatagramPacket packetEnvio;
    DatagramPacket packetRecebe;
    DatagramSocket socket;
    HashMap<Integer, HashMap<InetAddress, Integer>> listaServers;
    ByteArrayInputStream bin;
    ObjectInputStream oin;
    Authentication loginData;
    ByteArrayOutputStream bout;
    ObjectOutputStream oout;
    public ClientUDP(String ip, String port, Authentication loginData) {
        inicializaDados(ip, port, loginData);
    }

    private void inicializaDados(String ip, String port, Authentication loginData){
        try {
            this.ip = InetAddress.getByName(ip);
            this.port = Integer.parseInt(port);
        }catch (UnknownHostException uHE) {
            System.err.println("Error: " + uHE);
        }
        packetEnvio = null;
        packetRecebe = null;
        socket = null;
        listaServers = new HashMap<>();
        this.loginData = loginData;
    }
    public int run() {
        //Estabelecer ligação UDP
        //Envia pedido UDP
        estabeleceLigacaoUDP();

        //Recebe lista
       return recebeLista();
    }
    public boolean authenticated() {
        System.out.println(listaServers);
        return !listaServers.isEmpty();
    }
    /*Estabele ligação UDP com o server e envia data para o login ou registro*/
    private void estabeleceLigacaoUDP(){
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(utils.TIMEOUT_UDP*1000);
            bout = new ByteArrayOutputStream();
            oout = new ObjectOutputStream(bout);
            oout.writeObject(loginData);
            //oout.writeUnshared(TIME_REQUEST);
            oout.flush();
            packetEnvio = new DatagramPacket(bout.toByteArray(), bout.size(), ip, port);
            socket.send(packetEnvio);


        }catch (Exception e){
            System.err.println("Error: " + e);
        }
    }
    /*Recebe a lista do servidor*/
    private int recebeLista(){
        try {
            ServerInfoToClient response;
            do {
                /*READ SERIALIZABLE OBJECT*/
                packetRecebe = new DatagramPacket(new byte[2400], 2400);
                socket.receive(packetRecebe);

                bin = new ByteArrayInputStream(packetRecebe.getData(),0,packetRecebe.getLength());
                oin = new ObjectInputStream(bin);

                response = (ServerInfoToClient) oin.readObject();
                System.out.println(response.getIp());
                if (!response.isFinish())adicionaLista(response.getIp(), response.getPort());
            }while (!response.isFinish());
            return response.getCarga();
        }catch (SocketException sE) {
            System.err.println("Error SocketException" + sE);
            return -5;
        }catch (SocketTimeoutException sTE){
            System.err.println("Error SocketTimeoutException" + sTE);
            return -6;
        }catch (Exception e){
            System.err.println("Error" + e);
            return -7;
        }
    }

    /*Este método adiciona informação dos servidores ao hashmap*/
    private void adicionaLista(String line, int port){
       // String[] tokens = line.split(" ");

        try {
            InetAddress addr = InetAddress.getByName(line);


            HashMap<InetAddress, Integer> aux = new HashMap<>();
            aux.put(addr, port);
            listaServers.put(id++, aux);
        }catch (UnknownHostException e){
            System.err.println("Error: " + e);
            listaServers.clear();
        }
    }
    /*Getters*/
    public HashMap<Integer, HashMap<InetAddress, Integer>> getListaServers(){
        return listaServers != null? listaServers : null;
    }
}
