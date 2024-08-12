/*
 * RechercheController.java                                     25 nov. 2021
 */
package Recherche;

import static Tarif.Tarif.isInteger;
import static Tarif.Tarif.taille;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Agence.StockDonnee;
import Hotel.Hotel;
import Outils.Converteur;
import Outils.CustomIntegerStringConverter;
import Outils.Popup;
import Pays.Pays;
import Tarif.Tarif;
import Visites.Visite;
import Voyage.Voyage;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import liaisonVoyage.liaisonHotel.LiaisonHotel;
import liaisonVoyage.liaisonPays.LiaisonPays;
import liaisonVoyage.liaisonVisite.LiaisonVisite;

/**
 * Gestion de l'interface Recherche
 * Ici l'utilisateur peut rechercher dans l'application
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class RechercheController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    StockDonnee stock = StockDonnee.getInstance();

    // BOUTONS INTERFACE
    @FXML
    private Button btnEditer;

    @FXML
    private Button btnVoyages;

    @FXML
    private Button btnFiltre;

    /* Element du filtre */
    @FXML
    private TextField tfPaysCherche;

    @FXML
    private DatePicker tfDateDebut;

    @FXML
    private DatePicker tfDateFin;

    @FXML
    private TextField tfTarifMaxCherche;

    @FXML
    private CheckBox cbxSansHebergement;

    @FXML
    private CheckBox cbxAvecHebergement;

    @FXML
    private CheckBox cbxOrganise;

    @FXML
    private Button btnOkRecherche;

    /* Résultats global */
    @FXML
    private ListView<String> viewVoyage;

    @FXML
    private Button btnAfficherDetails;

    /* Résultats détails */
    @FXML
    private Label labelDureeVoyage;

    @FXML
    private Label labelTransport;

    @FXML
    private TableView<Tarif> tbvTarif;

    @FXML
    private TableColumn<Tarif, String> dateDep;

    @FXML
    private TableColumn<Tarif, String> dateFin;

    @FXML
    private TableColumn<Tarif, Integer> tGroupe1;

    @FXML
    private TableColumn<Tarif, Integer> tGroupe2;

    @FXML
    private TableColumn<Tarif, Integer> tGroupe3;

    @FXML
    private TableColumn<Tarif, Integer> tGroupe4;

    @FXML
    private TableColumn<Tarif, Integer> tGroupe5;

    @FXML
    private TableColumn<Tarif, Integer> tGroupe6;

    @FXML
    private TableColumn<Tarif, Integer> tGroupe7;

    @FXML
    private TableColumn<Tarif, Integer> tGroupe8;

    @FXML
    private TableColumn<Tarif, Integer> tGroupe9;

    @FXML
    private TableColumn<Tarif, Integer> tGroupe10;

    @FXML
    private ListView<String> viewPays;

    @FXML
    private ListView<String> viewHotel;

    @FXML
    private ListView<String> viewVisite;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tbvTarif.setEditable(true);
        dateDep.setCellValueFactory(new PropertyValueFactory<Tarif, String>("dateDepart"));
        dateFin.setCellValueFactory(new PropertyValueFactory<Tarif, String>("dateRetour"));
        tGroupe1.setCellValueFactory(new PropertyValueFactory<>("prixG1"));
        tGroupe2.setCellValueFactory(new PropertyValueFactory<>("prixG2"));
        tGroupe3.setCellValueFactory(new PropertyValueFactory<>("prixG3"));
        tGroupe4.setCellValueFactory(new PropertyValueFactory<>("prixG4"));
        tGroupe5.setCellValueFactory(new PropertyValueFactory<>("prixG5"));
        tGroupe6.setCellValueFactory(new PropertyValueFactory<>("prixG6"));
        tGroupe7.setCellValueFactory(new PropertyValueFactory<>("prixG7"));
        tGroupe8.setCellValueFactory(new PropertyValueFactory<>("prixG8"));
        tGroupe9.setCellValueFactory(new PropertyValueFactory<>("prixG9"));
        tGroupe10.setCellValueFactory(new PropertyValueFactory<>("prixG10"));

        TableColumn[] tGroupes = { tGroupe1, tGroupe2, tGroupe3, tGroupe4, tGroupe5,
                tGroupe6, tGroupe7, tGroupe8, tGroupe9, tGroupe10 };

        // Permet de modifier les tarifs et de cacher les -1 lorsque le tarif n'existe
        // pas
        for (int i = 0; i < tGroupes.length; i++) {
            tGroupes[i]
                    .setCellFactory(tc -> new TextFieldTableCell<Tarif, Integer>(new CustomIntegerStringConverter()) {

                        @Override
                        public void updateItem(Integer item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setText("");
                            } else {
                                int value = item.intValue();
                                if (value == -1) {
                                    setText("");
                                } else {
                                    setText(Integer.toString(value));
                                }
                            }
                        }
                    });
        }

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
        ObservableList<String> listNomPaysLier = FXCollections.observableList(listPaysLier);
        viewPays.setItems(listNomPaysLier);
        viewPays.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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
        viewHotel.setItems(listNomHotelLier);
        viewHotel.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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
        viewVisite.setItems(listNomVisiteLier);
        viewVisite.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
     * ouvrir passé en paramètre
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

    @FXML
    void cbxOrganiseClicked(ActionEvent event) {

    }

    @FXML
    void cbxSansHebergementClicked(ActionEvent event) {

    }

    @FXML
    void cbxAvecHebergementClicked(ActionEvent event) {

    }

    /**
     * Permet de renvoyer une chaine contenant tout les voyages associé au pays
     * rechercher
     * 
     * @param nomPaysRechercher nom du pays dont on veux les voyages
     * @param listeVoyages      liste des voyages pour les quelles on cherche
     * @return liste des voyages contenant ce pays
     */
    public List<Voyage> rechercheParPays(String nomPaysRechercher, List<Voyage> listeVoyages) {
        List<Voyage> listeVoyagesBis = new ArrayList<>();
        // Recupere l'id du voyage
        int idPays = stock.chercherIdPays(nomPaysRechercher);

        // Recherche dans toute les liaisonPays de notre stock
        for (int s = 0; s < stock.getStockLiaisonPays().size(); s++) {
            // Verifie si une liaison possede l'id du pays chercher
            if (stock.getStockLiaisonPays().get(s).getFkPays() == idPays) {
                // Recherche dans la liste de voyage entré en argument
                for (int i = 0; i < listeVoyages.size(); i++) {
                    // Si l'id voyage de notre liaison correspond à un voyage, alors celui ci est
                    // ajouté
                    if (listeVoyages.get(i).getIdVoyage() == stock.getStockLiaisonPays().get(s).getFkVoyage()) {

                        listeVoyagesBis.add(listeVoyages.get(i));
                    }
                }
            }
        }
        return listeVoyagesBis;
    }

    /**
     * Recherche des voyages avec des tarif comprise dans les 2 dates incluses
     * 
     * @param dateDepRechercher date de depart minium
     * @param dateRetRechercher date de retour maximum
     * @param listeVoyages      liste des voyages pour les quelles on cherche
     * @return liste des voyages compris dans cette période
     */
    public List<Voyage> rechercheParPeriode(LocalDate dateDepRechercher, LocalDate dateRetRechercher,
            List<Voyage> listeVoyages) {
        List<Voyage> listeVoyagesBis = new ArrayList<>();
        LocalDate dateDep;
        LocalDate dateRet;
        boolean voyageAfficher;
        // Recherche dans chacun des tarifs de notre stock
        for (int t = 0; t < stock.getStockTarif().size(); t++) {
            // Conversion de string à LocalDate
            dateDep = Converteur.convertToLocalDate(Date.valueOf(stock.getStockTarif().get(t).getDateDepart()));
            dateRet = Converteur.convertToLocalDate(Date.valueOf(stock.getStockTarif().get(t).getDateRetour()));
            // Vérifie si l'élément est inclus dans la période de temps
            if (((dateDep.isBefore(dateRetRechercher) && dateDep.isAfter(dateDepRechercher))
                    || dateDep.equals(dateDepRechercher))
                    && (dateRet.equals(dateRetRechercher)
                            || (dateRet.isBefore(dateRetRechercher) && dateRet.isAfter(dateDepRechercher)))) {
                // Recherche dans tout les voyages rentré en argument
                for (int i = 0; i < listeVoyages.size(); i++) {
                    voyageAfficher = false;
                    // Verifie si tarif est associé au voyage
                    if (stock.getStockTarif().get(t).getVoyage() == listeVoyages.get(i).getIdVoyage()) {
                        // Verifie que le voyage n'avait pas deja été ajouté à notre nouvelle liste
                        for (int e = 0; e < listeVoyagesBis.size(); e++) {
                            if (stock.getStockTarif().get(t).getVoyage() == listeVoyagesBis.get(e).getIdVoyage()) {
                                voyageAfficher = true;
                            }
                        }
                        // Si le voyage n'est pas déja ajouté, alors celui est rajouté
                        if (!voyageAfficher) {
                            listeVoyagesBis.add(listeVoyages.get(i));
                        }
                    }
                }

            }
        }
        return listeVoyagesBis;
    }

    /**
     * Recherche la liste de voyage ayant des tarifs en dessous de celui indiqué
     * 
     * @param tarifMax     tarif maximum
     * @param listeVoyages liste des voyages pour les quelles on cherche
     * @return liste des voyages avec un taridf inférieur à celui indiqué
     */
    public List<Voyage> rechercheParTarifMax(int tarifMax, List<Voyage> listeVoyages) {
        List<Voyage> listeVoyagesBis = new ArrayList<>();
        boolean prixValides;
        boolean voyageAffichers;
        // Recherche dans chacun des tarifs de notre stock
        for (int t = 0; t < stock.getStockTarif().size(); t++) {
            prixValides = false;
            int[] groupePrix = stock.getStockTarif().get(t).getPrix();
            // Vérifie un des prix est bien inférieur ou égale a celui rentré en argument
            for (int p = 0; p < groupePrix.length; p++) {
                if (groupePrix[p] >= 0 && groupePrix[p] <= tarifMax) {
                    prixValides = true;
                }
            }
            if (prixValides) {
                // Recherche dans tout les voyages rentré en argument
                for (int y = 0; y < listeVoyages.size(); y++) {
                    voyageAffichers = false;
                    // Vérifie si le tarif est associé a un voyage de la liste
                    if (listeVoyages.get(y).getIdVoyage() == stock.getStockTarif().get(t).getVoyage()) {
                        // Verifie que le voyage n'avait pas deja été ajouté à notre nouvelle liste
                        for (int e = 0; e < listeVoyagesBis.size(); e++) {
                            if (stock.getStockTarif().get(t).getVoyage() == listeVoyagesBis.get(e).getIdVoyage()) {
                                voyageAffichers = true;
                            }
                        }
                        // Si le voyage n'est pas déja ajouté, alors celui est rajouté
                        if (!voyageAffichers) {
                            listeVoyagesBis.add(listeVoyages.get(y));
                        }
                    }
                }
            }
        }
        return listeVoyagesBis;
    }

    /**
     * Méthode qui permet de sélectionner un voyage selon son type
     * 
     * @param voyageSansHeberg boolean qui indique si le voyage est avec hébergement
     * @param voyageAvecHeberg boolean qui indique si le voyage est sans hébergement
     * @param voyageOrga       boolean qui indique si le voyage est organisé
     * @param listeVoyages     liste des voyaeg
     * @return listes de voyages
     */
    public List<Voyage> rechercheParTypeVoyage(boolean voyageSansHeberg, boolean voyageAvecHeberg, boolean voyageOrga,
            List<Voyage> listeVoyages) {
        List<Voyage> listeVoyagesBis = new ArrayList<>();

        /** Retire les voyages sans hebergement si non selectionné */
        if (voyageSansHeberg) {
            for (int y = 0; y < listeVoyages.size(); y++) {
                if (stock.voyageSansHebergement(listeVoyages.get(y).getIdVoyage())) {
                    listeVoyagesBis.add(listeVoyages.get(y));
                }
            }
        }

        /** Retire les voyages avec hebergement mais sans visite */
        if (voyageAvecHeberg) {
            for (int y = 0; y < listeVoyages.size(); y++) {
                if (stock.voyageAvecHebergement(listeVoyages.get(y).getIdVoyage())) {
                    listeVoyagesBis.add(listeVoyages.get(y));
                }
            }
        } else {
            /** Retire les voyages organisé */
            if (voyageOrga) {
                for (int y = 0; y < listeVoyages.size(); y++) {
                    if (stock.voyageOrganise(listeVoyages.get(y).getIdVoyage())) {
                        listeVoyagesBis.add(listeVoyages.get(y));
                    }
                }
            }
        }
        return listeVoyagesBis;
    }

    @FXML
    void btnOKRechercheClicked(ActionEvent event) {
        final String NOM_PAYS_INCORRECT = "Le nom de pays est incorrect";
        final String DOIT_SAISIR_DEUX_DATES = "Vous devez saisir les deux dates pour utiliser ce filtre";
        // Reset
        viewVoyage.getItems().removeAll(viewVoyage.getItems());

        List<Voyage> listeVoyages = stock.getStockVoyage();
        String nomPaysRechercher = tfPaysCherche.getText();
        LocalDate dateDepRechercher = tfDateDebut.getValue();
        LocalDate dateRetRechercher = tfDateFin.getValue();
        String tarifMaxRechercher = tfTarifMaxCherche.getText();
        boolean voyageSansHeberg = cbxSansHebergement.isSelected();
        boolean voyageAvecHeberg = cbxAvecHebergement.isSelected();
        boolean voyageOrga = cbxOrganise.isSelected();

        // Recherche pays
        if (!nomPaysRechercher.equals("") && stock.paysExiste(nomPaysRechercher)) {
            listeVoyages = rechercheParPays(nomPaysRechercher, listeVoyages);
        } else if (!nomPaysRechercher.equals("") && !stock.paysExiste(nomPaysRechercher)) {
            Popup.popupErreur(NOM_PAYS_INCORRECT);
            return;
        }

        // Recherche sur une période
        if (dateDepRechercher != null && dateRetRechercher != null) {
            listeVoyages = rechercheParPeriode(dateDepRechercher, dateRetRechercher, listeVoyages);
        } else if ((dateDepRechercher == null && dateRetRechercher != null)
                || (dateDepRechercher != null && dateRetRechercher == null)) {
            Popup.popupErreur(DOIT_SAISIR_DEUX_DATES);
            return;
        }

        // Recherche tarif max
        if (!tarifMaxRechercher.equals("") && isInteger(tarifMaxRechercher) && taille(tarifMaxRechercher)) {
            int tarifMax = Integer.parseInt(tarifMaxRechercher);

            listeVoyages = rechercheParTarifMax(tarifMax, listeVoyages);
        } else if (!tarifMaxRechercher.equals("") && (!isInteger(tarifMaxRechercher) || !taille(tarifMaxRechercher))) {
            Popup.popupErreur("Tarif incorrect");
            return;
        }

        // Recherche type voyage
        if (!(!voyageSansHeberg && !voyageAvecHeberg && !voyageOrga)
                && !(voyageSansHeberg && voyageAvecHeberg && voyageOrga)) {

            listeVoyages = rechercheParTypeVoyage(voyageSansHeberg, voyageAvecHeberg, voyageOrga, listeVoyages);
        }

        // Ajoute à la tableView
        List<String> listVoyages = new ArrayList<>();
        for (Voyage voyage : listeVoyages) {
            listVoyages.add(voyage.getDesignation());
        }
        ObservableList<String> listNomVoyage = FXCollections.observableList(listVoyages);
        viewVoyage.setItems(listNomVoyage);
        viewVoyage.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        viewVoyage.refresh();
    }

    @FXML
    void btnFiltreClicked(ActionEvent event) {
        final String MESS_FILTRE = "Les filtres ont été supprimés.";
        tfPaysCherche.setText("");
        tfTarifMaxCherche.setText("");
        tfDateDebut.setValue(null);
        tfDateFin.setValue(null);
        cbxSansHebergement.setSelected(false);
        cbxAvecHebergement.setSelected(false);
        cbxOrganise.setSelected(false);

        Popup.popupInformation(MESS_FILTRE);
    }

    @FXML
    void btnAfficherDetailsClicked(ActionEvent event) {
        final String MESS_ERR = "Pas de voyage sélectionné";
        tbvTarif.getItems().removeAll(tbvTarif.getItems());
        labelDureeVoyage.setText("Durée voyage");
        labelTransport.setText("Transport");
        int idVoyageRecup = stock.chercherIdVoyage(viewVoyage.getSelectionModel().getSelectedItem());
        String nomVoyage = viewVoyage.getSelectionModel().getSelectedItem();
        if (idVoyageRecup == -1) {
            Popup.popupErreur(MESS_ERR);
        } else {
            Voyage row = null;

            // Cherche le voyage selectionné
            for (Voyage v : stock.getStockVoyage()) {
                if (v.getDesignation() == nomVoyage) {
                    row = v;
                }
            }

            labelDureeVoyage.setText("Le voyage dure " + row.getNbJour() + " jours");

            labelTransport.setText(
                    "L'aller se fait en " + row.getTransportAller() + " et le retour en " + row.getTransportRetour());

            ObservableList<Tarif> listeTarifs = FXCollections
                    .observableArrayList(stock.getStockTarifVoyage(row.getIdVoyage()));
            tbvTarif.getItems().addAll(listeTarifs);

            majListViewVoyage(row.getIdVoyage());
        }
    }
}
