/*
 *  MainSceneController.java
 */

package Agence;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.util.Optional;
import javafx.scene.control.ButtonType;

/**
 * Main class controller au lancement de l'application
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class MainSceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    /* Les différents boutons du main scene */
    @FXML
    private Button btnHotels;

    @FXML
    private Button btnPays;

    @FXML
    private Button btnTarifs;

    @FXML
    private Button btnVilles;

    @FXML
    private Button btnVisites;

    @FXML
    private Button btnVoyages;

    @FXML
    private Button btnModifier;

    @FXML
    void btnVillesClicked(ActionEvent event) throws IOException {
        openNewScene(event, Outils.Constantes.LIEN_AJOUT_VILLE);
    }

    @FXML
    void btnPaysClicked(ActionEvent event) throws IOException {
        openNewScene(event, Outils.Constantes.LIEN_AJOUT_PAYS);
    }

    @FXML
    void btnTarifsClicked(ActionEvent event) throws IOException {
        openNewScene(event, Outils.Constantes.LIEN_AJOUT_TARIF);
    }

    @FXML
    void btnHotelsClicked(ActionEvent event) throws IOException {
        openNewScene(event, Outils.Constantes.LIEN_AJOUT_HOTEL);
    }

    @FXML
    void btnVoyagesClicked(ActionEvent event) throws IOException {
        openNewScene(event, Outils.Constantes.LIEN_AJOUT_VOYAGE);
    }

    @FXML
    void btnVisitesClicked(ActionEvent event) throws IOException {
        openNewScene(event, Outils.Constantes.LIEN_AJOUT_VISITE);
    }

    @FXML
    void btnModifierClicked(ActionEvent event) throws IOException {
        openNewScene(event, Outils.Constantes.LIEN_MODIFIER_VOYAGE);
    }

    /** Liens du menu */
    @FXML
    void btnRechercheClicked(ActionEvent event) throws IOException {
        openNewScene(event, Outils.Constantes.LIEN_RECHERCHE);
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
        showConfirmation(event, Outils.Constantes.CONFIRMATION_QUITTER);
    }

    /**
     * Demande à l'utilisateur une confirmation sur l'action quitter
     * si l'utilisateur valide alors l'application se ferme
     * sinon l'application reste ouverte
     * 
     * @param event
     * @param message
     */
    private void showConfirmation(ActionEvent event, String message) {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // option != null.
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK) {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }

    }

    /**
     * Ouvre une nouvelle scène grâce à l'event et l'emplacement du fichier fxml à
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
