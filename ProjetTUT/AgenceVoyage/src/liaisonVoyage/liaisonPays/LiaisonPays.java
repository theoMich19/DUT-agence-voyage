/*
 * LiaisonPays    17/01/2022
 */
package liaisonVoyage.liaisonPays;

/**
 * Objet LiaisonPays reprennant le format de la bd pour permettre de passer la
 * base de données en objet.
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class LiaisonPays {

    private int idLiaisonPays;
    private int fkVoyage;
    private int fkPays;

    /**
     * Constructeur avec id
     * 
     * @param id       id de la liaison
     * @param fkVoyage fk du voyage
     * @param fkPays   fk du pays
     */
    public LiaisonPays(int id, int fkVoyage, int fkPays) {
        this.idLiaisonPays = id;
        this.fkVoyage = fkVoyage;
        this.fkPays = fkPays;
    }

    /**
     * Constructeur sans id
     * 
     * @param fkVoyage fk du voyage
     * @param fkPays   fk du pays
     */
    public LiaisonPays(int fkVoyage, int fkPays) {
        this.fkVoyage = fkVoyage;
        this.fkPays = fkPays;
    }

    /**
     * @return int return the idLiaisonPays
     */
    public int getIdLiaisonPays() {
        return idLiaisonPays;
    }

    /**
     * @param idLiaisonPays the idLiaisonPays to set
     */
    public void setIdLiaisonPays(int idLiaisonPays) {
        this.idLiaisonPays = idLiaisonPays;
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
     * @return int return the fkPays
     */
    public int getFkPays() {
        return fkPays;
    }

    /**
     * @param fkPays the fkPays to set
     */
    public void setFkPays(int fkPays) {
        this.fkPays = fkPays;
    }

    /**
     * @param
     */
    public String toString() {
        return fkVoyage + " <-> " + fkPays;
    }
}
