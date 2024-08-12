/*
 * AjouterVoyageController.java                                        07 nov. 2021
 */
package Voyage;

import static Tarif.Tarif.isInteger;
import static Tarif.Tarif.taille;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import Agence.StockDonnee;
import Hotel.Hotel;
import Outils.Popup;
import Pays.Pays;
import Ville.Ville;
import Visites.Visite;

import static Voyage.Voyage.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import liaisonVoyage.liaisonHotel.LiaisonHotel;
import liaisonVoyage.liaisonHotel.LiaisonHotelDAO;
import liaisonVoyage.liaisonPays.LiaisonPays;
import liaisonVoyage.liaisonPays.LiaisonPaysDAO;
import liaisonVoyage.liaisonVisite.LiaisonVisite;
import liaisonVoyage.liaisonVisite.LiaisonVisiteDAO;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * Gestion de l'interface des voyages avec l'utilisateur Celui-ci peut ajouter
 * un voyage, en saisissant sa désignation, le ville de destination et la ville
 * de retour. Pour le pays il aura le choix entre ceux déjà saisis dans la base
 * de données. Pour les transsports aller et retour il aura le choix entre
 * avion/train/car.
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class AjouterVoyageController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    StockDonnee stock = StockDonnee.getInstance();

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnSupprimerVoyage;

    @FXML
    private ComboBox<String> cmbTypeTranspAller;

    @FXML
    private ComboBox<String> cmbTypeTransportRet;

    @FXML
    private ListView<String> viewPays;

    @FXML
    private ListView<String> viewHotel;

    @FXML
    private ListView<String> viewVisite;

    @FXML
    private TableView<Voyage> tbvVoyages;

    @FXML
    private TableColumn<Voyage, String> voyage;

    @FXML
    private TableColumn<Voyage, String> transportAller;

    @FXML
    private TableColumn<Voyage, String> transportRetour;

    @FXML
    private TableColumn<Voyage, String> villeDestination;

    @FXML
    private TableColumn<Voyage, String> villeRetour;

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfVilleDest;

    @FXML
    private TextField tfVilleRetour;

    @FXML
    private TextField tfNbJour;

    private List<Voyage> listeVoyage = stock.getStockVoyage();

    private List<LiaisonPays> listeLiaisonPays = stock.getStockLiaisonPays();

    private List<LiaisonHotel> listeLiaisonHotel = stock.getStockLiaisonHotel();

    private List<LiaisonVisite> listeLiaisonVisite = stock.getStockLiaisonVisite();

    /**
     * Initialise le contenu de chaque combobox
     */
    public void initialize(URL url, ResourceBundle resource) {
        // initialisation des listView
        majListViewDonnees();
        // Initialisation du combobox des types transports allé et retour
        List<String> transports = new ArrayList<>(Arrays.asList("avion", "car", "train"));
        initCbb(transports);
        // Liste des voyages à afficher
        ObservableList<Voyage> listeVoyages = FXCollections.observableArrayList(stock.getStockVoyage());
        inittbview(transports, listeVoyages);

    }

    /**
     * Affiche des données dans la listView
     */
    public void majListViewDonnees() {
        // Initialisation du listview des pays de destination
        List<String> listPays = new ArrayList<>();
        for (Pays pays : stock.getStockPays()) {
            listPays.add(pays.getNomPays());
        }
        ObservableList<String> listNomPays = FXCollections.observableList(listPays);
        viewPays.setItems(listNomPays);
        viewPays.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Initialisation du listview des hotels
        List<String> listHotel = new ArrayList<>();
        for (Hotel hotel : stock.getStockHotel()) {
            listHotel.add(hotel.getNomHotel());
        }
        ObservableList<String> listNomHotel = FXCollections.observableList(listHotel);
        viewHotel.setItems(listNomHotel);
        viewHotel.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Initialisation du listview des visites
        List<String> listVisite = new ArrayList<>();
        for (Visite visite : stock.getStockVisite()) {
            listVisite.add(visite.getTitre());
        }
        ObservableList<String> listNomVisite = FXCollections.observableList(listVisite);
        viewVisite.setItems(listNomVisite);
        viewVisite.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * initialisation combovox des transport
     * 
     * @param transports list des transport
     */
    public void initCbb(List<String> transports) {
        List<String> listTranspAlle = new ArrayList<>();
        List<String> listTranspRetour = new ArrayList<>();

        listTranspAlle.addAll(transports);
        listTranspRetour.addAll(transports);

        ObservableList<String> listNomTranspAlle = FXCollections.observableList(listTranspAlle);
        cmbTypeTranspAller.setItems(listNomTranspAlle);
        cmbTypeTranspAller.getSelectionModel().selectFirst();

        ObservableList<String> listNomTranspRetour = FXCollections.observableList(listTranspRetour);
        cmbTypeTransportRet.setItems(listNomTranspRetour);
        cmbTypeTransportRet.getSelectionModel().selectFirst();
    }

    /**
     * Initialisation du tabView
     * 
     * @param transports   liste des transport
     * @param listeVoyages liste des voyage
     */
    public void inittbview(List<String> transports, ObservableList<Voyage> listeVoyages) {
        tbvVoyages.setEditable(true);
        voyage.setCellValueFactory(new PropertyValueFactory<Voyage, String>("designation"));
        transportAller.setCellValueFactory(new PropertyValueFactory<Voyage, String>("transportAller"));
        transportRetour.setCellValueFactory(new PropertyValueFactory<Voyage, String>("transportRetour"));
        villeDestination.setCellValueFactory(new PropertyValueFactory<Voyage, String>("villeDestination"));
        villeRetour.setCellValueFactory(new PropertyValueFactory<Voyage, String>("villeRetour"));
        // Permet de modifier les champs
        voyage.setCellFactory(TextFieldTableCell.<Voyage>forTableColumn());
        transportAller.setCellFactory(TextFieldTableCell.<Voyage>forTableColumn());
        transportRetour.setCellFactory(TextFieldTableCell.<Voyage>forTableColumn());
        villeDestination.setCellFactory(TextFieldTableCell.<Voyage>forTableColumn());
        villeRetour.setCellFactory(TextFieldTableCell.<Voyage>forTableColumn());
        // Lors de la modif de la cellule de désignation
        voyage.setOnEditCommit(event -> {
            Voyage row = event.getRowValue();
            if (designationVoyageCorrecte(event.getNewValue(), listeVoyage)) {
                row.setDesignation(event.getNewValue());
                modification(row);
            } else {
                tbvVoyages.refresh();
            }
        });
        // Lors de la modif de la cellule du transport aller
        transportAller.setOnEditCommit(event -> {
            Voyage row = event.getRowValue();
            if (transportCorrect(event.getNewValue(), transports)) {
                row.setTransportAller(event.getNewValue());
                modification(row);
            } else {
                tbvVoyages.refresh();
            }
        });
        // Lors de la modif de la cellule du transport retour
        transportRetour.setOnEditCommit(event -> {
            Voyage row = event.getRowValue();
            if (transportCorrect(event.getNewValue(), transports)) {
                row.setTransportRetour(event.getNewValue());
                modification(row);
            } else {
                tbvVoyages.refresh();
            }
        });
        // Lors de la modif de la cellule de la ville de destination
        villeDestination.setOnEditCommit(event -> {
            Voyage row = event.getRowValue();
            if (Ville.nomVilleCorrect(event.getNewValue())) {
                row.setVilleDestination(event.getNewValue());
                modification(row);
            } else {
                tbvVoyages.refresh();
            }
        });
        // Lors de la modif de la cellule de la ville de retour
        villeRetour.setOnEditCommit(event -> {
            Voyage row = event.getRowValue();
            if (Ville.nomVilleCorrect(event.getNewValue())) {
                row.setVilleRetour(event.getNewValue());
                modification(row);
            } else {
                tbvVoyages.refresh();
            }
        });

        tbvVoyages.setItems(listeVoyages);
    }

    /**
     * Ajoute le nouveau voyage à la bd puis à la liste des voyages
     * 
     * @param voyage objet voyage
     */
    public void ajout(Voyage voyage) {
        VoyageDAO.create(voyage);
        voyage.setIdVoyage(VoyageDAO.recupId(voyage));
        listeVoyage.add(voyage);
        stock.setStockVoyage(listeVoyage);
    }

    /**
     * Met à jour la bd puis l'objet
     * 
     * @param row bojet voyage
     */
    public void modification(Voyage row) {
        VoyageDAO.update(row);
        row.modifierVoyage(row.getDesignation(), row.getTransportAller(), row.getTransportRetour(),
                row.getVilleDestination(), row.getVilleRetour());
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
     * @param idHotel  id de l'hôtel
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

    /**
     * Méthode qui vérifie le nombre de jour
     * 
     * @param choixNbJournombre de jour
     */
    public boolean verifNbJour(String choixNbJour) {
        final String MESS_NBJOUR = "Le nb de jour est dans un format incorrect";
        boolean correct;

        if (!choixNbJour.equals("")
                && isInteger(choixNbJour)
                && taille(choixNbJour)) {
            correct = true;
        } else {
            correct = false;
            Popup.popupErreur(MESS_NBJOUR);
            System.err.println("Erreur sur le format de nbJour");
        }

        return correct;
    }

    @FXML
    void btnAjouterClicked(ActionEvent event) throws Exception {
        final String MESS_PAYS = "Aucun pays sélectionné";
        String nomVoyage = tfNom.getText();
        ObservableList<String> selectedItemsPays = viewPays.getSelectionModel().getSelectedItems();
        ObservableList<String> selectedItemsHotel = viewHotel.getSelectionModel().getSelectedItems();
        ObservableList<String> selectedItemsVisite = viewVisite.getSelectionModel().getSelectedItems();
        String choixVilleDesti = tfVilleDest.getText();
        String choixVilleRet = tfVilleRetour.getText();
        String choixTransportAlle = cmbTypeTranspAller.getSelectionModel().getSelectedItem();
        String choixTransportRet = cmbTypeTransportRet.getSelectionModel().getSelectedItem();
        String choixNbJour = tfNbJour.getText();
        int idPays = -1;
        int idHotel = -1;
        int idVisite = -1;
        int nbJour = -1;

        if (!selectedItemsPays.isEmpty()
                && designationVoyageCorrecte(nomVoyage, listeVoyage)
                && Ville.nomVilleCorrect(choixVilleDesti)
                && Ville.nomVilleCorrect(choixVilleRet)
                && verifNbJour(choixNbJour)) {

            nbJour = Integer.parseInt(choixNbJour);

            Voyage voyage = new Voyage(nomVoyage, choixTransportAlle, choixTransportRet,
                    choixVilleDesti, choixVilleRet, nbJour);
            ajout(voyage);

            int idVoyage = voyage.getIdVoyage();

            for (String element : selectedItemsPays) {
                for (Pays pays : stock.getStockPays()) {
                    if (pays.getNomPays().equals(element)) {
                        idPays = pays.getIdPays();
                    }
                }
                ajoutPays(idVoyage, idPays);
            }
            for (String element : selectedItemsHotel) {
                for (Hotel hotel : stock.getStockHotel()) {
                    if (hotel.getNomHotel().equals(element)) {
                        idHotel = hotel.getIdHotel();
                    }
                }
                ajoutHotel(idVoyage, idHotel);
            }
            for (String element : selectedItemsVisite) {
                for (Visite visite : stock.getStockVisite()) {
                    if (visite.getTitre().equals(element)) {
                        idVisite = visite.getIdVisites();
                    }
                }
                ajoutVisite(idVoyage, idVisite);
            }

            Popup.popupInformation("Le voyage " + nomVoyage + " a bien été ajouté.");

            // Ajoute à la tableView
            tbvVoyages.getItems().add(voyage);
            tbvVoyages.refresh();

            // redirection sur tarif
            openNewScene(event, "/Tarif/AjouterTarif.fxml");
        } else if (selectedItemsPays.isEmpty()) {
            Popup.popupErreur(MESS_PAYS);
        } // les autres erreurs sont géré dans les vérifications
    }

    @FXML
    void btnSupprimerVoyageClicked(ActionEvent event) {
        final String ERR_NO_LIGNE = "Aucune ligne sélectionnée.";
        final String ERR_INNATENDUE = "Une erreur innatendue s'est produite";
        // Arrête la méthode si aucune ligne n'a été sélectionnée
        if (tbvVoyages.getSelectionModel().getSelectedItem() == null) {
            Popup.popupErreur(ERR_NO_LIGNE);
            return;
        }
        Voyage row = tbvVoyages.getSelectionModel().getSelectedItem();
        if (VoyageDAO.delete(row)) {
            stock.suppresionLiaisonParVoyage(row.getIdVoyage());
            stock.suppressionTarifParVoyage(row.getIdVoyage());
            stock.getStockVoyage().remove(row);
            tbvVoyages.getItems().removeAll(tbvVoyages.getSelectionModel().getSelectedItems());
        } else {
            Popup.popupErreur(ERR_INNATENDUE);
        }
    }

    @FXML
    void btnEditerClicked(ActionEvent event) throws IOException {
        openNewScene(event, "/Agence/MainScene.fxml");
    }

    @FXML
    void btnRechercheClicked(ActionEvent event) throws IOException {
        openNewScene(event, "/Recherche/Recherche.fxml");
    }

    @FXML
    void btnAideClicked(ActionEvent event) throws IOException {
        openNewScene(event, "/Aide/Aide.fxml");
    }

    @FXML
    void btnExporterClicked(ActionEvent event) throws IOException {
        openNewScene(event, "/Agence/Exporter.fxml");
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
     * @param emplacementFichier
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