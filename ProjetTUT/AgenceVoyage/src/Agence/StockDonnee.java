/*
 * StockDonnee.java     15/11/2021
 */
package Agence;

import java.util.ArrayList;
import java.util.List;

import Hotel.Hotel;
import Hotel.HotelDAO;
import Voyage.Voyage;
import Voyage.VoyageDAO;
import liaisonVoyage.liaisonHotel.LiaisonHotel;
import liaisonVoyage.liaisonHotel.LiaisonHotelDAO;
import liaisonVoyage.liaisonPays.LiaisonPays;
import liaisonVoyage.liaisonPays.LiaisonPaysDAO;
import liaisonVoyage.liaisonVisite.LiaisonVisite;
import liaisonVoyage.liaisonVisite.LiaisonVisiteDAO;
import Pays.Pays;
import Pays.PaysDAO;
import Ville.Ville;
import Ville.VilleDAO;
import Visites.Visite;
import Visites.VisitesDAO;
import Tarif.Tarif;
import Tarif.TarifDAO;

/**
 * Gestion listes et données dans le fichier simplification pour de nombreuses
 * méthodes (permet d'éviter d'appeler la base de données à chaque requête, on
 * appel la liste correspondante).
 * StockDonnee est un singleton, il permet de ne pas en avoir un différent
 * pour chaque interface et ainsi le garder tant que l'application est allumée.
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class StockDonnee {

    // Stock des données (Singleton)
    private static StockDonnee instance;

    /* Les différentes listes, Pays, Ville, Voyage, Tarifs */
    private List<Pays> stockPays;

    private List<Ville> stockVille;

    private List<Voyage> stockVoyage;

    private List<Tarif> stockTarif;

    private List<Hotel> stockHotel;

    private List<Visite> stockVisite;

    private List<LiaisonPays> stockLiaisonPays;

    private List<LiaisonHotel> stockLiaisonHotel;

    private List<LiaisonVisite> stockLiaisonVisite;

    private int nbGroupeMax;

    /**
     * Constructeur d'initialisation des stocks des données grâce aux params
     * En privé car singleton
     * 
     * @param stockPays
     * @param stockVille
     * @param stockVoyage
     * @param stockTarif
     * @param stockHotel
     * @param stockVisite
     */
    private StockDonnee(List<Pays> stockPays, List<Ville> stockVille, List<Voyage> stockVoyage,
            List<Tarif> stockTarif, List<Hotel> stockHotel, List<Visite> stockVisite,
            List<LiaisonPays> stockLiaisonPays, List<LiaisonHotel> stockLiaisonHotel,
            List<LiaisonVisite> stockLiaisonVisite, int nbGroupeMax) {
        this.stockPays = stockPays;
        this.stockVille = stockVille;
        this.stockVoyage = stockVoyage;
        this.stockTarif = stockTarif;
        this.stockHotel = stockHotel;
        this.stockVisite = stockVisite;
        this.stockLiaisonPays = stockLiaisonPays;
        this.stockLiaisonHotel = stockLiaisonHotel;
        this.stockLiaisonVisite = stockLiaisonVisite;
        this.nbGroupeMax = nbGroupeMax;
    }

    /**
     * Permet de décaler les villes ainsi que les tarifs lors de la
     * suppression d'un groupe
     * 
     * @param nbGroupeSupr Le groupe qui a été supprimé
     */
    public void decalerGroupe(int nbGroupeSupr) {
        for (Ville aDecaler : stockVille) {
            if (aDecaler.getGroupe() > nbGroupeSupr) {
                aDecaler.decalerGroupe();
            }
        }
        for (Tarif aDecaler : stockTarif) {
            aDecaler.decalerGroupe(nbGroupeSupr);
        }
    }

    /**
     * Supprimer toutes les liaison associé a un voyage
     * 
     * @param voyage id du voyage dont on souhaite supprimer les liaisons
     */
    public void suppresionLiaisonParVoyage(int voyage) {
        for (int p = 0; p < stockLiaisonPays.size(); p++) {
            if (stockLiaisonPays.get(p).getFkVoyage() == voyage) {

                this.stockLiaisonPays.remove(p);
            }
        }
        for (int h = 0; h < stockLiaisonHotel.size(); h++) {
            if (stockLiaisonHotel.get(h).getFkVoyage() == voyage) {
                this.stockLiaisonHotel.remove(h);
            }
        }
        for (int v = 0; v < stockLiaisonVisite.size(); v++) {
            if (stockLiaisonVisite.get(v).getFkVoyage() == voyage) {

                this.stockLiaisonVisite.remove(v);
            }
        }
    }

    /**
     * Permet de supprimer les voyages associés à un pays
     * 
     * Pour ceci cherche les voyages qui sont associé a ce pays,
     * 
     * Supprime toute les liaison avec les pays sur ce voyage puis le supprime
     * 
     * @param pays Id du pays
     */
    public void suppressionVoyageParPays(int pays) {
        for (int s = 0; s < stockLiaisonPays.size(); s++) {
            if (stockLiaisonPays.get(s).getFkPays() == pays) {
                for (int i = 0; i < stockVoyage.size(); i++) {

                    if (stockVoyage.get(i).getIdVoyage() == stockLiaisonPays.get(s).getFkVoyage()) {

                        suppresionLiaisonParVoyage(stockVoyage.get(i).getIdVoyage());

                        this.stockVoyage.remove(i);
                    }
                }
            }
        }
    }

    /**
     * Permet de supprimer les tarifs associés à un voyage
     * 
     * @param voyage Id du voyage
     */
    public void suppressionTarifParVoyage(int voyage) {

        for (int i = 0; i < stockTarif.size(); i++) {
            if (stockTarif.get(i).getVoyage() == voyage) {
                this.stockTarif.remove(i);
            }
        }
    }

    /**
     * Verifie que l'hotel n'est pas deja utilisé dans un voyage
     * 
     * @param row objet Hôtel
     * @return true si il est utilisé, faux dans le cas contraire
     */
    public boolean verifHotelUtilise(Hotel row) {

        boolean utilise = false;

        for (LiaisonHotel h : stockLiaisonHotel) {
            if (h.getFkHotel() == row.getIdHotel()) {
                utilise = true;
            }
        }

        return utilise;
    }

    /**
     * Verifie que la visite n'est pas deja utilisé dans un voyage
     * 
     * @param row objet Visite
     * @return true si il est utilisé, faux dans le cas contraire
     */
    public boolean verifVisiteUtilise(Visite row) {

        boolean utilise = false;

        for (LiaisonVisite v : stockLiaisonVisite) {
            if (v.getFkVisite() == row.getIdVisites()) {
                utilise = true;
            }
        }

        return utilise;
    }

    /**
     * Suppression liaision hotel
     * 
     * @param idHotel id de l'hôtel
     */
    public void suppresionLiaisonHotel(int idHotel) {
        for (int i = 0; i < stockLiaisonHotel.size(); i++) {
            if (stockLiaisonHotel.get(i).getFkHotel() == idHotel) {
                stockLiaisonHotel.remove(i);
            }
        }
    }

    /**
     * Suppression liaision visite
     * 
     * @param idVisite id de la visite
     */
    public void suppresionLiaisonVisite(int idVisite) {
        for (int i = 0; i < stockLiaisonVisite.size(); i++) {
            if (stockLiaisonVisite.get(i).getFkVisite() == idVisite) {
                stockLiaisonVisite.remove(i);
            }
        }
    }

    /**
     * méthode de supression d'une liaison d'un voyage à une visite
     * 
     * @param idVoyage id du voyage
     * @param idVisite id de la visite
     */
    public void suppresionLiaisonVisite(int idVoyage, int idVisite) {
        for (int v = 0; v < stockLiaisonVisite.size(); v++) {
            if (stockLiaisonVisite.get(v).getFkVoyage() == idVoyage
                    && stockLiaisonVisite.get(v).getFkVisite() == idVisite) {
                this.stockLiaisonVisite.remove(v);
            }
        }
    }

    /**
     * méthode de suppression d'une liaison d'un voyage à une visite
     * 
     * @param idVoyage id du voyage
     * @param idHotel  id de l'hôtel
     */
    public void suppresionLiaisonHotel(int idVoyage, int idHotel) {
        for (int h = 0; h < stockLiaisonHotel.size(); h++) {
            if (stockLiaisonHotel.get(h).getFkVoyage() == idVoyage
                    && stockLiaisonHotel.get(h).getFkHotel() == idHotel) {
                this.stockLiaisonHotel.remove(h);
            }
        }
    }

    /**
     * méthode de supression d'une liaison d'un voyage à un pays
     * 
     * @param idVoyage id du voyage
     * @param idPays   id du pays
     */
    public void suppresionLiaisonPays(int idVoyage, int idPays) {
        for (int p = 0; p < stockLiaisonPays.size(); p++) {
            if (stockLiaisonPays.get(p).getFkVoyage() == idVoyage
                    && stockLiaisonPays.get(p).getFkPays() == idPays) {
                this.stockLiaisonPays.remove(p);
            }
        }
    }

    /**
     * Méthode qui permet de savoir le nombre de jours d'un voyage (passé en
     * argument)
     * 
     * @param idVoyage id du voyage
     * @return Retourne le nombre de jour que contient le voyage
     */
    public int nbJourVoyage(int idVoyage) {

        int nbJour = -1;

        for (Voyage v : stockVoyage) {
            if (v.getIdVoyage() == idVoyage) {
                nbJour = v.getNbJour();
            }
        }
        return nbJour;
    }

    /**
     * Supprime un voyage dans la liste
     * 
     * @param idVoyage id du voyage
     */
    public void suppresionVoyage(int idVoyage) {
        for (int v = 0; v < stockVoyage.size(); v++) {
            if (stockVoyage.get(v).getIdVoyage() == idVoyage) {
                stockVoyage.remove(v);
            }
        }
    }

    /**
     * Permet de trouver l'id d'un voyage à l'aide de son nom
     * 
     * @param nomVoyage nom du voyage
     * @return l'id du voyage
     */
    public int chercherIdVoyage(String nomVoyage) {
        int idVoyage = -1;
        for (Voyage voyage : stockVoyage) {
            if (voyage.getDesignation().equals(nomVoyage)) {
                idVoyage = voyage.getIdVoyage();
            }
        }
        return idVoyage;
    }

    /**
     * Méthode de recherche qui permet de trouver l'id du voyage à partir de son nom
     * 
     * @param nomPays nom du pays
     * @return l'id du pays
     */
    public int chercherIdPays(String nomPays) {
        int idPays = -1;
        for (Pays pays : stockPays) {
            if (pays.getNomPays().toLowerCase().equals(nomPays.toLowerCase())) {
                idPays = pays.getIdPays();
            }
        }
        return idPays;
    }

    /**
     * Méthode de recherche qui permet de trouver l'id du voyage à partir de son nom
     * 
     * @param nomHotel nom de l'hôtel
     * @return l'id de l'hôtel
     */
    public int chercherIdHotel(String nomHotel) {
        int idHotel = -1;
        for (Hotel hotel : stockHotel) {
            if (hotel.getNomHotel().equals(nomHotel)) {
                idHotel = hotel.getIdHotel();
            }
        }
        return idHotel;
    }

    /**
     * Vérifie que le Pays n'existe pas déjà,
     * 
     * @param nomPays Contient le nom du pays que l'on souhaite ajouter
     * @return true si le pays existe deja, false dans le cas contraire
     */
    public boolean paysExiste(String nomPays) {
        boolean nomPaysPris;
        nomPaysPris = false;
        for (Pays nomAChercher : stockPays) {
            if (nomAChercher.getNomPays().toLowerCase().equals(nomPays.toLowerCase())) {
                nomPaysPris = true;
            }
        }
        return nomPaysPris;
    }

    /**
     * Verifie que le voyage est un voyage sans hebergement
     * 
     * @param idVoyage id du voyage
     * @return true si le voyage n'as pas d'hébergement
     */
    public boolean voyageSansHebergement(int idVoyage) {
        boolean verifier = true;
        for (LiaisonHotel h : stockLiaisonHotel) {
            if (h.getFkVoyage() == idVoyage) {
                verifier = false;
            }
        }
        return verifier;
    }

    /**
     * Verifie que le voyage est un voyage avec hebergement
     * 
     * @param idVoyage id du voyage
     * @return true si le voyage existe
     */
    public boolean voyageAvecHebergement(int idVoyage) {
        boolean verifier = false;
        for (LiaisonHotel h : stockLiaisonHotel) {
            if (h.getFkVoyage() == idVoyage) {
                verifier = true;
            }
        }
        return verifier;
    }

    /**
     * Méthode qui permet de savoir si le voyage est organisé
     * 
     * @param idVoyage id du voyage
     * @return true si le voyage est organisé
     */
    public boolean voyageOrganise(int idVoyage) {
        boolean verifier = false;
        for (LiaisonHotel h : stockLiaisonHotel) {
            if (h.getFkVoyage() == idVoyage) {
                verifier = true;
            }
        }
        if (verifier) {
            verifier = false;
            for (LiaisonVisite v : stockLiaisonVisite) {
                if (v.getFkVoyage() == idVoyage) {
                    verifier = true;
                }
            }
        }
        return verifier;
    }

    /**
     * Renvoie les pays lié a un voyage
     * 
     * @param idVoyage id du voyage
     * @return list de pays
     */
    public List<String> listePaysParVoyage(int idVoyage) {

        List<String> resultat = new ArrayList<>();

        for (LiaisonPays lp : stockLiaisonPays) {
            if (lp.getFkVoyage() == idVoyage) {
                for (Pays p : stockPays) {
                    if (p.getIdPays() == lp.getFkPays()) {
                        resultat.add(p.getNomPays());
                    }
                }
            }
        }

        return resultat;
    }

    /**
     * Renvoie les hotel lié a un voyage
     * 
     * @param idVoyage id du voyage
     * @return list d'hôtel
     */
    public List<String> listeHotelParVoyage(int idVoyage) {

        List<String> resultat = new ArrayList<>();

        for (LiaisonHotel lh : stockLiaisonHotel) {
            if (lh.getFkVoyage() == idVoyage) {
                for (Hotel h : stockHotel) {
                    if (h.getIdHotel() == lh.getFkHotel()) {
                        resultat.add(h.getNomHotel());
                    }
                }
            }
        }

        return resultat;
    }

    /**
     * Renvoie les visites lié a un voyage
     * 
     * @param idVoyage id du voyage
     * @return list de visites
     */
    public List<String> listeVisiteParVoyage(int idVoyage) {

        List<String> resultat = new ArrayList<>();

        for (LiaisonVisite lv : stockLiaisonVisite) {
            if (lv.getFkVoyage() == idVoyage) {
                for (Visite v : stockVisite) {
                    if (v.getIdVisites() == lv.getFkVisite()) {
                        resultat.add(v.getTitre());
                    }
                }
            }
        }

        return resultat;
    }

    /**
     * Getter de stockPays
     * 
     * @return List<Pays> return the stockPays
     */
    public List<Pays> getStockPays() {
        return stockPays;
    }

    /**
     * Setter de stockPays
     * 
     * @param stockPays the stockPays to set
     */
    public void setStockPays(List<Pays> stockPays) {
        this.stockPays = stockPays;
    }

    /**
     * Getter de stockVille
     * 
     * @return List<Ville> return the stockVille
     */
    public List<Ville> getStockVille() {
        return stockVille;
    }

    /**
     * Setter de stockVille
     * 
     * @param stockVille the stockVille to set
     */
    public void setStockVille(List<Ville> stockVille) {
        this.stockVille = stockVille;
    }

    /**
     * Getter de stockVoyage
     * 
     * @return List<Voyage> return the stockVoyage
     */
    public List<Voyage> getStockVoyage() {
        return stockVoyage;
    }

    /**
     * Setter de stockVoyage
     * 
     * @param stockVoyage the stockVoyage to set
     */
    public void setStockVoyage(List<Voyage> stockVoyage) {
        this.stockVoyage = stockVoyage;
    }

    /**
     * Getter de stockTarif
     * 
     * @return List<Tarif> return the stockTarif
     */
    public List<Tarif> getStockTarif() {
        return stockTarif;
    }

    /**
     * Renvoie les tarifs associés à un voyage
     * 
     * @param idVoyage Voyage associé
     * @return Une liste des tarifs associés
     */
    public List<Tarif> getStockTarifVoyage(int idVoyage) {
        List<Tarif> stockTarifVoyage = new ArrayList<Tarif>();

        for (Tarif aVerif : stockTarif) {
            if (aVerif.getVoyage() == idVoyage) {
                stockTarifVoyage.add(aVerif);
            }
        }

        return stockTarifVoyage;
    }

    /**
     * getter de stockHotel
     * 
     * @return List<Hotel> return the stockHotel
     */
    public List<Hotel> getStockHotel() {
        return stockHotel;
    }

    /**
     * Setter destockHotel
     * 
     * @param stockHotel the stockHotel to set
     */
    public void setStockHotel(List<Hotel> stockHotel) {
        this.stockHotel = stockHotel;
    }

    /**
     * Getter de stockVisite
     * 
     * @return List<Visites> return the stockVisites
     */
    public List<Visite> getStockVisite() {
        return stockVisite;
    }

    /**
     * Setter de stockVisite
     * 
     * @param stockVisite the stockVisite to set
     */
    public void setStockVisite(List<Visite> stockVisite) {
        this.stockVisite = stockVisite;
    }

    /**
     * Getter de stockLiaisonPays
     * 
     * @return List<LiaisonPays> return the stockLiaisonPays
     */
    public List<LiaisonPays> getStockLiaisonPays() {
        return stockLiaisonPays;
    }

    /**
     * Setter de stockLiaisonPays
     * 
     * @param stockLiaisonPays the stockLiaisonPays to set
     */
    public void setStockLiaisonPays(List<LiaisonPays> stockLiaisonPays) {
        this.stockLiaisonPays = stockLiaisonPays;
    }

    /**
     * Getter de stockLiaisonHotel
     * 
     * @return List<LiaisonHotel> return the stockLiaisonHotel
     */
    public List<LiaisonHotel> getStockLiaisonHotel() {
        return stockLiaisonHotel;
    }

    /**
     * Setter de stockLiaisonHotel
     * 
     * @param stockLiaisonHotel the stockLiaisonHotel to set
     */
    public void setStockLiaisonHotel(List<LiaisonHotel> stockLiaisonHotel) {
        this.stockLiaisonHotel = stockLiaisonHotel;
    }

    /**
     * Getter de stockLiaisonVisite
     * 
     * @return List<LiaisonVisite> return the stockLiaisonVisite
     */
    public List<LiaisonVisite> getStockLiaisonVisite() {
        return stockLiaisonVisite;
    }

    /**
     * Setter de stockLiaisonVisite
     * 
     * @param stockLiaisonVisite the stockLiaisonVisite to set
     */
    public void setStockLiaisonVisite(List<LiaisonVisite> stockLiaisonVisite) {
        this.stockLiaisonVisite = stockLiaisonVisite;
    }

    /**
     * Setter de nbGroupeMax
     * 
     * @param nbGroupeMax the nbGroupeMax to set
     */
    public void setNbGroupeMax(int nbGroupeMax) {
        this.nbGroupeMax = nbGroupeMax;
    }

    /**
     * Getter de nbGroupeMax
     * 
     * @return List<Tarif> return the stockTarif
     */
    public int getNbGroupeMax() {
        return nbGroupeMax;
    }

    /**
     * Permet de reduire de 1 le nombre de groupes existants
     * tant que celui n'est pas inférieur ou égal à 0
     */
    public void reduireNbGroupe() {
        if (this.nbGroupeMax >= 0) {
            this.nbGroupeMax--;
        }
    }

    /**
     * Permet d'augmenter de un le nombre de groupes existants
     * tant que celui ci n'est pas superieur ou égal à 10
     */
    public void monterNbGroupe() {
        if (this.nbGroupeMax < 10) {
            this.nbGroupeMax++;
        }
    }

    /**
     * Setter de stockTarif
     * 
     * @param stockTarif the stockTarif to set
     */
    public void setStockTarif(List<Tarif> stockTarif) {
        this.stockTarif = stockTarif;
    }

    /**
     * Permet d'afficher les pays stockés
     */
    public void afficherStockPays() {
        for (Pays afficher : stockPays) {
            System.out.println(afficher.toString());
        }
    }

    /**
     * Permet d'afficher les villes stockées
     */
    public void afficherStockVille() {
        for (Ville afficher : stockVille) {
            System.out.println(afficher.toString());
        }
    }

    /**
     * Permet d'afficher les voyages stockés
     */
    public void afficherStockVoyage() {
        for (Voyage afficher : stockVoyage) {
            System.out.println(afficher.toString());
        }
    }

    /**
     * Permet d'afficher les tarifs stockés
     */
    public void afficherStockTarif() {
        for (Tarif afficher : stockTarif) {
            System.out.println(afficher.toString());
        }
    }

    /**
     * Permet d'afficher les liaisonPays stockés
     */
    public void afficherStockLiaisonPays() {
        for (LiaisonPays afficher : stockLiaisonPays) {
            System.out.println(afficher.toString());
        }
    }

    /**
     * Permet d'afficher les liaisonHotel stockés
     */
    public void afficherStockLiaisonHotel() {
        for (LiaisonHotel afficher : stockLiaisonHotel) {
            System.out.println(afficher.toString());
        }
    }

    /**
     * Permet d'afficher les liaisonVisite stockés
     */
    public void afficherStockLiaisonVisite() {
        for (LiaisonVisite afficher : stockLiaisonVisite) {
            System.out.println(afficher.toString());
        }
    }

    /**
     * Permet d'afficher le nb de groupe existant
     */
    public void afficherNbGroupeMax() {
        System.out.println(nbGroupeMax);
    }

    /**
     * Singleton
     * Permet de renvoyer l'instance de StockDonnee,
     * si elle n'existe pas, celle-ci est créée
     * 
     * @return instance de StockDonnee
     */
    public static StockDonnee getInstance() {
        if (instance == null) {
            instance = new StockDonnee(PaysDAO.readTable(), VilleDAO.readTable(),
                    VoyageDAO.readTable(), TarifDAO.readTable(),
                    HotelDAO.readTable(), VisitesDAO.readTable(),
                    LiaisonPaysDAO.readTable(), LiaisonHotelDAO.readTable(),
                    LiaisonVisiteDAO.readTable(), VilleDAO.nbGroupeMax());
        }

        return instance;
    }

}
