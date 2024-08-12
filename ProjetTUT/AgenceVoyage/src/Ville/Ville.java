/*
 * Ville.java                                                           07/11/2021
 */
package Ville;

import java.util.List;

import Outils.Popup;

/**
 * Objet Ville reprennant le format de la bd pour permettre de passer la base de
 * données en objet. Les villes contenues ici sont seulement des villes
 * françaises
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class Ville {

    private int id;
    private String nomVille;
    private int groupe;

    /* nombre de groupe maximum */
    private final static int GRP_MAX = 10;

    /**
     * Constructeur utilisé lors de l'écriture de la bd en objet. Les objets ont
     * déja
     * été vérifiés et donc aucune vérification supplémentaire n'est faite.
     * 
     * @param nomVille nom de la ville
     * @param groupe   numéro du groupe de ville
     */
    public Ville(int id, String nomVille, int groupe) {
        this.id = id;
        this.nomVille = nomVille;
        this.groupe = groupe;
    }

    /**
     * Contructeur d'une ville avec les paramètres. Vérification que le nom de la
     * ville est non vide, inexistant et qu'elle ne contienne pas de caractère non
     * souhaité, mais également que le numéro du groupe est compris entre 1 et 10 et
     * qu'il soit parmis les groupes existants. L'id de la ville est mis à -1 pour
     * dire qu'elle n'est pas saisie.
     * 
     * @param nomVille       Nom de la ville que l'on souhaite créer
     * @param groupe         Numéro du groupe compris entre 1 et 10
     * @param stockVille     Contient toute les villes deja créer
     * @param groupePlusHaut numéro de groupe déjà créé le plus haut
     */
    public Ville(String nomVille, int groupe, List<Ville> stockVille, int groupePlusHaut) {
        if (nomVilleCorrect(nomVille, stockVille) && groupeOk(groupe, groupePlusHaut)) {
            this.id = -1;
            this.nomVille = nomVille;
            this.groupe = groupe;
        }
    }

    /**
     * Permet de modifier les valeurs de l'objet souhaité, tout peut être modifié
     * sauf l'id. On présume que la vérification a été faite au préalable.
     * 
     * @param nomVille Contient le nouveau nom de la ville
     * @param groupe   Contient le nouveau nom de groupe
     */
    public void modifierVille(String nomVille, int groupe) {
        this.nomVille = nomVille;
        this.groupe = groupe;
    }

    /**
     * Vérifie que la ville fait partie d'un des 10 groupes et qu'il ne soit pas
     * dans
     * un groupe non créé.
     * 
     * @param groupe         Groupe a vérifier
     * @param groupePlusHaut numéro de groupe déjè créé le plus haut
     * @return Renvoie true si le groupe est correct, faux dans le cas contraire
     */
    public static boolean groupeOk(int groupe, int groupePlusHaut) {
        // (attendre réponse du client pour ajout groupe)
        if (groupe >= 1 && groupe <= GRP_MAX && groupe <= groupePlusHaut) {
            return true;
        }
        return false;
    }

    /**
     * Vérifie que le nom de la ville n'est pas déjà utilisé.
     * 
     * @param nomVille nom de la ville
     * @return existe false si aucune ville de ce nom n'est trouvé, sinon true.
     */
    public static boolean villeExiste(List<Ville> stockVille, String nomVille) {
        boolean nomVillePrise = false;

        for (Ville nomAChercher : stockVille) {
            if (nomAChercher.getNomVille().toLowerCase().equals(nomVille.toLowerCase())) {
                nomVillePrise = true;
            }
        }
        return nomVillePrise;
    }

    /**
     * Permet de vérifier s'il existe une autre ville qui a le même groupe
     * 
     * @param stockVille Différente ville à comparer
     * @return true s'il n'y a pas d'autre ville avec le même groupe,
     *         false dans le cas contraire
     */
    public boolean seuleDansGroupe(List<Ville> stockVille) {
        boolean estSeule = true;

        for (Ville aVerif : stockVille) {
            if (aVerif.groupe == this.groupe && !this.nomVille.equals(aVerif.nomVille)) {
                estSeule = false;
            }
        }
        return estSeule;
    }

    /**
     * Permet de décaler le groupe de -1
     */
    public void decalerGroupe() {
        this.groupe--;
    }

    /**
     * @return int return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        if (this.id == -1) {
            this.id = id;
        } else {
            System.err.println("Vous n'avez pas le droit de toucher à l'id");
        }
    }

    /**
     * @return String return the nomVille
     */
    public String getNomVille() {
        return nomVille;
    }

    /**
     * @param nomVille the nomVille to set
     */
    public void setNomVille(String nomVille, List<Ville> stockVille) {
        if (!nomVille.equals("") && !villeExiste(stockVille, nomVille)
                && villeEstFormatCorrect(nomVille)) {
            this.nomVille = nomVille;
        } else {
            System.err.println("Impossible de changer le nom, il est incorrect");
        }
    }

    /**
     * @return int return the groupe
     */
    public int getGroupe() {
        return groupe;
    }

    /**
     * @param groupe the groupe to set
     */
    public void setGroupe(int groupe, int groupePlusHaut) {
        if (groupeOk(groupe, groupePlusHaut)) {
            this.groupe = groupe;
        } else {
            System.err.println("Impossible de changer le groupe, il est incorrect");
        }
    }

    /**
     * Vérifie que le nom de la ville est dans le bon format :
     * Pas de chiffre dans le nom.
     * 
     * @param nomVille
     */
    public static boolean villeEstFormatCorrect(String nomVille) {
        boolean estCorrect = true;

        if (nomVille.matches(".*[0-9].*") || nomVille.length() > 80) {
            estCorrect = false;
        }

        return estCorrect;
    }

    @Override
    public String toString() {
        return id + " | " + nomVille + " | " + groupe;
    }

    /**
     * Combine toutes les vérifications du nom passé en argument et renvoit
     * la popup d'erreur appropriée lorsque ce n'est pas le cas.
     * 
     * @param nomVille    nom de la ville
     * @param listeVilles liste des villes
     * @return true si le nom est correct, sinon false
     */
    public static boolean nomVilleCorrect(String nomVille, List<Ville> listeVilles) {
        final String NOM_VIDE = "Aucun nom de ville n'a été saisi.\n";
        final String FORMAT_INCORRECT = "Le nom contient peut-être un numéro ou est"
                + " plus long que 80 caractères.\n";
        final String EXISTE_DEJA = "La ville saisie existe déjà.\n";

        StringBuilder erreurs = new StringBuilder();
        boolean nomVilleCorrect = true;

        if (nomVille.equals("")) {
            nomVilleCorrect = false;
            erreurs.append(NOM_VIDE);
        }
        if (!villeEstFormatCorrect(nomVille)) {
            nomVilleCorrect = false;
            erreurs.append(FORMAT_INCORRECT);
        }
        if (villeExiste(listeVilles, nomVille)) {
            nomVilleCorrect = false;
            erreurs.append(EXISTE_DEJA);
        }

        if (!nomVilleCorrect) {
            Popup.popupErreur(erreurs.toString());
        }

        return nomVilleCorrect;
    }

    /**
     * Vérifie que la cellule soit non vide et le format correct.
     * 
     * @param nomVille nom de la ville
     * @return true si le nom est correct, sinon false
     */
    public static boolean nomVilleCorrect(String nomVille) {
        final String NOM_VIDE = "Aucun nom de ville n'a été saisi.\n";
        final String FORMAT_INCORRECT = "Le nom de la ville contient peut-être un numéro ou est"
                + " plus long que 80 caractères.\n";

        StringBuilder erreurs = new StringBuilder();
        boolean nomVilleCorrect = true;

        if (nomVille.equals("")) {
            nomVilleCorrect = false;
            erreurs.append(NOM_VIDE);
        }
        if (!villeEstFormatCorrect(nomVille)) {
            nomVilleCorrect = false;
            erreurs.append(FORMAT_INCORRECT);
        }

        if (!nomVilleCorrect) {
            Popup.popupErreur(erreurs.toString());
        }

        return nomVilleCorrect;
    }

    /**
     * Formatte le nom de la ville
     * 
     * @param nomVille nom de la ville
     * @return le nom formatté
     */
    public static String cleanNomVille(String nomVille) {
        // enlève les espaces au début et à la fin
        nomVille = nomVille.trim();
        // met une majuscule à la première lettre
        nomVille = Character.toUpperCase(nomVille.charAt(0)) + nomVille.substring(1);
        return nomVille;
    }
}
