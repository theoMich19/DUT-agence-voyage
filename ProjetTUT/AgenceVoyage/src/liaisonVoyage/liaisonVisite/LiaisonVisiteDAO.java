package liaisonVoyage.liaisonVisite;

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
public class LiaisonVisiteDAO {

    private static String requete;

    private static Connection connection = CreateConnection.importConnection();

    /**
     * Permet d'insérer une visite dans la base de données
     * 
     * @param liaisonvisite
     */
    public static void create(LiaisonVisite liaisonvisite) {
        requete = "INSERT INTO bd.T_liaison_voyage_visite (voyage, visite) VALUES (?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, liaisonvisite.getFkVoyage());
            preparedStatement.setInt(2, liaisonvisite.getFkVisite());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("DAO : erreur ajout liaisonVisite");
        }
    }

    /**
     * Méthode de suppression d'une visite dans la base de données
     * 
     * @param liaisonvisite visite à supprimer de la base de données
     * @return true si le voyage a pu être supprimé
     */
    public static boolean delete(LiaisonVisite liaisonvisite) {
        requete = "DELETE FROM t_liaison_voyage_visite WHERE (`idLiaison_Voyage_Visite` = ?);";
        int nbMaj = 0;
        boolean estSupprime = true;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, liaisonvisite.getIdLiaisonVisite());

            nbMaj = preparedStatement.executeUpdate();

        } catch (Exception e) {
            estSupprime = false;
        }
        // Affiche sur la concole un message d'erreur si l'opération a échoué
        if (nbMaj == 0) {
            System.err.println("BD : La visite n'a pas pu être supprimée");
            estSupprime = false;
        }
        return estSupprime;
    }

    /**
     * Méthode de suppression d'une liaison visite - voyage dans la base de données à
     * partir de l'id du voyage et de la visite
     * 
     * @param idViste  id de la visite
     * @param idVoyage id du voyage
     * @return true si la liaison a pu être supprimé
     */
    public static boolean delete(int idViste, int idVoyage) {
        requete = "DELETE FROM t_liaison_voyage_visite WHERE voyage = ? AND visite = ?;";
        int nbMaj = 0;
        boolean estSupprime = true;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, idVoyage);
            preparedStatement.setInt(2, idViste);
            nbMaj = preparedStatement.executeUpdate();

        } catch (Exception e) {
            estSupprime = false;
        }
        // Affiche sur la concole un message d'erreur si l'opération a échoué
        if (nbMaj == 0) {
            System.err.println("BD : La liaison avec la visite n'a pas pu être supprimée");
            estSupprime = false;
        }
        return estSupprime;
    }

    /**
     * Méthode qui permet de récuperer l'id d'une liaisonvisite présente dans la bd
     * 
     * @param liaisonvisite liaisonvisite présente dans la bd
     * @return Retourne l'id de l'objet ou -1 si celui-ci n'est pas présent
     */
    public static int recupId(LiaisonVisite liaisonvisite) {

        requete = "SELECT idLiaison_Voyage_Visite FROM bd.t_liaison_voyage_visite WHERE voyage = ? AND visite = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, liaisonvisite.getFkVoyage());
            preparedStatement.setInt(2, liaisonvisite.getFkVisite());

            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                return res.getInt("idLiaison_Voyage_Visite");
            }
        } catch (Exception e) {
            System.err.println("DAO : erreur, id non trouvé");
        }
        return -1;
    }

    /**
     * Méthode qui permet de lire la base de donnnées de visite
     * 
     * @return La liste de visites contenues dans la bd
     */
    public static List<LiaisonVisite> readTable() {

        List<LiaisonVisite> stockLiaisonVisite = new ArrayList<LiaisonVisite>();

        try {
            Statement statement = connection.createStatement();
            String requete = "SELECT idLiaison_Voyage_Visite, voyage, visite FROM `t_liaison_voyage_visite`";
            ResultSet res = statement.executeQuery(requete);
            while (res.next()) {
                stockLiaisonVisite.add(
                        new LiaisonVisite(res.getInt("idLiaison_Voyage_Visite"), res.getInt("voyage"),
                                res.getInt("visite")));
            }
            statement.close();

        } catch (Exception e) {
        }

        return stockLiaisonVisite;
    }
}
