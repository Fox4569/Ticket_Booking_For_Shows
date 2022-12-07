package pt.isec.g45.tp.model;

import pt.isec.g45.tp.model.tcp.ClientTCP;
import pt.isec.g45.tp.model.tcp.UpdateTCP;
import pt.isec.g45.tp.model.udp.ClientUDP;
import pt.isec.g45.tp.utils.Authentication;
import pt.isec.g45.tp.utils.ServerInfoToClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientManager extends Thread{
    ClientUDP clientUDP;
    ClientTCP clientTCP;

    String serverAddr;
    String serverPort;
    HashMap<Integer, HashMap<InetAddress, Integer>> serversActive;
    AppManager app;
    UpdateTCP uTCP;
    int id = 0;
    public ClientTCP getClientTCP() {
        return clientTCP;
    }
    public UpdateTCP getuTCP() {
        return uTCP;
    }
    public ClientManager(String serverAddr, String serverPort, AppManager app) {
        this.serverAddr = serverAddr;
        this.serverPort=serverPort;
        this.app = app;
        uTCP = new UpdateTCP(this);

    }
    public AppManager getApp() {
        return app;
    }
    public void updateServersActiveList(ArrayList<ServerInfoToClient> aux) {
        serversActive.clear();
        id = 0;
        if (aux == null) return;
        for (ServerInfoToClient siftc : aux) {
            adicionaLista(siftc.getIp(), siftc.getPort());
        }

    }

    private void adicionaLista(String line, int port){
        // String[] tokens = line.split(" ");

        try {
            InetAddress addr = InetAddress.getByName(line);


            HashMap<InetAddress, Integer> aux = new HashMap<>();
            aux.put(addr, port);
            serversActive.put(id++, aux);
        }catch (UnknownHostException e){
            System.err.println("Error: " + e);
            serversActive.clear();
        }
    }
    public Boolean authentication(Authentication file) {
        clientUDP = new ClientUDP(serverAddr, serverPort, file);
        int value = clientUDP.run();
        if (!clientUDP.authenticated()) return false;
        serversActive = clientUDP.getListaServers();

        clientTCP = new ClientTCP(serversActive, this);
        clientTCP.start();
        if (uTCP.getRunning())
            uTCP.start();
        else
            uTCP.setRunning(true);
        Boolean isAdmin = value == -2;
        app.getFsm().getData().getUser().setAdmin(isAdmin);
        app.getFsm().getData().getUser().setPassword(file.getPassword());
        app.getFsm().getData().getUser().setUsername(file.getUsername());
        return true;
    }
    @Override
    public void run() {
        super.run();
    }
}
