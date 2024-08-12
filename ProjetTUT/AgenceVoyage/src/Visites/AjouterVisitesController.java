package Visites;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Agence.StockDonnee;
import Outils.Popup;

import static Visites.Visite.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * Permet de gérer l'interface de Tarif après la création d'un voyage**
 *
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class AjouterVisitesController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    StockDonnee stock = StockDonnee.getInstance();

    @FXML
    private Button btnQuitter;

    @FXML
    private Button btnAide;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnEditer;

    @FXML
    private Button btnRecherche;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button btnVoyages;

    @FXML
    private TableView<Visite> tbvVisites;

    @FXML
    private TableColumn<Visite, String> titre;

    @FXML
    private TableColumn<Visite, String> description;

    @FXML
    private TextArea tfDescription;

    @FXML
    private TextField tfTitreVisite;

    private List<Visite> listeVisites = stock.getStockVisite();

    /**
     * Initialise la table webview
     */
    @Override
    public void initialize(URL url, ResourceBundle resource) {
        // Liste des villes à afficher
        ObservableList<Visite> listeVisites = FXCollections.observableArrayList(
                stock.getStockVisite());

        tbvVisites.setEditable(true);
        titre.setCellValueFactory(new PropertyValueFactory<Visite, String>("titre"));
        description.setCellValueFactory(new PropertyValueFactory<Visite, String>("description"));

        // Permet de modifier le champ pays
        titre.setCellFactory(TextFieldTableCell.<Visite>forTableColumn());
        description.setCellFactory(TextFieldTableCell.<Visite>forTableColumn());

        // Lors de la modif de la cellule du nom
        titre.setOnEditCommit(event -> {
            Visite row = event.getRowValue();
            row.setTitre(event.getNewValue());
            modification(row);
        });
        // Lors de la modif de la cellule des formalités
        description.setOnEditCommit(event -> {
            Visite row = event.getRowValue();
            row.setDescription(event.getNewValue());
            modification(row);
        });

        tbvVisites.setItems(listeVisites);
    }

    /**
     * Ajoute la nouvelle visite à la bd puis à la liste des visites
     * 
     * @param visite objet visite
     */
    public void ajout(Visite visite) {
        VisitesDAO.create(visite);
        visite.setIdVisites(VisitesDAO.recupId(visite));
        listeVisites.add(visite);
        stock.setStockVisite(listeVisites);
        // Ajoute à la tableView
        tbvVisites.getItems().add(visite);
        tbvVisites.refresh();
    }

    /**
     * Met à jour la bd puis l'objet
     * 
     * @param row objet visite
     */
    public void modification(Visite row) {
        VisitesDAO.update(row);
        row.modifierVisites(row.getTitre(), row.getDescription());
    }

    @FXML
    void btnAjouterClicked(ActionEvent event) {
        String AJOUT_FONCTIONNE = "La visite %s a bien été ajoutée.";
        String titre = tfTitreVisite.getText();
        String description = tfDescription.getText();

        titre = cleanNomVisite(titre);
        if (nomVisiteCorrect(titre, stock.getStockVisite())) {
            Visite visite = new Visite(titre, description);
            ajout(visite);

            Popup.popupInformation(String.format(AJOUT_FONCTIONNE, titre));
            // Vide les textField et textArea
            tfTitreVisite.clear();
            tfDescription.clear();
        }
    }

    /**
     * Méthode appelée lors de la suppression d'une ville
     * 
     * @param row objet visite
     */
    public void supprimerMaj(Visite row) {
        final String SUPPRESSION_ECHEC = "La visite n'a pas pu être supprimée car"
                                         + " celle-ci est liée à un voyage.";

        if (VisitesDAO.delete(row)) {
            stock.getStockVisite().remove(row);
            tbvVisites.getItems().removeAll(tbvVisites.getSelectionModel().getSelectedItems());
        } else {
            Popup.popupErreur(SUPPRESSION_ECHEC);
        }
    }

    @FXML
    void btnSupprimerVisitesClicked(ActionEvent event) {
        final String SUPPRESSION_CONFIRMATION = "Voulez-vous vraiment supprimer cette visite ?"
                                                + " Celle-ci est associée à un ou plusieurs voyages.";
        final String RIEN_SELECTIONNE = "Aucune ligne sélectionnée.";
        final String TITRE_ALERT_CONFIRMATION = "Supprimer visite";

        // Arrête la méthode si aucune ligne n'a été sélectionnée
        if (tbvVisites.getSelectionModel().getSelectedItem() == null) {
            Popup.popupErreur(RIEN_SELECTIONNE);
            return;
        }

        Visite row = tbvVisites.getSelectionModel().getSelectedItem();
        if (stock.verifVisiteUtilise(row)) {
            Alert alertConfirmation = new Alert(AlertType.CONFIRMATION);

            alertConfirmation.setTitle(TITRE_ALERT_CONFIRMATION);
            alertConfirmation.setHeaderText(null);
            alertConfirmation.setContentText(SUPPRESSION_CONFIRMATION);
            System.out.println(SUPPRESSION_CONFIRMATION);

            // option != null.
            Optional<ButtonType> option = alertConfirmation.showAndWait();

            if (option.get() == ButtonType.OK) {
                stock.suppresionLiaisonVisite(row.getIdVisites());
                supprimerMaj(row);
            }
        } else {
            supprimerMaj(row);
        }
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
    void btnQuitterClicked(ActionEvent event) throws IOException {
        if (Popup.confirmationQuitter(event, Outils.Constantes.CONFIRMATION_QUITTER) == -1) {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
