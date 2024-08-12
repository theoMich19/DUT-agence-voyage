package Outils;

import javafx.util.converter.IntegerStringConverter;

/**
 * Classe utilisée dans la modification des tarifs
 * L'int est dans un premier temps converti en String pour pouvoir être modifié,
 * puis reconverti en int. En cas d'erreur une popup apparaît.
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class CustomIntegerStringConverter extends IntegerStringConverter {

    private final IntegerStringConverter converter = new IntegerStringConverter();

    @Override
    public String toString(Integer object) {
        final String ERREUR = "Impossible de convertir en String : ";
        try {
            return converter.toString(object);
        } catch (NumberFormatException e) {
            Popup.popupErreur(ERREUR + e);
        }
        return null;
    }

    @Override
    public Integer fromString(String string) {
        final String ERREUR_STRING = " n'est pas un nombre positif.";
        final String ERREUR_NOMBRE = " n'est pas un nombre.";
        try {
            if (converter.fromString(string) < -1) {
                Popup.popupErreur(string + ERREUR_STRING);
                return -1;
            }
            return converter.fromString(string);
        } catch (NumberFormatException e) {
            Popup.popupErreur(string + ERREUR_NOMBRE);
        } catch (Exception e) {
            // gère les cas où la cellule est vide lors de la validation
            System.err.println("Cellule vide lors de la validation");
        }
        return -1;
    }
}