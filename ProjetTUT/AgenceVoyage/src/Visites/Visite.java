package Visites;

import java.util.List;

import Outils.Popup;

/**
 * Objet représentant une visite.
 * Une visite est décrite par un id, un titre et une description.
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class Visite {

    private int idVisites;
    private String titre;
    private String description;

    /**
     * Construcuteur avec id visie
     * 
     * @param idVisites   id de la visite
     * @param titre       titre de la visite
     * @param description description de la visite
     */
    public Visite(int idVisites, String titre, String description) {
        this.idVisites = idVisites;
        this.titre = titre;
        this.description = description;
    }

    /**
     * Constructeur sans id vsite
     * 
     * @param titre       titre de la visite
     * @param description description de la visite
     */
    public Visite(String titre, String description) {
        this.titre = titre;
        this.description = description;
    }

    /**
     * Méthode qui permet de modifier la visite
     * 
     * @param titre       nouveau titre de la visite
     * @param description nouvelle description de la visite
     */
    public void modifierVisites(String titre, String description) {
        this.titre = titre;
        this.description = description;
    }

    /**
     * @return int return the idVisites
     */
    public int getIdVisites() {
        return idVisites;
    }

    /**
     * @param idVisites the idVisites to set
     */
    public void setIdVisites(int idVisites) {
        this.idVisites = idVisites;
    }

    /**
     * @return String return the titre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * @param titre the titre to set
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * @return String return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return idVisites + " | " + titre + " | " + description;
    }

    /**
     * Combine toutes les vérifications du nom passé en argument et renvoit
     * la popup d'erreur appropriée lorsque ce n'est pas le cas.
     * 
     * @param nomVisite    nom de la visite
     * @param listeVisites liste de la visite
     * @return true si le nom est correct, sinon false
     */
    public static boolean nomVisiteCorrect(String nomVisite, List<Visite> listeVisites) {
        final String NOM_VIDE = "Aucun nom de visite n'a été saisi.\n";
        final String FORMAT_INCORRECT = "Le nom contient peut-être un numéro ou est"
                + " plus long que 80 caractères.\n";
        final String EXISTE_DEJA = "La visite saisie existe déjà.\n";

        StringBuilder erreurs = new StringBuilder();
        boolean nomVisiteCorrect = true;

        if (nomVisite.equals("")) {
            nomVisiteCorrect = false;
            erreurs.append(NOM_VIDE);
        }
        if (!visiteEstFormatCorrect(nomVisite)) {
            nomVisiteCorrect = false;
            erreurs.append(FORMAT_INCORRECT);
        }
        if (visiteExiste(listeVisites, nomVisite)) {
            nomVisiteCorrect = false;
            erreurs.append(EXISTE_DEJA);
        }

        if (!nomVisiteCorrect) {
            Popup.popupErreur(erreurs.toString());
        }

        return nomVisiteCorrect;
    }

    /**
     * Vérifie que le nom de la visite est dans le bon format :
     * Pas de chiffre dans le nom.
     * 
     * @param nomVisite nom de la visite
     */
    public static boolean visiteEstFormatCorrect(String nomVisite) {
        boolean estCorrect = true;

        if (nomVisite.matches(".*[0-9].*")) {
            estCorrect = false;
        }

        return estCorrect;
    }

    /**
     * Vérifie que le nom de la visite n'est pas déjà utilisé.
     * 
     * @param nomVille nom de la visite
     * @return existe false si aucune ville de ce nom n'est trouvé, sinon true.
     */
    public static boolean visiteExiste(List<Visite> stockVisites, String nomVisite) {
        boolean nomVisitePrise = false;

        for (Visite nomAChercher : stockVisites) {
            if (nomAChercher.getTitre().toLowerCase().equals(nomVisite.toLowerCase())) {
                nomVisitePrise = true;
            }
        }
        return nomVisitePrise;
    }

    /**
     * Formatte le nom de la visite
     * 
     * @param nomVille nom de la visite
     * @return le nom formatté
     */
    public static String cleanNomVisite(String nomVisite) {
        // enlève les espaces au début et à la fin
        nomVisite = nomVisite.trim();
        return nomVisite;
    }

}
