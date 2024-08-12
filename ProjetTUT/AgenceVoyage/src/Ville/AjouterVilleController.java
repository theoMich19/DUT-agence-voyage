/*
 * AjouterVilleController.java                                        07 nov. 2021
 */

package Ville;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Agence.StockDonnee;
import Outils.Popup;
import Tarif.TarifDAO;

import static Ville.Ville.*;

/**
 * Gestion de l'interface Ville
 * Ici l'utilisateur peut saisir le nom de la ville, et choisir son numéro de
 * groupe.
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class AjouterVilleController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    StockDonnee stock = StockDonnee.getInstance();

    @FXML
    private Button btnAjouterVille;

    @FXML
    private Button btnSupprimerVille;

    @FXML
    private Label lblGrpMax;

    @FXML
    private TextField tfNomVille;

    @FXML
    private ComboBox<Integer> ccbNumGroupe;

    @FXML
    private TableView<Ville> tbvVilles;

    @FXML
    private TableColumn<Ville, String> ville;

    @FXML
    private TableColumn<Ville, Integer> groupe;

    private List<Ville> listeVilles = stock.getStockVille();
    private List<Integer> listGroupe = new ArrayList<Integer>();
    private ObservableList<Integer> listNbGroupe;

    /** Initialise les erreurs */
    private Alert alertConfirmation = new Alert(AlertType.CONFIRMATION);

    /**
     * Initialise le contenu de chaque combobox
     */
    @Override
    public void initialize(URL url, ResourceBundle resource) {

        // Initialisation du combobox des groupes
        int numGroupe;
        for (numGroupe = 1; numGroupe <= stock.getNbGroupeMax(); numGroupe++) {
            listGroupe.add(numGroupe);
        }
        if (stock.getNbGroupeMax() != 10) {
            listGroupe.add(numGroupe);
            lblGrpMax.setText("Sélectionnez " + numGroupe + " pour créer un nouv. groupe");
        } else {
            lblGrpMax.setText("Limite de groupes atteinte");
        }
        listNbGroupe = FXCollections.observableList(listGroupe);
        ccbNumGroupe.setItems(listNbGroupe);
        // sélection par défaut pour éviter que rien ne soit sélectionné
        ccbNumGroupe.getSelectionModel().select(stock.getNbGroupeMax());

        // Liste des villes à afficher
        ObservableList<Ville> listeVilles = FXCollections.observableArrayList(
                stock.getStockVille());

        tbvVilles.setEditable(true);
        ville.setCellValueFactory(new PropertyValueFactory<Ville, String>("nomVille"));
        groupe.setCellValueFactory(new PropertyValueFactory<Ville, Integer>("groupe"));
        // Permet de modifier le champ ville
        ville.setCellFactory(TextFieldTableCell.<Ville>forTableColumn());
        // Lors de la modif de la cellule du nom, on met à jour la bd puis l'objet
        ville.setOnEditCommit(event -> {
            Ville row = event.getRowValue();
            if (nomVilleCorrect(event.getNewValue(), listeVilles)) {
                row.setNomVille(event.getNewValue(), listeVilles);
                modification(row);
            } else {
                tbvVilles.refresh();
            }
        });

        tbvVilles.setItems(listeVilles);
    }

    /**
     * Ajoute la nouvelle ville à la bd puis à la liste de villes
     * 
     * @param ville objet ville
     */
    public void ajout(Ville ville) {
        VilleDAO.create(ville);
        ville.setId(VilleDAO.recupId(ville));
        listeVilles.add(ville);
        stock.setStockVille(listeVilles);
        // Ajoute à la tableView
        tbvVilles.getItems().add(ville);
        tbvVilles.refresh();
    }

    /**
     * Met à jour la bd puis l'objet
     * 
     * @param row objet ville
     */
    public void modification(Ville row) {
        VilleDAO.update(row);
        row.modifierVille(row.getNomVille(), row.getGroupe());
    }

    @FXML
    void ccbNumGroupeClicked(ActionEvent event) {
    }

    @FXML
    void btnAjouterVilleClicked(ActionEvent event) throws NumberFormatException, SQLException {
        final String LIMITE_GRP = "Limite de groupes atteinte.";
        final String ERR_NO_VILLE = "Aucune ville saisie";

        String selection_groupe = "Sélectionnez %s pour créer un nouv. groupe";
        String ajout_fonctionne = "La ville %s a bien été ajoutée";
        String nomVille = tfNomVille.getText();
        int numGroupe = ccbNumGroupe.getSelectionModel().getSelectedItem();
        boolean nouveauGrp = false;

        // stop fonction si pas de saisie
        if (nomVille.equals("")) {
            Popup.popupErreur(ERR_NO_VILLE);
            return;
        }

        // Si un nouveau groupe est créé, alors on donne la nouvelle valeur à
        // stockDonnee
        if (numGroupe == (stock.getNbGroupeMax() + 1)) {
            stock.monterNbGroupe();
            nouveauGrp = true;
        }

        nomVille = cleanNomVille(nomVille);
        // On vérifie qu'il n'y a pas de problème avec les informations saisies
        if (nomVilleCorrect(nomVille, listeVilles)) {
            Ville ville = new Ville(nomVille, numGroupe, listeVilles,
                    stock.getNbGroupeMax());
            ajout(ville);

            Popup.popupInformation(String.format(ajout_fonctionne, nomVille));
            // Vide le textField
            tfNomVille.clear();

            // Ajoute à la cmb un numéro à condition qu'il n'y en ai pas 10
            // et que le groupe tout juste ajouté corresponde au dernier de la liste
            if (nouveauGrp && (stock.getNbGroupeMax() != 10) && (ville.getGroupe() == stock.getNbGroupeMax())) {
                int i = listGroupe.get(listGroupe.size() - 1) + 1;
                listGroupe.add(i);
            }
            if (stock.getNbGroupeMax() != 10) {
                int groupe = stock.getNbGroupeMax() + 1;
                lblGrpMax.setText(String.format(selection_groupe, groupe));
            } else {
                lblGrpMax.setText(LIMITE_GRP);
            }
            listNbGroupe = FXCollections.observableList(listGroupe);
            ccbNumGroupe.setItems(listNbGroupe);
            ccbNumGroupe.getSelectionModel().select(stock.getNbGroupeMax());
        }
    }

    @FXML
    void btnSupprimerVilleClicked(ActionEvent event) {
        final String SUPPRESSION_CONFIRMATION = "Voulez-vous vraiment supprimer ce groupe ?\n"
                                                + " Tous les tarifs associés seront également supprimés.";
        final String PAS_LIGNE = "Aucune ligne sélectionnée.";
        String selection_grp = "Sélectionnez %d pour créer un nouv. groupe";

        // Arrête la méthode si aucune ligne n'a été sélectionnée
        if (tbvVilles.getSelectionModel().getSelectedItem() == null) {
            Popup.popupErreur(PAS_LIGNE);
            return;
        }
        Ville row = tbvVilles.getSelectionModel().getSelectedItem();

        // S'il n'y a plus d'autres villes dans le groupe
        if (row.seuleDansGroupe(stock.getStockVille())) {
            alertConfirmation.setTitle("Supprimer groupe");
            alertConfirmation.setHeaderText(null);
            alertConfirmation.setContentText(SUPPRESSION_CONFIRMATION);
            System.out.println(SUPPRESSION_CONFIRMATION);

            // option != null.
            Optional<ButtonType> option = alertConfirmation.showAndWait();

            // Décale tous les groupes
            if (option.get() == ButtonType.OK) {
                int groupeSupr = row.getGroupe();
                supprimerMaj(row);
                stock.decalerGroupe(groupeSupr);
                VilleDAO.decalerGroupe(stock.getStockVille());
                TarifDAO.decalerPrix(stock.getStockTarif());
                stock.reduireNbGroupe();
                if (stock.getNbGroupeMax() >= 0) {
                    int i = listGroupe.get(listGroupe.size() - 1) - 1;
                    listGroupe.remove(i);
                    lblGrpMax.setText(String.format(selection_grp, i));
                }
            }
        } else {
            supprimerMaj(row);
        }
    }

    /**
     * Méthode appelée lors de la suppression d'une ville.
     * Enlève des objets la ville supprimée dans la bd.
     * 
     * @param row objet ville
     */
    public void supprimerMaj(Ville row) {
        final String SUPPRESSION_ECHEC = "La ville n'a pu être supprimée.";

        if (VilleDAO.delete(row)) {
            stock.getStockVille().remove(row);
            tbvVilles.getItems().removeAll(tbvVilles.getSelectionModel().getSelectedItems());
            listNbGroupe = FXCollections.observableList(listGroupe);
            ccbNumGroupe.setItems(listNbGroupe);
            // sélection par défaut pour éviter que rien ne soit sélectionné
            ccbNumGroupe.getSelectionModel().select(stock.getNbGroupeMax() - 1);
        } else {
            Popup.popupErreur(SUPPRESSION_ECHEC);
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