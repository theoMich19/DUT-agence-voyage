/*
 * AjouterVoyageController.java                                        07 nov. 2021
 */
package Voyage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Agence.StockDonnee;
import Hotel.Hotel;
import Outils.Popup;
import Pays.Pays;
import Visites.Visite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import liaisonVoyage.liaisonHotel.LiaisonHotel;
import liaisonVoyage.liaisonHotel.LiaisonHotelDAO;
import liaisonVoyage.liaisonPays.LiaisonPays;
import liaisonVoyage.liaisonPays.LiaisonPaysDAO;
import liaisonVoyage.liaisonVisite.LiaisonVisite;
import liaisonVoyage.liaisonVisite.LiaisonVisiteDAO;

/**
 * Gestion de l'interface des voyages avec l'utilisateur. Celui-ci peut ajouter
 * un voyage, en saisissant sa désignation, la ville de destination et la ville
 * de retour. Pour le pays, il aura le choix entre ceux déjà saisis dans la base
 * de données. Pour les transsports aller et retour il aura le choix entre
 * avion/train/car.
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class ModifierVoyageController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    StockDonnee stock = StockDonnee.getInstance();

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnSupprimerVoyage;

    @FXML
    private Button btnAjouterJourHotel;

    @FXML
    private ComboBox<String> cmbVoyage;

    @FXML
    private ComboBox<String> cmbHotel;

    @FXML
    private ListView<String> viewPaysModifier;

    @FXML
    private ListView<String> viewHotelModifier;

    @FXML
    private ListView<String> viewVisiteModifier;

    @FXML
    private ListView<String> viewPaysLier;

    @FXML
    private ListView<String> viewHotelLier;

    @FXML
    private ListView<String> viewVisiteLier;

    @FXML
    private TextField tfJourdebutReservation;

    @FXML
    private TextField tfJourfinReservation;

    @FXML
    private Label labelAjouter;

    @FXML
    private Label labelSupprimer;

    @FXML
    private Label labelDureeSejour;

    private int idVoyageRecup;

    private int nombrePaysLier;

    private int idHotel;

    private List<LiaisonPays> listeLiaisonPays = stock.getStockLiaisonPays();

    private List<LiaisonHotel> listeLiaisonHotel = stock.getStockLiaisonHotel();

    private List<LiaisonVisite> listeLiaisonVisite = stock.getStockLiaisonVisite();

    /**
     * Initialise le contenu de élément
     */
    public void initialize(URL url, ResourceBundle resource) {
        // initialisaion listView
        InitListViewDonnees();
        // Initialisation du combobox des voyages
        afficheCbbVoyage();
        // Initialisation du combo box pour saisir le jour a un hotel
        afficheCbbHotel(stock.chercherIdVoyage(cmbVoyage.getSelectionModel().getSelectedItem()));
        tfNonEditable();
    }

    /**
     * Méthode qui permet d'initialiser la combobox de voyage
     */
    public void afficheCbbVoyage() {
        List<String> listVoyage = new ArrayList<>();
        for (Voyage newVoyage : stock.getStockVoyage()) {
            listVoyage.add(newVoyage.getDesignation());
        }
        ObservableList<String> listNomVoyage = FXCollections.observableList(listVoyage);
        cmbVoyage.setItems(listNomVoyage);
        idVoyageRecup = stock.chercherIdVoyage(cmbVoyage.getSelectionModel().getSelectedItem());
        if (idVoyageRecup == -1) {
            affichageLabelDefaut();
        }
    }

    /**
     * Méthode qui permet d'initialiser la combobox des hotels lié au voyage
     * 
     * @param idVoyage id du voyage
     */
    public void afficheCbbHotel(int idVoyage) {
        boolean hotelExiste = false;
        List<String> listHotel = new ArrayList<>();
        for (LiaisonHotel liaison : stock.getStockLiaisonHotel()) {
            if (idVoyage == liaison.getFkVoyage()) {
                // Trouve les objets Hotel correspondant à l'id de l'hôtel
                for (Hotel hotel : stock.getStockHotel()) {
                    if (hotel.getIdHotel() == liaison.getFkHotel()) {
                        listHotel.add(hotel.getNomHotel());
                        hotelExiste = true;
                    }
                }
            }
        }
        ObservableList<String> listNomHotel = FXCollections.observableList(listHotel);
        if (idVoyage < 1) {
            listHotel.add("Aucun voyage selectionné");
        } else if (listHotel.isEmpty()) {
            listHotel.add("Aucun hôtel n'est lié à ce voyage");
        }
        cmbHotel.setItems(listNomHotel);
        if (!hotelExiste)
            cmbHotel.getSelectionModel().selectFirst();
    }

    /**
     * Initialisation des listView qui possède les données (stock)
     * 1er initialisation au lancemelnt de la page
     */
    public void InitListViewDonnees() {
        /* Initialisation du listview des pays de destination */
        List<String> listPaysModifier = new ArrayList<>();
        for (Pays newPays : stock.getStockPays()) {
            listPaysModifier.add(newPays.getNomPays());
        }
        ObservableList<String> listNomPaysModifier = FXCollections.observableList(listPaysModifier);
        viewPaysModifier.setItems(listNomPaysModifier);
        viewPaysModifier.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        /* Initialisation du listview des hotels */
        List<String> listHotelModifier = new ArrayList<>();
        for (Hotel hotel : stock.getStockHotel()) {
            listHotelModifier.add(hotel.getNomHotel());
        }
        ObservableList<String> listNomHotelModifier = FXCollections.observableList(listHotelModifier);
        viewHotelModifier.setItems(listNomHotelModifier);
        viewHotelModifier.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        /* Initialisation du listview des visites */
        List<String> listVisiteModifier = new ArrayList<>();
        for (Visite newVisite : stock.getStockVisite()) {
            listVisiteModifier.add(newVisite.getTitre());
        }
        ObservableList<String> listNomVisiteModifier = FXCollections.observableList(listVisiteModifier);
        viewVisiteModifier.setItems(listNomVisiteModifier);
        viewVisiteModifier.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * méthode qui permet de mettre à jour les listView où les éléments ne sont pas
     * liée au voyage mais appartient à la bd
     * 
     * @param idVoyage id du voyage
     */
    public void majListViewDonnees(int idVoyage) {
        // Initialisation du listview des pays de destination
        List<String> listPaysModifier = new ArrayList<>();
        for (Pays pays : stock.getStockPays()) { // boucle sur tous pays
            listPaysModifier.add(pays.getNomPays());
            for (LiaisonPays lpays : stock.getStockLiaisonPays()) {
                if (pays.getIdPays() == lpays.getFkPays() && lpays.getFkVoyage() == idVoyage) {
                    listPaysModifier.remove(listPaysModifier.size() - 1);
                }
            }
        }
        ObservableList<String> listNomPaysModifier = FXCollections.observableList(listPaysModifier);
        viewPaysModifier.setItems(listNomPaysModifier);
        viewPaysModifier.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Initialisation du listview des hotels
        List<String> listHotelModifier = new ArrayList<>();
        for (Hotel hotel : stock.getStockHotel()) {
            listHotelModifier.add(hotel.getNomHotel());
            for (LiaisonHotel lHotel : stock.getStockLiaisonHotel()) {
                if (hotel.getIdHotel() == lHotel.getFkHotel() && lHotel.getFkVoyage() == idVoyage) {
                    listHotelModifier.remove(listHotelModifier.size() - 1);
                }
            }
        }
        ObservableList<String> listNomHotelModifier = FXCollections.observableList(listHotelModifier);
        viewHotelModifier.setItems(listNomHotelModifier);
        viewHotelModifier.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Initialisation du listview des visites
        List<String> listVisiteModifier = new ArrayList<>();
        for (Visite visite : stock.getStockVisite()) {
            listVisiteModifier.add(visite.getTitre());
            for (LiaisonVisite lVisite : stock.getStockLiaisonVisite()) {
                if (visite.getIdVisites() == lVisite.getFkVisite() && lVisite.getFkVoyage() == idVoyage) {
                    listVisiteModifier.remove(listVisiteModifier.size() - 1);
                }
            }
        }
        ObservableList<String> listNomVisiteModifier = FXCollections.observableList(listVisiteModifier);
        viewVisiteModifier.setItems(listNomVisiteModifier);
        viewVisiteModifier.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Méthode qui permet de mettre à jour la listView lier au voyage à partir de
     * son id
     * 
     * @param idVoyage id du voyage
     */
    public void majListViewVoyage(int idVoyage) {
        // Trouve l'id des pays correspondant associés à l'id du voyage
        List<String> listPaysLier = new ArrayList<>();
        for (LiaisonPays liaison : stock.getStockLiaisonPays()) {
            if (idVoyage == liaison.getFkVoyage()) {
                // Trouve les objets Pays correspondant à l'id du pays
                for (Pays pays : stock.getStockPays()) {
                    if (pays.getIdPays() == liaison.getFkPays()) {
                        listPaysLier.add(pays.getNomPays());
                    }
                }
            }
        }
        nombrePaysLier = listPaysLier.size();
        ObservableList<String> listNomPaysLier = FXCollections.observableList(listPaysLier);
        viewPaysLier.setItems(listNomPaysLier);
        viewPaysLier.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Trouve l'id des hôtels correspondant associés à l'id du voyage
        List<String> listHotelLier = new ArrayList<>();
        for (LiaisonHotel liaison : stock.getStockLiaisonHotel()) {
            if (idVoyage == liaison.getFkVoyage()) {
                // Trouve les objets Hotel correspondant à l'id de l'hôtel
                for (Hotel hotel : stock.getStockHotel()) {
                    if (hotel.getIdHotel() == liaison.getFkHotel()) {
                        listHotelLier.add(hotel.getNomHotel());
                    }
                }
            }
        }
        ObservableList<String> listNomHotelLier = FXCollections.observableList(listHotelLier);
        viewHotelLier.setItems(listNomHotelLier);
        viewHotelLier.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        if (listNomHotelLier.size() >= 1) {
            tfEditable();
        } else {
            tfNonEditable();
        }

        // Trouve l'id des visites correspondant associées à l'id du voyage
        List<String> listVisiteLier = new ArrayList<>();
        for (LiaisonVisite liaison : stock.getStockLiaisonVisite()) {
            if (idVoyage == liaison.getFkVoyage()) {
                // Trouve les objets Visite correspondant à l'id de la visite
                for (Visite visite : stock.getStockVisite()) {
                    if (visite.getIdVisites() == liaison.getFkVisite()) {
                        listVisiteLier.add(visite.getTitre());
                    }
                }
            }
        }
        ObservableList<String> listNomVisiteLier = FXCollections.observableList(listVisiteLier);
        viewVisiteLier.setItems(listNomVisiteLier);
        viewVisiteLier.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Méthode ajout Pays à l'objet stock
     * => création de la liaison entre le pays et le voyage
     * 
     * @param idVoyage id du voyage
     * @param idPays   id du pays
     */
    public void ajoutPays(int idVoyage, int idPays) {
        LiaisonPays liaisonPays = new LiaisonPays(idVoyage, idPays);
        LiaisonPaysDAO.create(liaisonPays);
        liaisonPays.setIdLiaisonPays(LiaisonPaysDAO.recupId(liaisonPays));
        listeLiaisonPays.add(liaisonPays);
        stock.setStockLiaisonPays(listeLiaisonPays);
    }

    /**
     * Méthode ajout Hotel à l'objet stock
     * => création de la liaison entre l'hotel et le voyage
     * 
     * @param idVoyage id du voyage
     * @param idHotel  id de l'hotel
     */
    public void ajoutHotel(int idVoyage, int idHotel) {
        LiaisonHotel liaisonHotel = new LiaisonHotel(idVoyage, idHotel);
        LiaisonHotelDAO.create(liaisonHotel);
        liaisonHotel.setIdLiaisonHotel(LiaisonHotelDAO.recupId(liaisonHotel));
        listeLiaisonHotel.add(liaisonHotel);
        stock.setStockLiaisonHotel(listeLiaisonHotel);
    }

    /**
     * Méthode ajout Visite à l'objet stock
     * => création de la liaison entre le pays et le voyage
     * 
     * @param idVoyage id du voyage
     * @param idVisite id de la visite
     */
    public void ajoutVisite(int idVoyage, int idVisite) {
        LiaisonVisite liaisonVisite = new LiaisonVisite(idVoyage, idVisite);
        LiaisonVisiteDAO.create(liaisonVisite);
        liaisonVisite.setIdLiaisonVisite(LiaisonVisiteDAO.recupId(liaisonVisite));
        listeLiaisonVisite.add(liaisonVisite);
        stock.setStockLiaisonVisite(listeLiaisonVisite);
    }

    @FXML
    /**
     * Méthode qui permet d'ajouter un ou plusieur élémént à un voyage par liaison
     * 
     * @param event
     */
    void btnAjouterClicked(ActionEvent event) {
        final String MESS_ERR_IDVOYAGE = "Vous n'avez pas selectionné de voyage.";
        final String MESS_ERR_NO_SELECT = "Aucun élémént sélectionné.";
        final String MESS_ALL_AJOUT = "Tous les éléments ont bien été ajoutés";

        // Arrête la méthode si aucune ligne n'a été sélectionnée ou pas de voyage
        // sélectionnée
        if (idVoyageRecup == -1) {
            Popup.popupErreur(MESS_ERR_IDVOYAGE);
            return;
        }
        if (viewPaysLier.getSelectionModel().getSelectedItems() == null
                && viewHotelLier.getSelectionModel().getSelectedItems() == null
                && viewVisiteLier.getSelectionModel().getSelectedItems() == null) {
            Popup.popupErreur(MESS_ERR_NO_SELECT);
            return;
        }

        boolean correct = true; // des éléments ont été ajoutés
        boolean probleme = true; // des éléménts n'ont pas pu être ajoutés
        StringBuilder messFinal = new StringBuilder();
        messFinal.append("Les éléments suivant sont déjà liés au voyage sélectionné :\n");
        ObservableList<String> selectedItemsPays = viewPaysModifier.getSelectionModel().getSelectedItems();
        ObservableList<String> selectedItemsHotel = viewHotelModifier.getSelectionModel().getSelectedItems();
        ObservableList<String> selectedItemsVisite = viewVisiteModifier.getSelectionModel().getSelectedItems();
        String nomVoyage = cmbVoyage.getSelectionModel().getSelectedItem();
        idVoyageRecup = stock.chercherIdVoyage(nomVoyage);
        int idPays = -1;
        int idHotel = -1;
        int idVisite = -1;

        /* pays */
        for (String element : selectedItemsPays) {
            for (Pays pays : stock.getStockPays()) {
                if (pays.getNomPays().equals(element)) {
                    idPays = pays.getIdPays();
                }
            }
            if (!recherchePays(idPays, idVoyageRecup)) {
                ajoutPays(idVoyageRecup, idPays);
                correct = false;
            } else {
                probleme = false;
                messFinal.append("- " + element + "\n");
            }
        }
        /* hotel */
        for (String element : selectedItemsHotel) {
            for (Hotel hotel : stock.getStockHotel()) {
                if (hotel.getNomHotel().equals(element)) {
                    idHotel = hotel.getIdHotel();
                }
            }
            if (!rechercheHotels(idHotel, idVoyageRecup)) {
                ajoutHotel(idVoyageRecup, idHotel);
                correct = false;
            } else {
                probleme = false;
                messFinal.append("- " + element + "\n");
            }
        }
        /* visite */
        for (String element : selectedItemsVisite) {
            for (Visite visite : stock.getStockVisite()) {
                if (visite.getTitre().equals(element)) {
                    idVisite = visite.getIdVisites();
                }
            }
            if (!rechercheVisites(idVisite, idVoyageRecup)) {
                ajoutVisite(idVoyageRecup, idVisite);
                correct = false;
            } else {
                probleme = false;
                messFinal.append("- " + element + "\n");
            }
        }
        if (!correct && probleme)
            Popup.popupInformation(MESS_ALL_AJOUT);
        if (!probleme) {
            Popup.popupErreur(messFinal.toString());
        }
        /* mise à jours des listView */
        majListViewVoyage(idVoyageRecup);
        majListViewDonnees(idVoyageRecup);
        afficheCbbHotel(idVoyageRecup);
    }

    /**
     * méthode qui permet de savoir si il existe déjà ou non une liaison pour le
     * pays selectionner
     * 
     * @param idPays   id du pays
     * @param idVoyage id du voyage
     * @return true si la liaison existe
     */
    public boolean recherchePays(int idPays, int idVoyage) {
        for (LiaisonPays pays : stock.getStockLiaisonPays()) {
            if (pays.getFkVoyage() == idVoyage && pays.getFkPays() == idPays) {
                return true;
            }
        }
        return false;
    }

    /**
     * méthode qui indique si il existe déjà ou non une liaison pour la visite
     * selectionné
     * 
     * @param idVisite id de la visite
     * @param idVoyage id du voyage
     * @return true si la liaison existe
     */
    public boolean rechercheVisites(int idVisite, int idVoyage) {
        for (LiaisonVisite visite : stock.getStockLiaisonVisite()) {
            if (visite.getFkVoyage() == idVoyage && visite.getFkVisite() == idVisite) {
                return true;
            }
        }
        return false;
    }

    /**
     * méthode qui indique si il existe déjà ou non une liaison pour l'hotel
     * selectionnné
     * 
     * @param idHotel  id de l'hotel
     * @param idVoyage id du voyage
     * @return true si la liaison existe
     */
    public boolean rechercheHotels(int idHotel, int idVoyage) {
        for (LiaisonHotel hotel : stock.getStockLiaisonHotel()) {
            if (hotel.getFkVoyage() == idVoyage && hotel.getFkHotel() == idHotel) {
                return true;
            }
        }
        return false;
    }

    @FXML
    /**
     * Méthode de suppressin d'un ou plusieurs élémént lié à un voyage
     * 
     * @param event
     */
    void btnSupprimerElementClicked(ActionEvent event) {
        final String MESS_ERR_IDVOYAGE = "Vous n'avez pas selectionné de voyage.";
        final String MESS_ERR_NO_SELECT = "Aucun élémént sélectionné.";

        // Arrête la méthode si aucune ligne n'a été sélectionnée ou pas de voyage
        // sélectionnée
        if (idVoyageRecup == -1) {
            Popup.popupErreur(MESS_ERR_IDVOYAGE);
            return;
        }
        if (viewPaysLier.getSelectionModel().getSelectedItems() == null
                && viewHotelLier.getSelectionModel().getSelectedItems() == null
                && viewVisiteLier.getSelectionModel().getSelectedItems() == null) {
            Popup.popupErreur(MESS_ERR_NO_SELECT);
            return;
        }
        ObservableList<String> selectedItemsVisite = viewVisiteLier.getSelectionModel().getSelectedItems();
        ObservableList<String> selectedItemsPays = viewPaysLier.getSelectionModel().getSelectedItems();
        ObservableList<String> selectedItemsHotel = viewHotelLier.getSelectionModel().getSelectedItems();
        int idPays = -1;
        int idHotel = -1;
        int idVisite = -1;

        /* pays */
        for (String element : selectedItemsPays) {
            for (Pays pays : stock.getStockPays()) {
                if (pays.getNomPays().equals(element)) {
                    idPays = pays.getIdPays();
                }
            }
            if (nombrePaysLier >= 2) {
                int action = Popup.popupActionVoyage(element);
                switch (action) {
                    case 1: // suppresion liaison pays uniquement
                        suppPaysUniquement(idPays, idVoyageRecup);
                        nombrePaysLier--;
                        break;
                    case 2:// suppression voyage
                        suppAll(idVoyageRecup);
                        nombrePaysLier = 0;
                        break;
                    default:
                        break;
                }
            } else if (nombrePaysLier == 1) {
                int action = Popup.popupActionPays(element);
                switch (action) {
                    case 1:// suppression voyage
                        suppAll(idVoyageRecup);
                        break;
                    default:
                        break;
                }
            }
        }
        /* hotel */
        for (String element : selectedItemsHotel) {
            for (Hotel hotel : stock.getStockHotel()) {
                if (hotel.getNomHotel().equals(element)) {
                    idHotel = hotel.getIdHotel();
                }
            }
            if (LiaisonHotelDAO.delete(idHotel, idVoyageRecup))
                stock.suppresionLiaisonHotel(idVoyageRecup, idHotel);
        }
        /* visite */
        for (String element : selectedItemsVisite) {
            for (Visite visite : stock.getStockVisite()) {
                if (visite.getTitre().equals(element)) {
                    idVisite = visite.getIdVisites();
                }
            }
            if (LiaisonVisiteDAO.delete(idVisite, idVoyageRecup))
                stock.suppresionLiaisonVisite(idVoyageRecup, idVisite);
        }
        /* maj des données affichées */
        int idVoyage = stock.chercherIdVoyage(cmbVoyage.getSelectionModel().getSelectedItem());
        affichageLabelDefaut();
        majListViewDonnees(idVoyage);
        majListViewVoyage(idVoyage);
        afficheCbbVoyage();/* maj des combobox */
        afficheCbbHotel(idVoyage);// actualisation de cbb hotel si voyage supprimer
    }

    /**
     * Méthode qui permet de supprimer la liaison d'un pays à un voyage
     * 
     * @param idPays    id du pays à supprimer
     * @param idVoyages id du voyage
     */
    public void suppPaysUniquement(int idPays, int idVoyage) {
        final String MESS_REUSSITE = "La suppression de la liaison pays à été réussi.";
        final String MESS_ERR = "Erreur dans la suppression";

        boolean suppReussite = false;

        if (LiaisonPaysDAO.delete(idPays, idVoyage)) {
            try {
                stock.suppresionLiaisonPays(idVoyage, idPays);
                suppReussite = true;
            } catch (Exception e) {
            }
        }

        if (suppReussite) {
            Popup.popupInformation(MESS_REUSSITE);
        } else {
            Popup.popupErreur(MESS_ERR);
        }
    }

    /**
     * Méthode qui supprime le voyage
     * 
     * @param idVoyage id du voyage
     */
    public void suppAll(int idVoyage) {
        final String MESS_REUSSITE = "La suppression du voyage et de ces liaisons à été réussi.";
        final String MESS_ERR = "Erreur dans la suppression";
        boolean suppReussite = false;
        if (VoyageDAO.delete(idVoyage)) {
            try {
                stock.suppresionLiaisonParVoyage(idVoyage);
                stock.suppresionVoyage(idVoyage);
                suppReussite = true;
            } catch (Exception e) {
            }
        }
        if (suppReussite) {
            Popup.popupInformation(MESS_REUSSITE);
            labelAjouter.setText("Élement à ajouter au voyage");
            labelSupprimer.setText("Aucun voyage selectionné");
            labelDureeSejour.setText("");
        } else {
            Popup.popupErreur(MESS_ERR);
        }
    }

    /**
     * Méthode qui permet de mettre à defaut les label
     */
    public void affichageLabelDefaut() {
        labelSupprimer.setText("Aucun voyage selectionné");
        labelAjouter.setText("Élement à ajouter au voyage");
        labelDureeSejour.setText("");
    }

    @FXML
    void cbbVoyageClicked(ActionEvent event) {
        final String MESS_HOTEL = "Saisissez le jour du voyage pour lequel la première nuit est prévue.";
        final String MESS_NO_HOTEL = "Ce voyage ne comporte aucun hôtel.";
        idVoyageRecup = stock.chercherIdVoyage(cmbVoyage.getSelectionModel().getSelectedItem());
        int dureeSejour = stock.nbJourVoyage(idVoyageRecup);
        labelAjouter.setText("Élement à ajouter sur \"" + cmbVoyage.getSelectionModel().getSelectedItem() + "\"");
        labelSupprimer.setText("Élement à supprimer sur \" " + cmbVoyage.getSelectionModel().getSelectedItem() + "\"");

        boolean hotelExiste = false;
        for (LiaisonHotel liaison : stock.getStockLiaisonHotel()) {
            if (idVoyageRecup == liaison.getFkVoyage()) {
                for (Hotel hotel : stock.getStockHotel()) {
                    if (hotel.getIdHotel() == liaison.getFkHotel()) {
                        hotelExiste = true;
                    }
                }
            }
        }
        if (hotelExiste) {
            labelDureeSejour.setText(MESS_HOTEL + "\nLa durée du voyage est de " + dureeSejour + " jours.");
            tfEditable();
        } else {
            labelDureeSejour.setText(MESS_NO_HOTEL + "\nLa durée du voyage est de " + dureeSejour + " jours.");
            tfNonEditable();
        }
        majListViewVoyage(idVoyageRecup);
        majListViewDonnees(idVoyageRecup);
        afficheCbbHotel(idVoyageRecup);
    }

    @FXML
    void cbbHotelClicked(ActionEvent event) {
        idHotel = stock.chercherIdHotel(cmbHotel.getSelectionModel().getSelectedItem());
        idVoyageRecup = stock.chercherIdVoyage(cmbVoyage.getSelectionModel().getSelectedItem());
        tfJourdebutReservation.setPromptText("");
        tfJourfinReservation.setPromptText("");
        if (LiaisonHotelDAO.recupJourDebut(idVoyageRecup, idHotel) != -1
                && LiaisonHotelDAO.recupJourFin(idVoyageRecup, idHotel) != -1) {
            affichagePromptTextDebut(idVoyageRecup, idHotel);
            affichagePromptTextFin(idVoyageRecup, idHotel);
        }
    }

    @FXML
    /**
     * Méthode qui permet d'ajouter le jour à un hotel lié au voyage
     * 
     * @param event
     */
    void btnAjouterJourHotelClicked(ActionEvent event) {
        // mess erreur
        final String MESS_ERR = "La valeur saisie est incorrecte. Elle doit être comprise entre 1 et ";
        final String MESS_MEME_JOUR = "Vous ne pouvez pas saisir le dernier jour du voyage car ce dernier correspond au trajet du retour.";
        final String MESS_ERR_VALUE = "Vous devez saisir un nombre entier positif.";
        final String MESS_ERR_IDVOYAGE = "Vous n'avez pas selectionné de voyage.";
        final String MESS_ERR_IDHOTEL = "Aucun Hôtel n'est affecté à ce voyage ou aucun n'a été choisi.";
        final String MESS_ERR_NO_SAISIE = "Vous n'avez pas saisie de valeurs.";
        final String MESS_ERR_MAJ = "Erreur dans la mise à jour.";
        final String MESS_AJOUT_BD = "L'hôtel vient d'être ajouté. Vous devez donc saisir la période complète.";
        final String MESS_SUP_INF = "La date de début est après la date de fin.";
        // mess succes
        final String MESS_SUCCES_DEBUT = "Mise à jour réussie du premier jour de la réservation.";
        final String MESS_SUCCES_FIN = "Mise à jour réussie du dernier jour de la réservation.";
        final String MESS_SUCCES_DOUBLE = "Mise à jour réussie du premier et du dernier jour de la réservation.";
        idVoyageRecup = stock.chercherIdVoyage(cmbVoyage.getSelectionModel().getSelectedItem());
        idHotel = stock.chercherIdHotel(cmbHotel.getSelectionModel().getSelectedItem());
        String debutReservation = tfJourdebutReservation.getText();
        String finReservation = tfJourfinReservation.getText();
        int jourDebut, jourFin;
        int jourMax = stock.nbJourVoyage(idVoyageRecup);
        int action = 0;
        int codeErreur = 0;

        // Arrête la méthode si aucune ligne n'a été sélectionnée ou pas de voyage
        // sélectionnée
        if (idVoyageRecup == -1) {
            Popup.popupErreur(MESS_ERR_IDVOYAGE);
            return;
        } else if (idHotel == -1) {
            Popup.popupErreur(MESS_ERR_IDHOTEL);
            return;
        }

        // gestion saisie
        if (debutReservation.equals("") && finReservation.equals("")) {
            Popup.popupErreur(MESS_ERR_NO_SAISIE);
            return;
        } else if (!debutReservation.equals("") && !finReservation.equals("")) {
            action = 3;
        } else if (!debutReservation.equals("")) {
            action = 1;
        } else if (!finReservation.equals("")) {
            action = 2;
        }

        // Modification appliquée selon la saisie
        switch (action) {
            case 1:
                if (debutReservation.matches("[0-9]*")) {
                    jourDebut = Integer.parseInt(debutReservation);
                    jourFin = LiaisonHotelDAO.recupJourFin(idVoyageRecup, idHotel);
                    if (jourFin == -1) {
                        codeErreur = 4; // errCoder 4
                        break;
                    }
                    if (jourDebut > 0 && jourDebut < jourMax && jourDebut < jourFin) {
                        if (LiaisonHotelDAO.updatejourDebut(jourDebut, idVoyageRecup, idHotel)) {
                            Popup.popupInformation(MESS_SUCCES_DEBUT);
                            affichagePromptTextDebut(idVoyageRecup, idHotel);
                        }
                    } else if (jourDebut == jourMax) {
                        codeErreur = 3;// errCode3
                    } else {
                        codeErreur = 2;// errCode2
                    }
                    tfJourdebutReservation.setText("");
                    tfJourfinReservation.setText("");
                } else {
                    codeErreur = 1; // errCode1
                }
                break;
            case 2:
                if (finReservation.matches("[0-9]*")) {
                    jourFin = Integer.parseInt(finReservation);
                    jourDebut = LiaisonHotelDAO.recupJourDebut(idVoyageRecup, idHotel);
                    if (jourDebut == -1) {
                        codeErreur = 4; // errCode4
                        break;
                    }
                    if (jourFin > 0 && jourFin < jourMax && jourFin > jourDebut) {
                        if (LiaisonHotelDAO.updateJourFin(jourFin, idVoyageRecup, idHotel)) {
                            Popup.popupInformation(MESS_SUCCES_FIN);
                            affichagePromptTextFin(idVoyageRecup, idHotel);
                        }
                    } else if (jourFin == jourMax) {
                        codeErreur = 3;// errCode3
                    } else {
                        codeErreur = 2;// errCode2
                    }
                    tfJourdebutReservation.setText("");
                    tfJourfinReservation.setText("");
                } else {
                    codeErreur = 1; // errCode1
                }
                break;
            case 3:
                if (debutReservation.matches("[0-9]*") && finReservation.matches("[0-9]*")) {
                    jourDebut = Integer.parseInt(debutReservation);
                    jourFin = Integer.parseInt(finReservation);

                    if (jourDebut > 0 && jourFin > 0
                            && jourDebut < jourMax && jourFin < jourMax
                            && jourDebut < jourFin) {
                        if (LiaisonHotelDAO.updatejourDebut(jourDebut, idVoyageRecup, idHotel)
                                && LiaisonHotelDAO.updateJourFin(jourFin, idVoyageRecup, idHotel)) {
                            Popup.popupInformation(MESS_SUCCES_DOUBLE);
                            affichagePromptTextDebut(idVoyageRecup, idHotel);
                            affichagePromptTextFin(idVoyageRecup, idHotel);
                        } else {
                            Popup.popupErreur(MESS_ERR_MAJ);
                        }

                    } else if (jourDebut == jourMax) {
                        codeErreur = 3;// errCode3
                    } else if (jourDebut > jourFin) {
                        codeErreur = 5; // errCode5

                    } else {
                        codeErreur = 2;// errCode2
                    }
                    tfJourdebutReservation.setText("");
                    tfJourfinReservation.setText("");
                } else {
                    codeErreur = 1; // errCode1
                }
                break;
            default:
                break;
        }
        // code d'erreur
        switch (codeErreur) {
            case 1:
                Popup.popupErreur(MESS_ERR_VALUE);
                break;
            case 2:
                Popup.popupErreur(MESS_ERR + (jourMax - 1) + " inclus.");
                break;
            case 3:
                Popup.popupErreur(MESS_MEME_JOUR);
                break;
            case 4:
                Popup.popupErreur(MESS_AJOUT_BD);
                break;
            case 5:
                Popup.popupErreur(MESS_SUP_INF);
                break;
        }
    }

    /**
     * méthode d'affichage dans les TextField des dates de reservations d'un
     * hotel pour le jour de debut
     * 
     * @param idVoyage id du voyage
     * @param idHotel  id de l'hôtel
     */
    public void affichagePromptTextDebut(int idVoyage, int idHotel) {
        if (LiaisonHotelDAO.recupJourDebut(idVoyage, idHotel) != -1) {
            int nbJour = LiaisonHotelDAO.recupJourDebut(idVoyage, idHotel);
            tfJourdebutReservation.setPromptText("jour " + Integer.toString(nbJour));
        }
    }

    /**
     * méthode d'affichage dans les TextField des dates de reservations d'un
     * hotel pour le jour de fin
     * 
     * @param idVoyage id du voyage
     * @param idHotel  id de l'hôtel
     */
    public void affichagePromptTextFin(int idVoyage, int idHotel) {
        if (LiaisonHotelDAO.recupJourFin(idVoyage, idHotel) != -1) {
            int nbJour = LiaisonHotelDAO.recupJourFin(idVoyage, idHotel);
            tfJourfinReservation.setPromptText("jour " + Integer.toString(nbJour));
        }
    }

    /**
     * méthode qui pertmet de passer les TextField en non editable
     */
    public void tfNonEditable() {
        tfJourdebutReservation.setEditable(false);
        tfJourfinReservation.setEditable(false);
    }

    /**
     * méthode qui pertmet de passer les TextField en non editable
     */
    public void tfEditable() {
        tfJourdebutReservation.setEditable(true);
        tfJourfinReservation.setEditable(true);
    }

    @FXML
    void btnEditerClicked(ActionEvent event) throws IOException {
        openNewScene(event, Outils.Constantes.LIEN_MAIN);
    }

    @FXML
    void btnVoyagesClicked(ActionEvent event) throws IOException {
        openNewScene(event, Outils.Constantes.LIEN_AJOUT_VOYAGE);
    }

    @FXML
    void btnExporterClicked(ActionEvent event) throws IOException {
        openNewScene(event, Outils.Constantes.LIEN_EXPORTER);
    }

    @FXML
    void btnRechercheClicked(ActionEvent event) throws IOException {
        openNewScene(event, Outils.Constantes.LIEN_RECHERCHE);
    }

    @FXML
    void btnAideClicked(ActionEvent event) throws IOException {
        openNewScene(event, Outils.Constantes.LIEN_AIDE);
    }

    @FXML
    void btnQuitterClicked(ActionEvent event) throws IOException {
        if (Popup.confirmationQuitter(event, Outils.Constantes.CONFIRMATION_QUITTER) == -1) {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Ouvre une nouvelle scene grâce à l'event et l'emplacement du fichier fxml à
     * ouvrir passé en paramètre.
     * 
     * @param event
     * @param emplacementFichier emplacement du fichier
     * @throws IOException
     */
    void openNewScene(ActionEvent event, String emplacementFichier) throws IOException {
        root = FXMLLoader.load(getClass().getResource(emplacementFichier));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}