-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema stud_v19_ese
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema stud_v19_ese
-- -----------------------------------------------------
USE `stud_v19_ese` ;

-- -----------------------------------------------------
-- Table `stud_v19_ese`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stud_v19_ese`.`User` ;

CREATE TABLE IF NOT EXISTS `stud_v19_ese`.`User` (
  `UserID` INT NOT NULL AUTO_INCREMENT,
  `Username` VARCHAR(30) NOT NULL,
  `Email` VARCHAR(45) NOT NULL,
  `PassHash` VARCHAR(45) NOT NULL,
  `FirstName` VARCHAR(50) NOT NULL,
  `LastName` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`UserID`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `UserID_UNIQUE` ON `stud_v19_ese`.`User` (`UserID` ASC);

CREATE UNIQUE INDEX `Username_UNIQUE` ON `stud_v19_ese`.`User` (`Username` ASC);


-- -----------------------------------------------------
-- Table `stud_v19_ese`.`Catalogue`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stud_v19_ese`.`Catalogue` ;

CREATE TABLE IF NOT EXISTS `stud_v19_ese`.`Catalogue` (
  `CatalogueID` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NULL,
  `Catalogue_CatalogueID` INT NOT NULL,
  PRIMARY KEY (`CatalogueID`),
  CONSTRAINT `fk_Catalogue_Catalogue1`
    FOREIGN KEY (`Catalogue_CatalogueID`)
    REFERENCES `stud_v19_ese`.`Catalogue` (`CatalogueID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Catalogue_Catalogue1_idx` ON `stud_v19_ese`.`Catalogue` (`Catalogue_CatalogueID` ASC);


-- -----------------------------------------------------
-- Table `stud_v19_ese`.`File`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stud_v19_ese`.`File` ;

CREATE TABLE IF NOT EXISTS `stud_v19_ese`.`File` (
  `FiletID` INT NOT NULL AUTO_INCREMENT,
  `File` MEDIUMBLOB NOT NULL,
  `UserID` INT NOT NULL,
  `Author` VARCHAR(30) NOT NULL,
  `Filename` VARCHAR(45) NOT NULL,
  `ServerFilename` VARCHAR(45) NOT NULL,
  `Size` VARCHAR(10) NOT NULL,
  `Mimetype` VARCHAR(10) NOT NULL,
  `Description` VARCHAR(45) NULL,
  `Accessed` BIGINT NOT NULL,
  `Views` BIGINT NOT NULL,
  `Date` DATETIME NOT NULL,
  `Access` TINYINT(1) NOT NULL,
  `User_UserID` INT NOT NULL,
  `CatalogueID` INT NOT NULL,
  `Catalogue_CatalogueID` INT NOT NULL,
  PRIMARY KEY (`FiletID`, `User_UserID`, `Catalogue_CatalogueID`),
  CONSTRAINT `fk_Document_User1`
    FOREIGN KEY (`User_UserID`)
    REFERENCES `stud_v19_ese`.`User` (`UserID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_File_Catalogue1`
    FOREIGN KEY (`Catalogue_CatalogueID`)
    REFERENCES `stud_v19_ese`.`Catalogue` (`CatalogueID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `DocumentID_UNIQUE` ON `stud_v19_ese`.`File` (`FiletID` ASC);

CREATE UNIQUE INDEX `Filename_UNIQUE` ON `stud_v19_ese`.`File` (`Filename` ASC);

CREATE UNIQUE INDEX `ServerFilename_UNIQUE` ON `stud_v19_ese`.`File` (`ServerFilename` ASC);

CREATE INDEX `fk_Document_User1_idx` ON `stud_v19_ese`.`File` (`User_UserID` ASC);

CREATE INDEX `fk_File_Catalogue1_idx` ON `stud_v19_ese`.`File` (`Catalogue_CatalogueID` ASC);


-- -----------------------------------------------------
-- Table `stud_v19_ese`.`AccessRights`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stud_v19_ese`.`AccessRights` ;

CREATE TABLE IF NOT EXISTS `stud_v19_ese`.`AccessRights` (
  `UserID` INT NOT NULL,
  `DocumentID` INT NOT NULL,
  `User_UserID` INT NOT NULL,
  `Document_DocumentID` INT NOT NULL,
  PRIMARY KEY (`UserID`, `DocumentID`, `Document_DocumentID`),
  CONSTRAINT `fk_AccessRights_User1`
    FOREIGN KEY (`User_UserID`)
    REFERENCES `stud_v19_ese`.`User` (`UserID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_AccessRights_Document1`
    FOREIGN KEY (`Document_DocumentID`)
    REFERENCES `stud_v19_ese`.`File` (`FiletID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_AccessRights_User1_idx` ON `stud_v19_ese`.`AccessRights` (`User_UserID` ASC);

CREATE INDEX `fk_AccessRights_Document1_idx` ON `stud_v19_ese`.`AccessRights` (`Document_DocumentID` ASC);


-- -----------------------------------------------------
-- Table `stud_v19_ese`.`Tags`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stud_v19_ese`.`Tags` ;

CREATE TABLE IF NOT EXISTS `stud_v19_ese`.`Tags` (
  `TagID` INT NOT NULL AUTO_INCREMENT,
  `Tags` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`TagID`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `TagID_UNIQUE` ON `stud_v19_ese`.`Tags` (`TagID` ASC);

CREATE UNIQUE INDEX `Tags_UNIQUE` ON `stud_v19_ese`.`Tags` (`Tags` ASC);


-- -----------------------------------------------------
-- Table `stud_v19_ese`.`Comments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stud_v19_ese`.`Comments` ;

CREATE TABLE IF NOT EXISTS `stud_v19_ese`.`Comments` (
  `CommentID` INT NOT NULL AUTO_INCREMENT,
  `FileID` INT NOT NULL,
  `UserID` INT NOT NULL,
  `Date` DATETIME NOT NULL,
  `Comment` VARCHAR(250) NOT NULL,
  `File_FileID` INT NOT NULL,
  `File_User_UserID` INT NOT NULL,
  `User_UserID` INT NOT NULL,
  `Comments_CommentID` INT NOT NULL,
  `Comments_File_FileID` INT NOT NULL,
  `Comments_File_User_UserID` INT NOT NULL,
  `Comments_User_UserID` INT NOT NULL,
  PRIMARY KEY (`CommentID`, `File_FileID`, `File_User_UserID`, `User_UserID`),
  CONSTRAINT `fk_Comments_Document1`
    FOREIGN KEY (`File_FileID` , `File_User_UserID`)
    REFERENCES `stud_v19_ese`.`File` (`FiletID` , `User_UserID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Comments_User1`
    FOREIGN KEY (`User_UserID`)
    REFERENCES `stud_v19_ese`.`User` (`UserID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Comments_Comments1`
    FOREIGN KEY (`Comments_CommentID` , `Comments_File_FileID` , `Comments_File_User_UserID` , `Comments_User_UserID`)
    REFERENCES `stud_v19_ese`.`Comments` (`CommentID` , `File_FileID` , `File_User_UserID` , `User_UserID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `CommentID_UNIQUE` ON `stud_v19_ese`.`Comments` (`CommentID` ASC);

CREATE INDEX `fk_Comments_Document1_idx` ON `stud_v19_ese`.`Comments` (`File_FileID` ASC, `File_User_UserID` ASC);

CREATE INDEX `fk_Comments_User1_idx` ON `stud_v19_ese`.`Comments` (`User_UserID` ASC);

CREATE INDEX `fk_Comments_Comments1_idx` ON `stud_v19_ese`.`Comments` (`Comments_CommentID` ASC, `Comments_File_FileID` ASC, `Comments_File_User_UserID` ASC, `Comments_User_UserID` ASC);


-- -----------------------------------------------------
-- Table `stud_v19_ese`.`DeletedComments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stud_v19_ese`.`DeletedComments` ;

CREATE TABLE IF NOT EXISTS `stud_v19_ese`.`DeletedComments` (
  `DeletedCommentID` INT NOT NULL AUTO_INCREMENT,
  `CommentID` INT NOT NULL,
  `BrukerID` INT NOT NULL,
  `DeletedComment` VARCHAR(250) NOT NULL,
  `DateDeleted` DATETIME NOT NULL,
  `Comments_CommentID` INT NOT NULL,
  `Comments_File_FileID` INT NOT NULL,
  `Comments_File_User_UserID` INT NOT NULL,
  `Comments_User_UserID` INT NOT NULL,
  PRIMARY KEY (`DeletedCommentID`, `Comments_CommentID`, `Comments_File_FileID`, `Comments_File_User_UserID`, `Comments_User_UserID`),
  CONSTRAINT `fk_DeletedComments_Comments1`
    FOREIGN KEY (`Comments_CommentID` , `Comments_File_FileID` , `Comments_File_User_UserID` , `Comments_User_UserID`)
    REFERENCES `stud_v19_ese`.`Comments` (`CommentID` , `File_FileID` , `File_User_UserID` , `User_UserID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `DeletedCommentID_UNIQUE` ON `stud_v19_ese`.`DeletedComments` (`DeletedCommentID` ASC);

CREATE UNIQUE INDEX `CommentID_UNIQUE` ON `stud_v19_ese`.`DeletedComments` (`CommentID` ASC);

CREATE INDEX `fk_DeletedComments_Comments1_idx` ON `stud_v19_ese`.`DeletedComments` (`Comments_CommentID` ASC, `Comments_File_FileID` ASC, `Comments_File_User_UserID` ASC, `Comments_User_UserID` ASC);


-- -----------------------------------------------------
-- Table `stud_v19_ese`.`File_has_Tags`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stud_v19_ese`.`File_has_Tags` ;

CREATE TABLE IF NOT EXISTS `stud_v19_ese`.`File_has_Tags` (
  `File_FileID` INT NOT NULL,
  `Tags_TagID` INT NOT NULL,
  PRIMARY KEY (`File_FileID`, `Tags_TagID`),
  CONSTRAINT `fk_Document_has_Tags_Document`
    FOREIGN KEY (`File_FileID`)
    REFERENCES `stud_v19_ese`.`File` (`FiletID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Document_has_Tags_Tags1`
    FOREIGN KEY (`Tags_TagID`)
    REFERENCES `stud_v19_ese`.`Tags` (`TagID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Document_has_Tags_Tags1_idx` ON `stud_v19_ese`.`File_has_Tags` (`Tags_TagID` ASC);

CREATE INDEX `fk_Document_has_Tags_Document_idx` ON `stud_v19_ese`.`File_has_Tags` (`File_FileID` ASC);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
