package pt.isec.g45.tp;

import pt.isec.g45.tp.model.ServerManager;

public class Server {

    int porto;
    String file;
    ServerManager serverManager;
    public Server(String[] args) {
        initializeData(args);
    }

    private void initializeData(String [] args){
        int porto = 5001;
        String file = "C:\\sqlite\\db\\";
        serverManager = new ServerManager(porto, file);
    }

    public void run() {
        serverManager.start();

    }
}
