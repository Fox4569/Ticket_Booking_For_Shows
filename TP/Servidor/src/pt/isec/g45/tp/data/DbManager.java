/*********************
*
 * @author: Bruno Sousa
*  @author: Jorge Santos
*  @author: João Baptista
*#####################################################################################################################
*   This file contains the class DbManager. This class is responsible for managing the database.
 *  Method to connect to Database: connectDb()
 *
************************/

package pt.isec.g45.tp.data;

import pt.isec.g45.tp.utils.Espetaculo;
import pt.isec.g45.tp.utils.Lugar;
import pt.isec.g45.tp.utils.Reserva;
import pt.isec.g45.tp.utils.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/***********
* This class has the purpose of managing the database.
* The constructor will take a String that indicates the path of the database file.
*
************/
public class DbManager {

    String path;            // Holds database file path in current server file system.
    Connection conn = null; // Reference for a connection to the SQLlite DB.
    static Statement stmt = null;  // Holds the statement (command) to execute on the DB.
    ResultSet rs = null;    // Holds the result of the previous executed command.

    /****************
     **@constructor
    * Constructor for DbManager class.
    * @param path: represents the path where the Database file is located (String).
    *
    *
    *****************/

    public DbManager(String path) {
        this.path = path + utils.DB_FILE_NAME;
    }

    /**************
     * This method allows to connect with the dataBase (SQLlite)
     * @return true if connection is established and false if it is not.
     * @see SQLException if it fails
    ***************/
    public boolean connectDB() {
        try {
            conn = DriverManager.getConnection(path);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /*****
     *  This method allows to register a show into the database (SQLlite).
     * @param descricao  String that describes the shows
     * @param tipo       String that indicates the type of the show
     * @param data_hora  String that indicates the time and date of the show
     * @param duracao    Integer that indicates the duration of the show
     * @param local      String that indicates the location of the show
     * @param localidade String that indicates the locality of the show
     * @param pais       String that indicates the country of the show
     * @param cla        String that indicates the Age Rating
     * @param visivel    Integer that indicates if the show is visible for users (0 - is not, 1 - it is).
     */
    public static void registerShow(String descricao, String tipo, String data_hora, int duracao, String local, String localidade, String pais, String cla, int visivel) {
        try {
            String query = Commands.registShow(descricao,tipo,data_hora,duracao,local,localidade,pais,cla,visivel);
            stmt.executeQuery(query);
        } catch (SQLException e) {
            return;
        }
    }
    // Espetáculo - Getters

    /**
     * This method allows the user to get available shows registered into the database.
     * @return Arraylist of the Object (Espetaculo)
     * @see SQLException if it fails to retrieve the shows.
     */
    public ArrayList<Espetaculo> getAvailableShows() {
        ArrayList<Espetaculo> espetaculos = new ArrayList<>();
        try {
            String query = Commands.SELECT_AVAILABLE_SHOWS;
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String desc = rs.getString("descricao");
                String tipo = rs.getString("tipo");
                String data = rs.getString("data_hora");
                int duracao = rs.getInt("duracao");
                String local = rs.getString("local");
                String localidade = rs.getString("localidade");
                String pais = rs.getString("pais");
                String classificao_etaria = rs.getString("classificacao_etaria");
                int visivel = rs.getInt("visivel");
                espetaculos.add(new Espetaculo(id,desc,tipo,data,duracao,local,localidade,pais,classificao_etaria,visivel));
            }
            espetaculos.add(new Espetaculo(-1));

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return espetaculos;
    }

    /***
     * This method gets a particular show
     * @param id Integer that indicates the id of the show registered into the database
     * @return Object (Espetaculo)
     * @see SQLException if it fails to retrieve the show.
     */
    public Espetaculo getEspetaculo(int id) {
        try {
            String query = Commands.GET_ESPETACULO+id;
            rs = stmt.executeQuery(query);

            String desc = rs.getString("descricao");
            String tipo = rs.getString("tipo");
            String data = rs.getString("data_hora");
            int duracao = rs.getInt("duracao");
            String local = rs.getString("local");
            String localidade = rs.getString("localidade");
            String pais = rs.getString("pais");
            String classificao_etaria = rs.getString("classificacao_etaria");
            int visivel = rs.getInt("visivel");

            return new Espetaculo(id, desc,tipo,data,duracao,local,localidade,pais,classificao_etaria, visivel);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * This method allows the user to get the "description" of a show
     * @param id Integer that indicates the id of the show registered into the database
     * @return description of the show (String)
     * @see  SQLException if it fails to retrieve the value from the database (SQLlite).
     */
    public String getDescricao(int id){
        try {
            String query = Commands.getDescricao(id);   //TODO IMPROVE THIS HARDCODING WITH LOCAL VARIABLES
            ResultSet aux;
            Statement aux2 = conn.createStatement();
            aux = aux2.executeQuery(query);

            return aux.getString("descricao");
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public int getShowIDByName(String name){
        try {
            String query = Commands.GET_ESPETACULO_ID_BY_NAME + name + "'";
            ResultSet aux;
            Statement aux2 = conn.createStatement();
            aux = aux2.executeQuery(query);

            return Integer.parseInt(aux.getString("id"));
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    // Espetáculo - Setters

    /****
     * This method allows to change the visibility of a show (Admin privilege)
     * @param value Integer {0,1} 0 if is not visible and 1 if it is.
     * @see SQLException if it fails to connect
     */
    public void setVisibilidadeEspetaculo(int value) {
        try {
            String query = Commands.SET_SHOW_VISIBILITY + value;
            stmt.executeQuery(query);
        } catch (SQLException e) {
            return;
        }
    }

    // LUGAR
    // Lugar - Getters

    /***
     * Method that allows to get available sits of a particular show
     * @param id_espetaculo Integer that indicates the id of the show in the database.
     * @return ArrayList of the Object (Lugar)
     * @see SQLException if it fails to connect
     */
    public ArrayList<Lugar> getAvailableSits(int id_espetaculo) {
        ArrayList<Lugar> lugares= new ArrayList<>();
        try {
            //String query = Commands.GET_AVAILABLE_SITS_FROM_SHOW + id_espetaculo;
            String query = Commands.getAvailableSitsFromShow(id_espetaculo);
            rs = stmt.executeQuery(query);

            while(rs.next()) {
                int id = rs.getInt("id");
                String fila = rs.getString("fila");
                String assento = rs.getString("assento");
                double preco = rs.getDouble("preco");
                lugares.add(new Lugar(id,fila,assento,preco,id_espetaculo));
            }
            lugares.add(new Lugar(-1,"xxx","zzz",1.02,2));


        } catch (SQLException e) {
            return null;
        }
        return lugares;
    }

    public ArrayList<Lugar> getSits(int id_espetaculo) {
        ArrayList<Lugar> lugares= new ArrayList<>();
        try {
            String query = Commands.GET_SITS_FROM_SHOW + id_espetaculo;
            rs = stmt.executeQuery(query);

            while(rs.next()) {
                int id = rs.getInt("id");
                String fila = rs.getString("fila");
                String assento = rs.getString("assento");
                double preco = rs.getDouble("preco");
                lugares.add(new Lugar(id,fila,assento,preco,id_espetaculo));
            }

            lugares.add(new Lugar(-1,"xxx","zzz",1.02,2));

        } catch (SQLException e) {
            return null;
        }
        return lugares;
    }

    // Lugar - Setters

    public static void registerSits(String fila, int assento, int preco, int espetaculo_id) {
        try {
            String query = Commands.registSitShow(fila, assento, preco, espetaculo_id);
            stmt.executeQuery(query);
        } catch (SQLException e) {
            return;
        }
    }

    // RESERVA
    // Reserva - Getters

    /***
     * Method that allows to get reservations in the database
     * @param reservas Empty ArrayList of the Object (reservas)
     * @param query    String that represents the command to execute
     * @return Arraylist of the Object (Reserva)
     * @throws SQLException if it fails to connect to the database or retrieve the value intended.
     */
    private ArrayList<Reserva> getReservas(ArrayList<Reserva> reservas, String query) throws SQLException {
        rs = stmt.executeQuery(query);

        while(rs.next()) {
            int id = rs.getInt("id");
            int pago = rs.getInt("pago");
            int id_utilizador = rs.getInt("id_utilizador");
            int id_espetaculo = rs.getInt("id_espetaculo");
            String data_hora = rs.getString("data_hora");
            String descricao = getDescricao(id_espetaculo);

            reservas.add(new Reserva(id, pago, id_utilizador, id_espetaculo, data_hora, descricao));
        }

        reservas.add(new Reserva(-1, 69, 69, 69, "zzz", "xxx"));

        return reservas;
    }

    /**
     * Method that allows to get the not paid reservations from the database
     *
     * @return ArrayList of the Object (Reserva)
     * @see String with the error of the exception {SQLException} if it fails to connect to the database.
     */
    public ArrayList<Reserva> getNotPaidReservations(){
        try {
            ArrayList<Reserva> reservas = new ArrayList<>();
            String query = Commands.GET_NOT_PAID_RESERVATIONS;

            return getReservas(reservas, query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method that allows to return the paid reservations
     * @return ArrayList of the Objet (Reserva)
     * @see String if it throws the exception SQLExcpetion.
     */

    public ArrayList<Reserva> getPaidReservations(){
        try {
            ArrayList<Reserva> reservas = new ArrayList<>();
            String query = Commands.GET_PAID_RESERVATIONS;

            return getReservas(reservas, query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    // Reserva - Setters

    /**
     * Method that allows to create a new reserve (add into the database)
     * @param data_hora String that indicates the time of the show
     * @param id_utilizador Integer that indicates the id of the user
     * @param id_espetaculo Integer that indicates the id of the show
     */
    public Boolean registNewReserve(String data_hora, int id_utilizador, int id_espetaculo) {
        try {

            String query = Commands.REGIST_RESERVE + Commands.registNewReserve(data_hora, id_utilizador,id_espetaculo);
            stmt.executeQuery(query);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Metthod that allows to change the status of a property on reservas table of the DB.
     * @param value Integer {0,1} 0 if it is not paid and 1 if it is.
     * @see String if an Exception its thrown it prints why it was thrown.
     */
    public Boolean setReservaPago(int value) {
        try {
            String query = Commands.SET_RESERVA_PAGO + value;
            stmt.executeQuery(query);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public String getReservaPago(int value) {
        return Commands.SET_RESERVA_PAGO + value;
    }

    /**
     * Method that allows to delete a reservation from the database.
     * @param id_reserva Integer that indicates the id of the reservation
     * @see String with the result of the error.
     */
    public boolean deleteReservation(int id_reserva) {
        try {
            String query = Commands.DELETE_NOT_PAID_RESERVATION + id_reserva;
            stmt.executeQuery(query);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // RESERVA_LUGAR
    // Reserva_Lugar - Getters
    // Reserva_Lugar - Setters

    // UTILIZADOR

    /**
     * Method that allows to register an Admin
     * @param username String that indicates the username of the user
     * @param password String that indicates the password of the user
     */
    public void registerAdmin(String username, String password) {
        try {
            String query = Commands.REGIST_ADMIN + Commands.registCommandAdmin(username, password);
            stmt.executeQuery(query);
        } catch (SQLException e) {
            return;
        }
    }

    /**
     * Method that allows to register a user
     * @param username String that indicates the username of the user
     * @param password String that indicates the password of the user
     */

    public void registerUser(String username, String password, String name) {
        try {
            String query = Commands.REGIST_USER + Commands.registCommand(username, password, name);
            stmt.executeQuery(query);
        } catch (SQLException e) {
            return;
        }
    }
    public String getRegisterCommand(String username, String password, String name) {
        return Commands.REGIST_USER + Commands.registCommand(username, password, name);
    }

    // Utilizador - Getters

    /**
     * Method that allows to the the password of a user
     * @param username String that indicates the username
     * @return String (password)
     */
    public String getPassword(String username) {
        try {
            String query = Commands.GET_PASSWORD + username + "'";
            rs = stmt.executeQuery(query);

            return rs.getString("password");
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }
    // Utilizador - Setters

    /**
     * Method that allows to change the username
     * @param oldUsername String that contains the old username
     * @param newUsername String that contains the new username
     */
    public void setUsername(String oldUsername, String newUsername){
        try {
            String query = Commands.SET_USERNAME + Commands.setUsername(oldUsername, newUsername);
            stmt.executeQuery(query);
        } catch (SQLException e) {
            return;
        }
    }
    public String getUsername(String oldUsername, String newUsername) {
        return Commands.SET_USERNAME + Commands.setUsername(oldUsername, newUsername);
    }
    /**
     * Method that allows to change the name of a user
     * @param username String that contains the username of the user
     * @param newName String that contains the new name of the user
     */
    public void setName(String username, String newName){
        try {
            String query = Commands.SET_NAME + Commands.setName(username, newName);
            stmt.executeQuery(query);
        } catch (SQLException e) {
            return;
        }
    }
    public String getNameCommand(String username, String newName) {
        return Commands.SET_NAME + Commands.setName(username, newName);
    }
    /**
     * Method that allows to change the password of a user
     * @param username String that indicates the username of the user
     * @param newPassword String that indicates the new Password of the  user
     */
    public void setPassword(String username, String newPassword){
        try {
            String query = Commands.SET_PASSWORD + Commands.setPassword(username, newPassword);
            stmt.executeQuery(query);
        } catch (SQLException e) {
            return;
        }
    }
    public String getCommandPassword(String username, String newPassword) {
        return Commands.SET_PASSWORD + Commands.setPassword(username, newPassword);
    }
    /**
     * Method that allows to change autentication status for the user
     *
     * @param value Integer {0,1} 0 if it is not authenticated and 1 if it is.
     */
    public void setAutenticado(int value, String username) {
        try {
            String query = Commands.SET_AUTENTICADO + value + Commands.setAutenticado(username);
            stmt.executeQuery(query);
        } catch (SQLException e) {
            return;
        }
    }
    public String getAutenticadoComando(int value, String username) {
        return Commands.SET_AUTENTICADO + value + Commands.setAutenticado(username);
    }
    // VERSÃO

    /**
     * Method that allows to create the TableVersion (used if it is necessary to create a new database)
     *
     */
    public void createTableVersion() {
        try {
            String query = Commands.CREATE_TABLE_VERSION;
            stmt.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to execute a not specified command.
     * @param query String that represents the SQL Command to execute.
     */
    public void executeCommand(String query) {
        try {
            stmt.executeQuery(query);

        }catch(SQLException e) {
            return;

        }
    }
    // Versão - Getter

    /**
     * Method that allows to get the Database Version
     * @return Integer that represents the Current Database Version
     */
    public int getDBVersao() {
        try {
            String query = Commands.GET_DB_VERSION;
            rs = stmt.executeQuery(query);
            return rs.getInt("versao");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Versão - Setter

    /**
     * Method that allows to put version of the database to 1.
     */
    public void restartVersion() {
        try {
            String query = Commands.SET_DB_VERSION + 1;
            stmt.executeQuery(query);
        } catch (SQLException e) {
            return;
        }
    }

    /**
     * method that allows to change the current version of the database.
     * @param version Integer that holds the new Version.
     */
    public void setTableVersion(int version) {
        try {
            String query = Commands.SET_DB_VERSION + version;
            stmt.executeQuery(query);
        } catch (SQLException e) {
            return;
        }
    }

    public ArrayList<Espetaculo> getFilteredShow(String filter, String nome) { // TODO
        try {
            System.out.println("DEBUG ### Filter: " + filter + "\nDEBUG ### Nome: " + nome);

            ArrayList<Espetaculo> espetaculos = new ArrayList<>();
            String query = Commands.getSelectFilteredShows(filter,nome) ;
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                String desc = rs.getString("descricao");
                String tipo = rs.getString("tipo");
                String data = rs.getString("data_hora");
                int duracao = rs.getInt("duracao");
                String local = rs.getString("local");
                String localidade = rs.getString("localidade");
                String pais = rs.getString("pais");
                String classificao_etaria = rs.getString("classificacao_etaria");
                int visivel = rs.getInt("visivel");
                espetaculos.add(new Espetaculo(id,desc,tipo,data,duracao,local,localidade,pais,classificao_etaria,visivel));
            }
            espetaculos.add(new Espetaculo(-1));
            return espetaculos;
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean addShowByFile(String filename){
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            Scanner sc = new Scanner(br);

            sc.useDelimiter("\\n");

            String line, line2;
            String designacao = null, tipo = null, data_hora = null, local = null, localidade = null, pais = null, classificacao_etaria = null, duracaoS;
            int duracao = 0;
            StringBuilder sb;
            sb = new StringBuilder();
            String filas;

            while (sc.hasNext()) {
                line = sc.nextLine();

                Scanner sc2 = new Scanner(line);

                sc2.useDelimiter(";");

                while(sc2.hasNext()){
                    line2 = sc2.next();

                    if(line2.equalsIgnoreCase("\"Designação\"")) {
                        designacao = sc2.next();
                        designacao = designacao.replace("\"", "");
                    }else if(line2.equalsIgnoreCase("\"Tipo\"")) {
                        tipo = sc2.next();
                        tipo = tipo.replace("\"", "");
                    }else if(line2.equalsIgnoreCase("\"Data\"")) {
                        sb.append(sc2.next());  //dia
                        sb.append(sc2.next());  //mes
                        sb.append(sc2.next());  //ano
                    }else if(line2.equalsIgnoreCase("\"Hora\"")){
                        sb.append(sc2.next());  //hora
                        sb.append(sc2.next());  //minutos
                    }else if(line2.equalsIgnoreCase("\"Duração\"")) {
                        duracaoS = sc2.next();
                        duracaoS = duracaoS.replace("\"", "");
                        duracao = Integer.parseInt(duracaoS);
                    }else if(line2.equalsIgnoreCase("\"Local\"")) {
                        local = sc2.next();
                        local = local.replace("\"", "");
                    }else if(line2.equalsIgnoreCase("\"Localidade\"")) {
                        localidade = sc2.next();
                        localidade = localidade.replace("\"", "");
                    }else if(line2.equalsIgnoreCase("\"País\"")){
                        pais = sc2.next();
                        pais = pais.replace("\"", "");
                    }else if(line2.equalsIgnoreCase("\"Classificação etária\"")) {
                        classificacao_etaria = sc2.next();
                        classificacao_etaria = classificacao_etaria.replace("\"", "");
                    }
                }

                if(line.contains("\"Fila\"")) {
                    //Adicionar o espetaculo
                    data_hora = sb.toString();
                    data_hora = data_hora.replace("\"", " ");

                    registerShow(designacao, tipo, data_hora, duracao, local, localidade, pais, classificacao_etaria, 1);

                    while (sc.hasNext()) {
                        filas = sc.nextLine();

                        Scanner sc3 = new Scanner(filas);

                        sc3.useDelimiter(";|:");

                        String fila = sc3.next();
                        fila = fila.replace("\"", "");

                        while (sc3.hasNext()) {
                            String assento = sc3.next();
                            assento = assento.replace("\"", "");
                            assento = assento.replace(" ", "");
                            String preco = sc3.next();
                            preco = preco.replace("\"", "");
                            preco = preco.replace(" ", "");

                            int id = getShowIDByName(designacao);

                            registerSits(fila, Integer.parseInt(assento), Integer.parseInt(preco), id);
                        }
                    }
                }

            }

            br.close();
        }catch (IOException e){
            System.out.println("Não foi possível localizar o ficheiro!");
            return false;
        }
        return true;
    }


    public synchronized int isADmin(String username) {
        try {
            String query = Commands.GET_USER_ISADMIN + username + "'";
            Statement stmt2 = conn.createStatement();

            rs = stmt2.executeQuery(query);


            return rs.getInt(1) == 1?-2:-1;

        } catch (SQLException e) {
            e.printStackTrace();
            return -5;
        }
    }

    public void deleteShow(int id) {
        try {
            String query = Commands.DELETE_SHOW + id ;
            //Statement stmt = conn.createStatement();

            stmt.executeQuery(query);



        } catch (SQLException e) {
            //e.printStackTrace();
            return;
        }
    }

    public void reserveSits(String id_espetaculo, int id_utilizador, ArrayList<Lugar> lugares) {
        try {
            int id_reserva = getIdReserva(Integer.parseInt(id_espetaculo), id_utilizador);

            if (id_reserva < 0)
                return;

            for (Lugar l : lugares) {
                String query = Commands.registSit(id_reserva, l.getId());
                stmt.executeQuery(query);
                System.out.println("DEBIGG:" + l.getId());
            }

        } catch (SQLException e) {
            //e.printStackTrace();
            return;
        }
    }

    public int getIdReserva(int id_espetaculo, int id_utilizador) {
        try {
            String query = Commands.getIdReserva(id_espetaculo, id_utilizador);
            //Statement stmt = conn.createStatement();

            rs = stmt.executeQuery(query);

            return rs.getInt(1);

        } catch (SQLException e) {
            //e.printStackTrace();
            return -1;
        }
    }

    public int getIdUsername(String username) {
        try {
            String query = Commands.getIdUsername(username);
            //Statement stmt = conn.createStatement();

            rs = stmt.executeQuery(query);

            return rs.getInt(1);

        } catch (SQLException e) {
            //e.printStackTrace();
            return -1;
        }
    }
}
