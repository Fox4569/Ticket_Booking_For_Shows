package pt.isec.g45.tp.utils;

public class utils {
    public static int TIMEOUT_UDP = 5;
    public static int TIMEOUT_TCP = 5;
    public static int BUFFERSIZE = 256;
    public static String PEDIDO_LIGACAO = "REQUEST";
    public static int SIZE_PEDIDO_LIGACAO = PEDIDO_LIGACAO.length();
    public static int MAX_SIZE = 50;
    public static int MAX_CHUNK_SIZE = 4000;
    public static int AUTO_PORT = 0;
    public static int PORTO_MULTICAST = 4004;
    public static String MULTICAST_IP = "239.39.39.39";

    public static String INTERFACE_WLAN0 = "wlan0";

    public static String DB_FILE_NAME = "PD-2022-23-TP.db";
    public static String DB_ADAPTER = "jdbc:sqlite:";

    public static int OBJECT_BUFFSIZE = 2400;

    public static String RESPONSE_EDIT_OK = "Edição realizada com sucesso";
    public static String RESPONSE_EDIT_NOT_OK = "Não foi possivel editar os seus dados";
}
