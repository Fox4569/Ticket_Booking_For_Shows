package pt.isec.g45.tp.model.UDP.confirmation;

import pt.isec.g45.tp.model.ServerManager;
import pt.isec.g45.tp.utils.ServerInfoToClient;
import pt.isec.g45.tp.utils.utils;

import java.io.IOException;
import java.net.*;
import java.nio.channels.IllegalBlockingModeException;
import java.util.ArrayList;
import java.util.Arrays;

public class DbConfirmation {
    DatagramSocket socket;
    ArrayList<ServerInfoToClient> activeServers;
    InetAddress ip;
    int nextDBV;
    int porto;
    ServerManager server;
    public DbConfirmation(ArrayList<ServerInfoToClient> activeServers, ServerManager server, int nextDBV) {
        this.activeServers = activeServers;
        this.server = server;
        this.nextDBV = nextDBV;

    }


    public Boolean startSocket() {
        try {

            //if (socket != null) return true;
            socket = new DatagramSocket(utils.AUTO_PORT);
            socket.setSoTimeout(1000);
            this.porto = socket.getPort();
            this.ip = InetAddress.getLocalHost();
            server.getHb().setIp(this.ip);
            System.out.println(this.ip);
            return true;
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
            return false;

        }

    }

    public Boolean dataBaseConnection() {
        try {
            while (!activeServers.isEmpty()) {
                int BUFFSIZE = utils.OBJECT_BUFFSIZE;
                byte[] buffer = new byte[BUFFSIZE];

                DatagramPacket packet = new DatagramPacket(buffer, BUFFSIZE);
                socket.receive(packet);
                String version = Arrays.toString(packet.getData());
                if (Integer.parseInt(version) == nextDBV)
                    activeServers.removeIf(info2Client -> packet.getAddress().getHostAddress().equals(info2Client.getIp()));
            }


        }catch (IllegalBlockingModeException | IOException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public int getPorto() {
        return porto;
    }

    public void setPorto(int porto) {
        this.porto = porto;
    }

    public Boolean checker() {
        if (!dataBaseConnection())
             return activeServers.isEmpty();
        return false;
    }
    public Boolean run() {
        return startSocket();
      //  if (!dataBaseConnection())
       //     return activeServers.isEmpty();
    }
}
