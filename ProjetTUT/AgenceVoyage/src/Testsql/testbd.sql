-- Tests d'insertion des données


-- insertion ville _groupe ville (5 groupes et 10 villes)
INSERT INTO bd.T_ville (nom) 
VALUES ('Paris'),
    ('Rodez'),
    ('Blagnac'),
    ('Perpignan');

INSERT INTO bd.T_ville (nom, groupe) 
VALUES ('Marseille', 2),
    ('Aix-en-Provence', 2),
    ('Vienne', 3),
    ('Lyon', 3),
    ('Limoges', 4),
    ('Brive La Gaillarde', 4);


-- insertion dans pays (12 pays)
INSERT INTO bd.T_pays (nom, formalites, conseil) 
VALUES ('Afghanistan','passeport','Pays possèdant des conflits interne'),
    ('Irak', 'passeport', 'Pays possèdant des conflits interne'),
    ('Angleterre', 'passeport', "Pays qui n'est plus dans Europe");

INSERT INTO bd.T_pays (nom, formalites) 
VALUES ('Italie','carte identite'),
    ('Pays-Bas','carte identite'),
    ('Irlande','carte identite');

INSERT INTO bd.T_pays (nom, conseil)
VALUES ('Allemagne', 'Possède des restriction sanitaire'),
    ('France', 'Possède des restriction sanitaire'),
    ('Belgique', 'Possède des restriction sanitaire');

INSERT INTO bd.T_pays (nom) 
VALUES ('Suisse'), 
    ('Espagne'),
    ('Portugal');


-- insertion hotel ( 5 hôtels)
INSERT INTO bd.T_hotel (nomHotel, adresse, ville, numTelephone)
VALUES ('Villa', '17 avenue Maurice', 'Paris', 0525788923),
	('Palace avenue', 'Avenu des champs Elisée', 'Paris', 0525788923),
	('Le paradis', '17 rue de la vallee', 'Kaboul', 0645788923),
	('Futura', '36 impasse des rossignole', 'Rome', 0615892347),
	('Le train', '12 boulevard des aires', 'Rennes', 0745891247),
	('La villa', '18 avenue Charles de Gaulles', 'Marseille', 7026485910),
	('Impérial Palace', "All de l'Impérial ", 'Annecy', 0450093000);


-- insertion visite (5 visites)
INSERT INTO bd.T_visite (titre, description) 
VALUES ('Visite du musée', 'Visite guidée du musée du centre ville'),
    ('Visite du Louvre', 'Visite guidée du Louvre'),
    ('La randonnée du lac', 'Départ en ville, 10 min de car puis randonnée de 1heure autoure du lac.'),
    ("Musée de l'Armée, Paris", "Collections d'armes et d'uniformes retraçant les conflits militaires français, et tombeau de Napoléon."),
    ("Let's Visit Airbus - Blagnac", "Deux visites sont possibles : AIRBUS (classique) et AIRBUS XL (permet de découvrir les essais en vol et de monter à bord de l'A400M).");


-- Insertion voyage (6 voyages)
INSERT INTO bd.T_voyages (designation, transportAller, transportRetour, villeDestination, villeRetour, nbJour)
VALUES ('Voyage de rêve','avion','car','Kaboul','Rodez', 8);
	
INSERT INTO bd.T_voyages (designation, transportAller, transportRetour, villeDestination, villeRetour, nbJour)
VALUES ('Voyage gastronomique','train','car','Rome','Blagnac', 10);

INSERT INTO bd.T_voyages (designation, transportAller, transportRetour, villeDestination, villeRetour, nbJour)
VALUES ('Tour Europe','train','car','Paris','Rome', 15);

INSERT INTO bd.T_voyages (designation, transportAller, transportRetour, villeDestination, villeRetour, nbJour)
VALUES ("Séjours balnéaires à l'ile Maurice",'avion','avion','Marseille','Paris', 16);

INSERT INTO bd.T_voyages (designation, transportAller, transportRetour, villeDestination, villeRetour, nbJour)
VALUES ('Séjour au Bénin','avion','avion','Paris','Paris', 9);

INSERT INTO bd.T_voyages (designation, transportAller, transportRetour, villeDestination, villeRetour, nbJour)
VALUES ('Visite de la capitale','car','car','Paris','Paris', 5);

-- Liaison voyage pays (voyages sont lié entre 1 et 3 pays)
INSERT INTO bd.T_liaison_voyage_pays (Lpvoyage, pays) 
VALUES (1, 2), 
    (1, 3), 
    (2, 3), 
    (2, 4), 
    (2, 5), 
    (3, 5), 
    (4, 8), 
    (5, 8),
    (6, 8);


-- Liaison voyage hotel
INSERT INTO bd.T_liaison_voyage_hotel (voyage, hotel, debut_reservation, fin_reservation) 
VALUES(6, 1, 1, 3), 
    (5, 1, 4, 6),
    (2, 4, 1, 6);


-- liaison voyage visite
INSERT INTO bd.T_liaison_voyage_visite (voyage, visite) 
VALUES(6,4),
    (6,5),
    (1,1), 
    (3,2);


-- Insertion tarif	
INSERT INTO bd.T_tarif (dateDepart, dateRetour, prixG1, Tvoyage) VALUES ('2022-05-02','2022-05-10','400', 1);
INSERT INTO bd.T_tarif (dateDepart, dateRetour, prixG1, prixG2, Tvoyage) VALUES ('2022-08-04','2022-08-14','401','130', 2);

INSERT INTO bd.T_tarif (dateDepart, dateRetour, prixG1, prixG2, prixG3, Tvoyage) 
VALUES ('2022-08-02','2022-08-10','152', '20', '15', 1),
    ('2022-08-05','2022-08-15','150', '20', '15', 2),
    ('2022-08-02','2022-08-10','152', '20', '15', 3),
    ('2022-08-05','2022-08-15','150', '20', '15', 4),
    ('2022-09-02','2022-9-07','90', '100', '124', 6),
    ('2022-10-05','2022-10-10','90', '100', '124', 6),
    ('2022-11-02','2022-11-07','90', '100', '135', 6); 
