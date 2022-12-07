package pt.isec.g45.tp.utils;

import java.io.Serializable;
import java.net.InetAddress;

public class HeartBeat implements Serializable {
    static final long serialVersionUID = 1234L;

    String msg;
    InetAddress ip;
    int port;
    boolean estado;
    int versaoBaseDeDados;
    int carga;
    public HeartBeat(String msg, InetAddress ip, int port, boolean estado, int versaoBaseDeDados, int carga) {
        this.msg = msg;
        this.ip = ip;
        this.port = port;
        this.estado = estado;
        this.versaoBaseDeDados = versaoBaseDeDados;
        this.carga = carga;

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public void setVersaoBaseDeDados(int versaoBaseDeDados) {
        this.versaoBaseDeDados = versaoBaseDeDados;
    }

    public void setCarga(int carga) {
        this.carga = carga;
    }

    public int getPort() {
        return port;
    }

    public boolean isEstado() {
        return estado;
    }

    public int getVersaoBaseDeDados() {
        return versaoBaseDeDados;
    }

    public int getCarga() {
        return carga;
    }

    public InetAddress getIp() {
        return ip;
    }

    @Override
    public String toString() {

        String ips = this.ip == null? "null":ip.toString();
        return String.format("Msg: %s\nFrom: %s", msg, ips);
    }
}
