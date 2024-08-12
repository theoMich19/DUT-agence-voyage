/*
 * TarifDAO.java                                                        14/11/2021
 */
package Tarif;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Agence.CreateConnection;
import Voyage.Voyage;

/**
 * DAO Tarif
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class TarifDAO {

    private static String requete;

    private static Connection connection = CreateConnection.importConnection();

    /**
     * Insère les tarifs dans la base de données
     * 
     * @param tarif objet tarif
     */
    public static int create(Tarif tarif) {

        int[] tabPrix = new int[10];
        int[] prixObjet = tarif.getPrix();

        requete = "INSERT INTO t_tarif (dateDepart, dateRetour, prixG1, prixG2, prixG3, prixG4, prixG5, prixG6, prixG7, prixG8, prixG9, prixG10, Tvoyage)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT idVoyage from T_voyages WHERE idVoyage = ?) )";
        for (int i = 0; i < tabPrix.length || i < prixObjet.length; i++) {
            tabPrix[i] = prixObjet[i];
        }

        // TODO faire requete pour récuperer id objet

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, tarif.getDateDepart());
            preparedStatement.setString(2, tarif.getDateRetour());
            preparedStatement.setInt(3, tabPrix[0]);
            preparedStatement.setInt(4, tabPrix[1]);
            preparedStatement.setInt(5, tabPrix[2]);
            preparedStatement.setInt(6, tabPrix[3]);
            preparedStatement.setInt(7, tabPrix[4]);
            preparedStatement.setInt(8, tabPrix[5]);
            preparedStatement.setInt(9, tabPrix[6]);
            preparedStatement.setInt(10, tabPrix[7]);
            preparedStatement.setInt(11, tabPrix[8]);
            preparedStatement.setInt(12, tabPrix[9]);
            preparedStatement.setInt(13, tarif.getVoyage());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * On cherche à récuperer l'id d'une ville présente dans la bd
     * 
     * @param ville Ville présente dans la bd
     * @return Retourne l'id de l'objet ou -1 si celui n'est pas présent
     */
    public static int recupId(Tarif tarif) {
        requete = "SELECT idTarif FROM bd.t_tarif WHERE dateDepart = ? AND dateRetour = ? AND Tvoyage = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, tarif.getDateDepart());
            preparedStatement.setString(2, tarif.getDateRetour());
            preparedStatement.setInt(3, tarif.getVoyage());

            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                return res.getInt("idTarif");
            }
        } catch (Exception e) {
            System.err.println("DAO : erreur, id non trouvé");
        }
        return -1;
    }

    /**
     * Méthode qui met à jour la base de données de Tarif
     * 
     * @param tarif objet Tarif
     */
    public static void update(Tarif tarif) {
        requete = "UPDATE bd.T_tarif SET dateDepart = ?, dateRetour = ?, "
                + "prixG1 = ?, prixG2 = ?, prixG3 = ?, prixG4 = ?, prixG5 = ?,"
                + " prixG6 = ?, prixG7 = ?, prixG8 = ?, prixG9 = ?, prixG10 = ? "
                + "WHERE (idTarif = ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, tarif.getDateDepart());
            preparedStatement.setString(2, tarif.getDateRetour());
            preparedStatement.setInt(3, tarif.getPrixG1());
            preparedStatement.setInt(4, tarif.getPrixG2());
            preparedStatement.setInt(5, tarif.getPrixG3());
            preparedStatement.setInt(6, tarif.getPrixG4());
            preparedStatement.setInt(7, tarif.getPrixG5());
            preparedStatement.setInt(8, tarif.getPrixG6());
            preparedStatement.setInt(9, tarif.getPrixG7());
            preparedStatement.setInt(10, tarif.getPrixG8());
            preparedStatement.setInt(11, tarif.getPrixG9());
            preparedStatement.setInt(12, tarif.getPrixG10());
            preparedStatement.setInt(13, tarif.getIdTarif());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("DAO : Erreur màj tarif");
        }
    }

    /**
     * Méthode de suppression d'un tarif dans la base de données
     * 
     * @param tarif tarif à supprimer
     * @return true si supprimé
     */
    public static boolean delete(Tarif tarif) {
        requete = "DELETE FROM `bd`.`t_tarif` WHERE (`idTarif` = ?);";
        int nbMaj = 0;
        boolean estSupprime = true;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, tarif.getIdTarif());

            nbMaj = preparedStatement.executeUpdate();
        } catch (Exception e) {
            estSupprime = false;
        }
        // Affiche sur la concole un message d'erreur si l'opération a échoué
        if (nbMaj == 0) {
            System.err.println("DAO : La période de tarif n'a pas pu être supprimée");
            estSupprime = false;
        }
        return estSupprime;
    }

    /**
     * Méthode de suppression d'un tarif pour un voyage
     * 
     * @param voyage Objet Voyage
     */
    public void deleteTarifForVoyage(Voyage voyage) {
        requete = "DELETE FROM `t_tarif` WHERE (`Tvoyage` = ?)";
        int nbMaj = 0;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, voyage.getIdVoyage());

            nbMaj = preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Affiche sur la concole un message d'erreur si l'opération a échoué
        if (nbMaj == 0) {
            System.err.println("DAO : Le tableau associé au voyage n'a pas pu être supprimé");
        }
    }

    public static List<Tarif> readTable() {
        List<Tarif> stockTarif = new ArrayList<Tarif>();

        try {
            Statement statement = connection.createStatement();
            requete = "SELECT idTarif, dateDepart, dateRetour, prixG1,"
                    + " prixG2, prixG3, prixG4, prixG5, prixG6, prixG7,"
                    + " prixG8, prixG9, prixG10, Tvoyage FROM t_tarif";

            ResultSet res = statement.executeQuery(requete);
            while (res.next()) {
                int id = res.getInt("idTarif");
                String datDep = res.getString("dateDepart");
                String datFin = res.getString("dateRetour");
                int[] prix = { res.getInt("prixG1"), res.getInt("prixG2"), res.getInt("prixG3"),
                        res.getInt("prixG4"), res.getInt("prixG5"), res.getInt("prixG6"), res.getInt("prixG7"),
                        res.getInt("prixG8"), res.getInt("prixG9"), res.getInt("prixG10") };
                int idVoyage = res.getInt("Tvoyage");
                stockTarif.add(new Tarif(id, datDep, datFin, prix, idVoyage));

            }
            statement.close();
        } catch (Exception e) {
            System.err.println("err readTable");
        }

        return stockTarif;
    }

    /**
     * Permet de décaler tout les groupes après la suppresion d'un
     * 
     * @param groupeSupr
     */
    public static void decalerPrix(List<Tarif> stockTarif) {
        for (Tarif aUpdate : stockTarif) {
            update(aUpdate);
        }
    }
}