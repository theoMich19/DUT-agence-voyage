/*
 * CreerTarifController.java                                        07 nov. 2021
 */
package Tarif;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Agence.StockDonnee;
import Outils.CustomIntegerStringConverter;
import Outils.Popup;
import Voyage.Voyage;

import static Tarif.Tarif.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * Permet de gérer l'interface de Tarif du menu edition
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class CreerTarifController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    /* Déclaration du stock des données */
    StockDonnee stock = StockDonnee.getInstance();

    /* Les différents éléments FXML; boutons, TextField & TextArea */
    @FXML
    private Button btnQuitter;

    @FXML
    private Button btnAide;

    @FXML
    private Button btnOkTarifs;

    @FXML
    private Button btnSupprimerVille;

    @FXML
    private ComboBox<String> cmbVoyage;

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
    private TableView<Tarif> tbvTarif;

    @FXML
    private DatePicker tfDateDebut;

    @FXML
    private DatePicker tfDateFin;

    @FXML
    private TextField tfTarifGrp1;

    @FXML
    private TextField tfTarifGrp10;

    @FXML
    private TextField tfTarifGrp2;

    @FXML
    private TextField tfTarifGrp3;

    @FXML
    private TextField tfTarifGrp4;

    @FXML
    private TextField tfTarifGrp5;

    @FXML
    private TextField tfTarifGrp6;

    @FXML
    private TextField tfTarifGrp7;

    @FXML
    private TextField tfTarifGrp8;

    @FXML
    private TextField tfTarifGrp9;

    @FXML
    private Label lblNbJour;

    private int nbJour;

    private List<Tarif> listeTarifs = stock.getStockTarif();

    private int idVoyageRecup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int groupeMax = stock.getNbGroupeMax();
        // Initialisation du combobox des voyages
        affichCbbVoyage();
        // Liste des Voyages à afficher
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

        // Permet de modifier le champ tarif
        dateDep.setCellFactory(TextFieldTableCell.<Tarif>forTableColumn());
        dateFin.setCellFactory(TextFieldTableCell.<Tarif>forTableColumn());

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

        // convert String to LocalDate
        // Lors de la modif de la cellule des date départ
        dateDep.setOnEditCommit(event -> {
            Tarif row = event.getRowValue();

            // incrémentation date de retour
            String dateDeFin = event.getNewValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(sdf.parse(dateDeFin));
            } catch (ParseException e) {
                Popup.popupErreur("Erreur, le format est incorrecte");
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, nbJour);
            dateDeFin = sdf.format(calendar.getTime());

            row.setDateDepart(event.getNewValue());
            row.setDateRetour(dateDeFin);
            modification(row);

            tbvTarif.refresh();
        });
        // Lors de la modif de la cellule des date retour
        dateFin.setOnEditCommit(event -> {
            Tarif row = event.getRowValue();
            // désincrémentation de la date de départ
            String dateDeBut = event.getNewValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(sdf.parse(dateDeBut));
            } catch (ParseException e) {
                Popup.popupErreur("Erreur, le format est incorrecte");
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, -nbJour);
            dateDeBut = sdf.format(calendar.getTime());

            row.setDateRetour(event.getNewValue());
            modification(row);

            tbvTarif.refresh();
        });
        if (groupeMax >= 1) {
            tGroupe1.setOnEditCommit(event -> {
                Tarif row = event.getRowValue();
                row.setPrixG1(event.getNewValue());
                modification(row);
            });
        } else {
            tGroupe1.setEditable(false);
        }
        if (groupeMax >= 2) {
            tGroupe2.setOnEditCommit(event -> {
                Tarif row = event.getRowValue();
                row.setPrixG2(event.getNewValue());
                modification(row);
            });
        } else {
            tGroupe2.setEditable(false);
        }
        if (groupeMax >= 3) {
            tGroupe3.setOnEditCommit(event -> {
                Tarif row = event.getRowValue();
                row.setPrixG3(event.getNewValue());
                modification(row);
            });
        } else {
            tGroupe3.setEditable(false);
        }
        if (groupeMax >= 4) {
            tGroupe4.setOnEditCommit(event -> {
                Tarif row = event.getRowValue();
                row.setPrixG4(event.getNewValue());
                modification(row);
            });
        } else {
            tGroupe4.setEditable(false);
        }
        if (groupeMax >= 5) {
            tGroupe5.setOnEditCommit(event -> {
                Tarif row = event.getRowValue();
                row.setPrixG5(event.getNewValue());
                modification(row);
            });
        } else {
            tGroupe5.setEditable(false);
        }
        if (groupeMax >= 6) {
            tGroupe6.setOnEditCommit(event -> {
                Tarif row = event.getRowValue();
                row.setPrixG6(event.getNewValue());
                modification(row);
            });
        } else {
            tGroupe6.setEditable(false);
        }
        if (groupeMax >= 7) {
            tGroupe7.setOnEditCommit(event -> {
                Tarif row = event.getRowValue();
                row.setPrixG7(event.getNewValue());
                modification(row);
            });
        } else {
            tGroupe7.setEditable(false);
        }
        if (groupeMax >= 8) {
            tGroupe8.setOnEditCommit(event -> {
                Tarif row = event.getRowValue();
                row.setPrixG8(event.getNewValue());
                modification(row);
            });
        } else {
            tGroupe8.setEditable(false);
        }
        if (groupeMax >= 9) {
            tGroupe9.setOnEditCommit(event -> {
                Tarif row = event.getRowValue();
                row.setPrixG9(event.getNewValue());
                modification(row);
            });
        } else {
            tGroupe9.setEditable(false);
        }
        if (groupeMax >= 10) {
            tGroupe10.setOnEditCommit(event -> {
                Tarif row = event.getRowValue();
                row.setPrixG10(event.getNewValue());
                modification(row);
            });
        } else {
            tGroupe10.setEditable(false);
        }
        tbvTarif.setItems(listeTarifs);
        tbvTarif.getItems();

        if (groupeMax < 1)
            tfTarifGrp1.setEditable(false);
        if (groupeMax < 2)
            tfTarifGrp2.setEditable(false);
        if (groupeMax < 3)
            tfTarifGrp3.setEditable(false);
        if (groupeMax < 4)
            tfTarifGrp4.setEditable(false);
        if (groupeMax < 5)
            tfTarifGrp5.setEditable(false);
        if (groupeMax < 6)
            tfTarifGrp6.setEditable(false);
        if (groupeMax < 7)
            tfTarifGrp7.setEditable(false);
        if (groupeMax < 8)
            tfTarifGrp8.setEditable(false);
        if (groupeMax < 9)
            tfTarifGrp9.setEditable(false);
        if (groupeMax <= 10)
            tfTarifGrp10.setEditable(false);

    }

    /**
     * méthode qui permet d'initialiser les éléments dans la combobox
     */
    public void affichCbbVoyage() {
        List<String> listVoyage = new ArrayList<>();
        for (Voyage voyage : stock.getStockVoyage()) {
            listVoyage.add(voyage.getDesignation());
        }
        ObservableList<String> listNomVoyage = FXCollections.observableList(listVoyage);
        cmbVoyage.setItems(listNomVoyage);
        idVoyageRecup = stock.chercherIdVoyage(cmbVoyage.getSelectionModel().getSelectedItem());

    }

    /**
     * Met à jour la bd puis l'objet
     * 
     * @param row objet Tarif
     */
    public void modification(Tarif row) {
        TarifDAO.update(row);
        row.modifierTarif(row.getDateDepart(), row.getDateRetour(), row.getPrix());
    }

    /**
     * Vérifie si les prix saisis sont dans le bon format
     * 
     * @param tarifGrp tableau de tarif
     * @param prix     tableau prix
     * @return true si les tarifs sont dans le bon format, false sinon
     */
    public boolean verifTarif(String[] tarifGrp, int[] prix) {
        boolean corrects = false;
        int groupeMax = stock.getNbGroupeMax();

        for (int i = 0; i < 10; i++) {
            if (i < groupeMax) { // doit y avoir une valeur
                // verif si diff de null
                if (!tarifGrp[i].equals("")) {
                    // verif si c'est un format int
                    if (isInteger(tarifGrp[i]) || taille(tarifGrp[i])) { // vérif si bon format, inf et supp
                        prix[i] = Integer.parseInt(tarifGrp[i]);
                        corrects = true;
                    }
                } else {
                    prix[i] = -1;
                }
            } else {
                if (!tarifGrp[i].equalsIgnoreCase("")) { // vérif si vide
                    System.err.println("Erreur String non-vide " + i + "\n"
                            + "> La valeur ne peut pas être affectée");
                    corrects = false; // afficher pb
                }
                prix[i] = -1; // affecte -1 si la cellule est vide
            }
        }
        return corrects;
    }

    /**
     * Vérifie que la date de Depart est inferieur à la date de retour, et
     * qu'elle sont superieur ou égale à la date actuelle;
     * 
     * @param datDep     date de départ
     * @param dateRetour date de retour
     * @return true si les dates sont correct, faux dans le cas contraire
     */
    public boolean verifDate(LocalDate dateDep, LocalDate dateRetour, int nbJour) {
        boolean dateValide;
        dateValide = false;
        LocalDate dateActuel = LocalDate.now();
        if (!dateDep.isBefore(dateActuel) && !dateRetour.isBefore(dateDep)
                && nbJour == Duration.between(dateDep.atStartOfDay(), dateRetour.atStartOfDay()).toDays()) {
            dateValide = true;
            for (Tarif aComparer : stock.getStockTarifVoyage(idVoyageRecup)) {
                if (aComparer.getDateDepart().equals(dateDep.toString())
                        && aComparer.getDateRetour().equals(dateRetour.toString())
                        && idVoyageRecup == aComparer.getVoyage()) {
                    dateValide = false;
                }
            }
        }
        return dateValide;
    }

    /**
     * Ajoute les nouveaux tarifs à la bd puis à la liste de villes
     * 
     * @param tarif objet tarif à ajouter
     */
    public void ajout(Tarif tarif) {
        TarifDAO.create(tarif);
        tarif.setIdTarif(TarifDAO.recupId(tarif));
        listeTarifs.add(tarif);
        stock.setStockTarif(listeTarifs);
        // permet de rafraîchir la table view
        tbvTarif.getItems().add(tarif);
        tbvTarif.refresh();
    }

    @FXML
    void btnTarifsClicked(ActionEvent event) {
        final String AJOUT_FONCTIONNE = "La plage de tarif a bien été ajoutée.";
        final String AJOUT_SAISIE = "Problème dans la saisie de tarifs ; causes possibles :\n"
                + " - Trop de valeurs saisies\n"
                + " - Tarif trop élevé ou trop faible\n";
        final String ERR_PERIODE_PASSE = "La période saisie est déjà passé.";
        final String ERR_DURREE = "Vous n'avez pas saisie une période qui correspond à la durée du séjour";
        final String AJOUT_VIDE = "Une des dates n'a pas été saisie.";
        final String AJOUT_TEXTE_DANS_VALEUR = "Les tarifs acceptent seulement des nombres";
        final String MESS_ERR_NO_VOYAGE = "Vous n'avez pas choisi de voyage dans la rubrique voyage";

        try {
            /* Date de début et de fin du voyage */
            LocalDate dateDep = tfDateDebut.getValue();
            LocalDate dateFin = tfDateFin.getValue();

            /* Tableau des tarifs */
            int[] prix = new int[10];
            String[] tarifGrp = new String[10];
            tarifGrp[0] = tfTarifGrp1.getText();
            tarifGrp[1] = tfTarifGrp2.getText();
            tarifGrp[2] = tfTarifGrp3.getText();
            tarifGrp[3] = tfTarifGrp4.getText();
            tarifGrp[4] = tfTarifGrp5.getText();
            tarifGrp[5] = tfTarifGrp6.getText();
            tarifGrp[6] = tfTarifGrp7.getText();
            tarifGrp[7] = tfTarifGrp8.getText();
            tarifGrp[8] = tfTarifGrp9.getText();
            tarifGrp[9] = tfTarifGrp10.getText();

            /*
             * récupération d'un string 'désignation' à la place de idvoyage
             * idvoyage est trouvé à partir de la désignation
             */
            String nomVoyage = cmbVoyage.getSelectionModel().getSelectedItem();

            int idVoyage = -1;

            int nbJour = -1;

            // Recupere les informations necessaire de voyages
            for (Voyage voyage : stock.getStockVoyage()) {
                if (voyage.getDesignation().equals(nomVoyage)) {
                    idVoyage = voyage.getIdVoyage();
                }
            }
            nbJour = stock.nbJourVoyage(idVoyage);
            if (idVoyage == -1) {
                Popup.popupErreur(MESS_ERR_NO_VOYAGE);
                return;
            }

            // vérifie que les dates ont bien été saisies
            if (dateDep != null && dateFin != null) {
                if (verifDate(dateDep, dateFin, nbJour) && verifTarif(tarifGrp, prix)) {

                    Tarif tarif = new Tarif(dateDep.toString(), dateFin.toString(), prix, idVoyage,
                            listeTarifs, stock.getNbGroupeMax());

                    ajout(tarif);
                    Popup.popupInformation(AJOUT_FONCTIONNE);
                } else {
                    if (dateDep.isBefore(LocalDate.now()) || dateFin.isBefore(LocalDate.now())) {
                        Popup.popupErreur(ERR_PERIODE_PASSE);
                    } else if (nbJour != Duration.between(dateDep.atStartOfDay(), dateFin.atStartOfDay()).toDays()) {
                        Popup.popupErreur(ERR_DURREE);
                    } else {
                        Popup.popupErreur(AJOUT_SAISIE);
                    }
                }
            } else {
                Popup.popupErreur(AJOUT_VIDE);
            }
        } catch (Exception e) {
            Popup.popupErreur(AJOUT_TEXTE_DANS_VALEUR);
        }
    }

    @FXML
    void btnSupprimerVilleClicked(ActionEvent event) throws IOException {
        final String SUPPRESSION_ECHEC = "La plage de tarifs n'a pas pu être supprimée.\n"
                                         + "Aucune ligne n'a été sélectionnée.";

        Tarif row = tbvTarif.getSelectionModel().getSelectedItem();

        if (TarifDAO.delete(row)) {
            stock.getStockTarif().remove(row);
            tbvTarif.getItems().removeAll(tbvTarif.getSelectionModel().getSelectedItems());
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
     * Ouvre une nouvelle scène grâce à l'event et l'emplacement du fichier fxml à
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
    void cbbVoyageClicked(ActionEvent event) {
        int groupeMax = stock.getNbGroupeMax();
        idVoyageRecup = stock.chercherIdVoyage(cmbVoyage.getSelectionModel().getSelectedItem());
        tbvTarif.getItems().removeAll(tbvTarif.getItems());
        stock.getStockTarif();
        ObservableList<Tarif> listeTarifs = FXCollections.observableArrayList(stock.getStockTarifVoyage(idVoyageRecup));
        tbvTarif.getItems().addAll(listeTarifs);

        nbJour = stock.nbJourVoyage(idVoyageRecup);
        final String MESSAGE = "Vous pouvez saisir des valeurs jusqu'au 'Tarif %d'";
        lblNbJour.setText(
                String.format("La durée du séjour est de %d jours.\n", nbJour) + String.format(MESSAGE, groupeMax));
    }

    @FXML
    void btnQuitterClicked(ActionEvent event) throws IOException {
        if (Popup.confirmationQuitter(event, Outils.Constantes.CONFIRMATION_QUITTER) == -1) {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
