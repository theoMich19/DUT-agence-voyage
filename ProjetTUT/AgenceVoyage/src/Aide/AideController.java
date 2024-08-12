/*
 * AideController.java                                               30/11/2021
 */
package Aide;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import Outils.Popup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * Class qui permet d'accéder au crédit de l'application (nom développeurs)
 * et au guide sous format pdf
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class AideController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button btnAide;

    @FXML
    private Button btnEditer;

    @FXML
    private Button btnRecherche;

    @FXML
    private Button btnVoyages;

    @FXML
    private Button btnExporter;

    @FXML
    private Button btnQuitter;

    @FXML
    private void openLink(ActionEvent event) throws URISyntaxException, IOException {
        // System.out.println("Lien GitHub cliqué");
        // Desktop.getDesktop().browse(new
        // URI("https://github.com/dylanroux/agence-voyage"));
        final String ERREUR_FICHIER = "Fichier introuvable. Assurez-vous que guide.pdf se trouve bien au même niveau que l'application.";

        System.out.println("Lien vers pdf cliqué");
        try {
            Desktop.getDesktop().open(new File("guide.pdf"));
        } catch (Exception e) {
            Popup.popupErreur(ERREUR_FICHIER);
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

}
