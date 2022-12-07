package pt.isec.g45.tp.utils;

import java.io.Serializable;
import java.util.ArrayList;

public class AckVerification implements Serializable {
    static final long serialVersionUID = 1L;

    ArrayList<ServerInfoToClient> info;
    EstadosToUpdate eUpdate;

    public AckVerification(){
        info = null;
        eUpdate = null;
    }

    public EstadosToUpdate geteUpdate(){
        return eUpdate;
    }
    public void setEUpdate(EstadosToUpdate e) {
        eUpdate = e;  // TODO PASSAR ESTE FILE PARA O SERVER
    }
    public ArrayList<ServerInfoToClient> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<ServerInfoToClient> info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "AckVerification{" +
                "info=" + info +
                '}';
    }
}
