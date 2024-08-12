/*
 * Pays.java         07/11/2021
 */
package Pays;

import java.util.List;

import Voyage.Voyage;
import liaisonVoyage.liaisonPays.LiaisonPays;
import Outils.Popup;

/**
 * Objet Pays reprennant le format de la bd pour permettre de passer la base de
 * données en objet
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class Pays {

    private int idPays;
    private String nomPays;
    private String formalites;
    private String conseil;

    /**
     * Constructeur utilisé lors de l'écriture de la bd en objet Les objets ont déja
     * été vérifiés et donc aucune vérification supplémentaire n'est faite.
     * 
     * @param idPays     id du pays
     * @param nomPays    nom du pays
     * @param formalites texte de formalité lié au pays
     * @param conseil    texte de conseil lié au pays
     */
    public Pays(int idPays, String nomPays, String formalites, String conseil) {
        this.idPays = idPays;
        this.nomPays = nomPays;
        this.formalites = formalites;
        this.conseil = conseil;
    }

    /**
     * Constructeur utilisé lors de l'ajout dans l'application On vérifie que le
     * pays n'est pas déjà créé
     * 
     * @param nomPays    nom du pays
     * @param formalites texte de formalité lié au pays
     * @param conseil    texte de conseil lié au pays
     * @param bdPays     Liste des pays enregistrés en local
     */
    public Pays(String nomPays, String formalites, String conseil, List<Pays> bdPays) throws Exception {
        if (!nomPays.equals("") && !paysExiste(bdPays, nomPays)) {
            this.idPays = -1;
            this.nomPays = nomPays;
            this.formalites = formalites;
            this.conseil = conseil;
        }
    }

    /**
     * Vérifie que le Pays n'existe pas déjà,
     * 
     * @param bdPays  Contient la liste de tous les pays déjà créés
     * @param nomPays Contient le nom du pays que l'on souhaite ajouter
     * @return true si le pays existe deja, false dans le cas contraire
     */
    public static boolean paysExiste(List<Pays> bdPays, String nomPays) {
        boolean nomPaysPris;
        nomPaysPris = false;
        for (Pays nomAChercher : bdPays) {
            if (nomAChercher.getNomPays().toLowerCase().equals(nomPays.toLowerCase())) {
                nomPaysPris = true;
            }
        }
        return nomPaysPris;
    }

    /**
     * Permet de modifier les valeurs de l'objet souhaité, tout peut etre modifié
     * sauf l'id. On présume que la vérification à été faite au préalable
     * 
     * @param nomVille   Contient le nouveau nom du pays
     * @param formalites contient les nouvelles formalités
     * @param conseil    contient les nouveaux conseils
     */
    public void modifierPays(String nomPays, String formalites, String conseil) {
        this.nomPays = nomPays;
        this.formalites = formalites;
        this.conseil = conseil;
    }

    /**
     * Vérifie que des voyage ne se situe pas dans ce pays
     * 
     * 
     * @param stockVoyage
     * @return true si un voyage se situe dans ce pays, false dans le cas contraire
     */
    public boolean voyageUtilisePays(List<Voyage> stockVoyage, List<LiaisonPays> stockLiaisonPays) {
        boolean verif = false;

        for (Voyage aVerif : stockVoyage) {
            for (LiaisonPays s : stockLiaisonPays) {
                if (s.getFkVoyage() == aVerif.getIdVoyage()
                        && this.idPays == s.getFkPays()) {
                    verif = true;
                }
            }
        }
        return verif;
    }

    /**
     * @return idPays
     */
    public int getIdPays() {
        return idPays;
    }

    /**
     * @param idPays l'id du pays
     */
    public void setIdPays(int idPays) {
        if (this.idPays == -1) {
            this.idPays = idPays;
        } else {
            System.err.println("Vous n'avez pas le droit de toucher à l'id");
        }
    }

    /**
     * @return String return the nomPays
     */
    public String getNomPays() {
        return nomPays;
    }

    /**
     * @param nomPays the nomPays to set
     */
    public void setNomPays(String nomPays) {
        this.nomPays = nomPays;
    }

    /**
     * @return String return the formalites
     */
    public String getFormalites() {
        return formalites;
    }

    /**
     * @param formalites the formalites to set
     */
    public void setFormalites(String formalites) {
        this.formalites = formalites;
    }

    /**
     * @return String return the conseils
     */
    public String getConseil() {
        return conseil;
    }

    /**
     * @param conseils the conseils to set
     */
    public void setConseil(String conseil) {
        this.conseil = conseil;
    }

    @Override
    public String toString() {
        return idPays + " | " + nomPays + " | " + formalites + " | " + conseil;
    }

    /**
     * Vérifie que le nom du pays est dans le bon format :
     * Pas de chiffre dans le nom et pas plus de 80 caractères
     * 
     * @param nomPays nom du pays
     * @return true si le format est correct
     */
    public static boolean paysEstFormatCorrect(String nomPays) {
        boolean estCorrect = true;

        if (nomPays.matches(".*[0-9].*") || nomPays.length() > 80) {
            estCorrect = false;
        }

        return estCorrect;
    }

    /**
     * Combine toutes les vérifications du nom passé en argument et renvoit
     * la popup d'erreur appropriée lorsque ce n'est pas le cas.
     * 
     * @param nomPays   nom du pays
     * @param listePays listes des pays
     * @return true si le nom est correct, sinon false
     */
    public static boolean nomPaysCorrect(String nomPays, List<Pays> listePays) {
        final String NOM_VIDE = "Aucun nom de pays n'a été saisi.\n";
        final String FORMAT_INCORRECT = "Le nom contient peut-être un numéro ou est"
                                        + " plus long que 80 caractères.\n";
        final String EXISTE_DEJA = "Le pays saisi existe déjà.\n";

        StringBuilder erreurs = new StringBuilder();
        boolean nomPaysCorrect = true;

        if (nomPays.equals("")) {
            nomPaysCorrect = false;
            erreurs.append(NOM_VIDE);
        }
        if (!paysEstFormatCorrect(nomPays)) {
            nomPaysCorrect = false;
            erreurs.append(FORMAT_INCORRECT);
        }
        if (paysExiste(listePays, nomPays)) {
            nomPaysCorrect = false;
            erreurs.append(EXISTE_DEJA);
        }

        if (!nomPaysCorrect) {
            Popup.popupErreur(erreurs.toString());
        }

        return nomPaysCorrect;
    }

    /**
     * Formatte le nom du pays
     * 
     * @param nomPays nom du pays
     * @return le nom formatté
     */
    public static String cleanNomPays(String nomPays) {
        // enlève les espaces au début et à la fin
        nomPays = nomPays.trim();
        // met une majuscule à la première lettre
        nomPays = Character.toUpperCase(nomPays.charAt(0)) + nomPays.substring(1);
        return nomPays;
    }
}
