/*
 * AjouterHotelController.java                                        09 janv. 2022
 */
package Hotel;

import static Hotel.Hotel.verifNum;
import static Ville.Ville.nomVilleCorrect;
import static Hotel.Hotel.nomHotelCorrect;
import static Hotel.Hotel.adresseCorrect;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Agence.StockDonnee;
import Outils.Popup;
import Ville.Ville;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

/**
 * Permet de gérer l'interface sur les Hôtels
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class AjouterHotelController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    StockDonnee stock = StockDonnee.getInstance();

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnSupprimerHotel;

    @FXML
    private TableView<Hotel> tbvHotel;

    @FXML
    private TableColumn<Hotel, String> nom;

    @FXML
    private TableColumn<Hotel, String> adresse;

    @FXML
    private TableColumn<Hotel, String> ville;

    @FXML
    private TableColumn<Hotel, String> telephone;

    @FXML
    private TextField tfAdresseHotel;

    @FXML
    private TextField tfNomHotel;

    @FXML
    private TextField tfTelephoneHotel;

    @FXML
    private TextField tfVilleHotel;

    private List<Hotel> listeHotel = stock.getStockHotel();

    /** Initialise les erreurs */
    private Alert alertConfirmation = new Alert(AlertType.CONFIRMATION);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Liste des hotels à afficher
        ObservableList<Hotel> listeHotel = FXCollections.observableArrayList(
                stock.getStockHotel());

        tbvHotel.setEditable(true);
        nom.setCellValueFactory(new PropertyValueFactory<Hotel, String>("nomHotel"));
        adresse.setCellValueFactory(new PropertyValueFactory<Hotel, String>("adresse"));
        ville.setCellValueFactory(new PropertyValueFactory<Hotel, String>("ville"));
        telephone.setCellValueFactory(new PropertyValueFactory<Hotel, String>("numTel"));

        // Permet de modifier les champs
        nom.setCellFactory(TextFieldTableCell.<Hotel>forTableColumn());
        adresse.setCellFactory(TextFieldTableCell.<Hotel>forTableColumn());
        ville.setCellFactory(TextFieldTableCell.<Hotel>forTableColumn());
        telephone.setCellFactory(TextFieldTableCell.<Hotel>forTableColumn());

        // Lors de la modif de la cellule du nom
        nom.setOnEditCommit(event -> {
            Hotel row = event.getRowValue();
            row.setNomHotel(event.getNewValue());
            modification(row);
        });
        // Lors de la modif de la cellule des formalités
        adresse.setOnEditCommit(event -> {
            Hotel row = event.getRowValue();
            row.setAdresse(event.getNewValue());
            modification(row);
        });
        // Lors de la modif de la cellule des conseils
        ville.setOnEditCommit(event -> {
            Hotel row = event.getRowValue();
            row.setVille(event.getNewValue());
            modification(row);
        });

        telephone.setOnEditCommit(event -> {
            Hotel row = event.getRowValue();
            row.setNumTel(event.getNewValue());
            modification(row);
        });

        tbvHotel.setItems(listeHotel);
    }

    /**
     * Ajoute le nouvel Hôtel à la bd puis à la liste des Hôtels
     * 
     * @param hotel objet Hôtel
     */
    public void ajout(Hotel hotel) {
        HotelDAO.create(hotel);
        hotel.setIdHotel(HotelDAO.recupId(hotel));
        listeHotel.add(hotel);
        stock.setStockHotel(listeHotel);
        // Ajoute à la tableView
        tbvHotel.getItems().add(hotel);
        tbvHotel.refresh();
    }

    /**
     * Met à jour la bd puis l'objet
     * 
     * @param row objet Hôtel
     */
    public void modification(Hotel row) {
        HotelDAO.update(row);
        row.modifierHotel(row.getNomHotel(), row.getAdresse(), row.getVille(), row.getNumTel());
    }

    /**
     * Méthode appelée lors de la suppression d'une ville
     * 
     * @param row objet Hôtel
     */
    public void supprimerMaj(Hotel row) {
        final String SUPPRESSION_ECHEC = "L'hôtel n'a pas pu être supprimé car"
                                         + " celui-ci est lié à un voyage.";

        if (HotelDAO.delete(row)) {
            stock.getStockHotel().remove(row);
            tbvHotel.getItems().removeAll(tbvHotel.getSelectionModel().getSelectedItems());
        } else {
            Popup.popupErreur(SUPPRESSION_ECHEC);
        }
    }

    @FXML
    void btnAjouterClicked(ActionEvent event) {
        String nomHotel = tfNomHotel.getText();
        String adresse = tfAdresseHotel.getText();
        String ville = tfVilleHotel.getText();
        String numTel = tfTelephoneHotel.getText();
        String AJOUT_FONCTIONNE = "L'hôtel %s a bien été ajouté.";

        // On vérifie que les champ sont correct
        if (nomHotelCorrect(nomHotel, listeHotel) && adresseCorrect(adresse)
            && nomVilleCorrect(ville) && verifNum(numTel)) {
            Hotel hotel = new Hotel(nomHotel, adresse, ville, numTel);
            // Ajout de l'objet Hôtel
            ajout(hotel);

            Popup.popupInformation(String.format(AJOUT_FONCTIONNE, nomHotel));

            // Vide les textField et textArea
            tfNomHotel.clear();
            tfAdresseHotel.clear();
            tfVilleHotel.clear();
            tfTelephoneHotel.clear();
        }
    }

    @FXML
    void btnSupprimerClicked(ActionEvent event) {
        final String SUPPRESSION_CONFIRMATION = "Voulez-vous vraiment supprimer cet hôtel ?"
                                                + " Celui-ci est associé à un ou plusieurs voyages.";
        final String PAS_DE_LIGNE = "Aucune ligne sélectionnée.";

        // Arrête la méthode si aucune ligne n'a été sélectionnée
        if (tbvHotel.getSelectionModel().getSelectedItem() == null) {
            Popup.popupErreur(PAS_DE_LIGNE);
            return;
        }

        Hotel row = tbvHotel.getSelectionModel().getSelectedItem();
        if (stock.verifHotelUtilise(row)) {
            alertConfirmation.setTitle("Supprimer pays");
            alertConfirmation.setHeaderText(null);
            alertConfirmation.setContentText(SUPPRESSION_CONFIRMATION);
            System.out.println(SUPPRESSION_CONFIRMATION);

            // option != null.
            Optional<ButtonType> option = alertConfirmation.showAndWait();

            if (option.get() == ButtonType.OK) {
                stock.suppresionLiaisonHotel(row.getIdHotel());
                // suppression de l'objet Hôtel
                supprimerMaj(row);
            }
        } else {
            // suppression de l'objet Hôtel
            supprimerMaj(row);
        }
    }

    @FXML
    void btnEditerClicked(ActionEvent event) throws IOException {
        openNewScene(event, "/Agence/MainScene.fxml");
    }

    @FXML
    void btnVoyagesClicked(ActionEvent event) throws IOException {
        openNewScene(event, "/Voyage/AjouterVoyage.fxml");
    }

    @FXML
    void btnExporterClicked(ActionEvent event) throws IOException {
        openNewScene(event, "/Agence/Exporter.fxml");
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
    void btnQuitterClicked(ActionEvent event) throws IOException {
        if (Popup.confirmationQuitter(event, Outils.Constantes.CONFIRMATION_QUITTER) == -1) {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Ouvre une nouvelle scene grâce à l'event et l'emplacement du fichier fxml à
     * ouvrir
     * passé en paramètre
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
