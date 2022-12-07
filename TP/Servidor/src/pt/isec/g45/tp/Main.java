package pt.isec.g45.tp;


public class Main {
    public static void main(String[] args) {
      if (args.length != 2) {
          System.err.println("Main.exe \"Porto UDP\" \"Diretoria Base de dados\"");
      }
      Server server = new Server(args);
      server.run();
    }
}