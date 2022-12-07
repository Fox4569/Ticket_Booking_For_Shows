package pt.isec.g45.tp.utils;

import java.io.Serializable;

public class ServerInfoToClient implements Serializable,Comparable<ServerInfoToClient> {
     static final long serialVersionUID = 1L;
     private final int port;
     private final String ip;
     private final int carga;
     private final int versao;
     public ServerInfoToClient(int port, String ip, int carga,int versao) {
          this.port = port;
          this.ip = ip;
          this.carga =carga;
          this.versao = versao;
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

     @Override
     public String toString() {
          return port !=-1? String.format("%s %d %d", ip, port,versao):"FINISH";
     }


     @Override
     public int compareTo(ServerInfoToClient o) {
          return Integer.compare(getCarga(), o.getCarga());
     }
}
