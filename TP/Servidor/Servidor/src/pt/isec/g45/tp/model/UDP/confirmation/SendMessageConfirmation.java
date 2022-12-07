package pt.isec.g45.tp.model.UDP.confirmation;

import java.net.InetAddress;

public class SendMessageConfirmation {


    int dbVersao;
    InetAddress ip;
    int porto;
    public SendMessageConfirmation(int dbVersao, InetAddress ip, int porto) {
        this.dbVersao = dbVersao;
        this.porto = porto;
        this.ip = ip;
    }


    public Boolean established() {
        return false;
    }
    public void run () {

    }
}
