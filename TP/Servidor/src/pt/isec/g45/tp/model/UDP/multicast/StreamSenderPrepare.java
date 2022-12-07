/**
 *  @author: Bruno Sousa
 *  @author: Jorge Santos
 *  @author: João Baptista
 * #####################################################################################################################
 * # This class is responsible for sending a Prepare Message to the multicast channel.                                 #
 * #####################################################################################################################
 * #                                                                                                                   #
 * #                                                                                                                   #
 * #            Prepare Sender Mechanism:                                                                              #
 * #                        |                                                                                          #
 * #                        |                                                                                          #
 * #                        |----------------> Initialize the socket (when the thread starts)                          #
 * #                        |                       (method name: initializeSocket())                                  #
 * #                        |                                                                                          #
 * #                        |----------------> Waits for ServerManager to execute the method to send for MC channel    #
 * #                        |                     (Method name: void sender() )                                        #
 * #                        |                                                                                          #
 * #                        |----------------> Waits for ServerManager to call the method: sender() from here.         #
 * #                        |                     (ServerManager method name: sendHeartBeat())                         #
 * #####################################################################################################################
 * #                                                                                                                   #
 * #                            Final Result: Sends an Prepare message to the multicast channel.                       #
 * #                                                                                                                   #
 * #####################################################################################################################
 * */

package pt.isec.g45.tp.model.UDP.multicast;

import pt.isec.g45.tp.utils.MulticastAction;
import pt.isec.g45.tp.utils.MulticastMsg;
import pt.isec.g45.tp.utils.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class StreamSenderPrepare{
    InetAddress group;
    int port;
    MulticastSocket socket;
    InetAddress ip;

    public StreamSenderPrepare() {
        try {
            this.group = InetAddress.getByName(utils.MULTICAST_IP);
            this.ip = InetAddress.getLocalHost();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.port = utils.PORTO_MULTICAST;

    }


    public boolean initializeSocket()  {
        try {
            socket = new MulticastSocket(port);

        } catch (IOException e) {
            return false;
        }
        return true;
    }
    // TODO PREPARE- COMIT LOGIC
    public void sender(InetAddress new_ip, int dbVersao, String Comando) {
        String message;

        message = new_ip.getHostName(); //HostName so we can use GetAdressByName
        String versao = Integer.toString(dbVersao);
        String []args = {message, versao, Comando};
        try {
            //Scanner sc = new Scanner(System.in);
            MulticastMsg msg = new MulticastMsg(MulticastAction.PREPARE, args); // args [0] -> mss, args[1] -> versao, args[2]-> comando
            System.out.println(msg.getMacAction());
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
// TODO arranjar isto

    public void run() {
        if (!initializeSocket())return;
        //sender();


    }
}
