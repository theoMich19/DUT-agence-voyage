/*
 * VoyageDAO.java                                                    11/11/2021
 */
package Voyage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Agence.CreateConnection;

/**
 * DAO de Voyage
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class VoyageDAO {

    private static String requete;

    private static Connection connection = CreateConnection.importConnection();

    /**
     * Crée un nouveau voyage dans la base de données à partir d'un objet voyage
     * 
     * @param voyage le voyage à ajouter
     */
    public static int create(Voyage voyage) {
        requete = "INSERT INTO T_voyages (designation, transportAller, transportRetour, villeDestination, villeRetour, nbJour)"
                + " VALUES (?, ?, ?, ?, ?, ?)";
        int nbMaj = 0;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, voyage.getDesignation());
            preparedStatement.setString(2, voyage.getTransportAller());
            preparedStatement.setString(3, voyage.getTransportRetour());
            preparedStatement.setString(4, voyage.getVilleDestination());
            preparedStatement.setString(5, voyage.getVilleRetour());
            preparedStatement.setInt(6, voyage.getNbJour());

            nbMaj = preparedStatement.executeUpdate();
        } catch (Exception e) {
        }

        return nbMaj;
    }

    /**
     * On cherche à récuperer l'id d'un voyage présent dans la bd
     * 
     * @param voyage Voyage présente dans la bd
     * @return Retourne l'id de l'objet ou -1 si celui n'est pas présent
     */
    public static int recupId(Voyage voyage) {

        requete = "SELECT idVoyage FROM bd.t_voyages WHERE designation = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, voyage.getDesignation());

            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                return res.getInt("idVoyage");
            }
        } catch (Exception e) {
            System.err.println("DAO : erreur, id non trouvé");
        }
        return -1;
    }

    /**
     * Met à jour une voyage à partir de son id
     * 
     * @param voyage voyage a mettre à jour dans la base de données
     */
    public static void update(Voyage voyage) {
        requete = "UPDATE bd.T_voyages SET designation = ?, transportAller = ?, transportRetour = ?,"
                + " villeDestination = ?, villeRetour = ?, nbJour = ? WHERE idVoyage = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, voyage.getDesignation());
            preparedStatement.setString(2, voyage.getTransportAller());
            preparedStatement.setString(3, voyage.getTransportRetour());
            preparedStatement.setString(4, voyage.getVilleDestination());
            preparedStatement.setString(5, voyage.getVilleRetour());
            preparedStatement.setInt(6, voyage.getNbJour());
            preparedStatement.setInt(7, voyage.getIdVoyage());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("DAO : Erreur màj voyages");
        }
    }

    /**
     * Suppression d'un voyage à partir de son nom
     * 
     * @param voyage voyage à supprimer
     * @return false si le voyage n'a pas pu être supprimé
     */
    public static boolean delete(Voyage voyage) {
        requete = "DELETE FROM `t_voyages` WHERE (`idVoyage` = ?);";
        boolean estSupprime = true;
        int nbMaj = 0;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, voyage.getIdVoyage());

            nbMaj = preparedStatement.executeUpdate();
        } catch (Exception e) {
            estSupprime = false;
        }
        // Affiche sur la console un message d'erreur si l'opération a échoué
        if (nbMaj == 0) {
            estSupprime = false;
            System.err.println("DAO : Le voyage n'a pas pu être supprimé");
        }
        return estSupprime;
    }

    /**
     * Suppression d'un voyage à partir de son id
     * 
     * @param idVoyage id du voyage à supprimer
     * @return false si le voyage n'a pas pu être supprimé
     */
    public static boolean delete(int idVoyage) {
        requete = "DELETE FROM `t_voyages` WHERE (`idVoyage` = ?);";
        boolean estSupprime = true;
        int nbMaj = 0;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, idVoyage);

            nbMaj = preparedStatement.executeUpdate();
        } catch (Exception e) {
            estSupprime = false;
        }
        // Affiche sur la console un message d'erreur si l'opération a échoué
        if (nbMaj == 0) {
            estSupprime = false;
            System.err.println("DAO : Le voyage n'a pas pu être supprimé");
        }
        return estSupprime;
    }

    /**
     * Méthode qui permet de lire la table de voyage
     * 
     * @return liste de voyage
     */
    public static List<Voyage> readTable() {

        List<Voyage> stockVoyage = new ArrayList<Voyage>();

        try {
            Statement statement = connection.createStatement();
            requete = "SELECT idVoyage, designation, transportAller,"
                    + " transportRetour, villeDestination,"
                    + " villeRetour, nbJour FROM `t_voyages`";
            ResultSet res = statement.executeQuery(requete);
            while (res.next()) {
                stockVoyage.add(new Voyage(res.getInt("idVoyage"),
                        res.getString("designation"),
                        res.getString("transportAller"),
                        res.getString("transportRetour"),
                        res.getString("villeDestination"),
                        res.getString("villeRetour"),
                        res.getInt("nbJour")));
            }
            statement.close();
        } catch (Exception e) {
        }

        return stockVoyage;
    }
}
