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

public class StreamSenderAbort {
    InetAddress group;
    int port;
    MulticastSocket socket;
    InetAddress ip;

    public StreamSenderAbort() {
        try {
            this.group = InetAddress.getByName(utils.MULTICAST_IP);
            this.ip = InetAddress.getLocalHost();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.port = 4004;

    }


    public boolean initializeSocket()  {
        try {
            socket = new MulticastSocket(port);

        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public void sender() {
        String message;

        String []args = {};
        try {
            //Scanner sc = new Scanner(System.in);

            MulticastMsg msg = new MulticastMsg(MulticastAction.ABORT, args);
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


    public void run() {
        if (!initializeSocket())return;
        sender();


    }
}
