package Agence;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Voyage.Voyage;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import liaisonVoyage.liaisonHotel.LiaisonHotel;
import liaisonVoyage.liaisonPays.LiaisonPays;
import liaisonVoyage.liaisonVisite.LiaisonVisite;
import javafx.scene.Node;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Hotel.Hotel;
import Outils.CustomIntegerStringConverter;
import Outils.Popup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import Pays.Pays;
import Tarif.Tarif;
import Ville.Ville;
import Visites.Visite;

/**
 * Class de controler pour l'interface d'exporter le fichier Json
 */
public class ExporterController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    StockDonnee stock = StockDonnee.getInstance();

    @FXML
    private Button btnAide;

    @FXML
    private Button btnEditer;

    @FXML
    private Button btnVoyages;

    @FXML
    private Button btnExporter;

    @FXML
    private Button btnRecherche;

    @FXML
    private Button btnQuitter;

    @FXML
    private ComboBox<String> cmbVoyages;

    @FXML
    private TextField tfNomFichier;

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

    private int idVoyageRecup = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation du combobox des voyages
        List<String> listeVoyages = new ArrayList<>();
        for (Voyage voyage : stock.getStockVoyage()) {
            listeVoyages.add(voyage.getDesignation());
        }
        ObservableList<String> listeNomVoyages = FXCollections.observableList(listeVoyages);
        cmbVoyages.setItems(listeNomVoyages);

        // Liste des villes à afficher
        ObservableList<Tarif> listeTarifs = FXCollections.observableArrayList(
                stock.getStockTarifVoyage(idVoyageRecup));

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
        tbvTarif.setItems(listeTarifs);
    }

    /**
     * Action lorsque l'utilisateur clique sur "cmbVoyages"
     * 
     * @param event
     * @throws JSONException
     */
    @FXML
    void cmbVoyagesClicked(ActionEvent event) throws JSONException {
        idVoyageRecup = stock.chercherIdVoyage(cmbVoyages.getSelectionModel().getSelectedItem());
        tbvTarif.getItems().removeAll(tbvTarif.getItems());
        stock.getStockTarif();
        ObservableList<Tarif> listeTarifs = FXCollections.observableArrayList(stock.getStockTarifVoyage(idVoyageRecup));
        tbvTarif.getItems().addAll(listeTarifs);
    }

    /**
     * Exporte le fichier .json du voyage sélectionné dans le répertoire choisi
     * par l'utilisateur.
     * 
     * @param event
     * @throws JSONException
     */
    @FXML
    void btnExporterClicked(ActionEvent event) throws JSONException {
        final String EXPORT_ECHEC = "Échec de l'exportation";
        final String DEFAULT_NAME = "voyage"; // nom par défault du fichier
        String EXPORT_REUSSI = "Le fichier a bien été généré.\n"
                + "Emplacement : \n %s";

        String choixVoyage = cmbVoyages.getSelectionModel().getSelectedItem();
        Tarif choixTarif = tbvTarif.getSelectionModel().getSelectedItem();

        // Si rien de sélectionné on ne fait rien
        if (choixVoyage == null || choixTarif == null) {
            return;
        }

        // Crée un objet JSONObject
        JSONObject jsonObject = new JSONObject();
        // Met les données correspondant au voyage sélectionné
        setJsonData(jsonObject, choixVoyage, choixTarif);

        // Ouvre une fenêtre Windows pour sélectionner l'emplacement de sauvegarde
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(DEFAULT_NAME);
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("JSON", "*.json"));

        try {
            // On enregistre le lieu de sauvegarde dans un objet File.
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                if (saveJsonToFile(jsonObject, file)) {
                    Popup.popupInformation(String.format(EXPORT_REUSSI, file.getAbsolutePath()));
                }
            }
        } catch (Exception e) {
            Popup.popupErreur(EXPORT_ECHEC);
        }
    }

    /**
     * Place tous les objets liés au voyage sélectionné dans un JSONObject
     * 
     * @param jsonObject
     * @param choixVoyage choix du voyage
     * @return jsonObject
     * @throws JSONException
     */
    public JSONObject setJsonData(JSONObject jsonObject, String choixVoyage, Tarif choixTarif) throws JSONException {

        // Trouve l'objet voyage sélectionné à partir de son nom et enregistre son id
        int idVoyage = -1;
        for (Voyage voyageCherche : stock.getStockVoyage()) {
            if (voyageCherche.getDesignation().equals(choixVoyage)) {
                jsonObject.put("voyage", voyageCherche);
                idVoyage = voyageCherche.getIdVoyage();
            }
        }

        // Exporte le tarif
        jsonObject.put("tarif", choixTarif);

        // Trouve les villes liées au voyage
        JSONArray jsonArrayVilles = new JSONArray();
        int prix[] = choixTarif.getPrix();
        for (int g = 0; g < stock.getNbGroupeMax(); g++) {
            if (prix[g] != -1) {
                int num = g + 1;
                jsonArrayVilles.put("groupe" + num);
                for (Ville v : stock.getStockVille()) {
                    if (v.getGroupe() == (g + 1)) {
                        JSONObject jsonVilles = new JSONObject();
                        jsonVilles.put("villes", v);
                        jsonArrayVilles.put(jsonVilles);
                    }
                }
            }
        }
        jsonObject.put("listeVilles", (Object) jsonArrayVilles);

        // Trouve l'id des pays correspondant associés à l'id du voyage
        JSONArray jsonArrayPays = new JSONArray();
        for (LiaisonPays liaison : stock.getStockLiaisonPays()) {
            if (idVoyage == liaison.getFkVoyage()) {
                // Trouve les objets Pays correspondant à l'id du pays et les place
                // dans un JSONArray
                for (Pays pays : stock.getStockPays()) {
                    if (pays.getIdPays() == liaison.getFkPays()) {
                        JSONObject jsonPays = new JSONObject();
                        jsonPays.put("pays", pays);
                        jsonArrayPays.put(jsonPays);
                    }
                }
            }
        }
        jsonObject.put("listePays", (Object) jsonArrayPays);

        // Trouve l'id des hôtels correspondant associés à l'id du voyage
        JSONArray jsonArrayHotel = new JSONArray();
        for (LiaisonHotel liaison : stock.getStockLiaisonHotel()) {
            if (idVoyage == liaison.getFkVoyage()) {
                // Trouve les objets Hotel correspondant à l'id de l'hôtel et les place
                // dans un JSONArray
                for (Hotel hotel : stock.getStockHotel()) {
                    if (hotel.getIdHotel() == liaison.getFkHotel()) {
                        JSONObject jsonHotel = new JSONObject();
                        jsonHotel.put("hotel", hotel);
                        jsonArrayHotel.put(jsonHotel);
                    }
                }
            }
        }
        jsonObject.put("listeHotels", (Object) jsonArrayHotel);

        // Trouve l'id des visites correspondant associées à l'id du voyage
        JSONArray jsonArrayVisite = new JSONArray();
        for (LiaisonVisite liaison : stock.getStockLiaisonVisite()) {
            if (idVoyage == liaison.getFkVoyage()) {
                // Trouve les objets Visite correspondant à l'id de la visite et les place
                // dans un JSONArray
                for (Visite visite : stock.getStockVisite()) {
                    if (visite.getIdVisites() == liaison.getFkVisite()) {
                        JSONObject jsonVisite = new JSONObject();
                        jsonVisite.put("visite", visite);
                        jsonArrayVisite.put(jsonVisite);
                    }
                }
            }
        }
        jsonObject.put("listeVisites", (Object) jsonArrayVisite);

        return jsonObject;
    }

    /**
     * Met en forme et écrit le fichier json depuis un objet File.
     * 
     * @param jsonObject
     * @param file
     * @return true si écriture réussie
     */
    private boolean saveJsonToFile(JSONObject jsonObject, File file) {
        final String ERREUR_ECRITURE = "Erreur lors de l'écriture du fichier.";

        boolean isWritten = false;

        try {
            PrintWriter writer;
            writer = new PrintWriter(file, "UTF-8");
            // Met en forme le String pour pouvoir être lu par un humain
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement je = gson.fromJson(jsonObject.toString(), JsonElement.class);
            String prettyJsonString = gson.toJson(je);
            // écrit le fchier
            writer.write(prettyJsonString);
            writer.close();
            isWritten = true;
        } catch (IOException ex) {
            Popup.popupErreur(ERREUR_ECRITURE);
        }

        return isWritten;
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
