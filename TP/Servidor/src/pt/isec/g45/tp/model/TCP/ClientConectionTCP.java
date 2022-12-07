/**
 *
 *  @author: Bruno Sousa
 *  @author: Jorge Santos
 *  @author: João Baptista
 * #####################################################################################################################
 * ##       This class is responsible to act as a worker on the Master-Workers mechanism                              ##
 * ##           -> Workers might be {Client, Parallel Server acting as a client to receive Database}                  ##
 * #####################################################################################################################
 * ##                                                                                                                 ##
 * ##                                Client Launches this worker                                                      ##
 * ##                              /                |                                                                 ##
 * ##                             /                 |                                                                 ##
 * ##       Worker Mechanism -----            Constructor() -> Gets data about the pre-established socket             ##
 * ##              |                                |                                                                 ##
 * ##              |                                |                                                                 ##
 * ##              |                                |                                                                 ##
 * ##              |                           Wait for client to communicate   (method: communication())             ##
 * ##              |                                |                                                                 ##
 * ##              |                                |                                                                 ##
 * ##              |                           Respond to client's message      (method: response())                  ##
 * ##              |                                |                                                                 ##
 * ##              |                                |                                                                 ##
 * ##              |                           This response varies depending the code it received                    ##
 * ##              |                                |                         (CodigosTCP)                            ##
 * ##              |                                |                                                                 ##
 * ##              |                                |---------->  CODE 1 (Client- Edits an registered data on db)     ##
 * ##              |                                |                     (EDITAR_DADOS_REGISTOS)                     ##
 * ##              |                                |                                                                 ##
 * ##              |                                |---------->  CODE 2 (Client- Retrieves the current shows on db)  ##
 * ##              |                                |                     (CONSULTA_ESPETACULOS)                      ##
 * ##              |                                |                                                                 ##
 * ##              |                                |---------->  CODE 3 (Client- Retrieve a show from db)            ##
 * ##              |                                |                     (SELECT_ESPETACULO)                         ##
 * ##              |                                |                                                                 ##
 * ##              |                                |---------->  CODE 4 (Client- Retrieves available sits of a show) ##
 * ##              |                                |                      (SELECT_AVAILABLE_SITS_FROM_SHOW)          ##
 * ##              |                                |                                                                 ##
 * ##              |                                |---------->  CODE 5 (SClient- Retrieves current db file)         ##
 * ##              |________________________________|---------->           (TRANSFERE_DB)                             ##
 * #####################################################################################################################
 */

package pt.isec.g45.tp.model.TCP;

import pt.isec.g45.tp.model.ServerManager;
import pt.isec.g45.tp.utils.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Filter;

public class ClientConectionTCP extends Thread{

    byte []fileChunk = new byte[utils.MAX_CHUNK_SIZE];
    int nbytes;

    String requestedCanonicalFilePath;
    FileInputStream requestedFileInputStream;

    File localDirectory;

    Socket socket;
    MsgFromClientTCP req;
    ServerManager server;
    ServerTCP tcpServer;
    String fileName, localFilePath = null;
    Boolean threadActive= true;

    public Boolean getThreadActive() {
        return threadActive;
    }

    public void setThreadActive(Boolean threadActive) {
        this.threadActive = threadActive;
    }

    public Socket getSocket() {
        return socket;
    }

    /**
     * Method to open database file
     * @return Boolean {true,false} true if the file is open, false otherwise.
     */

    public boolean getFile() {
        try{


            requestedCanonicalFilePath = new File(localDirectory + File.separator + utils.DB_FILE_NAME).getCanonicalPath();

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
    // CONSTRUTOR

    /**
     * @constructor
     * @param s Socket with the connection (to the client)
     * @param server Reference for ServerManager Object
     */
    public ClientConectionTCP(Socket s, ServerManager server, ServerTCP tcpServer) {
        this.socket = s;
        this.server = server;
        this.tcpServer = tcpServer;
        start();
    }
    private boolean sendPrepare(String comando) {

        return !server.sendPrepare(comando);
    }

    // TODO antes da conversa e espera de argumentos,
    //  o utilizador deve mandar um objeto que tem as suas informaçoes para se obter o seu id para futura referencia
    private Boolean prepareServersForChange(MsgFromClientTCP msg) {
        String []args = msg.getArgs();
       if(msg.getCodigo() == CodigosTCP.EDITAR_DADOS_REGISTO) {
           if (sendPrepare(server.getSqlFromEditDataRegisto(args[1], args[0], args[2]))) {
               if (sendPrepare(server.getSqlFromEditDataRegisto(args[1], args[0], args[2]))) {
                   return false;
               };
           }

       }
       else if(msg.getCodigo() == CodigosTCP.PAY_RESERVATION) {
            if (sendPrepare(server.getPayCommmand(Integer.parseInt(args[0])))) {
                if (sendPrepare(server.getPayCommmand(Integer.parseInt(args[0])))) {
                    return false;
                }
            }
       } else if (msg.getCodigo() == CodigosTCP.LOGOUT) {
           if (sendPrepare(server.getLogoutCommand((args[0])))) {
               if (sendPrepare(server.getLogoutCommand((args[0])))) {
                   return false;
               }
           }
       }
        server.sendCommit();
        return true;
    }
    /**
     * Method that responds to a client. it varies depending on wut it has received.
     * @param codigo Code from
     * @param out    Gets a Reference to ObjectsOutputStream
     * @param msg    Reference for an object MsgFromClientTCP
     */
    public void response(CodigosTCP codigo, ObjectOutputStream out, MsgFromClientTCP msg) {

        String [] args = msg.getArgs();
        // Verifica o código recebido e executa a açao necessaria.
        switch(codigo) {
            case PAY_RESERVATION -> {
                //prepareServersForChange(ms)
               if (prepareServersForChange(msg)) {
                   Boolean isDone = server.payReserivation();
                   try {
                       if (isDone) {
                           out.writeObject("Reservation has been payed");
                           out.flush();
                       }else {
                           out.writeObject("Reservation not in the db.");
                           out.flush();
                       }
                   } catch (IOException e) {
                       throw new RuntimeException(e); // TODO Criar um sistema de erros que nao afeta o server em si
                   }
               }
            }
            case EDITAR_DADOS_REGISTO -> {
                String response;
                if (prepareServersForChange(msg)) {
                    server.editDataRegisto(args[1],args[0], args[2]); // args[0] -> Username, args[1] -> campo, args[2] -> replacement
                    server.setDBVersao(server.getDBVersao() +1);
                    server.sendHeartBeat(); // TODO verificar se a ediçao e realizada e informar o utilizador

                     response = utils.RESPONSE_EDIT_OK;
                }
                else {
                     response = utils.RESPONSE_EDIT_NOT_OK;

                }


                try {
                    out.writeObject(response);
                    out.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e); // TODO Criar um sistema de erros que nao afeta o server em si
                }
            }

            // obter espetaculos da base de dados e envia para o utilizador
            case CONSULTA_ESPETACULOS -> {
                ArrayList<Espetaculo> espetaculos = server.getEspetaculosAtivos();

                try {
                    for(Espetaculo e: espetaculos) {
                        out.writeObject(e);
                        out.flush();
                    }
                } catch (IOException e) {
                    //throw new RuntimeException(e);
                    return;
                }

            }
            case LOGOUT -> {
                prepareServersForChange(msg);
                server.logout(args[0]);
                tcpServer.removeFromTCP(this);
                try {
                    out.writeObject("Conta desconectada.");
                    out.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            case MAKE_RESERVATION -> {
                Boolean isDone = server.makeReservation(args[0],args[2],Integer.parseInt(args[1]));



                try {
                    if(isDone) {
                        out.writeObject("Reserva Feito");
                        out.flush();
                    }
                    else {
                        out.writeObject("Reserva Não foi feito");
                        out.flush();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            case RESERVE_SITS -> {

                server.reserveSits(args[0], args[1], msg.getLugares());

                Boolean isDone = true;

                try {
                    if(isDone) {
                        out.writeObject("Reserva Feito");
                        out.flush();
                    }
                    else {
                        out.writeObject("Reserva Não foi feito");
                        out.flush();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            case GET_FILTERED_SHOW -> {
                String fs = args[0]; // Filter
                ArrayList<Espetaculo> espetaculos = server.getFilteredShow(fs, args[1]); // args[1] filter

                try {
                    for(Espetaculo e: espetaculos) {
                        out.writeObject(e);
                        out.flush();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            // Seleciona um espetaculo da base de dados  e envia para o utilizador (cliente)
            case CONSULTA_ESPETACULO -> {
                int id = Integer.parseInt(args[0]); // args[0] -> id
                Espetaculo show = server.getEspetaculo(id);

                try {
                    out.writeObject(show);
                    out.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            case DELETE_SHOW -> {
                int id = Integer.parseInt(args[0]); // args[0] -> id
                //prepareServersForChange(msg);
                server.deleteShow(id);
                try {
                    out.writeObject("Done");
                    out.flush();
                } catch (IOException e) {
                    //throw new RuntimeException(e);
                    return;
                }

            }
            case INSERT_SHOW -> {
                String filename = args[0];
                //prepareServersForChange(msg);

                server.insertShow(filename);
                try {
                    out.writeObject("Done");
                    out.flush();
                } catch (IOException e) {
                    //throw new RuntimeException(e);
                    return;
                }
            }
            case DELETE_NOT_PAYED_RESERVATION -> {

                Boolean isDone  = server.deleteNotPayedReservation(Integer.parseInt(args[0]));

                try {
                    if (isDone){
                        out.writeObject("Reserva com o id " + args[0] + " foi eliminada.");
                        out.flush();
                    }
                    else {
                        out.writeObject("Reserva com o id " + args[0] + " não foi eliminada.");
                        out.flush();
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            // Seleciona lugares disponiveis no espetaculo
            case SELECT_AVAILABLE_SITS_FROM_SHOW -> {
                int id = Integer.parseInt(args[0]); // args[0] -> id
                ArrayList<Lugar> lugares = server.getLugaresDisponiveis(id);

                try {
                    for(Lugar e: lugares) {
                        out.writeObject(e);
                        out.flush();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            case GET_NOT_PAYED_RESERVATIONS -> {
                ArrayList<Reserva> reservas = server.getNotPayedReservas();

                try {
                    for(Reserva e: reservas) {
                        out.writeObject(e);
                        out.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            case GET_PAYED_RESERVATION -> {
                ArrayList<Reserva> reservas = server.getPayedReservas();

                try {
                    for(Reserva e: reservas) {
                        System.out.println("DEBUGGG - RESERVA: " + e);
                        out.writeObject(e);
                        out.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            // Transferir base de dados para outro servidor
            case TRANSFERE_DB -> {
                localDirectory= server.getLocalDirectory();
                fileName = utils.DB_FILE_NAME;
                if (getFile()) {
                    send_db(out);
                }
            }
            case ASK_FOR_SITS -> {
                int id = Integer.parseInt(args[0]); // args[0] -> id
                ArrayList<Lugar> lugares = server.getLugares(id);

                try {
                    for(Lugar e: lugares) {
                        out.writeObject(e);
                        out.flush();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }

    void verifica_prepare_servers() {

    }
    // TODO acabar comunicação com o cliente

    /**
     * This method waits for a message from client.
     * After that it will call the response method.
     */
    public void comunication() { // TODO Melhorar loop (ver exceçao)

            try(ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()) ){

                while(threadActive) {

                    req = (MsgFromClientTCP) in.readObject();
                    response(req.getCodigo(), out, req);
                }
            } catch (IOException | ClassNotFoundException e) {
                //e.printStackTrace();
                tcpServer.removeFromTCP(this);
                return;
            }

    }

    /*Este método é o responsável por enviar a base de dados para outros servidor*/
    private void send_db(ObjectOutputStream out) {
        try {
            // envia bytes do ficheiro enquanto este os bytes lidos forem tiverem tamanho > 0
            do {

                nbytes = requestedFileInputStream.read(fileChunk);

                if (nbytes != -1) {//EOF
                    out.write(fileChunk, 0, nbytes);
                    out.flush();
                }

            } while (nbytes > 0);

        } catch (IOException e) {
            e.printStackTrace();
            server.getHb().setCarga(server.getHb().getCarga() -1);

        }

    }
    @Override
    public void run() {
       comunication();
    }
}
