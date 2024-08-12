-- Crée la bd et la remplace si elle existe déjà
DROP DATABASE IF EXISTS bd;
CREATE DATABASE bd;
USE bd;

-- Crée la table contenant les pays
CREATE TABLE `t_pays` (
  `idPays` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `formalites` varchar(255) DEFAULT NULL,
  `conseil` text,
  PRIMARY KEY (`idPays`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Crée la table contenant les villes
CREATE TABLE `t_ville` (
  `idVille` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `groupe` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`idVille`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Crée la table des voyages
CREATE TABLE `t_voyages` (
  `idVoyage` int NOT NULL AUTO_INCREMENT,
  `designation` varchar(255) NOT NULL,
  `transportAller` enum('avion','car','train') NOT NULL,
  `transportRetour` enum('avion','car','train') NOT NULL,
  `villeDestination` varchar(255) NOT NULL,
  `villeRetour` varchar(255) NOT NULL,
  `nbJour` int DEFAULT NULL,
  PRIMARY KEY (`idVoyage`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Crée la table contenant les tarifs
CREATE TABLE `t_tarif` (
  `idTarif` int NOT NULL AUTO_INCREMENT,
  `dateDepart` date NOT NULL,
  `dateRetour` date NOT NULL,
  `prixG1` int DEFAULT -1,
  `prixG2` int DEFAULT -1,
  `prixG3` int DEFAULT -1,
  `prixG4` int DEFAULT -1,
  `prixG5` int DEFAULT -1,
  `prixG6` int DEFAULT -1,
  `prixG7` int DEFAULT -1,
  `prixG8` int DEFAULT -1,
  `prixG9` int DEFAULT -1,
  `prixG10` int DEFAULT -1,
  `Tvoyage` int NOT NULL,
  PRIMARY KEY (`idTarif`),
  KEY `fk_Tvoyage` (`Tvoyage`),
  CONSTRAINT `fk_Tvoyage` FOREIGN KEY (`Tvoyage`) REFERENCES `t_voyages` (`idVoyage`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Crée la table contenant les Hotel 
CREATE TABLE `t_hotel` (
  `idHotel` int NOT NULL AUTO_INCREMENT,
  `nomHotel` varchar(45) NOT NULL,
  `adresse` varchar(45) NOT NULL,
  `ville` varchar(45) NOT NULL,
  `numTelephone` varchar(10) NOT NULL,
  PRIMARY KEY (`idHotel`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Crée la table contenant les visites
CREATE TABLE `t_visite` (
  `idVisite` int NOT NULL AUTO_INCREMENT,
  `titre` varchar(45) NOT NULL,
  `description` mediumtext NOT NULL,
  PRIMARY KEY (`idVisite`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Crée une table pour lier les voyages avec les pays
CREATE TABLE `t_liaison_voyage_pays` (
  `idLiaison_Voyage_Pays` int NOT NULL AUTO_INCREMENT,
  `LpVoyage` int NOT NULL,
  `pays` int NOT NULL,
  PRIMARY KEY (`idLiaison_Voyage_Pays`),
  KEY `fk_Voyage1_idx` (`LpVoyage`),
  KEY `fk_paysBis_idx` (`pays`),
  CONSTRAINT `fk_paysBis` FOREIGN KEY (`pays`) REFERENCES `t_pays` (`idPays`) ON DELETE CASCADE,
  CONSTRAINT `fk_Voyage1` FOREIGN KEY (`LpVoyage`) REFERENCES `t_voyages` (`idVoyage`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Crée une table pour lier les voyages avec les hotels
CREATE TABLE `t_liaison_voyage_hotel` (
  `idLiaison_Voyage_Hotel` int NOT NULL AUTO_INCREMENT,
  `voyage` int NOT NULL,
  `hotel` int NOT NULL,
  `debut_reservation` int NOT NULL,
  `fin_reservation` int NOT NULL,
  PRIMARY KEY (`idLiaison_Voyage_Hotel`),
  KEY `Voyage2_idx` (`voyage`),
  KEY `hotel_idx` (`hotel`),
  CONSTRAINT `hotel` FOREIGN KEY (`hotel`) REFERENCES `t_hotel` (`idHotel`) ON DELETE CASCADE, 
  CONSTRAINT `Voyage2` FOREIGN KEY (`voyage`) REFERENCES `t_voyages` (`idVoyage`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- Crée une table pour lier les voyages avec les visites
CREATE TABLE `t_liaison_voyage_visite` (
  `idLiaison_Voyage_Visite` int NOT NULL AUTO_INCREMENT,
  `voyage` int NOT NULL,
  `visite` int NOT NULL,
  PRIMARY KEY (`idLiaison_Voyage_Visite`),
  KEY `Voyage3_idx` (`voyage`),
  KEY `Visite_idx` (`visite`),
  CONSTRAINT `Visite` FOREIGN KEY (`visite`) REFERENCES `t_visite` (`idVisite`) ON DELETE CASCADE,
  CONSTRAINT `Voyage3` FOREIGN KEY (`voyage`) REFERENCES `t_voyages` (`idVoyage`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;