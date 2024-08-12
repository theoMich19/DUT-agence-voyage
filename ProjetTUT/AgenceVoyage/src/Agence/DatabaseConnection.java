/*
 * DatebaseConnection.java
 */
package Agence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connexion Base de données
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class DatabaseConnection {

    /** Instance de DatabaseConnection */
    private static DatabaseConnection instance;

    /** Connexion à la bd */
    private Connection connection;

    /** Information nécessaire pour se connecter à la bd */
    private String url = "jdbc:mysql://localhost:3306/bd";
    private String username = "root";
    private String password = "root";

    /**
     * Permet d'initialiser l'objet.
     * Crée une connexion à la base de données
     * 
     * @throws SQLException
     */
    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
        }
    }

    /**
     * Permet de renvoyer la connexion à la bd
     * Nécessaire pour envoyer des requêtes à la bd
     * 
     * @return connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Permet d'appeler notre instance
     * Si elle n'est pas créée, alors on fait appel
     * à l'initialisation de notre objet.
     * 
     * @return Instance
     * @throws SQLException
     */
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
}