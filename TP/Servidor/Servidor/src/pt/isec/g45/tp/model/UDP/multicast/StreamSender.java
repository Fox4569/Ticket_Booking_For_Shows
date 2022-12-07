/**
 *  @author: Bruno Sousa
 *  @author: Jorge Santos
 *  @author: João Baptista
 * #####################################################################################################################
 * # This class is responsible for sending a HeartBeat from 10 to 10 seconds to the multicast channel.                 #
 * #####################################################################################################################
 * #                                                                                                                   #
 * #                                                                                                                   #
 * #            HeartBeat Sender Mechanism:                                                                            #
 * #                        |                                                                                          #
 * #                        |                                                                                          #
 * #                        |----------------> Initialize the socket (when the thread starts)                          #
 * #                        |                       (method name: initializeSocket())                                  #
 * #                        |                                                                                          #
 * #                        |----------------> Waits for ServerManager to create a Timer Thread                        #
 * #                        |                     (ServerManager method name: initializeHeartBeatTimer())              #
 * #                        |                                                                                          #
 * #                        |----------------> Waits for ServerManager to call the method: sender() from here.         #
 * #                        |                     (ServerManager method name: sendHeartBeat())                         #
 * #####################################################################################################################
 * #                                                                                                                   #
 * #                            Final Result: Sends an HeartBeat from x to x seconds.                                  #
 * #                                                                                                                   #
 * #####################################################################################################################
 * */

package pt.isec.g45.tp.model.UDP.multicast;

import pt.isec.g45.tp.utils.HeartBeat;
import pt.isec.g45.tp.utils.MulticastAction;
import pt.isec.g45.tp.utils.MulticastMsg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class StreamSender extends Thread{
    InetAddress group;
    int port;
    MulticastSocket socket;
    InetAddress ip;

    /**
     * @Constructor
     * @param group Endereço do multicast
     * @param port  Porta do Multicast
     */
    public StreamSender(InetAddress group, int port) {
        this.group = group;
        this.port = port;
        debugInitializeIP();
    }
    //TODO remover esta funçao
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

    public void sender(HeartBeat hb) {
        String message;


        try {
            //Scanner sc = new Scanner(System.in);

            message = "HeartBeat";
            MulticastMsg msg = new MulticastMsg(MulticastAction.HEARTBEAT, hb);
            //HeartBeat hb = new HeartBeat(message, ip, 0, false, 1, 5);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bout);
            out.writeObject(msg);
            out.flush();
            DatagramPacket packet = new DatagramPacket(bout.toByteArray(), bout.size(), group, port);
            socket.send(packet);


        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        //socket.close();
    }


    @Override
    public void run() {
        if (!initializeSocket())return;
        //sender();


    }
}
