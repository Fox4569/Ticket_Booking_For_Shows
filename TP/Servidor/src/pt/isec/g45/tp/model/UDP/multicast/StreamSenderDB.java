/**
 * @Author: Bruno Sousa
 * Test Class, this shall not be considered to be part of the software.
 * */


package pt.isec.g45.tp.model.UDP.multicast;

import pt.isec.g45.tp.utils.MulticastAction;
import pt.isec.g45.tp.utils.MulticastMsg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class StreamSenderDB extends Thread{
    InetAddress group;
    int port;
    MulticastSocket socket;
    InetAddress ip;

    public StreamSenderDB() {
        try {
            this.group = InetAddress.getByName("239.39.39.39");
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

    public void sender(InetAddress new_ip, int port) {
        String message;

        message = new_ip.getHostName(); //HostName so we can use GetAdressByName
        String portS = Integer.toString(port);
        String []args = {message, portS};
        try {
            //Scanner sc = new Scanner(System.in);

            MulticastMsg msg = new MulticastMsg(MulticastAction.TRANSFERE_DB, args);
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
