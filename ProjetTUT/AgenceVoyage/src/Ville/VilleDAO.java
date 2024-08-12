/*
 * VilleDAO.java                                     14 nov. 2021
 */
package Ville;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Agence.CreateConnection;

/**
 * DAO de ville
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class VilleDAO {

    private static String requete;

    private static Connection connection = CreateConnection.importConnection();

    /**
     * Méthode qui permet d'insere une ville dans la base de données
     * 
     * @param ville ville à inserer dans la base de donnée
     */
    public static void create(Ville ville) {
        // Si le groupe est incorrect on l'affecte au premier par défaut.
        if (ville.getGroupe() < 1 || ville.getGroupe() > 10) {
            ville.setGroupe(1, 10);
        }
        requete = "INSERT INTO T_ville (nom, groupe) VALUES (?,?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, ville.getNomVille());
            preparedStatement.setInt(2, ville.getGroupe());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("DAO : erreur ajout ville");
        }
    }

    /**
     * On cherche à récuperer l'id d'une ville présente dans la bd
     * 
     * @param ville Ville présente dans la bd
     * @return Retourne l'id de l'objet ou -1 si celui n'est pas présent
     */
    public static int recupId(Ville ville) {
        requete = "SELECT idVille FROM bd.t_ville WHERE nom = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, ville.getNomVille());

            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                return res.getInt("idVille");
            }
        } catch (Exception e) {
            System.err.println("DAO : erreur, id non trouvé");
        }
        return -1;
    }

    /**
     * Met à jour une ville à partir de son id
     * 
     * @param ville ville a mettre à jour dans la base de données
     */
    public static void update(Ville ville) {
        requete = "UPDATE bd.T_ville SET nom = ?, groupe = ? WHERE idVille = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, ville.getNomVille());
            preparedStatement.setInt(2, ville.getGroupe());
            preparedStatement.setInt(3, ville.getId());

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            System.err.println("DAO : Erreur màj ville");
        }
    }

    /**
     * Méthode de suppressions d'une ville dans la base de données
     * 
     * @param ville ville à supprimer de la base de données
     * @return true si la suppression à fonctionner
     */
    public static boolean delete(Ville ville) {
        int nbMaj = 0;
        boolean estSupprime = true;
        requete = "DELETE FROM `t_ville` WHERE (`idVille` = ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, ville.getId());

            nbMaj = preparedStatement.executeUpdate();

        } catch (Exception e) {
            estSupprime = false;
        }
        // Affiche sur la concole un message d'erreur si l'opération a échoué
        if (nbMaj == 0) {
            System.err.println("DAO : La ville n'a pas pu être supprimée");
            estSupprime = false;
        }
        return estSupprime;
    }

    /**
     * Méthode qui permet de lire la table de ville
     * 
     * @return liste de ville
     */
    public static List<Ville> readTable() {
        List<Ville> stockVille = new ArrayList<Ville>();
        requete = "SELECT idVille, nom, groupe FROM `t_ville`";

        try {
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery(requete);
            while (res.next()) {
                stockVille.add(new Ville(res.getInt("idVille"), res.getString("nom"), res.getInt("groupe")));
            }
            statement.close();

        } catch (Exception e) {
        }

        return stockVille;
    }

    /**
     * Méthode qui permet de récuperer le nombre de groupe
     * 
     * @return nombre groupe max
     */
    public static int nbGroupeMax() {
        int resultat = -1;

        try {
            Statement statement = connection.createStatement();
            String requete = "SELECT Max(groupe) AS test FROM bd.t_ville ";
            ResultSet res = statement.executeQuery(requete);
            while (res.next()) {
                resultat = res.getInt("test");
            }
            statement.close();

        } catch (Exception e) {

        }
        return resultat;
    }

    /**
     * Permet de décaler tout les groupes après la suppresion d'un
     * 
     * @param groupeSupr
     */
    public static void decalerGroupe(List<Ville> stockVille) {
        for (Ville aUpdate : stockVille) {
            update(aUpdate);
        }
    }
}