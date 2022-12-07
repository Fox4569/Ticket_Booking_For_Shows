/**
 * @Author: Bruno Sousa
 * @Author: Jorge Santos
 * @Author: João Baptista
 * #####################################################################################################################
 * ##                     This class is responsible for receiving the database file.                                  ##
 * #####################################################################################################################
 * ##                                                                                                                 ##
 * ##                                                                                                                 ##
 * ##               Server TCP (Client/Master)                                                                        ##
 * ##                           |                                                                                     ##
 * ##                           |                                                                                     ##
 * ##                           |---------> Creates a ServerSocket to wait for clients                                ##
 * ##                           |            (method: createSocket())                                                 ##
 * ##                           |                                                                                     ##
 * ##                           |---------> Waits for a client to request a connection and creates a new Worker       ##
 * ##                           |            (method: connectClient())                                                ##
 * ##                           |                                                                                     ##
 * #####################################################################################################################
 * ##                                                                                                                 ##
 * ##                   This class Accepts sockets and distributes their connections with new workers                 ##
 * #####################################################################################################################
 * */

package pt.isec.g45.tp.model.TCP;

import pt.isec.g45.tp.model.ServerManager;
import pt.isec.g45.tp.utils.utils;

import java.io.IOException;

import java.net.ServerSocket;
import java.util.ArrayList;

public class ServerTCP extends Thread{

    int port;

    ServerSocket s;
    ServerManager servermanager;

    private final TcpServerClients  connectionsClients = new TcpServerClients();

    static public class TcpServerClients extends ArrayList<ClientConectionTCP> {

    }

    public TcpServerClients getConnectionsClients() {
        return connectionsClients;
    }

    public ServerTCP(ServerManager sc) {
        servermanager = sc;
    }

    public boolean createSocket()  {
        try {
            s = new ServerSocket(utils.AUTO_PORT);
            //s.setSoTimeout(30000); // TODO CONSTaNTE
        }catch (IOException e) {
            return false;
        }
        port = s.getLocalPort();

//        servermanager.addServerToList(port);
        servermanager.getHb().setPort(port);
        return  true;
    }
    // Turn off all connections with clients
    public void turnOffClientConnection() {
        for(ClientConectionTCP con: connectionsClients) {
            con.setThreadActive(false);
        }
    }

    /**
     * Simple method to create a new Connection, after creating it, it will add that connection to an array
     * so the serverTCP Object can keep tracking all connections and the current amount.
     */
    public void connectClient() { //TODO remove a client
        try {
            var toClient =s.accept();
            connectionsClients.add(new ClientConectionTCP(toClient, servermanager));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public int getNumberOfClients() {
        return connectionsClients.size();
    }

    @Override
    public void run() {
        if (createSocket()) {
            System.out.println(port);
            servermanager.getHb().setPort(port);
            System.out.println(s.getInetAddress());
            while(true) {
                connectClient();

            }
        }

    }
}
