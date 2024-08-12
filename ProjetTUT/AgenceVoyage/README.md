## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## Edition du code et execution

- Installer le jdk-17 : https://www.oracle.com/java/technologies/downloads/#jdk17-windows
- Installer SceneBuilder : https://gluonhq.com/products/scene-builder/
- Installer JavaFX : https://download2.gluonhq.com/openjfx/17.0.1/openjfx-17.0.1_windows-x64_bin-sdk.zip
- Mettre dans un dossier "Java" le jdk et l'openjfx
- Récupérer les fichiers de code (cf repository)

### Sur VS CODE

- Créer un nouveau projet java (ctrl + shift + p -> Create Java Projet) : "AgenceVoyage"
- Créer un dossier ProjetTUT et y mettre le projet
- Dans l'onglet à gauche, dans "Java Project", ajouter dans "Referenced Librairies" tous les .jar du dossier "lib" de votre openjfx          
- Dans la barre de menu du haut, ajouter une configuration d'execution. Dans le fichier launch.json ajouter la ligne :
           ```"vmArgs": "--module-path \"D:/Java/javafx-sdk-17.0.1/lib\" --add-modules javafx.controls,javafx.fxml"``` (en modifiant le chemin de lib si besoin)
           
### Sur SceneBuilder

- Ouvrir le fichier MainScene.fxml
- Dans l'onglet Controller, spécifier le nom de la classe : MainSceneController
