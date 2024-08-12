package Hotel;

import Ville.Ville;

import java.util.List;

import Outils.Popup;

/**
 * Objet Hotel reprennant le format de la bd pour permettre de passer la base de
 * données en objet.
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class Hotel {

    private int id;
    private String nomHotel;
    private String adresse;
    private String ville;
    private String numTel;

    /**
     * Constructeur utilisé lors de l'ouverture de l'application
     * L'id est donc deja connu
     * 
     * @param id       id de l'hôtel
     * @param nomHotel nom de l'hôtel
     * @param adresse  adresse de l'hôtel
     * @param ville    villede l'hôtel
     * @param numTel   numéro de téléphone de l'hôtel
     */
    public Hotel(int id, String nomHotel, String adresse, String ville, String numTel) {
        this.id = id;
        this.nomHotel = nomHotel;
        this.adresse = adresse;
        this.ville = ville;
        this.numTel = numTel;
    }

    /**
     * Constructeur utilisé lorsque l'on fait un ajout a travers l'application
     * Ici l'id n'est pas connu
     * 
     * @param nomHotel nom de l'hôtel
     * @param adresse  adresse de l'hôtel
     * @param ville    ville de l'hôtel
     * @param numTel   numéro de téléphone de l'hôtel
     */
    public Hotel(String nomHotel, String adresse, String ville, String numTel) {
        this.id = -1;
        this.nomHotel = nomHotel;
        this.adresse = adresse;
        this.ville = ville;
        this.numTel = numTel;
    }

    /**
     * Permet de modifier les valeurs de l'objet souhaité, tout peut être modifié
     * sauf l'id. On présume que la vérification a été faite au préalable.
     * 
     * @param nomHotel Contient le nouveau nom de l'hotel
     * @param adresse  Contient la nouvelle adresse
     * @param ville    Contient la nouvelle ville de l'hôtel
     * @param numTel   Continent le nouveau numéro de téléphone de l'hôtel
     */
    public void modifierHotel(String nomHotel, String adresse, String ville, String numTel) {
        this.nomHotel = nomHotel;
        this.adresse = adresse;
        this.ville = ville;
        this.numTel = numTel;
    }

    /**
     * Vérifie que le numéro est bien composé de 10 chiffres
     * 
     * @param aVerif numéro à vérifier
     * @return true s'il contient 10 chiffres
     */
    public static boolean verifNum(String aVerif) {
        final String NOM_VIDE = "Aucun numéro n'a été saisi\n";
        final String FORMAT_INCORRECT = "Le format saisi est incorrect.\nIl doit être de la forme : XXXXXXXXXX \nSoit l'équivalent de 10 chiffres";

        StringBuilder erreurs = new StringBuilder();

        boolean verif = false;

        if (aVerif.trim().equals("")) {
            erreurs.append(NOM_VIDE);
        } else if (aVerif.matches("[0-9]+") && aVerif.length() == 10) {
            verif = true;
        } else {
            erreurs.append(FORMAT_INCORRECT);
        }
        if (!verif) {
            Popup.popupErreur(erreurs.toString());
        }
        return verif;
    }

    /**
     * Vérifie que la cellule soit non vide et le format correct.
     * 
     * @param nomHotel nom de l'hôtel à vérifier
     * @return true si le nom est correct, sinon false
     */
    public static boolean nomHotelCorrect(String nomHotel, List<Hotel> listeHotel) {
        final String NOM_VIDE = "Aucun nom d'hôtel n'a été saisi.\n";
        final String FORMAT_INCORRECT = "Le nom contient peut-être un numéro ou est"
                                        + " plus long que 80 caractères.\n";
        final String EXISTE_DEJA = "L'hôtel saisi existe déjà.\n";

        StringBuilder erreurs = new StringBuilder();
        boolean nomHotelCorrect = true;

        if (nomHotel.equals("")) {
            nomHotelCorrect = false;
            erreurs.append(NOM_VIDE);
        }
        if (!Ville.villeEstFormatCorrect(nomHotel)) {
            nomHotelCorrect = false;
            erreurs.append(FORMAT_INCORRECT);
        }
        if (hotelExiste(listeHotel, nomHotel)) {
            nomHotelCorrect = false;
            erreurs.append(EXISTE_DEJA);
        }

        if (!nomHotelCorrect) {
            Popup.popupErreur(erreurs.toString());
        }
        return nomHotelCorrect;
    }

    /**
     * vérife que la cellule soit non vide et le format correcte
     * 
     * @param adresse adresse à vérifier
     * @return true si l'adresse est correct, sinon false
     */
    public static boolean adresseCorrect(String adresse) {
        final String NOM_VIDE = "Aucune adresse n'a été saisie.\n";

        StringBuilder erreurs = new StringBuilder();
        boolean nomAdresseCorrect = true;

        if (adresse.equals("")) {
            nomAdresseCorrect = false;
            erreurs.append(NOM_VIDE);
        }

        if (!nomAdresseCorrect) {
            Popup.popupErreur(erreurs.toString());
        }
        return nomAdresseCorrect;
    }

    /**
     * Vérifie que l'hôtel n'existe pas déjà,
     * 
     * @param bdHotel  Contient la liste de tous les hôtels déjà créés
     * @param nomHotel Contient le nom de l'hôtel que l'on souhaite ajouter
     * @return true si l'hôtel existe deja, false dans le cas contraire
     */
    public static boolean hotelExiste(List<Hotel> bdHotels, String nomHotel) {
        boolean nomHotelPris;
        nomHotelPris = false;
        for (Hotel nomAChercher : bdHotels) {
            if (nomAChercher.getNomHotel().toLowerCase().equals(nomHotel.toLowerCase())) {
                nomHotelPris = true;
            }
        }
        return nomHotelPris;
    }

    /**
     * @return String return the nomHotel
     */
    public int getIdHotel() {
        return id;
    }

    /**
     * @return String return the nomHotel
     */
    public String getNomHotel() {
        return nomHotel;
    }

    /**
     * @return String return the adresse
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * @return String return the ville
     */
    public String getVille() {
        return ville;
    }

    /**
     * @return String return the numTel
     */
    public String getNumTel() {
        return numTel;
    }

    /**
     * @param id the id to set
     */
    public void setIdHotel(int id) {
        if (this.id == -1) {
            this.id = id;
        }
    }

    /**
     * @param nomHotel the nomHotel to set
     */
    public void setNomHotel(String nomHotel) {
        this.nomHotel = nomHotel;
    }

    /**
     * @param adresse the adresse to set
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * @param ville the ville to set
     */
    public void setVille(String ville) {
        this.ville = ville;
    }

    /**
     * @param numTel the numTel to set
     */
    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    @Override
    public String toString() {
        return id + " | " + nomHotel + " | " + adresse + " | " + ville + " | " + numTel;
    }
}
