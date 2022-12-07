/**
 * @Author: Bruno Sousa
 * @Author: Jorge Santos
 * @Author: João Baptista
 * #####################################################################################################################
 * ##                     This class is responsible for receiving the database file.                                  ##
 * #####################################################################################################################
 * ##                                                                                                                 ##
 * ##                                                                                                                 ##
 * ##               Ask for DB -> Another Server Receives the request -> Waits to receive the Database                ##
 * ##               (method: askForDb())                                 (method: receive_db())                       ##
 * #####################################################################################################################
 */
package pt.isec.g45.tp.model.TCP;

import pt.isec.g45.tp.model.ServerManager;
import pt.isec.g45.tp.utils.MsgFromClientTCP;
import pt.isec.g45.tp.utils.utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static pt.isec.g45.tp.utils.CodigosTCP.TRANSFERE_DB;

public class LigacaoTCPBD {

    String fileName, localFilePath = null;
    FileOutputStream localFileOutputStream = null;
    String ip;
    int serverPort;
    ServerSocket serverSocket = null;
    Socket socket;
    BufferedReader bin;
    ObjectOutputStream out;

    File localDirectory;
    ObjectInputStream in;
    PrintWriter pout;
    byte[] filechunk;
    int nbytes;
    ServerManager server;

    /**
     * @Constructor
     * @param localDirectory Directory where the database it is going to be.
     * @param fileName  Name of the database.
     * @param server    Reference for ServerManager
     * @param ip        IpAddress from the server it will be requested.
     * @param port      Port from the server it will be requested.
     */
    public LigacaoTCPBD(File localDirectory, String fileName, ServerManager server, String ip, int port) {
        this.localDirectory = localDirectory;
        this.fileName = fileName;
        filechunk= new byte[utils.MAX_CHUNK_SIZE];
        this.server = server;
        this.ip = ip;
        this.serverPort = port;
    }

    /**
     * Simple method that sends a Serialized Object to a server.
     * In this case, the object is MsgFromClientTCP.
     *
     */
    private void askForDb() {
        try{

            MsgFromClientTCP toSV = new MsgFromClientTCP(TRANSFERE_DB);
            out.writeObject(toSV);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        if (getFile()) {
            if (established()) {
                askForDb();
                receive_db();
            }

        }
    }
// TODO testar classes


    /**
     * Method that receives a file from a server.
     * Basically, it will receive chunks of bytes that will form the whole db.
     */
    private void receive_db(){
        try{
            while((nbytes = in.read(filechunk)) > 0){
                //System.out.println("Recebido o bloco n. " + ++contador + " com " + nbytes + " bytes.");
                localFileOutputStream.write(filechunk, 0, nbytes);
                //System.out.println("Acrescentados " + nbytes + " bytes ao ficheiro " + localFilePath+ ".");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
    /*Estabelece lgiacao com o servidor */
    private boolean established() {
        try {
             // TODO clear this

            socket = new Socket(this.ip, serverPort);

            socket.setSoTimeout(utils.TIMEOUT_TCP*1000);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    /*Abre um ficheiro na diretoria mencionada*/
    private boolean getFile() {
        try{

            localFilePath = localDirectory.getCanonicalPath()+File.separator+fileName;
            localFileOutputStream = new FileOutputStream(localFilePath);
            System.out.println("Ficheiro " + localFilePath + " criado.");
            return true;

        }catch(IOException e){

            if(localFilePath == null){
                System.out.println("Ocorreu a excepcao {" + e +"} ao obter o caminho canonico para o ficheiro local!");
            }else{
                System.out.println("Ocorreu a excepcao {" + e +"} ao tentar criar o ficheiro " + localFilePath + "!");
            }

            return false;
        }
    }
}
