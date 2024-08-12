/*
 * Voyage.java                                                       15/11/2021
 */
package Voyage;

import java.util.List;

import Outils.Popup;

/**
 * Objet Voyage reprennant le format de la bd pour permettre de passer la base
 * de données en objet
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class Voyage {

    private int idVoyage;
    private String designation;
    private String transportAller;
    private String transportRetour;
    private String villeDestination;
    private String villeRetour;
    private int nbJour;

    /**
     * Constructeur utilisé lors de l'écriture de la bd en objet Les objets ont déja
     * été vérifié et donc aucune vérification n'est ajouté
     * 
     * @param idVoyage         id du voyage
     * @param designation      Nom du voyage
     * @param transportAller   Type de transport choisi
     * @param transportRetour  Type de transport choisi
     * @param villeDestination Ville chosi, pas dans la List ville
     * @param villeRetour      Ville chosi, pas dans la List ville
     * @param nbJour           Nombre de jour du voyage
     */
    public Voyage(int idVoyage, String designation, String transportAller, String transportRetour,
            String villeDestination, String villeRetour, int nbJour) {

        this.idVoyage = idVoyage;
        this.designation = designation;
        this.transportAller = transportAller;
        this.transportRetour = transportRetour;
        this.villeDestination = villeDestination;
        this.villeRetour = villeRetour;
        this.nbJour = nbJour;
    }

    /**
     * Contructeur de l'objet voyage avec les paramètres et leurs vérification avant
     * création. L'id du Pays est supposé correct car l'interface ne permet de
     * saisir que des pays présent dans l'application
     * 
     * @param designation      Nom du voyage
     * @param transportAller   Type de transport choisi
     * @param transportRetour  Type de transport choisi
     * @param villeDestination Ville chosi, pas dans la List ville
     * @param villeRetour      Ville chosi, pas dans la List ville
     * @param nbJour           Nombre de jour du voyage
     */
    public Voyage(String designation, String transportAller, String transportRetour, String villeDestination,
            String villeRetour, int nbJour) {

        if (idVoyage >= 0 && !designation.equals("") && !transportAller.equals("") && !transportRetour.equals("")
                && !villeDestination.equals("") && !villeRetour.equals("")) {
            this.idVoyage = -1;
            this.designation = designation;
            this.transportAller = transportAller;
            this.transportRetour = transportRetour;
            this.villeDestination = villeDestination;
            this.villeRetour = villeRetour;
            this.nbJour = nbJour;
        } else {
            System.err.println("Probleme a la creation de l'objet voyage");
        }
    }

    /**
     * Permet de modifier les valeurs de l'objet souhaité, tout peut etre modifié
     * sauf l'id. On présume que la vérification à été faite au préalable
     * 
     * @param designation      Contient le nouveau nom du voyage
     * @param transportAller   contient le nouveau transport aller
     * @param transportRetour  contient le nouveau transport retour
     * @param villeDestination contient la nouvelle ville destination
     * @param villeRetour      contient la nouvelle ville retour
     */
    public void modifierVoyage(String designation, String transportAller, String transportRetour,
            String villeDestination, String villeRetour) {
        this.designation = designation;
        this.transportAller = transportAller;
        this.transportRetour = transportRetour;
        this.villeDestination = villeDestination;
        this.villeRetour = villeRetour;
    }

    /**
     * Vérifie que le voyage n'existe pas déjà
     * 
     * @param bdVoyage  Contient la liste de tous les voyages déjà créés
     * @param nomVoyage Contient le nom du voyage que l'on souhaite ajouter
     * @return true si le voyage existe deja, false dans le cas contraire
     */
    public static boolean voyageExiste(List<Voyage> bdVoyages, String nomVoyage) {
        boolean nomVoyagePris = false;

        for (Voyage nomAChercher : bdVoyages) {
            if (nomAChercher.getDesignation().toLowerCase().equals(nomVoyage.toLowerCase())) {
                nomVoyagePris = true;
            }
        }
        return nomVoyagePris;
    }

    /**
     * @return int return the idVoyage
     */
    public int getIdVoyage() {
        return idVoyage;
    }

    /**
     * @param idVoyage the idVoyage to set
     */
    public void setIdVoyage(int idVoyage) {
        if (this.idVoyage == -1) {
            this.idVoyage = idVoyage;
        } else {
            System.err.println("Vous n'avez pas le droit de toucher à l'id");
        }
    }

    /**
     * @return String return the designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * @param designation the designation to set
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * @return String return the transportAller
     */
    public String getTransportAller() {
        return transportAller;
    }

    /**
     * @param transportAller the transportAller to set
     */
    public void setTransportAller(String transportAller) {
        this.transportAller = transportAller;
    }

    /**
     * @return String return the transportRetour
     */
    public String getTransportRetour() {
        return transportRetour;
    }

    /**
     * @param transportRetour the transportRetour to set
     */
    public void setTransportRetour(String transportRetour) {
        this.transportRetour = transportRetour;
    }

    /**
     * @return String return the villeDestination
     */
    public String getVilleDestination() {
        return villeDestination;
    }

    /**
     * @param villeDestination the villeDestination to set
     */
    public void setVilleDestination(String villeDestination) {
        this.villeDestination = villeDestination;
    }

    /**
     * @return String return the villeRetour
     */
    public String getVilleRetour() {
        return villeRetour;
    }

    /**
     * @param villeRetour the villeRetour to set
     */
    public void setVilleRetour(String villeRetour) {
        this.villeRetour = villeRetour;
    }

    /**
     * @return int return the nbJour
     */
    public int getNbJour() {
        return nbJour;
    }

    /**
     * @param nbJour the nbJour to set
     */
    public void setNbJour(int nbJour) {
        this.nbJour = nbJour;
    }

    @Override
    public String toString() {
        return idVoyage + " | " + designation + " | " + transportAller + " | " + transportRetour
                + " | " + villeDestination + " | " + villeRetour + " | " + nbJour;
    }

    /**
     * Vérification si le transport modifié dans le tbv correspond
     * à ceux initialisés (avion, car, train)
     * 
     * @param transport       moyen de transoport à vérifier
     * @param listeTransports listes des transport
     * @return true si le transport se trouve dans la liste, sinon false
     */
    public static boolean transportCorrect(String transport, List<String> listeTransports) {
        final String TRANSPORT_INCORRECT = "Le type de transport est incorrect\n"
                + "Les transports corrects sont :\n";
        boolean transportCorrect = true;

        if (!listeTransports.contains(transport)) {
            Popup.popupErreur(TRANSPORT_INCORRECT + listeTransports.toString());
            transportCorrect = false;
        }

        return transportCorrect;
    }

    /**
     * Vérifie que le nom du voyage est dans le bon format :
     * 
     * @param nomVoyage nom du voyage
     * @return true si le format est correct
     */
    public static boolean estFormatCorrect(String nomVoyage) {
        return nomVoyage.toCharArray().length > 80 ? false : true;
    }

    /**
     * Combine toutes les vérifications du nom passé en argument et renvoit
     * la popup d'erreur appropriée lorsque ce n'est pas le cas.
     * 
     * @param nomVoyage    nom du voyage
     * @param listeVoyages liste des voyages
     * @return true si le nom est correct, sinon false
     */
    public static boolean designationVoyageCorrecte(String nomVoyage, List<Voyage> listeVoyages) {
        final String NOM_VIDE = "Aucune désignation n'a été saisie.\n";
        final String FORMAT_INCORRECT = "Le nom est plus long que 80 caractères.\n";
        final String EXISTE_DEJA = "La voyage saisi existe déjà.\n";

        StringBuilder erreurs = new StringBuilder();
        boolean nomVoyageCorrect = true;

        if (nomVoyage.equals("")) {
            nomVoyageCorrect = false;
            erreurs.append(NOM_VIDE);
        }
        if (!estFormatCorrect(nomVoyage)) {
            nomVoyageCorrect = false;
            erreurs.append(FORMAT_INCORRECT);
        }
        if (voyageExiste(listeVoyages, nomVoyage)) {
            nomVoyageCorrect = false;
            erreurs.append(EXISTE_DEJA);
        }

        if (!nomVoyageCorrect) {
            Popup.popupErreur(erreurs.toString());
        }

        return nomVoyageCorrect;
    }
}
