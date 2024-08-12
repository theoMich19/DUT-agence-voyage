/*
 * App.java
 */
package Agence;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

/**
 * Classe contenant la méthode main permettant de lancer l'application.
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Permet le chargement de la connexion et du stock de la bd
        // Evite que la premiere page prenne du temps à charger
        CreateConnection.importConnection();
        StockDonnee.getInstance();

        Parent root;
        try {
            // récupération de la fenetre fxml faite avec SceneBuilder
            root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
            // création d'une scene à partir de la fenetre root
            Scene scene = new Scene(root);
            // récupération de la feuille de style css
            scene.getStylesheets().add("Agence/interface.css");
            // ajout du logo
            primaryStage.getIcons().add(new Image("Agence/logo.png"));
            // ajout du titre de la fenêtre
            primaryStage.setTitle("Agence Évasion / Admin");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}