/*
 * Popup.java
 */
package Outils;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * class contenant les différentes popup
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class Popup {

    /**
     * Demande à l'utilisateur une confirmation sur l'action quitter
     * si l'utilisateur valide alors l'application se ferme
     * sinon l'application reste ouverte
     * 
     * @param event
     * @param message
     */
    public static int confirmationQuitter(ActionEvent event, String message) {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // option != null.
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK) {
            return -1;
        }
        return 0;

    }

    /**
     * Message popup affichant les informations d'action
     * 
     * @param message message à afficher
     */
    public static void popupInformation(String message) {
        Alert alertInformation = new Alert(AlertType.INFORMATION);

        alertInformation.setTitle("Information");
        alertInformation.setHeaderText(null);
        alertInformation.setContentText(message);
        System.out.println(message);
        alertInformation.showAndWait();
    }

    /**
     * Message popup affichant les erreurs lors de l'action
     * 
     * @param message message à afficher
     */
    public static void popupErreur(String message) {
        Alert alertError = new Alert(AlertType.ERROR);

        alertError.setTitle("Erreur");
        alertError.setHeaderText(null);
        alertError.setContentText(message);
        System.err.println(message);
        alertError.showAndWait();
    }

    /**
     * Message popup affichant les erreurs lors de l'action
     * 
     * @param messageBuilder message à afficher
     */
    public static void popupErreur(StringBuilder messageBuilder) {
        Alert alertError = new Alert(AlertType.ERROR);

        alertError.setTitle("Erreur");
        alertError.setHeaderText(null);
        alertError.setContentText(messageBuilder + "");
        System.err.println(messageBuilder);
        alertError.showAndWait();
    }

    /* POPUP AjouterPaysController */

    /**
     * Popup qui permet un affichage pour la gestion des voyages qui sont liés
     * uniquement à ce pays
     * 
     * @param messageBuilder liste des voyage qui sont uniquement liés à ce pays
     * @return 1 si on supprime
     */
    public static int popupSuppChoix(StringBuilder messageBuilder) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Attention");
        alert.setHeaderText(messageBuilder + "\nVoulez-vous retirer ce pays et ces voyages ?");
        ButtonType oui = new ButtonType("Oui");
        ButtonType non = new ButtonType("Non");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(oui, non);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == oui) {
            return 1;
        }
        return 2;
    }

    /**
     * Popup qui permet un affichage pour la gestion des voyages qui sont liès à
     * plusieur pays.
     * 
     * @return 1 si on supprime
     */
    public static int popupSupp() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Attention");
        alert.setHeaderText("Ce pays est lié à un ou plusieurs voyages. Voulez-vous le supprimer ?");
        ButtonType oui = new ButtonType("Oui");
        ButtonType non = new ButtonType("Non");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(oui, non);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == oui) {
            return 1;
        }
        return 2;
    }

    /* POPUP pour modifierVoyageController */

    /**
     * Méthode de popup qui permet de savoir l'action que souhaite réaliser
     * utilisateur.
     * 
     * @param nomPays nom du pays
     */
    public static int popupActionVoyage(String nomPays) {
        final String MESSAGE_SUPP = "Que souhaitez-vous faire :"
                + "\n- supprimer seulement la liaison pays"
                + "\n- supprimer le voyage associé à ce pays"
                + "\n- annuler";
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Suppression da la liaison pays : " + nomPays);
        alert.setHeaderText(MESSAGE_SUPP);
        ButtonType pays = new ButtonType("Supprimer la liaison avec pays");
        ButtonType voyage = new ButtonType("Supprimer le voyage et le pays");
        ButtonType annuler = new ButtonType("annuler");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(pays, voyage, annuler);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == pays) {
            return 1;
        } else if (option.get() == voyage) {
            return 2;
        }
        return 3;
    }

    /**
     * Méthode de popup qui permet de savoir l'action que souhaite réaliser
     * utilisateur.
     * 
     * @param nomPays nom du pays
     */
    public static int popupActionPays(String nomPays) {
        final String MESSAGE_SUPP = "Que souhaitez-vous faire :"
                + "\n- supprimer le voyage associé à ce pays"
                + "\n- annuler";
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Suppression da la liaison pays : " + nomPays);
        alert.setHeaderText(MESSAGE_SUPP);
        ButtonType voyage = new ButtonType("Supprimer");
        ButtonType annuler = new ButtonType("Annuler");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(voyage, annuler);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == voyage) {
            return 1;
        }
        return 2;
    }

    /**
     * Méthode de popup qui permet de confirmer que l'utilisateur souhaite
     * supprimer les voyages qui ne possèdent que ce pays en lien.
     * 
     * @param nomPays nom du pays
     */
    public static int popupSuppVoyagePaysUnique(String nomPays) {
        final String MESSAGE_SUPP = "Certains voyage sont liés à ce pays uniquement, êtes-vous sûr de vouloir supprimer ce pays ?";
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Suppression du pays " + nomPays);
        alert.setHeaderText(MESSAGE_SUPP);
        ButtonType oui = new ButtonType("Oui");
        ButtonType non = new ButtonType("Non");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(oui, non);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == oui) {
            return 1;
        }
        return 2;
    }

}
