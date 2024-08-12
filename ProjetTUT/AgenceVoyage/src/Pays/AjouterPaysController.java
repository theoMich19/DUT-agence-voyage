/*
 * AjouterPaysController.java                                               14/11/2021
 */

package Pays;

import static Pays.Pays.cleanNomPays;
import static Pays.Pays.nomPaysCorrect;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import Agence.StockDonnee;
import Outils.Popup;
import Voyage.Voyage;
import Voyage.VoyageDAO;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import liaisonVoyage.liaisonPays.LiaisonPays;
import liaisonVoyage.liaisonPays.LiaisonPaysDAO;

/**
 * Permet de gérer l'interface sur les Pays
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class AjouterPaysController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    /* Déclaration du stock des données */
    StockDonnee stock = StockDonnee.getInstance();

    /* Les différentes éléments FXML; boutons, TextField & TextArea */
    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnSupprimerPays;

    @FXML
    private TableView<Pays> tbvPays;

    @FXML
    private TableColumn<Pays, String> pays;

    @FXML
    private TableColumn<Pays, String> formalites;

    @FXML
    private TableColumn<Pays, String> conseils;

    @FXML
    private TextArea tfConseils;

    @FXML
    private TextArea tfFormalites;

    @FXML
    private TextField tfNomPays;

    private List<Pays> listePays = stock.getStockPays();

    /**
     * Initialise la table webview
     */
    @Override
    public void initialize(URL url, ResourceBundle resource) {
        // Liste des villes à afficher
        ObservableList<Pays> listePays = FXCollections.observableArrayList(
                stock.getStockPays());

        tbvPays.setEditable(true);
        pays.setCellValueFactory(new PropertyValueFactory<Pays, String>("nomPays"));
        formalites.setCellValueFactory(new PropertyValueFactory<Pays, String>("formalites"));
        conseils.setCellValueFactory(new PropertyValueFactory<Pays, String>("conseil"));

        // Permet de modifier le champ pays
        pays.setCellFactory(TextFieldTableCell.<Pays>forTableColumn());
        formalites.setCellFactory(TextFieldTableCell.<Pays>forTableColumn());
        conseils.setCellFactory(TextFieldTableCell.<Pays>forTableColumn());

        // Lors de la modif de la cellule du nom
        pays.setOnEditCommit(event -> {
            Pays row = event.getRowValue();
            if (nomPaysCorrect(event.getNewValue(), listePays)) {
                row.setNomPays(event.getNewValue());
                modification(row);
            } else {
                tbvPays.refresh();
            }
        });
        // Lors de la modif de la cellule des formalités
        formalites.setOnEditCommit(event -> {
            Pays row = event.getRowValue();
            row.setFormalites(event.getNewValue());
            modification(row);
        });
        // Lors de la modif de la cellule des conseils
        conseils.setOnEditCommit(event -> {
            Pays row = event.getRowValue();
            row.setConseil(event.getNewValue());
            modification(row);
        });

        tbvPays.setItems(listePays);
    }

    /**
     * Ajoute le nouveau pays à la bd puis à la liste des pays
     * 
     * @param pays objet pays à ajouter
     */
    public void ajout(Pays pays) {
        PaysDAO.create(pays);
        pays.setIdPays(PaysDAO.recupId(pays));
        listePays.add(pays);
        stock.setStockPays(listePays);
        // Ajoute à la tableView
        tbvPays.getItems().add(pays);
        tbvPays.refresh();
    }

    /**
     * Met à jour la bd puis l'objet
     * 
     * @param row objet pays
     */
    public void modification(Pays row) {
        PaysDAO.update(row);
        row.modifierPays(row.getNomPays(), row.getFormalites(), row.getConseil());
    }

    /**
     * Méthode appelée lors de la suppression d'un pays
     * 
     * @param row objet pays
     */
    public void supprimerMaj(Pays row) {
        final String SUPPRESSION_ECHEC = "Le pays n'a pas pu être supprimé car"
                + " celui-ci est lié à un voyage.";
        final String SUPP_SUCCES = "Le pays a bien été supprimé";
        if (PaysDAO.delete(row)) {
            stock.getStockPays().remove(row);
            tbvPays.getItems().removeAll(tbvPays.getSelectionModel().getSelectedItems());
            Popup.popupInformation(SUPP_SUCCES);
        } else {
            Popup.popupErreur(SUPPRESSION_ECHEC);
        }
    }

    /**
     * Méthode appelée lors de la suppression des voyages liés à un pays.
     * Elle supprime le ou les voyages liés au pays supprimé.
     * 
     * @param row objet pays
     */
    public void supprimerVoyage(Pays row) {
        for (LiaisonPays i : stock.getStockLiaisonPays()) {
            if (i.getFkPays() == row.getIdPays()) {
                for (Voyage s : stock.getStockVoyage()) {
                    if (i.getFkVoyage() == s.getIdVoyage()) {
                        VoyageDAO.delete(s);
                    }
                }
            }
        }
    }

    /**
     * Lorsque l'on appuie sur le bouton d'ajout, alors une vérification
     * des valeurs saisie dans cette partie cette vérifié,
     * alors celle ci sont ajouté à notre stock.
     * Dans le cas contraire, une popup annoncant l'erreur apparait.
     * 
     * @param event
     * @throws Exception
     */
    @FXML
    void btnAjouterClicked(ActionEvent event) throws Exception {
        final String MESS_ERR = "Vous n'avez saisie auncune information.";
        String ajout_fonctionne = "Le pays %s a bien été ajouté.";
        String nomPays = tfNomPays.getText();
        String formalites = tfFormalites.getText();
        String conseils = tfConseils.getText();

        if (nomPays.trim().equals("")) {
            Popup.popupErreur(MESS_ERR);
            return;
        }

        nomPays = cleanNomPays(nomPays);
        // On vérifie que le nom du pays est correct
        if (nomPaysCorrect(nomPays, listePays)) {
            Pays pays = new Pays(nomPays, formalites, conseils, listePays);
            ajout(pays);

            Popup.popupInformation(String.format(ajout_fonctionne, nomPays));

            // Vide les textField et textArea
            tfNomPays.clear();
            tfFormalites.clear();
            tfConseils.clear();
        }
    }

    /**
     * Lorsque que l'on appuie sur le bouton supprimer, alors le programme
     * vérifie que l'on a bien sélectionné un pays. Si ce n'est pas le cas,
     * une erreur est renvoyé.
     * Dans le cas contraire, alors on regarde si le pays est lié au voyage,
     * - Si il est lié, une popup lui demande si il veut supprimer les voyages
     * associés ou si il souhaite abandonner sa suppresion.
     * Si il accepte, les voyages et le pays sont supprimer, dans le cas contraire,
     * on revient au point de départ
     * - Si aucun voyage n'est lié, alors le pays est supprimer directement
     * 
     * @param event
     */
    @FXML
    void btnSupprimerPaysClicked(ActionEvent event) {
        StringBuilder listVoyage = new StringBuilder(); // list voyage avec 1 liaison
        listVoyage.append("Les voyages qui sont uniquement liés à ce pays sont : ");
        int nombreChar = listVoyage.length();
        // Arrête la méthode si aucune ligne n'a été sélectionnée
        if (tbvPays.getSelectionModel().getSelectedItem() == null) {
            Popup.popupErreur("Aucune ligne sélectionnée.");
            return;
        }
        Pays row = tbvPays.getSelectionModel().getSelectedItem();
        if (row.voyageUtilisePays(stock.getStockVoyage(), stock.getStockLiaisonPays())) {
            for (Voyage voyage : stock.getStockVoyage()) {
                int nombreLiaison = 0;
                for (LiaisonPays liaisonPays : stock.getStockLiaisonPays()) {
                    if (voyage.getIdVoyage() == liaisonPays.getFkVoyage())
                        nombreLiaison++;
                }
                if (nombreLiaison == 1 && verifUneLiaison(voyage, row))
                    listVoyage.append("\n-" + voyage.getDesignation());
            }
            if (nombreChar < listVoyage.length()) {
                int action = Popup.popupSuppChoix(listVoyage);
                switch (action) {
                    case 1:
                        boolean succes = false;
                        for (Voyage voyage : stock.getStockVoyage()) {
                            int nombreLiaison = 0;
                            for (LiaisonPays liaisonPays : stock.getStockLiaisonPays()) {
                                if (voyage.getIdVoyage() == liaisonPays.getFkVoyage())
                                    nombreLiaison++;
                            }
                            if (nombreLiaison == 1 && verifUneLiaison(voyage, row)) {
                                if (LiaisonPaysDAO.delete(row.getIdPays(), voyage.getIdVoyage()) &&
                                        VoyageDAO.delete(voyage.getIdVoyage()))
                                    succes = true;
                            }
                        }
                        if (succes)
                            supprimerMaj(row);
                        break;
                    case 2:
                        break;
                }
            } else {
                int action = Popup.popupSupp();
                switch (action) {
                    case 1:
                        boolean succes = false;
                        for (Voyage voyage : stock.getStockVoyage()) {
                            for (LiaisonPays liaisonPays : stock.getStockLiaisonPays()) {
                                if (voyage.getIdVoyage() == liaisonPays.getFkVoyage()
                                        && liaisonPays.getFkPays() == row.getIdPays())
                                    if (LiaisonPaysDAO.delete(row.getIdPays(), voyage.getIdVoyage()) &&
                                            VoyageDAO.delete(voyage.getIdVoyage()))
                                        succes = true;
                            }
                        }
                        if (succes)
                            supprimerMaj(row);
                        break;
                    case 2:
                        break;
                }
            }
        } else { // pas de voyage lié à ce pays
            supprimerMaj(row);
        }
    }

    /**
     * Méthode qui permet de savoir si la liaison unique correspond bien au voyage
     * et au pays passé en argument
     * 
     * @param voyage objet voyage
     * @param row    objet pays
     * @return true si la liaison est valide
     */
    public boolean verifUneLiaison(Voyage voyage, Pays row) {
        for (LiaisonPays liaisonPays : stock.getStockLiaisonPays()) {
            if (voyage.getIdVoyage() == liaisonPays.getFkVoyage()
                    && row.getIdPays() == liaisonPays.getFkPays()) {
                return true;
            }
        }
        return false;
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
