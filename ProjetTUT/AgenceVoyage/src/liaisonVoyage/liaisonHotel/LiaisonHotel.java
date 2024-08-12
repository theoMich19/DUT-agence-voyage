/*
 * LiaisonHotel    17/01/2022
 */
package liaisonVoyage.liaisonHotel;

/**
 * Objet LiaisonHotel reprennant le format de la bd pour permettre de passer la
 * base de données en objet.
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class LiaisonHotel {

    private int idLiaisonHotel;
    private int fkVoyage;
    private int fkHotel;

    /**
     * Constructeur avec id
     * 
     * @param id       id la liaison
     * @param fkVoyage fk du voyage
     * @param fkHotel  fk de l'hotel
     */
    public LiaisonHotel(int id, int fkVoyage, int fkHotel) {
        this.idLiaisonHotel = id;
        this.fkVoyage = fkVoyage;
        this.fkHotel = fkHotel;
    }

    /**
     * Constructeur sans id
     * 
     * @param fkVoyage fk du voyage
     * @param fkHotel  fk de l'hotel
     */
    public LiaisonHotel(int fkVoyage, int fkHotel) {
        this.fkVoyage = fkVoyage;
        this.fkHotel = fkHotel;
    }

    /**
     * @return int return the idLiaisonHotel
     */
    public int getIdLiaisonHotel() {
        return idLiaisonHotel;
    }

    /**
     * @param idLiaisonHotel the idLiaisonHotel to set
     */
    public void setIdLiaisonHotel(int idLiaisonHotel) {
        this.idLiaisonHotel = idLiaisonHotel;
    }

    /**
     * @return int return the fkVoyage
     */
    public int getFkVoyage() {
        return fkVoyage;
    }

    /**
     * @param fkVoyage the fkVoyage to set
     */
    public void setfkVoyage(int fkVoyage) {
        this.fkVoyage = fkVoyage;
    }

    /**
     * @return int return the fkHotel
     */
    public int getFkHotel() {
        return fkHotel;
    }

    /**
     * @param fkHotel the fkHotel to set
     */
    public void setfkHotel(int fkHotel) {
        this.fkHotel = fkHotel;
    }

    /**
     * @param
     */
    public String toString() {
        return fkVoyage + " <-> " + fkHotel;
    }
}
