package pt.isec.g45.tp.model.tcp;

import pt.isec.g45.tp.model.ClientManager;
import pt.isec.g45.tp.model.fsm.AppState;
import pt.isec.g45.tp.utils.AckVerification;
import pt.isec.g45.tp.utils.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class UpdateTCP extends  Thread{

    ServerSocket socket;
    int listeningPort;
    ObjectOutputStream oout;
    ObjectInputStream oin;
    Socket toClientSocket;
    Boolean isRunning;
    ClientManager clManager;
    AckVerification aux;

    public Boolean getRunning() {
        return isRunning;
    }

    public void setRunning(Boolean running) {
        isRunning = running;
    }

    public UpdateTCP(ClientManager cl) {
        socket = null;
        isRunning = true;
        this.clManager =cl;

    }
    @Override
    public void run() {
        //Tenta estabelecer ligação TCP
        estabeleceLigacao();
        while(isRunning) {
            aguardaRequest();
            readRequest();
            sendAnswer();
        }



    }
    public void sendAnswer(){

        try {
            oout.writeObject("RECEBIDO");
            oout.flush();
        } catch (IOException e) {
            return;
        }
    }
    public void readRequest() {
        try {
            aux = (AckVerification) oin.readObject();
            System.out.println(aux.toString());
            clManager.updateServersActiveList(aux.getInfo());
            if (aux.geteUpdate() != null) {
                switch (aux.geteUpdate()) {
                    case SHOWS -> {
                        if (clManager.getApp().getState() == AppState.ADMIN_PANEL_STATE || clManager.getApp().getState() == AppState.MAKE_RESERVATION || clManager.getApp().getState() == AppState.BEGIN_MENU) {

                        }
                    }
                    case RESERVAS -> {

                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            return;
        }
    }
    public void aguardaRequest() {
        try {
            toClientSocket = socket.accept();
            toClientSocket.setSoTimeout(utils.TIMEOUT_TCP * 1000);
            oout = new ObjectOutputStream(toClientSocket.getOutputStream());
            oin = new ObjectInputStream(toClientSocket.getInputStream());

        } catch (IOException e) {
            try {
                if (toClientSocket != null)
                    toClientSocket.close();

            } catch (IOException ex) {
                System.out.println("Erro: " + ex);
            }
        }
    }
    public void disconectSocket() {
        try {
            this.socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void estabeleceLigacao() {
        try {
            socket = new ServerSocket(utils.AUTO_PORT_TCP);
            listeningPort = socket.getLocalPort();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
