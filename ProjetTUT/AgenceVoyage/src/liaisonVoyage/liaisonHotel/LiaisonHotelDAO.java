package liaisonVoyage.liaisonHotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Agence.CreateConnection;

/**
 * Dao de LiaisonHotel
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class LiaisonHotelDAO {

    private static String requete;

    private static Connection connection = CreateConnection.importConnection();

    /**
     * Permet d'insérer une liaison hôtel dans la base de données
     * 
     * @param liaisonhotel objet liaisonhotel
     */
    public static void create(LiaisonHotel liaisonhotel) {
        requete = "INSERT INTO bd.T_liaison_voyage_hotel (voyage, hotel, debut_reservation, fin_reservation) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, liaisonhotel.getFkVoyage());
            preparedStatement.setInt(2, liaisonhotel.getFkHotel());
            preparedStatement.setInt(3, -1);
            preparedStatement.setInt(4, -1);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("DAO : erreur ajout liaisonhotel");
        }
    }

    /**
     * Méthode de suppression d'un hôtel dans la base de données
     * 
     * @param liaisonhotel à supprimer de la base de données
     * @return true si le voyage a pu être supprimé
     */
    public static boolean delete(LiaisonHotel liaisonhotel) {
        requete = "DELETE FROM t_liaison_voyage_hotel WHERE (`idLiaison_Voyage_Hotel` = ?);";
        int nbMaj = 0;
        boolean estSupprime = true;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, liaisonhotel.getIdLiaisonHotel());

            nbMaj = preparedStatement.executeUpdate();

        } catch (Exception e) {
            estSupprime = false;
        }
        // Affiche sur la concole un message d'erreur si l'opération a échoué
        if (nbMaj == 0) {
            System.err.println("BD : L'hôtel n'a pas pu être supprimé");
            estSupprime = false;
        }
        return estSupprime;
    }

    /**
     * Méthode de suppression d'une liaison hotel - voyage dans la base de données à
     * partir de l'id du voyage et de l'hotel
     * 
     * @param idHotel  id de l'hotel
     * @param idVoyage id du voyage
     * @return true si la liaison a pu être supprimé
     */
    public static boolean delete(int idHotel, int idVoyage) {
        requete = "DELETE FROM t_liaison_voyage_hotel WHERE voyage = ? AND hotel = ?;";
        int nbMaj = 0;
        boolean estSupprime = true;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, idVoyage);
            preparedStatement.setInt(2, idHotel);
            nbMaj = preparedStatement.executeUpdate();

        } catch (Exception e) {
            estSupprime = false;
        }
        // Affiche sur la concole un message d'erreur si l'opération a échoué
        if (nbMaj == 0) {
            System.err.println("BD : La liaison avec l'hotel n'a pas pu être supprimé");
            estSupprime = false;
        }
        return estSupprime;
    }

    /**
     * Méthode de mise à jour du jour de debut de reservation de l'hotel dans la
     * base de données
     * 
     * @param jour     jour de début où le client va dormir à l'hotel
     * @param idVoyage id du voyage
     * @param idHotel  id de l'hotel
     * @return
     */
    public static boolean updatejourDebut(int jourDebut, int idVoyage, int idHotel) {
        requete = "UPDATE bd.T_liaison_voyage_hotel SET debut_reservation = ? WHERE voyage = ? AND hotel = ?;";
        int nbMaj = 0;
        boolean estSupprime = true;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, jourDebut);
            preparedStatement.setInt(2, idVoyage);
            preparedStatement.setInt(3, idHotel);
            nbMaj = preparedStatement.executeUpdate();

        } catch (Exception e) {
            estSupprime = false;
        }
        // Affiche sur la concole un message d'erreur si l'opération a échoué
        if (nbMaj == 0) {
            System.err.println("BD : la modification na pas pu être effectuée");
            estSupprime = false;
        }
        return estSupprime;
    }

    /**
     * Méthode de mise à jour du jour de debut de reservation de l'hotel dans la
     * base de données
     * 
     * @param jour     dernier jour où le client va dormir à l'hotel
     * @param idVoyage id du voyage
     * @param idHotel  id de l'hotel
     * @return
     */
    public static boolean updateJourFin(int jourFin, int idVoyage, int idHotel) {
        requete = "UPDATE bd.T_liaison_voyage_hotel SET fin_reservation = ? WHERE voyage = ? AND hotel = ?;";
        int nbMaj = 0;
        boolean estSupprime = true;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, jourFin);
            preparedStatement.setInt(2, idVoyage);
            preparedStatement.setInt(3, idHotel);
            nbMaj = preparedStatement.executeUpdate();

        } catch (Exception e) {
            estSupprime = false;
        }
        // Affiche sur la concole un message d'erreur si l'opération a échoué
        if (nbMaj == 0) {
            System.err.println("BD : la modification na pas pu être effectuée");
            estSupprime = false;
        }
        return estSupprime;
    }

    /**
     * Méthode qui récuperer l'id d'un hôtel présent dans la bd
     * 
     * @param liaisonhotel liaisonhotel présent dans la bd
     * @return Retourne l'id de l'objet ou -1 si celui-ci n'est pas présent
     */
    public static int recupId(LiaisonHotel liaisonhotel) {

        requete = "SELECT idLiaison_Voyage_Hotel FROM bd.t_liaison_voyage_hotel WHERE voyage = ? AND hotel = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, liaisonhotel.getFkVoyage());
            preparedStatement.setInt(2, liaisonhotel.getFkHotel());

            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                return res.getInt("idLiaison_Voyage_Hotel");
            }
        } catch (Exception e) {
            System.err.println("DAO : erreur, id non trouvé");
        }
        return -1;
    }

    /**
     * Méthode qui permet de récupérer le jour de début de la réservation dans
     * l'hotel par rapport à un voyage
     * 
     * @param idVoyage id du voyaeg
     * @param idHotel  id de l'hôtel
     * @return le jour de début ou -1 si non trouvé
     */
    public static int recupJourDebut(int idVoyage, int idHotel) {

        requete = "SELECT debut_reservation FROM bd.t_liaison_voyage_hotel WHERE voyage = ? AND hotel = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, idVoyage);
            preparedStatement.setInt(2, idHotel);

            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                return res.getInt("debut_reservation");
            }
        } catch (Exception e) {
            System.err.println("DAO : erreur, id non trouvé");
        }
        return -1;
    }

    /**
     * Méthode qui permet de récupérer le jour de fin de la réservation dans
     * l'hotel par rapport à un voyage
     * 
     * @param idVoyage id du voyaeg
     * @param idHotel  id de l'hôtel
     * @return le jour de fin ou -1 si non trouvé
     */
    public static int recupJourFin(int idVoyage, int idHotel) {

        requete = "SELECT fin_reservation FROM bd.t_liaison_voyage_hotel WHERE voyage = ? AND hotel = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, idVoyage);
            preparedStatement.setInt(2, idHotel);

            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                return res.getInt("fin_reservation");
            }
        } catch (Exception e) {
            System.err.println("DAO : erreur, id non trouvé");
        }
        return -1;
    }

    /**
     * Méthode qui permet de lire la base de donnnées de hôtel
     * 
     * @return La liste des liaisons hôtel contenus dans la bd
     */
    public static List<LiaisonHotel> readTable() {

        List<LiaisonHotel> stockliaisonhotel = new ArrayList<LiaisonHotel>();

        try {

            Statement statement = connection.createStatement();
            String requete = "SELECT idLiaison_Voyage_Hotel, voyage, hotel FROM `t_liaison_voyage_hotel`";
            ResultSet res = statement.executeQuery(requete);
            while (res.next()) {
                stockliaisonhotel.add(
                        new LiaisonHotel(res.getInt("idLiaison_Voyage_Hotel"), res.getInt("voyage"),
                                res.getInt("hotel")));
            }
            statement.close();

        } catch (Exception e) {
        }
        return stockliaisonhotel;
    }
}
