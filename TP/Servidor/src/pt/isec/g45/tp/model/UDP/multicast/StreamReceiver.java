/**
 *  @author: Bruno Sousa
 *  @author: Jorge Santos
 *  @author: João Baptista
 * #####################################################################################################################
 * # This class is responsible for receiving messages from the Multicast Mechanism (Receiver)                         #
 * #####################################################################################################################
 * #                                                                                                                   #
 * #                            Wait to Receive an Object (MulticastMsg)                                               #
 * #                          /         |                                                                              #
 * #                         /          |                                                                              #
 * #        Receiver --------           |-------> Multicast Code 1 (HeartBeat)                                         #
 * #                                    |         (Registered in MulticastAction)                                      #
 * #                                    |                                                                              #
 * #                                    |-------> Multicast Code 2 (Prepare)                                           #
 * #                                    |         (Registered in MulticastAction)                                      #
 * #                                    |                                                                              #
 * #                                    |-------> Multicast Code 3 (Commit)                                            #
 * #                                    |         (Registered in MulticastAction)                                      #
 * #                                    |                                                                              #
 * #                                    |-------> Multicast Code 4 (Abort)                                             #
 * #                                    |         (Registered in MulticastAction)                                      #
 * #                                    |                                                                              #
 * #                                    |-------> Multicast Code 5 (Debug)                                             #
 * #                                    |          (Not registered)                                                    #
 * #####################################################################################################################
 * #                                                                                                                   #
 * #    Receiver will always wait for the same type of Object.                                                         #
 * #        - > To identify the consecutive answer, it will identify one of the codes written above.                   #
 * #####################################################################################################################
 */

package pt.isec.g45.tp.model.UDP.multicast;

import pt.isec.g45.tp.model.ServerManager;
import pt.isec.g45.tp.model.TCP.LigacaoTCPBD;
import pt.isec.g45.tp.utils.MulticastAction;
import pt.isec.g45.tp.utils.MulticastMsg;
import pt.isec.g45.tp.utils.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;

import static pt.isec.g45.tp.utils.utils.DB_FILE_NAME;

public class StreamReceiver extends Thread{

    InetAddress group;
    int port;
    MulticastSocket socket;
    ObjectInputStream in;
    InetAddress ip;
    NetworkInterface nif;
    MulticastMsg mss;
    ServerManager server;
    String storageCommand;

    int newVersion;

    /**
     * @constructor
     * @param group  Address for the multicast channel.
     * @param port   Port where it will be connected
     * @param server Reference for the Object ServerManager
     */
    public StreamReceiver(InetAddress group, int port, ServerManager server) {
        this.group = group;
        this.port = port;
        debugInitializeIP();
        this.server = server;
    }
    // TODO remover esta funcao
    public void debugInitializeIP() {
        try {
            this.ip = InetAddress.getByName("localhost");

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean initializeSocket()  {
        try {
            socket = new MulticastSocket(port);

        } catch (IOException e) {
            return false;
        }
        return true;
    }
    public void initializeNetworkInterface(String interf) throws SocketException {
        try{
            nif = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()); //e.g., 127.0.0.1, 192.168.10.1, ...
        }catch (SocketException | NullPointerException | UnknownHostException | SecurityException ex){
            nif = NetworkInterface.getByName(interf); //e.g., lo, eth0, wlan0, en0, ...
        }
    }

    /**
     * This method is responsible for receiving and handling the different codes and consecutive actions.
     * From the object we take out the code, so we can proceed with the right action.
     * @servernote: Interface might change.
     *
     */
    public void receiver() {
        try {
            initializeNetworkInterface("wlan0");

        }catch (SocketException e) {
            e.printStackTrace();
        }

        try {
            //Join the multicast channel
            socket.joinGroup(new InetSocketAddress(utils.MULTICAST_IP, utils.PORTO_MULTICAST), nif);

            int i = 0; // only for debug, replace with thread boolean
            while (i < 1000) {
                byte [] buffer = new byte[2400]; // Change 2400 with Constant from utils
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length); // Create a Datagram Packet for UDP

                socket.receive(packet); // Receive a packet from the multicast channel.

                //Deserializar objeto
                in = new ObjectInputStream(new ByteArrayInputStream(packet.getData(), 0,
                        packet.getLength()));


                    mss = (MulticastMsg) in.readObject(); // receive the object (Multicast Object)
                if (mss.getMacAction() == MulticastAction.HEARTBEAT) { // Action 1
                    System.out.println("\nHEARTBEAT: " + mss.getHb().toString());
                    server.addServerToList(mss.getHb());
                    System.out.println(server.getActiveServers());
                    if (mss.getHb().getIp() == server.getHb().getIp()) {
                        server.aumentaTimerActiveServers();
                    }
                   /* if (mss.getHb().getVersaoBaseDeDados() > server.getDBVersao()) { // UPDATE DATABASE
                        server.turnOffTcpServer();
                        LigacaoTCPBD lig = new LigacaoTCPBD(server.getLocalDirectory(), DB_FILE_NAME,server, mss.getHb().getIp().getHostAddress(),mss.getHb().getPort());
                        lig.run();
                        server.turnOnTcpServer();
                    }*/


                }
                else if(mss.getMacAction() == MulticastAction.PREPARE) { // Action 2
                    storageCommand = mss.getArgs()[2];
                    newVersion = Integer.parseInt(mss.getArgs()[1]);
                    System.out.println("\nPREPARE: " + mss.getArgs()[2] +"\n PREPARE: New Version -> " + mss.getArgs()[2]);

                    continue;
                }
                else if(mss.getMacAction() == MulticastAction.COMMIT) { // Action 3
                    server.executeCommand(storageCommand);
                    server.setDBVersao(newVersion);
                    System.out.println("\nCOMMIT: " + storageCommand +"\n COMMIT: New Version -> " + newVersion);

                    continue;
                }
                else if(mss.getMacAction() == MulticastAction.ABORT) { // Action 4
                    storageCommand = "";
                    newVersion = Integer.parseInt(mss.getArgs()[0]);
                    server.setDBVersao(newVersion);
                    System.out.println("\nABORT: " + storageCommand +"\n ABORT: New Version -> " + newVersion);

                    continue;
                }
                /*TRANSFERE_DB MIGHT NEVER BE USED. Only for debbuging. */
                else if(mss.getMacAction() == MulticastAction.TRANSFERE_DB) { // Action 5
                    continue;
                }
                else continue; // If its none of the codes, just continue without an action.
                i++;
            }
            socket.close();
            System.out.println("Socket Closed");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    // Thread runner.
    @Override
    public void run() {
        System.out.println("RECEIVER: Establishing connection to " + ip + " on port: " + port + "." );
        if (!initializeSocket())return;
        System.out.println("RECEIVER: Waiting for a message on multicast channel...");
        receiver();
    }
}