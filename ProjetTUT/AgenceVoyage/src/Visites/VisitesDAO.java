/*
 * VisitesDAO    17/01/2022
 */

package Visites;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Agence.CreateConnection;

/**
 * DAO de visites
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class VisitesDAO {

    private static String requete;

    private static Connection connection = CreateConnection.importConnection();

    /**
     * Méthode qui permet d'insere une ville dans la base de données
     * 
     * @param visites visites à inserer dans la base de donnée
     */
    public static void create(Visite visites) {

        requete = "INSERT INTO T_visite (titre, description) VALUES (?,?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, visites.getTitre());
            preparedStatement.setString(2, visites.getDescription());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("DAO : erreur ajout visites");
        }
    }

    /**
     * On cherche à récuperer l'id d'une visites présente dans la bd
     * 
     * @param visites visites présente dans la bd
     * @return Retourne l'id de l'objet ou -1 si celui n'est pas présent
     */
    public static int recupId(Visite visites) {

        requete = "SELECT idVisite FROM bd.t_visite WHERE titre = ? AND description = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, visites.getTitre());
            preparedStatement.setString(2, visites.getDescription());

            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                return res.getInt("idVisite");
            }
        } catch (Exception e) {
            System.err.println("DAO : erreur, id non trouvé");
        }
        return -1;
    }

    /**
     * On cherche à récuperer l'id d'une visites présente dans la bd
     * 
     * @param visites visites présente dans la bd
     * @return Retourne l'id de l'objet ou -1 si celui n'est pas présent
     */
    public static int recupId(String NomVisite) {

        requete = "SELECT idVisite FROM bd.t_visite WHERE titre = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, NomVisite);

            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                return res.getInt("idVisite");
            }
        } catch (Exception e) {
            System.err.println("DAO : erreur, id non trouvé");
        }
        return -1;
    }

    /**
     * Met à jour une visite à partir de son id
     * 
     * @param visites visite a mettre à jour dans la base de données
     */
    public static void update(Visite visites) {
        requete = "UPDATE bd.T_visite SET titre = ?, description = ? WHERE idVisite = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, visites.getTitre());
            preparedStatement.setString(2, visites.getDescription());
            preparedStatement.setInt(3, visites.getIdVisites());

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            System.err.println("DAO : Erreur màj visite");
        }
    }

    /**
     * Méthode de suppressions d'une visite dans la base de données
     * 
     * @param visites visite à supprimer de la base de données
     * @return
     */
    public static boolean delete(Visite visites) {
        int nbMaj = 0;
        boolean estSupprime = true;

        requete = "DELETE FROM `t_visite` WHERE (`idVisite` = ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, visites.getIdVisites());

            nbMaj = preparedStatement.executeUpdate();

        } catch (Exception e) {
            estSupprime = false;
        }
        // Affiche sur la concole un message d'erreur si l'opération a échoué
        if (nbMaj == 0) {
            System.err.println("DAO : La visite n'a pas pu être supprimée");
            estSupprime = false;
        }
        return estSupprime;
    }

    /**
     * Méthode qui permet de lire la table de visite
     * 
     * @return liste de visite
     */
    public static List<Visite> readTable() {
        List<Visite> stockVille = new ArrayList<Visite>();
        requete = "SELECT idVisite, titre, description FROM `t_visite`";

        try {
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery(requete);
            while (res.next()) {
                stockVille
                        .add(new Visite(res.getInt("idVisite"), res.getString("titre"), res.getString("description")));
            }
            statement.close();

        } catch (Exception e) {
        }

        return stockVille;
    }
}
