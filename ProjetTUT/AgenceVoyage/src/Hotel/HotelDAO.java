/*
 * HotelDAO.java                                     9 jan. 2022
 */
package Hotel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Agence.CreateConnection;

/**
 * DAO de pays
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class HotelDAO {

    private static String requete;

    private static Connection connection = CreateConnection.importConnection();

    /**
     * Permet d'insérer un hotel dans la base de données
     * 
     * @param hotel objet hôtel
     */
    public static void create(Hotel hotel) {
        requete = ("INSERT INTO t_hotel (nomHotel, adresse, ville, numTelephone) VALUES (?, ?, ?, ?)");

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, hotel.getNomHotel());
            preparedStatement.setString(2, hotel.getAdresse());
            preparedStatement.setString(3, hotel.getVille());
            preparedStatement.setString(4, hotel.getNumTel());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("DAO : erreur ajout hôtel");
        }
    }

    /**
     * On cherche à récuperer l'id d'un hotel présent dans la bd
     * 
     * @param hotel Hotel présente dans la bd
     * @return Retourne l'id de l'objet ou -1 si celui n'est pas présent
     */
    public static int recupId(Hotel hotel) {
        requete = "SELECT idHotel FROM bd.t_hotel WHERE nomHotel = ? AND adresse = ? AND ville = ? AND numTelephone = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, hotel.getNomHotel());
            preparedStatement.setString(2, hotel.getAdresse());
            preparedStatement.setString(3, hotel.getVille());
            preparedStatement.setString(4, hotel.getNumTel());

            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                return res.getInt("idHotel");
            }
        } catch (Exception e) {
            System.err.println("DAO : erreur, id non trouvé");
        }
        return -1;
    }

    /**
     * Méthode qui permet de mettre à jour la base de données de la table des hotel
     * 
     * @param hotel objet hôtel
     */
    public static void update(Hotel hotel) {
        requete = "UPDATE bd.T_hotel SET nomHotel = ?, adresse = ?, ville = ?, numTel = ? WHERE idHotel = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, hotel.getNomHotel());
            preparedStatement.setString(2, hotel.getAdresse());
            preparedStatement.setString(3, hotel.getVille());
            preparedStatement.setString(4, hotel.getNumTel());

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            System.err.println("Erreur màj hôtel");
        }
    }

    /**
     * Suppression d'un hotel à partir de son nom
     * 
     * @param hotel Hotel à supprimer
     * @return false si l'hotel n'a pas pu être supprimé
     */
    public static boolean delete(Hotel hotel) {
        requete = "DELETE FROM `t_hotel` WHERE (`idHotel` = ?);";
        boolean estSupprime = true;
        int nbMaj = 0;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, hotel.getIdHotel());

            nbMaj = preparedStatement.executeUpdate();
        } catch (Exception e) {
            estSupprime = false;
        }
        // Affiche sur la console un message d'erreur si l'opération a échoué
        if (nbMaj == 0) {
            estSupprime = false;
            System.err.println("DAO : L'hôtel n'a pas pu être supprimé");
        }
        return estSupprime;
    }

    /**
     * Méthode qui permet de lire la table de hotel
     * 
     * @return liste de Hotel
     */
    public static List<Hotel> readTable() {

        List<Hotel> stockHotel = new ArrayList<Hotel>();

        try {
            Statement statement = connection.createStatement();
            requete = "SELECT idHotel, nomHotel, adresse, ville, numTelephone FROM `t_hotel`";
            ResultSet res = statement.executeQuery(requete);
            while (res.next()) {
                stockHotel.add(new Hotel(res.getInt("idHotel"), res.getString("nomHotel"),
                        res.getString("adresse"), res.getString("ville"), res.getString("numTelephone")));
            }
            statement.close();
        } catch (Exception e) {
        }

        return stockHotel;
    }
}
