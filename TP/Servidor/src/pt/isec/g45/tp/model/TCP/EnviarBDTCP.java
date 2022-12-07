/**
 *  Author: Bruno Sousa
 *  Test Class with logic
 *   *
 * */


package pt.isec.g45.tp.model.TCP;

import pt.isec.g45.tp.utils.utils;

import java.io.*;
import java.net.*;

public class EnviarBDTCP {


    File localDirectory;
    String fileName, localFilePath = null;
    FileOutputStream localFileOutputStream = null;
    int serverPort;
    Socket socketToServer = null;
    PrintWriter pout;
    InputStream in;
    byte []fileChunk = new byte[utils.MAX_CHUNK_SIZE];
    int nbytes;
    int port;
    InetAddress ip;
    String requestedCanonicalFilePath;
    FileInputStream requestedFileInputStream;
    OutputStream out;


    public EnviarBDTCP(File localDirectory, InetAddress ip, int porto, String filename) {
        this.localDirectory = localDirectory;
        this.ip = ip;
        this.port = porto;
        this.fileName = filename;
    }

    public boolean getFile() {
        try{


            requestedCanonicalFilePath = new File(localDirectory + File.separator + fileName).getCanonicalPath();

            if (!requestedCanonicalFilePath.startsWith(localDirectory.getCanonicalPath() + File.separator)) {
                System.out.println("Nao e' permitido aceder ao ficheiro " + requestedCanonicalFilePath + "!");
                System.out.println("A directoria de base nao corresponde a " + localDirectory.getCanonicalPath() + "!");
            }

            requestedFileInputStream = new FileInputStream(requestedCanonicalFilePath);
            System.out.println("Ficheiro " + requestedCanonicalFilePath + " aberto para leitura.");


        }catch(IOException e){

            if(localFilePath == null){
                System.out.println("Ocorreu a excepcao {" + e +"} ao obter o caminho canonico para o ficheiro local!");
            }else{
                System.out.println("Ocorreu a excepcao {" + e +"} ao tentar criar o ficheiro " + localFilePath + "!");
            }

            return false;
        }
        return true;
    }
    public boolean established() {

        try{

            this.serverPort = port; // TODO clear this

            socketToServer = new Socket(this.ip, serverPort);

            socketToServer.setSoTimeout(utils.TIMEOUT_TCP*1000);


            return true;

        }catch(UnknownHostException e){
            System.out.println("Destino desconhecido:\n\t"+e);

        }catch(NumberFormatException e){
            System.out.println("O porto do servidor deve ser um inteiro positivo:\n\t"+e);
        }catch(SocketTimeoutException e){
            System.out.println("Não foi recebida qualquer bloco adicional, podendo a transferencia estar incompleta:\n\t"+e);
        }catch(SocketException e){
            System.out.println("Ocorreu um erro ao nível do socket TCP:\n\t"+e);
        }catch(IOException e){
            System.out.println("Ocorreu um erro no acesso ao socket ou ao ficheiro local " + localFilePath +":\n\t"+e); // TODO CLEAR ALL EXCEPTIONS
        }
        return false;
    }


    public void run() {
        if (getFile()) {
            if (established()) {
                send_db();
            }

        }

    }

    private void send_db() {
        try {
            do {
                nbytes = requestedFileInputStream.read(fileChunk);

                if (nbytes != -1) {//EOF
                    out.write(fileChunk, 0, nbytes);
                    out.flush();
                }

            } while (nbytes > 0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
