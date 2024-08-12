/*
 * PaysDAO.java                                     14 nov. 2021
 */
package Pays;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Agence.CreateConnection;

/**
 * DAO de pays
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class PaysDAO {

    private static String requete;

    private static Connection connection = CreateConnection.importConnection();

    /**
     * Permet d'insérer un pays dans la base de données
     * 
     * @param pays objet pays
     */
    public static void create(Pays pays) {
        requete = ("INSERT INTO T_pays (nom, formalites, conseil) VALUES (?, ?, ?)");

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, pays.getNomPays());
            preparedStatement.setString(2, pays.getFormalites());
            preparedStatement.setString(3, pays.getConseil());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("DAO : erreur ajout pays");
        }
    }

    /**
     * Méthode qui permet de mettre à jour la base de données de la table des pays
     * 
     * @param pays objet pays
     */
    public static void update(Pays pays) {
        requete = "UPDATE bd.T_pays SET nom = ?, formalites = ?, conseil = ? WHERE idPays = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, pays.getNomPays());
            preparedStatement.setString(2, pays.getFormalites());
            preparedStatement.setString(3, pays.getConseil());
            preparedStatement.setInt(4, pays.getIdPays());

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            System.err.println("Erreur màj pays");
        }
    }

    /**
     * Méthode de suppression d'un pays dans la base de données
     * 
     * @param pays pays à supprimer de la base de données
     * @return true si le voyage a pu être supprimé
     */
    public static boolean delete(Pays pays) {
        requete = "DELETE FROM `bd`.`t_pays` WHERE (`idPays` = ?);";
        int nbMaj = 0;
        boolean estSupprime = true;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, pays.getIdPays());

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
     * Méthode de suppression d'un pays dans la base de données à partir de son id
     * 
     * @param idPays id du pays que l'on souhaite supprimé
     * @return true si le voyage a pu être supprimé
     */
    public static boolean delete(int idPays) {
        requete = "DELETE FROM `bd`.`t_pays` WHERE (`idPays` = ?);";
        int nbMaj = 0;
        boolean estSupprime = true;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, idPays);

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
     * On cherche à récuperer l'id d'un pays présente dans la bd
     * 
     * @param pays Pays présent dans la bd
     * @return Retourne l'id de l'objet ou -1 si celui n'est pas présent
     */
    public static int recupId(Pays pays) {

        requete = "SELECT idPays FROM bd.t_pays WHERE nom = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, pays.getNomPays());

            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                return res.getInt("idPays");
            }
        } catch (Exception e) {
            System.err.println("DAO : erreur, id non trouvé");
        }
        return -1;
    }

    /**
     * méthode qui permet de récuperer l'id d'un pays présente dans la bd
     * 
     * @param pays nom du pays
     * @return Retourne l'id de l'objet ou -1 si celui n'est pas présent
     */
    public static int recupId(String pays) {

        requete = "SELECT idPays FROM bd.t_pays WHERE nom = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, pays);

            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                return res.getInt("idPays");
            }
        } catch (Exception e) {
            System.err.println("DAO : erreur, id non trouvé");
        }
        return -1;
    }

    /**
     * Vérifie que le pays n'est pas présent dans un voyage ou plus
     * 
     * @param pays objet pays
     * @return true si le pays est dans un voyage ou plus, faux dans le cas
     *         contraire
     */
    public boolean estDansVoyage(Pays pays) {

        boolean retour;

        retour = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Statement statement = connection.createStatement();
            String requete = "SELECT idVoyage FROM t_voyages WHERE pays = " + pays.getIdPays() + ";";
            ResultSet test = statement.executeQuery(requete);
            if (test.next()) {
                retour = true;
            }
            test.close();
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return retour;
    }

    /**
     * Méthode qui permet de lire la base de donnner de pays
     * 
     * @return La liste de pays contenue dans la bd
     */
    public static List<Pays> readTable() {

        List<Pays> stockPays = new ArrayList<Pays>();

        try {

            Statement statement = connection.createStatement();
            String requete = "SELECT idPays, nom, formalites, conseil FROM `t_pays`";
            ResultSet res = statement.executeQuery(requete);
            while (res.next()) {
                stockPays.add(new Pays(res.getInt("idPays"), res.getString("nom"), res.getString("formalites"),
                        res.getString("conseil")));
            }
            statement.close();

        } catch (Exception e) {
        }

        return stockPays;
    }
}
