package com.generation.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private boolean connected;
    private String username;
    private String password;
    private String url;
    private String nomeSchema;
    private static Database instance;

    
    private Connection connection;

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private Database() {
        this.connected = false;
        this.username = "root";
        this.password = "root";
        this.nomeSchema = "losco";
        this.url = "jdbc:mysql://localhost:3306/" + nomeSchema + "?useSSL=false&serverTimezone=UTC";
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void openConnection() {

        try {
            if (connection == null || connection.isClosed()) {
                
                Class.forName(DRIVER);

                
                connection = DriverManager.getConnection(url, username, password);
                connected = true;
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Errore nel Driver MySQL: controlla il percorso" +
                    " alla classe e di aver importato il JAR con la libreria e driver");
        } catch (SQLException e) {
            System.out.println("Errore nell'apertura della connessione nella classe Database " +
                    " controlla che username, password, nome delle schema e url siano corretti");
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connected = false;
            }
        } catch (Exception e) {
            System.out.println("Errore nella chiusura della connessione " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public Connection getConnection() {
        return connection;
    }

}
