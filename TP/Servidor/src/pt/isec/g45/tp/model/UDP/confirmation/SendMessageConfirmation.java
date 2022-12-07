package pt.isec.g45.tp.model.UDP.confirmation;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class SendMessageConfirmation {

    DatagramSocket socket;
    int dbVersao;
    InetAddress ip;
    int porto;
    public SendMessageConfirmation(int dbVersao, InetAddress ip, int porto) {
        this.dbVersao = dbVersao;
        this.porto = porto;
        this.ip = ip;
    }


    public Boolean established() {
        try {
            socket = new DatagramSocket();
            return true;
        } catch (SocketException e) {
            return false;
        }

    }

    public void enviaConfirmxao() {
        byte[] sendbuf = "Confimr".getBytes();
        DatagramPacket packet = new DatagramPacket(sendbuf, sendbuf.length, ip, porto);
        try {
            socket.send(packet);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run () {
        established();


    }
}
