/*
 * Tarif.java                                                       07/11/2021
 */
package Tarif;

import java.util.List;

/**
 * Objet Tarif reprennant le format de la bd pour permettre de passer la base de
 * donnée en objet Le String des dates doit être au format aaaa-mm-jj
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class Tarif {

    private int idTarif;
    private String dateDepart;
    private String dateRetour;
    private int prixG1;
    private int prixG2;
    private int prixG3;
    private int prixG4;
    private int prixG5;
    private int prixG6;
    private int prixG7;
    private int prixG8;
    private int prixG9;
    private int prixG10;
    private int voyage;

    /**
     * Constructeur utilisé lors de l'écriture de la bd en objet Les objets ont déja
     * été vérifié et donc aucune vérification n'est ajouté
     * 
     * @param idTarif    id du tarif
     * @param dateDepart date de départ du voyage
     * @param dateRetour date de retour du voyage
     * @param tabTarif   tableau des tarifs(tarifs des 10 groupes)
     * @param voyage
     */
    public Tarif(int idTarif, String dateDepart, String dateRetour, int[] tabTarif, int voyage) {
        this.idTarif = idTarif;
        this.dateDepart = dateDepart;
        this.dateRetour = dateRetour;
        this.voyage = voyage;
        this.prixG1 = tabTarif[0];
        this.prixG2 = tabTarif[1];
        this.prixG3 = tabTarif[2];
        this.prixG4 = tabTarif[3];
        this.prixG5 = tabTarif[4];
        this.prixG6 = tabTarif[5];
        this.prixG7 = tabTarif[6];
        this.prixG8 = tabTarif[7];
        this.prixG9 = tabTarif[8];
        this.prixG10 = tabTarif[9];
    }

    /**
     * Constructeur utilisé lors de l'ajou a travers l'application d'un tarif Le
     * voyage n'est pas vérifié car l'ajout d'un tarif se fait après avoir choisi un
     * voyage Il est vérifié que les dates sont dans le bon format Il est vérifié
     * que le tarif est correct L'id est saisi à -1 pour dire qu'elle n'est pas
     * saisi
     * 
     * @param dateDepart  date de départ du voyage
     * @param dateRetour  date de retour du voyage
     * @param tabTarif    tableau des tarifs(tarifs des 10 groupes)
     * @param voyage      Contient l'id du voyage
     * @param stockTarif  Contient tout les tarifs déja créer
     * @param nbGroupeMax Contient le total de groupe créer
     */
    public Tarif(String dateDepart, String dateRetour, int[] tabTarif, int voyage,
            List<Tarif> stockTarif, int nbGroupeMax) {
        this.idTarif = -1;
        this.dateDepart = dateDepart;
        this.dateRetour = dateRetour;
        this.voyage = voyage;
        this.prixG1 = tabTarif[0];
        this.prixG2 = tabTarif[1];
        this.prixG3 = tabTarif[2];
        this.prixG4 = tabTarif[3];
        this.prixG5 = tabTarif[4];
        this.prixG6 = tabTarif[5];
        this.prixG7 = tabTarif[6];
        this.prixG8 = tabTarif[7];
        this.prixG9 = tabTarif[8];
        this.prixG10 = tabTarif[9];
    }

    /**
     * Vérifie que le tarif n'est pas négatif ainsi qu'il ne soit pas trop grand.
     * 
     * @param prix tableau de prix
     * @return estCorrect
     */
    public static boolean estTarifCorrect(int[] prix, int nbGroupeMax) {
        boolean estCorrect = true;
        for (int i = 0; i < nbGroupeMax; i++) {
            if (prix[i] > 100000 || prix[i] < 0) {
                estCorrect = false;
            }
        }

        return estCorrect;
    }

    @Override
    public String toString() {
        StringBuilder afficher = new StringBuilder();

        afficher.append(idTarif).append(" | ").append(dateDepart).append(" | ")
                .append(dateRetour).append(" | ").append(prixG1).append(" | ")
                .append(prixG2).append(" | ").append(prixG3).append(" | ")
                .append(prixG4).append(" | ").append(prixG5).append(" | ")
                .append(prixG6).append(" | ").append(prixG7).append(" | ")
                .append(prixG8).append(" | ").append(prixG9).append(" | ")
                .append(prixG10).append(" | ").append(voyage);
        return afficher.toString();
    }

    /**
     * Permet de modifier l'objet Tarif
     * 
     * @param dateDepart date de départ
     * @param dateRetour date de retour
     * @param prix       tableau de prix
     */
    public void modifierTarif(String dateDepart, String dateRetour, int[] prix) {
        this.dateDepart = dateDepart;
        this.dateRetour = dateRetour;
        this.prixG1 = prix[0];
        this.prixG2 = prix[1];
        this.prixG3 = prix[2];
        this.prixG4 = prix[3];
        this.prixG5 = prix[4];
        this.prixG6 = prix[5];
        this.prixG7 = prix[6];
        this.prixG8 = prix[7];
        this.prixG9 = prix[8];
        this.prixG10 = prix[9];
    }

    /**
     * Méthode qui permet de décaler les groupes
     * 
     * @param nbGroupeSupr
     */
    public void decalerGroupe(int nbGroupeSupr) {
        switch (nbGroupeSupr) {
            case 1:
                prixG1 = prixG2;
            case 2:
                prixG2 = prixG3;
            case 3:
                prixG3 = prixG4;
            case 4:
                prixG4 = prixG5;
            case 5:
                prixG5 = prixG6;
            case 6:
                prixG6 = prixG7;
            case 7:
                prixG7 = prixG8;
            case 8:
                prixG8 = prixG9;
            case 9:
                prixG9 = prixG10;
            case 10:
                prixG10 = -1;
        }
    }

    /**
     * Vérifie que la chaine de caractères est bien un int sous le format de 8
     * nombres ou moins avant la virgule et 2 ou moins après la virgule, vérifie
     * également que le tarif n'est pas négatif et qu'il ne soit pas supérieur à
     * 100 000,00
     * 
     * @param str Chaine de caractères que l'on souhaite vérifier
     * @return true si la chaine est sous le bon format, false dans le cas contraire
     */
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Méthode de vérification qui indique si la taille est conforme ou non
     * 
     * @param str String à vérifier
     */
    public static boolean taille(String str) {
        boolean correcte = false;
        int nombre = Integer.parseInt(str);

        if (1 < nombre && nombre < 100000) {
            correcte = true;
        }
        return correcte;
    }

    /**
     * @return int return the idTarif
     */
    public int getIdTarif() {
        return idTarif;
    }

    /**
     * @param idTarif the idTarif to set
     */
    public void setIdTarif(int idTarif) {
        this.idTarif = idTarif;
    }

    /**
     * @return String return the dateDepart
     */
    public String getDateDepart() {
        return dateDepart;
    }

    /**
     * @param dateDepart the dateDepart to set
     */
    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    /**
     * @return String return the dateRetour
     */
    public String getDateRetour() {
        return dateRetour;
    }

    /**
     * @param dateRetour the dateRetour to set
     */
    public void setDateRetour(String dateRetour) {
        this.dateRetour = dateRetour;
    }

    /**
     * @return int return the prixG1
     */
    public int getPrixG1() {
        return prixG1;
    }

    /**
     * @param prixG1 the prixG1 to set
     */
    public void setPrixG1(int prixG1) {
        this.prixG1 = prixG1;
    }

    /**
     * @return int return the prixG2
     */
    public int getPrixG2() {
        return prixG2;
    }

    /**
     * @param prixG2 the prixG2 to set
     */
    public void setPrixG2(int prixG2) {
        this.prixG2 = prixG2;
    }

    /**
     * @return int return the prixG3
     */
    public int getPrixG3() {
        return prixG3;
    }

    /**
     * @param prixG3 the prixG3 to set
     */
    public void setPrixG3(int prixG3) {
        this.prixG3 = prixG3;
    }

    /**
     * @return int return the prixG4
     */
    public int getPrixG4() {
        return prixG4;
    }

    /**
     * @param prixG4 the prixG4 to set
     */
    public void setPrixG4(int prixG4) {
        this.prixG4 = prixG4;
    }

    /**
     * @return int return the prixG5
     */
    public int getPrixG5() {
        return prixG5;
    }

    /**
     * @param prixG5 the prixG5 to set
     */
    public void setPrixG5(int prixG5) {
        this.prixG5 = prixG5;
    }

    /**
     * @return int return the prixG6
     */
    public int getPrixG6() {
        return prixG6;
    }

    /**
     * @param prixG6 the prixG6 to set
     */
    public void setPrixG6(int prixG6) {
        this.prixG6 = prixG6;
    }

    /**
     * @return int return the prixG7
     */
    public int getPrixG7() {
        return prixG7;
    }

    /**
     * @param prixG7 the prixG7 to set
     */
    public void setPrixG7(int prixG7) {
        this.prixG7 = prixG7;
    }

    /**
     * @return int return the prixG8
     */
    public int getPrixG8() {
        return prixG8;
    }

    /**
     * @param prixG8 the prixG8 to set
     */
    public void setPrixG8(int prixG8) {
        this.prixG8 = prixG8;
    }

    /**
     * @return int return the prixG9
     */
    public int getPrixG9() {
        return prixG9;
    }

    /**
     * @param prixG9 the prixG9 to set
     */
    public void setPrixG9(int prixG9) {
        this.prixG9 = prixG9;
    }

    /**
     * @return int return the prixG10
     */
    public int getPrixG10() {
        return prixG10;
    }

    /**
     * @param prixG10 the prixG10 to set
     */
    public void setPrixG10(int prixG10) {
        this.prixG10 = prixG10;
    }

    /**
     * @return int return the voyage
     */
    public int getVoyage() {
        return voyage;
    }

    /**
     * @param voyage the voyage to set
     */
    public void setVoyage(int voyage) {
        this.voyage = voyage;
    }

    /**
     * 
     * @return int[] return the prix
     */
    public int[] getPrix() {
        int[] prix = {
                prixG1, prixG2, prixG3, prixG4, prixG5,
                prixG6, prixG7, prixG8, prixG9, prixG10
        };
        return prix;
    }

}
