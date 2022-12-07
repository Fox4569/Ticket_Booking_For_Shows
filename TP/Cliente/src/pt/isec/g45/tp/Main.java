package pt.isec.g45.tp;

import javafx.application.Application;
import pt.isec.g45.tp.ui.gui.MainJFX;

public class Main {

    public static void main(String[] args) {

        System.out.println("A executar a GUI...");

        Application.launch(MainJFX.class, args);

        /*
        ClientUDP a;
        ClientTCP b;
        if (args.length != 2) {
            System.out.println("Syntax - java ClientUDP serverAddr serverPort");
            return;
        }

        a = new ClientUDP(args[0], args[1]);
        a.run();
        System.out.println(a.getListaServers());
        b = new ClientTCP(a.getListaServers());
        b.run();
        */

    }
}
