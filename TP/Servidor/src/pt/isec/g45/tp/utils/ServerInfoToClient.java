package pt.isec.g45.tp.utils;

import java.io.Serializable;

public class ServerInfoToClient implements Serializable,Comparable<ServerInfoToClient> {
     static final long serialVersionUID = 1L;
     private  int port;
     private  String ip;
     private  int carga;
     private  int versao;
     private int checker;
     public ServerInfoToClient(int port, String ip, int carga,int versao) {
          this.port = port;
          this.ip = ip;
          this.carga =carga;
          this.versao = versao;
          this.checker = 0;
     }

     public int getChecker() {
          return checker;
     }

     public void setChecker(int checker) {
          this.checker = checker;
     }

     public int getPort() {
          return port;
     }

     public String getIp() {
          return ip;
     }

     public int getCarga() {
          return carga;
     }

     public boolean isFinish() {
          return port == -1;
     }

     public int getVersao() {
          return versao;
     }

     public void setPort(int port) {
          this.port = port;
     }

     public void setIp(String ip) {
          this.ip = ip;
     }

     public void setCarga(int carga) {
          this.carga = carga;
     }

     public void setVersao(int versao) {
          this.versao = versao;
     }

     @Override
     public String toString() {
          return port !=-1? String.format("IP: %s ::: Porto: %d ::: Versao: %d", ip, port,versao):"FINISH";
     }


     @Override
     public int compareTo(ServerInfoToClient o) {
          return Integer.compare(getCarga(), o.getCarga());
     }
}
