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
import pt.isec.g45.tp.utils.AckVerification;
import pt.isec.g45.tp.utils.utils;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Timer;
import java.util.TimerTask;

public class ServerTCP extends Thread{
    Boolean isRunning;
    Boolean isServerUp;
    int port;
    Timer timer; // Timer of the ACK

    ServerSocket s;
    ServerManager servermanager;

    ObjectOutputStream out;
    ObjectInputStream in;
    Socket socket; // socket to send a message for update to each client
    private final TcpServerClients  connectionsClients = new TcpServerClients();

    static public class TcpServerClients extends ArrayList<ClientConectionTCP> {

    }
    public void removeFromTCP(ClientConectionTCP c) {
        connectionsClients.remove(c);
    }


    public synchronized void sendSignal(){
        AckVerification product = new AckVerification();
        product.setInfo(servermanager.getServerInfoToClients());
        for (ClientConectionTCP c: connectionsClients) {
            System.out.println("IP: " +  c.getSocket().getInetAddress() + " ::: PORTO: " + utils.AUTO_PORT_TCP);

            if (!sendMessage(product,  c.getSocket().getInetAddress(),  utils.AUTO_PORT_TCP)) {
                c.threadActive = false;
                //connectionsClients.remove(c);
            }
        }
    }
    public synchronized boolean sendMessage(AckVerification message, InetAddress ip, int porto) {
        try {
            socket = new Socket(ip, porto);
            socket.setSoTimeout(utils.TIMEOUT_TCP*1000);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(message);
            out.flush();
            String aux = (String) in.readObject();
            return aux != null;
        } catch (IOException | ClassNotFoundException | ConcurrentModificationException e) {
            return false;
        }
    }
    public synchronized void ackVerification() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sendSignal(); // TODO

            }
        };

        timer.scheduleAtFixedRate(task, 10000,  10000); // TODO improve with constant
    }


    public TcpServerClients getConnectionsClients() {
        return connectionsClients;
    }

    public ServerTCP(ServerManager sc) {
        servermanager = sc;
        this.isRunning = true;
        this.isServerUp = true;
        this.timer = new Timer();
    }

    public Boolean getRunning() {
        return isRunning;
    }

    public void setRunning(Boolean running) {
        isRunning = running;
    }

    public Boolean getServerUp() {
        return isServerUp;
    }

    public void setServerUp(Boolean serverUp) {
        isServerUp = serverUp;
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
    public void connectClient() {
        try {
            var toClient =s.accept();
            connectionsClients.add(new ClientConectionTCP(toClient, servermanager,this));


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
            ackVerification();
            while (isServerUp) {
                while(isRunning) {
                    connectClient();  // This loop can get too complex, up to O(n^4) due to database lookups. It can trigger errors.

                }
            }
        }

    }
}
