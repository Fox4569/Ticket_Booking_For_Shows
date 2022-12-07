/**
 * @author: Bruno Sousa
 * @author: Jorge Santos
 * @author: João Baptista
 * This file holds the class ServerManager. This class has an extremely important role in this project.
 * Basically, from here we start the threads that represent the multiple servers (UDP and TCP).
 * For that reason, this file has the power to change the course of the project. The order of execution is
 * defined here, at the end of the file.
 *___________________________________________________________________________________________________________
 * Logic:
 *                                            Receiver Thread (First to be launched)
 *                                          /
 *                           MulticastUdp---- Sender (Hb Thread, others) (Takes about 30 seconds to start HB thread)
 *                         /
 *                       /      (Launched after receiving Heartbeats for 30 seconds)
 *      ServerManager  ----------------- FirstClientContact Server UDP Thread
 *                      \
 *                       \
 *                        \               ClientConnectionTCP (Thread launched by ServerTCP, this thread will be
 *                         \                                    responsible for client-server communication)
 *                          \             /
 *                           Server TCP - This thread waits for a client to join and creates a new ClientConnectionTCP
 * ____________________________________________________________________________________________________________________
 *     The schematics above explains the architecture of the server, allowing a soft vision of the Objects that are
 *      responsible for the communication.
 *
 * @servernote: ServerTCP will also launch a new thread for server-2-server communication, in other words, the update
 *              of the database (File) is made through TCP.
 * @servernote: ServerManager also allows to connect the database with the Servers.
 */

package pt.isec.g45.tp.model;

import pt.isec.g45.tp.data.DbManager;
import pt.isec.g45.tp.model.TCP.LigacaoTCPBD;
import pt.isec.g45.tp.model.TCP.ServerTCP;
import pt.isec.g45.tp.model.UDP.MulticastUDP;
import pt.isec.g45.tp.model.UDP.ServerPreClientUDP;
import pt.isec.g45.tp.model.UDP.multicast.StreamSenderDB;
import pt.isec.g45.tp.model.UDP.multicast.StreamSenderPrepare;
import pt.isec.g45.tp.utils.*;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import static pt.isec.g45.tp.utils.utils.*;

public class ServerManager extends Thread{

    int portUDP;  // Port UDP for Connection with the user.
    File localDirectory; // DB file
    Boolean fileExists;  // Holds the result of File.fileExist() for the database.
    ServerPreClientUDP serverPreClientUDP;   // Server that connects with trough UDP with client

    MulticastUDP serverMulticast;   // Multicast Server Responsible for connecting the servers

    InetAddress serverMulticastAddress; // Ip adress for the Multicast Server (239.39.39.39)
    ServerTCP svTCP;   // Server that connects through TCP to a client or Server.
    Timer timer; // Timer of the heartBeat

    ArrayList<ServerInfoToClient> serverInfoToClients;  // List that holds information about current active servers

    DbManager db;  // Reference for DatabaseManager Object
    String fileDB; // Name of the Database File.
    HeartBeat hb;  // Reference for a HeartBeat (Primary communication for server-2-server)
    int lastDbVersionHB;  // Holds the last known version of this server database.

    /**
     * @Constructor
     * @param portUDP  Integer that holds the port for UDP server (arguments)
     * @param fileDatabase String that holds the path for database.
     */
    public ServerManager(int portUDP, String fileDatabase) {
        this.portUDP = portUDP;
        this.fileExists = verifyDirectory(fileDatabase);
        serverPreClientUDP = new ServerPreClientUDP(portUDP, this);
        this.svTCP = new ServerTCP(this);
        serverInfoToClients = new ArrayList<>();
        fileDB = DB_ADAPTER + fileDatabase;
        timer = new Timer();
        hb = new HeartBeat("HeartBeat", null,0,false,0,0);
        lastDbVersionHB = 1;
    }

    /*Permite inicializar o endereço do servidor*/
    private void initializeServerAddressMulticast() throws UnknownHostException { // TODO HANDLE EXCEPTIONS
        serverMulticastAddress = InetAddress.getByName(MULTICAST_IP);
    }


    /*Permite verificar o directorio de um ficheiro*/
    private boolean verifyDirectory(String fileDatabase){
        localDirectory = new File(fileDatabase);
        if (!localDirectory.exists()) {
            System.out.println("A directoria " + localDirectory + " nao existe!");
            return false;
        }
        if (!localDirectory.isDirectory()) {
            System.out.println("O caminho " + localDirectory + " nao se refere a uma directoria!");
            return false;

        }

        if (!localDirectory.canRead()) {
            System.out.println("Sem permissoes de leitura na directoria " + localDirectory + "!");
            return false;
        }
        return true;
    }

    /**
     * Getter
     * @return ArrayList of The oject (ServerInfoToClient)
     */
    public ArrayList<ServerInfoToClient>  getActiveServers() {
        return serverInfoToClients;
        //return debugFunctionAbove();
    }

   ////////////////////////////////////////////////////
   /*                DATABASE COMMANDS               */
   ////////////////////////////////////////////////////

    // Campo basically will tell us which field should be changed on the database.
   public void editDataRegisto(String campo, String username, String replacement) {
       switch(campo.toUpperCase().trim()) {
           case "USERNAME" -> db.setUsername(username, replacement);
           case "PASSWORD" -> db.setPassword(username, replacement);
           case "NOME" -> db.setName(username, replacement);
       }
   }
   public String getSqlFromEditDataRegisto(String campo, String username, String replacement) {
       switch(campo.toUpperCase().trim()) {
           case "USERNAME" ->{
               return db.getUsername(username, replacement);

           }
           case "PASSWORD" -> db.getCommandPassword(username, replacement);
           case "NOME" -> db.getNameCommand(username, replacement);
       }
       return null;
   }
    // Description on the DbManager file.
    public int getLastDbVersionHB() {
        return lastDbVersionHB;
    }

    // Description on the DbManager file.
    public void setLastDbVersionHB(int lastDB) {
        lastDbVersionHB = lastDB;
    }
    // Description on the DbManager file.
    public Espetaculo getEspetaculo(int id) {
        return db.getEspetaculo(id);
    }
    // Description on the DbManager file.
    public ArrayList<Espetaculo> getEspetaculosAtivos() {
        return db.getAvailableShows();
    }

    // Description on the DbManager file.
    public ArrayList<Lugar> getLugares(int espetaculo_id) {
        return db.getAvailableSits(espetaculo_id);
    }
    // It is synchronized for the fact that multiple threads can use this function.
    public synchronized int  getDBVersao() {
        return db.getDBVersao();
    }
    // It is synchronized for the fact that multiple threads can use this function.
    public synchronized void  setDBVersao(int versao) {
        db.setTableVersion(versao);
    }
    // It is synchronized for the fact that multiple threads can use this function.
    public synchronized  void restartDBVersao() {
        db.restartVersion();
    }
    // Regists a new reservation in the database
    public void registNewReserve(String data_hora, int id_utilizador, int id_espetaculo, int id_lugar) {
        db.registNewReserve(data_hora, id_utilizador, id_espetaculo); // TODO fazer o check do lugar, reservar o lugar
    }
    // Allows to execute a command on the database.
    public void executeCommand(String query) {
        db.executeCommand(query);
    }


    ////////////////////////////////////////////////////
    /*             END OF DATABASE COMMANDS           */
    ////////////////////////////////////////////////////



    public HeartBeat getHb() {
        return hb;
    }

    public File getLocalDirectory() {
        return localDirectory;
    }
    public ArrayList<ServerInfoToClient> getServerInfoToClients() {
        return serverInfoToClients;
    }

    /**
     * Simple method that allows to check if an ip is already on the ArrayList serverInfoToClients.
     * @param hostname String that holds the hostname of the server.
     * @return Boolean {true, false} true if the server is on the list and false if it is not.
     */
    public boolean verifyIPinList(String hostname) {
        for (ServerInfoToClient sv: getServerInfoToClients()) {
            if (sv.getIp().equals(hostname)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method allows to add a server into the Arraylist to send to users.
     * @param hb HeartBeat from a server that holds vital information for this method to work.
     */
    public synchronized void addServerToList(HeartBeat hb) {
        System.out.println(hb.getIp()+"    "+hb.getPort());
        if (!verifyIPinList(hb.getIp().getHostAddress())) serverInfoToClients.add(new ServerInfoToClient(hb.getPort(), hb.getIp().getHostAddress(),hb.getCarga(),hb.getVersaoBaseDeDados()));
        Collections.sort(serverInfoToClients); // TODO Verify this statement !
    }
    /*This method allows to initialize the multicast server thread. (Receiver for the first 30 seconds)*/
    private void initializeMulticastServer( ) {
        try {
            initializeServerAddressMulticast();
            hb.setIp(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        serverMulticast = new MulticastUDP(PORTO_MULTICAST, serverMulticastAddress, INTERFACE_WLAN0, this);
    }
    public void sendCommit() {

    }
    public boolean sendPrepare(String comando) {
        StreamSenderPrepare prep = new StreamSenderPrepare();
        prep.start();
        prep.sender(hb.getIp(),getDBVersao()+1, comando);

        return true;
    }
    /**
     * Method that interrupts a program for x seconds using the method sleep.
     */
    @Deprecated
     public void wait_s() {
        try {

            Thread.sleep(3000);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method that allows to authenticate a user.
     * @param file Object that holds authentication information
     * @return Boolean wiht the result of Authentication
     */
    public boolean validateData(Authentication file) {

        String username = file.getUsername();
        String password = file.getPassword();
        String passRequested = db.getPassword(username);
        System.out.println("Debug password: " + password);
        System.out.println("Debug username: " + username);
        System.out.println("Debug passRequested: " + passRequested);

        if (passRequested == null) {
            return false;
        }
        else {
            System.out.println(password.equals(passRequested));
            return password.equals(passRequested);
        }
    }
    public boolean register(Authentication file) {
        System.out.println("Debug password: " + file.getPassword());

        if (file.getType() != AuthenticationEnum.REGISTER) return false;
        // TODO verifica dados (password required and username)
        System.out.println("Debug password: " + file.getPassword());
        System.out.println("Debug username: " + file.getUsername());
        db.registerUser(file.getUsername(), file.getPassword());
        return true;
    }
    public boolean handleData(Authentication file) {
        switch (file.getType()){
            case REGISTER ->{return register(file);}
            case LOGIN -> {
                return validateData(file);
            }
            default -> {
                return false;
            }
        }

    }
    @Deprecated
    public void test(){

        System.out.println(db.getAvailableShows());
    }

    /**
     * Method that creates a sender from MulticastUdp Thread. This will allow to send a HeartBeat each 10 seconds.
     */
    public void sendHeartBeat() {
        //if (hb.getIp() == null) hb.setIp();
        hb.setVersaoBaseDeDados(getDBVersao());
        hb.setCarga(svTCP.getNumberOfClients());
        serverMulticast.createSender(hb);
    }


    public void sendAskForDB(int port) {
        StreamSenderDB dbSender = new StreamSenderDB();
        dbSender.sender(hb.getIp(), port);
    }

    /**
     * Method that allows to initialize the thread HeartBeat.
     */
    public void initializeHeartBeatTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sendHeartBeat();

            }
        };

        timer.scheduleAtFixedRate(task, 5000,  5000); // TODO improve with constant
    }


    /**
     * Function that allows to stop the program for x seconds.
     * @param seconds integer that represent the time in seconds.
     */
    public void wait_timer(int seconds) {
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Method that allows to check if a version of the database if lesser than the others databases from others servers
     * @return null if is not to update, otherwise an Object (ServerInfoToClient) that holds the information needed
     * to establish a connection between servers.
     */
    public ServerInfoToClient toUpdateDB() {
        for (ServerInfoToClient nf: serverInfoToClients) {
            if (getDBVersao() < nf.getVersao()) {
                System.out.println(nf.getVersao());
                return nf;
            }
        }
        return null;
    }


    /**
     * Method that allows the thread to start.
     * Here is established the order the servers will start.
     * Ãfter that, is up to the servers to reorganize.
     */
    @Override
    public void run() {
        if (fileExists) {
            db = new DbManager(this.fileDB);
            if (!db.connectDB()) {
                return;
            }


            initializeMulticastServer();
            serverMulticast.start();
            System.out.println("DEBUG: VERSAO DB: " + getDBVersao());

            // Aguardar os 30 segundos a receber TODO atualizar database
            wait_timer(5000);
            if (toUpdateDB() != null) {
                ServerInfoToClient sv = toUpdateDB();
                System.out.println("BD -> OUTDATED");
                LigacaoTCPBD lig = new LigacaoTCPBD(localDirectory, DB_FILE_NAME,this, sv.getIp(),sv.getPort());
                lig.run();
            }
            System.out.println("DEBUG: VERSAO DB: " + getDBVersao());
            serverPreClientUDP.start();
            // Pos-Arranque
            initializeHeartBeatTimer();

            svTCP.start();
        }


    }

}
