package liaisonVoyage.liaisonPays;

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
public class LiaisonPaysDAO {

    private static String requete;

    private static Connection connection = CreateConnection.importConnection();

    /**
     * Permet d'insérer une liaisonpays dans la base de données
     * 
     * @param liaisonpays
     */
    public static void create(LiaisonPays liaisonpays) {
        requete = "INSERT INTO T_liaison_voyage_pays (LpVoyage, pays) VALUES (?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, liaisonpays.getFkVoyage());
            preparedStatement.setInt(2, liaisonpays.getFkPays());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("DAO : erreur ajout liaisonPays");
        }
    }

    /**
     * Méthode de suppression d'un liaisonpays dans la base de données
     * 
     * @param liaisonpays liaisonpays à supprimer de la base de données
     * @return true si supprimé
     */
    public static boolean delete(LiaisonPays liaisonpays) {
        requete = "DELETE FROM t_liaison_voyage_pays WHERE (`idLiaison_Voyage_Pays` = ?);";
        int nbMaj = 0;
        boolean estSupprime = true;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, liaisonpays.getIdLiaisonPays());

            nbMaj = preparedStatement.executeUpdate();

        } catch (Exception e) {
            estSupprime = false;
        }
        // Affiche sur la concole un message d'erreur si l'opération a échoué
        if (nbMaj == 0) {
            System.err.println("BD : Le pays n'a pas pu être supprimé");
            estSupprime = false;
        }
        return estSupprime;
    }

    /**
     * méthode de supression d'une liaison pays voyage à partir des Id
     * 
     * 
     * @param idPays   id du pays
     * @param idVoyage id du voyage
     * @return true si la liaison a été supprimer
     */
    public static boolean delete(int idPays, int idVoyage) {
        requete = "DELETE FROM t_liaison_voyage_pays WHERE LpVoyage = ? AND pays = ?;";
        int nbMaj = 0;
        boolean estSupprime = true;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, idVoyage);
            preparedStatement.setInt(2, idPays);
            nbMaj = preparedStatement.executeUpdate();

        } catch (Exception e) {
            estSupprime = false;
        }
        // Affiche sur la concole un message d'erreur si l'opération a échoué
        if (nbMaj == 0) {
            System.err.println("BD : La liaison avec le pays n'a pas pu être supprimé");
            estSupprime = false;
        }
        return estSupprime;
    }

    /**
     * On cherche à récuperer l'id d'une liaisonpays présente dans la bd
     * 
     * @param liaisonpays liaisonpays dont on cherche la présence
     * @return Retourne l'id de l'objet ou -1 si celui-ci n'est pas présent
     */
    public static int recupId(LiaisonPays liaisonpays) {

        requete = "SELECT idLiaison_Voyage_Pays FROM bd.t_liaison_voyage_pays WHERE LpVoyage = ? AND pays = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, liaisonpays.getFkVoyage());
            preparedStatement.setInt(2, liaisonpays.getFkPays());

            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                return res.getInt("idLiaison_Voyage_Pays");
            }
        } catch (Exception e) {
            System.err.println("DAO : erreur, id non trouvé");
        }
        return -1;
    }

    /**
     * Méthode qui permet de lire la base de donnnées de pays
     * 
     * @return La liste de pays contenus dans la bd
     */
    public static List<LiaisonPays> readTable() {

        List<LiaisonPays> stockLiaisonPays = new ArrayList<LiaisonPays>();

        try {

            Statement statement = connection.createStatement();
            String requete = "SELECT idLiaison_Voyage_Pays, LpVoyage, pays FROM `t_liaison_voyage_pays`";
            ResultSet res = statement.executeQuery(requete);
            while (res.next()) {
                stockLiaisonPays.add(
                        new LiaisonPays(res.getInt("idLiaison_Voyage_Pays"), res.getInt("LpVoyage"),
                                res.getInt("pays")));
            }
            statement.close();

        } catch (Exception e) {
        }

        return stockLiaisonPays;
    }
}
