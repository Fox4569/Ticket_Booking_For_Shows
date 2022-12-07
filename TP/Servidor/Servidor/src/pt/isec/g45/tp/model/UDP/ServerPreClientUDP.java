/**
 * @author: Bruno Sousa
 * @author: Jorge Santos
 * @author: João Baptista
 * This class is responsible for distributing a list of active tcp servers to a first connection client,
 * therefore it will create a udp server that takes clients.
 *  In order:
 *      First Connection:
 *          -> Client (trough the ip and port) connects to this udp server and sends an Object (Authentication).
 *              (Authentication) -> Object responsible to allow the client to authenticate into the tcp servers.
 *                  |-> This will allow for Register/Login.
 *          -> After receiving an Authentication file, the server sends this object to the ServerManager, so it can
 *              verify with the database.
 *          -> After authentication is validated, the server will send an ordered list of active tcp servers that can
 *          take this client.
 *          -> End-ly, it will restart the process all over again, unless this thread gets interrupted (by a higher proccess).
 */


package pt.isec.g45.tp.model.UDP;

import pt.isec.g45.tp.model.ServerManager;
import pt.isec.g45.tp.utils.Authentication;
import pt.isec.g45.tp.utils.ServerInfoToClient;
import pt.isec.g45.tp.utils.utils;

import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class ServerPreClientUDP extends Thread{
    DatagramSocket socket; // variable that holds a reference to the connection
    int portoUDP;
    ServerManager server; // reference to the object ServerManager
    ByteArrayOutputStream bout;
    ObjectOutputStream oout;
    ByteArrayInputStream bin;
    ObjectInputStream oin;
    Authentication loginData;  // reference for the object received by a client
    InetAddress ip;

    /**
     * @constructor
     * @param portoUDP Integer that will give the server that port it will create a socket to.
     * @param server Reference to the Object ServerManager
     */
    public ServerPreClientUDP(int portoUDP, ServerManager server)
    {
        this.portoUDP = portoUDP;
        this.server = server;
        this.ip = null;
        this.socket = null;
    }

    // Construtor atraves do socket para resolver problemas de obter informaçao
    public ServerPreClientUDP(DatagramSocket socket, ServerManager server) {
        this.socket = socket;
        this.server = server;
        this.ip = this.socket.getInetAddress();
        this.portoUDP = this.socket.getLocalPort();
    }


    public boolean startSocket() {
        try {

            //if (socket != null) return true;
            socket = null;
            socket = new DatagramSocket(this.portoUDP);
            this.ip = InetAddress.getLocalHost();
            server.getHb().setIp(this.ip);
            System.out.println(this.ip);
            return true;
        } catch (SocketException e) {
            return false;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
    public InetAddress getIP() {return this.ip;}

    /**
     * Method that handles the client connection
     * @return Boolean if it fails to connect to any client.
     */

    public boolean clientConnection() {

        //TODO Colocar num while que verifica se é para estar ativo

      while (true) {
          try {
              /*Preparar para receber login data serializable da classe Authentication*/
              int BUFFSIZE = utils.OBJECT_BUFFSIZE;
              byte[] buffer = new byte[BUFFSIZE];

              DatagramPacket packet = new DatagramPacket(buffer, BUFFSIZE);
              socket.receive(packet);
                /*DEBUG STATUS*/
                /*Preparar para receber um objeto serializável*/
              bin = new ByteArrayInputStream(packet.getData(),0,packet.getLength());
              oin = new ObjectInputStream(bin);

              loginData = (Authentication)  oin.readObject();
              System.out.println("Username: "+loginData.getUsername()+"\nPassword: " + loginData.getPassword());

              //TODO else returns another message saying wrong login
              if (validaData(loginData)) {

                  ArrayList<ServerInfoToClient> listaServers = server.getActiveServers();


                  for (ServerInfoToClient sv : listaServers) {
                      System.out.println(sv.toString());
                      bout = new ByteArrayOutputStream();
                      oout = new ObjectOutputStream(bout);
                      oout.writeObject(sv);
                      //oout.writeUnshared(TIME_REQUEST);
                      oout.flush();

                      packet.setData(bout.toByteArray());
                      packet.setLength(bout.size());
                      System.out.println(packet);
                      socket.send(packet);

                  }
                  /*AVISAR CLIENTE QUE JA NAO EXISTEM MAIS SERVERS*/
                  bout = new ByteArrayOutputStream();
                  oout = new ObjectOutputStream(bout);
                  oout.writeObject(new ServerInfoToClient(-1, "",0,0));
                  //oout.writeUnshared(TIME_REQUEST);
                  oout.flush();

                  packet.setData(bout.toByteArray());
                  packet.setLength(bout.size());
                  socket.send(packet);
              }


          } catch (IOException e) {
              return false;
          } catch (ClassNotFoundException e) {
              throw new RuntimeException(e);
          }
      }

    }

    public boolean validaData(Authentication file) {
        return server.handleData(file);
        //return true;
    }

    /**
     * Method that allows the execution of the thread.
     *
     */
    @Override
    public void run() {
        if (!startSocket()) throw new RuntimeException();
        if (clientConnection())throw new RuntimeException();
    }
}
