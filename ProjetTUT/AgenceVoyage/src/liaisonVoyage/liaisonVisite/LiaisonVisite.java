/*
 * LiaisonVisite                                            31/01/2022
 */
package liaisonVoyage.liaisonVisite;

/**
 * Objet LiaisonVisite reprennant le format de la bd pour permettre
 * de passer la base de données en objet.
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class LiaisonVisite {

    private int idLiaisonVisite;
    private int fkVoyage;
    private int fkVisite;

    /**
     * Constructeur avec id
     * 
     * @param id       id de la liaison
     * @param fkVoyage fk du voyage
     * @param fkVisite fk de la visite
     */
    public LiaisonVisite(int id, int fkVoyage, int fkVisite) {
        this.idLiaisonVisite = id;
        this.fkVoyage = fkVoyage;
        this.fkVisite = fkVisite;
    }

    /**
     * Constructeur sans id
     * 
     * @param fkVoyage fk du voyage
     * @param fkVisite fk de la visite
     */
    public LiaisonVisite(int fkVoyage, int fkVisite) {
        this.fkVoyage = fkVoyage;
        this.fkVisite = fkVisite;
    }

    /**
     * @return int return the idLiaisonVisite
     */
    public int getIdLiaisonVisite() {
        return idLiaisonVisite;
    }

    /**
     * @param idLiaisonVisite the idLiaisonVisite to set
     */
    public void setIdLiaisonVisite(int idLiaisonVisite) {
        this.idLiaisonVisite = idLiaisonVisite;
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
     * @return int return the fkVisite
     */
    public int getFkVisite() {
        return fkVisite;
    }

    /**
     * @param fkVisite the fkVisite to set
     */
    public void setFkVisite(int fkVisite) {
        this.fkVisite = fkVisite;
    }

    /**
     * @param
     */
    public String toString() {
        return fkVoyage + " <-> " + fkVisite;
    }
}
