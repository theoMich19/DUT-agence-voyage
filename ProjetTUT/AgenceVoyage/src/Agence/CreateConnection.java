/*
 * CreateConnection.java                                               25 nov. 2021
 */
package Agence;

import java.sql.Connection;

/**
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class CreateConnection {

    /**
     * Permet d'appeler la connexion à la bd
     * Gère les erreurs possibles
     */
    public static Connection importConnection() {
        Connection databaseConnection = null;
        try {
            databaseConnection = DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {

        }
        return databaseConnection;
    }
}