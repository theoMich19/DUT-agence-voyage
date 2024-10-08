# Agence de voyages
Développement d'une application administrateur d'une agence de voyages.  
L'application est réalisée à l'aide de Java, JavaFX, CSS et SQL  
La connexion à SQL est réalisée à l'aide de l'API JDBC.  

# Ressources
Documents : https://drive.google.com/drive/folders/1YFiyyfPZQjJOavXKIfGgZumMkN9pZv1K?usp=sharing  
ZenHub : https://github.com/dylanroux/agence-voyage#workspaces/zenhub-6170288d46f7de001480b748/board

# Manuel
1) Préparation 
- Avoir une machine sous Windows 10 ou Windows 11.  
- Télécharger le zip à l’adresse https://github.com/dylanroux/agence-voyage/releases  
- Télécharger au moins le jdk 15.0 et l'ajouter à son PATH si ce n'est pas déjà fait https://www.oracle.com/java/technologies/javase/jdk15-archive-downloads.html  
- Télécharger l'installer MySQL https://dev.mysql.com/downloads/file/?id=508936
- Dans l'installer MySQL : télécharger et installer "MySQL Server", "MySQL Workbench" et "Connector/J"
- Pour configurer le serveur : utiliser "Development Computer", spécifier le port 3306 et mettre en mot de passe : root (ne pas changer son nom d'utilisateur "root")
- Le Workbench est l'interface pour manipuler la base de données, il faut donc initialiser la base de données en utilisant le fichier "agencevoyage/ProjetTUT/AgenceVoyage/src/Agence/bd.sql" et l'exécuter.

2) Lancement de l’application  
- Extraire le fichier .zip téléchargé.
- Pour lancer l’application double cliquez sur le fichier launch.bat situé à la racine du dossier nouvellement créé.

3) Utilisation du menu edition  
Lorsque vous lancez l’application vous arrivez sur une première interface exposant le menu d'édition, ce dernier vous permet d'accéder aux différents menus de gestion des différents éléments de l’application. 
À l’aide des différents boutons, vous pouvez accéder au menu d'édition de pays, ville, voyage et tarifs. 
Afin d'accéder à ces menus il suffit de cliquer sur le bouton correspondant et l’interface affichera le menu d’édition correspondant à celui cliqué.

Edition pays :  
Dans ce menu vous pouvez ajouter modifier ou supprimer un pays, ainsi que consulter les pays déjà créés.
Pour ajouter un pays, vous devez renseigner le nom du pays(obligatoire). De plus vous pouvez renseigner des formalités et conseil mais ces derniers ne sont pas obligatoires
Pour supprimer il vous suffit de vous rendre dans le tableau situé en bas de la page, puis sélectionner la ligne concernant le pays que vous souhaitez supprimer puis il faut cliquer sur le bouton supprimer ( attention en cas de suppressions de pays qui est lié à un voyage un message vous demandant une confirmation de suppression vous seras afficher)
Pour modifier un pays, des conseils, ou des formalités vous devez vous rendre dans le tableau d’affichage qui contient toutes les données et modifier directement en cliquant sur le texte; une zone de modification apparaîtra une fois le text cliqué

Edition ville :  
Dans ce menu d’édition, vous pouvez ajouter modifier ou supprimer une ville dans un groupe de ville ainsi que consulter les différents villes ainsi que le groupes auxquels elles sont affecté
Pour ajouter il vous suffit d’écrire le nom de la ville que vous souhaitez intégré et de selectionner un groupe déjà créer ou créer un nouveau groupe en sélectionnant le dernier groupe de la liste
Pour supprimer une ville dans un groupe, vous devez vous rendre dans le tableau de données situé en bas de l’interface puis sélectionner la ligne que vous souhaitez et enfin appuyer sur le bouton de suppression 
Pour modifier vous devez vous rendre dans le tableau de données puis cliquez sur le texte, vous pourrez alors modifier votre ville

Edition voyage :  
Dans ce menu vous pouvez réaliser l’ajout de voyage en renseignant la désignation, la ville de départ, la ville de retour, le pays de destination ainsi que les transport 
Pour ajouter un voyage il faut obligatoirement sélectionner et remplir chacun des champs affiché (designation, ville de destination, ville de retour, les moyens de transport et le pays de destination)
Pour supprimer un voyage de la base de donnée, vous devez vous rendre dans le tableau de données situé en bas de l’interface puis sélectionner le voyage à supprimer puis cliquer sur le bouton indiqué
Pour modifier les informations d’un voyage vous devez vous rendre en bas de l’interface cliquer sur la zone de texte à modifier puis saisir les modifications
une fois un voyage créer vous serez redirigé vers une page d’ajout tarif a partir de laquel vous pouvez créer vos premier tarifs ( le fonctionnement de l’interface d'édition de tarif est expliqué par la suite)

Edition tarif :  
Ce menu permet la création et l’affectation de tarif au voyage sélectionné. Vous pouvez affecter autant de tarifs qu’il y a de groupes de villes.
Pour ajouter des tarifs pour une période vous devez sélectionner le voyage auquel vous voulez affecter des périodes de voyages et des tarifs si vous avez accéder au menu edition de tarif directement par le menu d'édition.En revanche si vous accédez au tarifs après avoir créer un voyage vous n’avez pas à sélectionner ce dernier. Puis les dates de voyages aller et retour. Enfin vous devez saisir les prix pour les groupes de villes existant (les groupes de villes qui n’existe pas possède un prix égale à -1)
Pour supprimer une période de voyage, vous devez vous rendre dans le tableau situé en bas de l’interface puis sélectionner la ligne que vous souhaitez supprimer et cliquer sur le bouton
Pour modifier un tarif ou une période vous devez vous rendre sur le tableau en bas de l'interface puis sélectionne le champ que vous souhaitez modifier

4) Utilisation de la recherche  
Dans le menu situé sur le côté gauche de l’application, vous pouvez accéder aux recherches à n’importe quel moment; cette fonctionnalité vous permettra de réaliser une recherche avec différent filtre ; vous pouvez rechercher des voyages selon :  
- le pays  
- une période
- un tarif max
- un type de voyage 
Les résultats de la recherche s’afficheront sous les différents filtres dans un tableau avec toutes les indications et renseignements.
Il est également possible d'afficher les détails d'un des résultats.

5) Aide  
Dans le menu à gauche vous pouvez accéder à l’aide qui vous permet d’avoir des information sur l’application, un lien vers les notes de version et un lien vers un pdf qui donne des aides sur l'utilisation de l'application.
#   D U T - a g e n c e - v o y a g e  
 