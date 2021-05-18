-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema *SchemaName*
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema *SchemaName*
-- -----------------------------------------------------
USE `*SchemaName*` ;

-- -----------------------------------------------------
-- Table `*SchemaName*`.`Position`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`Position` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`Position` (
  `idPosition` TINYINT NOT NULL AUTO_INCREMENT,
  `position` VARCHAR(45) NOT NULL DEFAULT 'No Position Defined',
  PRIMARY KEY (`idPosition`),
  UNIQUE INDEX `idPosition_UNIQUE` (`idPosition` ASC),
  UNIQUE INDEX `position_UNIQUE` (`position` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`Role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`Role` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`Role` (
  `idRole` TINYINT NOT NULL AUTO_INCREMENT,
  `role` VARCHAR(45) NOT NULL DEFAULT 'No Role Defined',
  `read` TINYINT NOT NULL DEFAULT 1,
  `write` TINYINT NOT NULL DEFAULT 0,
  `edit` TINYINT NOT NULL DEFAULT 0,
  `delete` TINYINT NOT NULL DEFAULT 0,
  `create` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`idRole`),
  UNIQUE INDEX `idRole_UNIQUE` (`idRole` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`User` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`User` (
  `idUser` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL DEFAULT 'No Username Defined',
  `email` VARCHAR(45) NOT NULL DEFAULT 'No Email Defined',
  `password` VARCHAR(45) NOT NULL DEFAULT 'No Password Defined',
  `firstName` VARCHAR(45) NOT NULL DEFAULT 'No Name Defined',
  `lastName` VARCHAR(45) NOT NULL DEFAULT 'No Last Name Defined',
  `employeeNumber` INT NOT NULL DEFAULT 0,
  `numLogin` INT NULL DEFAULT 0,
  `Position_idPosition` TINYINT NOT NULL,
  `Role_idRole` TINYINT NOT NULL,
  PRIMARY KEY (`idUser`),
  UNIQUE INDEX `idUser_UNIQUE` (`idUser` ASC),
  INDEX `fk_User_Position1_idx` (`Position_idPosition` ASC),
  INDEX `fk_User_Role1_idx` (`Role_idRole` ASC),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  UNIQUE INDEX `employeeNumber_UNIQUE` (`employeeNumber` ASC),
  CONSTRAINT `fk_User_Position1`
    FOREIGN KEY (`Position_idPosition`)
    REFERENCES `*SchemaName*`.`Position` (`idPosition`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_User_Role1`
    FOREIGN KEY (`Role_idRole`)
    REFERENCES `*SchemaName*`.`Role` (`idRole`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`Language`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`Language` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`Language` (
  `idLanguage` TINYINT NOT NULL AUTO_INCREMENT,
  `language` VARCHAR(45) NOT NULL DEFAULT 'No Language Set',
  PRIMARY KEY (`idLanguage`),
  UNIQUE INDEX `idLanguage_UNIQUE` (`idLanguage` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`ExamType`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`ExamType` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`ExamType` (
  `idExamType` TINYINT NOT NULL AUTO_INCREMENT,
  `examType` VARCHAR(45) NOT NULL DEFAULT 'Written',
  PRIMARY KEY (`idExamType`),
  UNIQUE INDEX `idExamType_UNIQUE` (`idExamType` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`GradeScale`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`GradeScale` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`GradeScale` (
  `idGradeScale` TINYINT NOT NULL AUTO_INCREMENT,
  `scale` VARCHAR(20) NOT NULL DEFAULT 'A-F',
  PRIMARY KEY (`idGradeScale`),
  UNIQUE INDEX `idGradeScale_UNIQUE` (`idGradeScale` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`TeachingLocation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`TeachingLocation` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`TeachingLocation` (
  `idTeachingLocation` INT NOT NULL AUTO_INCREMENT,
  `narvik` TINYINT NOT NULL DEFAULT 0,
  `tromso` TINYINT NOT NULL DEFAULT 0,
  `alta` TINYINT NOT NULL DEFAULT 0,
  `moIRana` TINYINT NOT NULL DEFAULT 0,
  `bodo` TINYINT NOT NULL DEFAULT 0,
  `webBased` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`idTeachingLocation`),
  UNIQUE INDEX `idTeachingLocation_UNIQUE` (`idTeachingLocation` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`CourseDescription`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`CourseDescription` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`CourseDescription` (
  `idCourse` INT NOT NULL AUTO_INCREMENT,
  `year` SMALLINT NOT NULL,
  `dateCreated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dateChanged` DATETIME NULL,
  `singleCourse` TINYINT NOT NULL,
  `continuation` TINYINT NOT NULL,
  `semesterFall` TINYINT NOT NULL,
  `semesterSpring` TINYINT NOT NULL,
  `archived` TINYINT NULL,
  `CreatedBy_idUser` INT NOT NULL,
  `Language_idLanguage` TINYINT NOT NULL,
  `ExamType_idExamType` TINYINT NOT NULL,
  `GradeScale_idGradeScale` TINYINT NOT NULL,
  `TeachingLocation_idTeachingLocation` INT NOT NULL,
  `ArchivedBy_idUser` INT NULL,
  PRIMARY KEY (`idCourse`),
  UNIQUE INDEX `CourseCode_UNIQUE` (`idCourse` ASC),
  INDEX `fk_CourseDescription_CreatedBy_User1_idx` (`CreatedBy_idUser` ASC),
  INDEX `fk_CourseDescription_Language1_idx` (`Language_idLanguage` ASC),
  INDEX `fk_CourseDescription_ExamType1_idx` (`ExamType_idExamType` ASC),
  INDEX `fk_CourseDescription_GradeScale1_idx` (`GradeScale_idGradeScale` ASC),
  INDEX `fk_CourseDescription_TeachingLocation1_idx` (`TeachingLocation_idTeachingLocation` ASC),
  INDEX `fk_CourseDescription_ArchivedBy_User1_idx1` (`ArchivedBy_idUser` ASC),
  CONSTRAINT `fk_CourseDescription_CreatedBy_User1`
    FOREIGN KEY (`CreatedBy_idUser`)
    REFERENCES `*SchemaName*`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CourseDescription_Language1`
    FOREIGN KEY (`Language_idLanguage`)
    REFERENCES `*SchemaName*`.`Language` (`idLanguage`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CourseDescription_ExamType1`
    FOREIGN KEY (`ExamType_idExamType`)
    REFERENCES `*SchemaName*`.`ExamType` (`idExamType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CourseDescription_GradeScale1`
    FOREIGN KEY (`GradeScale_idGradeScale`)
    REFERENCES `*SchemaName*`.`GradeScale` (`idGradeScale`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CourseDescription_TeachingLocation1`
    FOREIGN KEY (`TeachingLocation_idTeachingLocation`)
    REFERENCES `*SchemaName*`.`TeachingLocation` (`idTeachingLocation`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CourseDescription_ArchivedBy_User1`
    FOREIGN KEY (`ArchivedBy_idUser`)
    REFERENCES `*SchemaName*`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`Degree`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`Degree` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`Degree` (
  `idDegree` TINYINT NOT NULL AUTO_INCREMENT,
  `degree` VARCHAR(45) NOT NULL DEFAULT 'No Degree Defined',
  PRIMARY KEY (`idDegree`),
  UNIQUE INDEX `idDegree_UNIQUE` (`idDegree` ASC),
  UNIQUE INDEX `degree_UNIQUE` (`degree` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`StudyPoints`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`StudyPoints` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`StudyPoints` (
  `idStudyPoints` TINYINT NOT NULL AUTO_INCREMENT,
  `studyPoints` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`idStudyPoints`),
  UNIQUE INDEX `studyPoints_UNIQUE` (`studyPoints` ASC),
  UNIQUE INDEX `idStudyPoints_UNIQUE` (`idStudyPoints` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`CourseCode`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`CourseCode` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`CourseCode` (
  `idCourseCode` INT NOT NULL AUTO_INCREMENT,
  `courseCode` VARCHAR(45) NOT NULL DEFAULT 'XXX-0000',
  `name_nb_no` VARCHAR(45) NOT NULL DEFAULT 'Intet Navn Definert',
  `name_nb_nn` VARCHAR(45) NOT NULL DEFAULT 'Inkje Namn Definert',
  `name_en_gb` VARCHAR(45) NOT NULL DEFAULT 'No Name Defined',
  `Degree_idDegree` TINYINT NOT NULL,
  `StudyPoints_idStudyPoints` TINYINT NOT NULL,
  PRIMARY KEY (`idCourseCode`),
  INDEX `fk_CourseCode_Degree1_idx` (`Degree_idDegree` ASC),
  INDEX `fk_CourseCode_StudyPoints1_idx` (`StudyPoints_idStudyPoints` ASC),
  UNIQUE INDEX `idCourseCode_UNIQUE` (`idCourseCode` ASC),
  UNIQUE INDEX `courseCode_UNIQUE` (`courseCode` ASC),
  CONSTRAINT `fk_CourseCode_Degree1`
    FOREIGN KEY (`Degree_idDegree`)
    REFERENCES `*SchemaName*`.`Degree` (`idDegree`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CourseCode_StudyPoints1`
    FOREIGN KEY (`StudyPoints_idStudyPoints`)
    REFERENCES `*SchemaName*`.`StudyPoints` (`idStudyPoints`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`Prerequisites`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`Prerequisites` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`Prerequisites` (
  `idPrerequisites` INT NOT NULL AUTO_INCREMENT,
  `required` TINYINT NOT NULL DEFAULT 0,
  `CourseDescription_idCourse` INT NOT NULL,
  `CourseCode_idCourseCode` INT NOT NULL,
  PRIMARY KEY (`idPrerequisites`),
  UNIQUE INDEX `idPrerequisites_UNIQUE` (`idPrerequisites` ASC),
  INDEX `fk_Prerequisites_CourseDescription1_idx` (`CourseDescription_idCourse` ASC),
  INDEX `fk_Prerequisites_CourseCode1_idx` (`CourseCode_idCourseCode` ASC),
  CONSTRAINT `fk_Prerequisites_CourseDescription1`
    FOREIGN KEY (`CourseDescription_idCourse`)
    REFERENCES `*SchemaName*`.`CourseDescription` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Prerequisites_CourseCode1`
    FOREIGN KEY (`CourseCode_idCourseCode`)
    REFERENCES `*SchemaName*`.`CourseCode` (`idCourseCode`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`Comments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`Comments` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`Comments` (
  `idComment` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL DEFAULT 'No Title Defined',
  `content` VARCHAR(240) NOT NULL DEFAULT 'No Content',
  `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CourseDescription_idCourse` INT NOT NULL,
  `User_idUser` INT NOT NULL,
  PRIMARY KEY (`idComment`),
  UNIQUE INDEX `idComments_UNIQUE` (`idComment` ASC),
  INDEX `fk_Comments_CourseDescription1_idx` (`CourseDescription_idCourse` ASC),
  INDEX `fk_comments_User1_idx` (`User_idUser` ASC),
  CONSTRAINT `fk_Comments_CourseDescription1`
    FOREIGN KEY (`CourseDescription_idCourse`)
    REFERENCES `*SchemaName*`.`CourseDescription` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Comments_User1`
    FOREIGN KEY (`User_idUser`)
    REFERENCES `*SchemaName*`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`OffersOnlineStudents`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`OffersOnlineStudents` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`OffersOnlineStudents` (
  `idOffersOnlineStudents` INT NOT NULL AUTO_INCREMENT,
  `streaming` TINYINT NOT NULL DEFAULT 0,
  `webMeetingLecture` TINYINT NOT NULL DEFAULT 0,
  `webMeetingEvening` TINYINT NOT NULL DEFAULT 0,
  `followUp` TINYINT NOT NULL DEFAULT 0,
  `organizedArrangements` TINYINT NOT NULL DEFAULT 0,
  `other` VARCHAR(200) NULL DEFAULT 'No Description',
  `CourseDescription_idCourse` INT NOT NULL,
  PRIMARY KEY (`idOffersOnlineStudents`),
  UNIQUE INDEX `idOffersOnlineStudents_UNIQUE` (`idOffersOnlineStudents` ASC),
  INDEX `fk_OffersOnlineStudents_CourseDescription1_idx` (`CourseDescription_idCourse` ASC),
  CONSTRAINT `fk_Offers_OnlineStudents_CourseDescription1`
    FOREIGN KEY (`CourseDescription_idCourse`)
    REFERENCES `*SchemaName*`.`CourseDescription` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`CourseCoordinator`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`CourseCoordinator` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`CourseCoordinator` (
  `User_idUser` INT NOT NULL,
  `CourseDescription_idCourse` INT NOT NULL,
  `CoursePart` VARCHAR(45) NOT NULL DEFAULT 'Part Not Defined',
  PRIMARY KEY (`User_idUser`, `CourseDescription_idCourse`),
  INDEX `fk_CourseDescription_has_USer_User1_idx` (`User_idUser` ASC),
  INDEX `fk_CourseCoordinator_CourseDescription1_idx` (`CourseDescription_idCourse` ASC),
  CONSTRAINT `fk_CourseDescription_has_User_User1`
    FOREIGN KEY (`User_idUser`)
    REFERENCES `*SchemaName*`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CourseCoordinator_CourseDescription1`
    FOREIGN KEY (`CourseDescription_idCourse`)
    REFERENCES `*SchemaName*`.`CourseDescription` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`CourseLog`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`CourseLog` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`CourseLog` (
  `idCourseLog` INT NOT NULL AUTO_INCREMENT,
  `dateChanged` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `User_idUser` INT NOT NULL,
  `CourseDescription_idCourse` INT NOT NULL,
  PRIMARY KEY (`idCourseLog`),
  INDEX `fk_CourseLog_User1_idx` (`User_idUser` ASC),
  INDEX `fk_CourseLog_CourseDescription1_idx` (`CourseDescription_idCourse` ASC),
  UNIQUE INDEX `idCourseLog_UNIQUE` (`idCourseLog` ASC),
  CONSTRAINT `fk_CourseLog_User1`
    FOREIGN KEY (`User_idUser`)
    REFERENCES `*SchemaName*`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CourseLog_CourseDescription1`
    FOREIGN KEY (`CourseDescription_idCourse`)
    REFERENCES `*SchemaName*`.`CourseDescription` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`Approval`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`Approval` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`Approval` (
  `idApproval` INT NOT NULL AUTO_INCREMENT,
  `approvalDeadline` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `approved` TINYINT NULL DEFAULT 0,
  `approvedDate` DATETIME NULL,
  `approvedCourseCoordinator` TINYINT NULL DEFAULT 0,
  `approvedDateCourseCoordinator` DATETIME NULL,
  `approvedInstituteLeader` TINYINT NULL DEFAULT 0,
  `approvedDateInstituteLeader` DATETIME NULL,
  `CourseDescription_idCourse` INT NOT NULL,
  PRIMARY KEY (`idApproval`),
  UNIQUE INDEX `idApproval_UNIQUE` (`idApproval` ASC),
  INDEX `fk_Approval_CourseDescriptions1_idx` (`CourseDescription_idCourse` ASC),
  CONSTRAINT `fk_Approval_CourseDescription1`
    FOREIGN KEY (`CourseDescription_idCourse`)
    REFERENCES `*SchemaName*`.`CourseDescription` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`AcademicContent`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`AcademicContent` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`AcademicContent` (
  `idAcademicContent` INT NOT NULL AUTO_INCREMENT,
  `academicContent` VARCHAR(300) NOT NULL DEFAULT 'No Description',
  `dateCreated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idAcademicContent`),
  UNIQUE INDEX `idAcademicContent_UNIQUE` (`idAcademicContent` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`LearningMethods`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`LearningMethods` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`LearningMethods` (
  `idLearningMethods` INT NOT NULL AUTO_INCREMENT,
  `learningMethods` VARCHAR(150) NOT NULL DEFAULT 'No Description',
  `dateCreated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idLearningMethods`),
  UNIQUE INDEX `idLearningMethods_UNIQUE` (`idLearningMethods` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`CompetenceGoals`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`CompetenceGoals` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`CompetenceGoals` (
  `idCompetenceGoals` INT NOT NULL AUTO_INCREMENT,
  `competenceGoals` VARCHAR(1000) NOT NULL DEFAULT 'No Description',
  `dateCreated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idCompetenceGoals`),
  UNIQUE INDEX `idCompetenceGoals_UNIQUE` (`idCompetenceGoals` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`WorkRequirements`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`WorkRequirements` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`WorkRequirements` (
  `idWorkRequirements` INT NOT NULL AUTO_INCREMENT,
  `workRequirements` VARCHAR(500) NOT NULL DEFAULT 'No Description',
  `dateCreated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idWorkRequirements`),
  UNIQUE INDEX `idCompetenceGoals_UNIQUE` (`idWorkRequirements` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`CourseLeader`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`CourseLeader` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`CourseLeader` (
  `idCourseLeader` INT NOT NULL AUTO_INCREMENT,
  `User_idUser` INT NOT NULL,
  `CourseCode_idCourseCode` INT NOT NULL,
  PRIMARY KEY (`idCourseLeader`),
  INDEX `fk_User_has_CourseCode_CourseCode1_idx` (`CourseCode_idCourseCode` ASC),
  INDEX `fk_User_has_CourseCode_User1_idx` (`User_idUser` ASC),
  UNIQUE INDEX `idCourseLeader_UNIQUE` (`idCourseLeader` ASC),
  CONSTRAINT `fk_User_has_CourseCode_User1`
    FOREIGN KEY (`User_idUser`)
    REFERENCES `*SchemaName*`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_User_has_CourseCode_CourseCode1`
    FOREIGN KEY (`CourseCode_idCourseCode`)
    REFERENCES `*SchemaName*`.`CourseCode` (`idCourseCode`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`CourseCode_has_CourseDescription`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`CourseCode_has_CourseDescription` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`CourseCode_has_CourseDescription` (
  `CourseCode_idCourseCode` INT NOT NULL,
  `CourseDescription_idCourse` INT NOT NULL,
  PRIMARY KEY (`CourseCode_idCourseCode`, `CourseDescription_idCourse`),
  INDEX `fk_CourseCode_has_CourseDescription_CourseDescription1_idx` (`CourseDescription_idCourse` ASC),
  INDEX `fk_CourseCode_has_CourseDescription_CourseCode1_idx` (`CourseCode_idCourseCode` ASC),
  CONSTRAINT `fk_CourseCode_has_CourseDescription_CourseCode1`
    FOREIGN KEY (`CourseCode_idCourseCode`)
    REFERENCES `*SchemaName*`.`CourseCode` (`idCourseCode`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CourseCode_has_CourseDescription_CourseDescription1`
    FOREIGN KEY (`CourseDescription_idCourse`)
    REFERENCES `*SchemaName*`.`CourseDescription` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`AcademicContent_has_CourseDescription`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`AcademicContent_has_CourseDescription` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`AcademicContent_has_CourseDescription` (
  `AcademicContent_idAcademicContent` INT NOT NULL,
  `CourseDescription_idCourse` INT NOT NULL,
  PRIMARY KEY (`AcademicContent_idAcademicContent`, `CourseDescription_idCourse`),
  INDEX `fk_AcademicContent_has_CourseDescription_CourseDescription1_idx` (`CourseDescription_idCourse` ASC),
  INDEX `fk_AcademicContent_has_CourseDescription_AcademicContent1_idx` (`AcademicContent_idAcademicContent` ASC),
  CONSTRAINT `fk_AcademicContent_has_CourseDescription_AcademicContent1`
    FOREIGN KEY (`AcademicContent_idAcademicContent`)
    REFERENCES `*SchemaName*`.`AcademicContent` (`idAcademicContent`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_AcademicContent_has_CourseDescription_CourseDescription1`
    FOREIGN KEY (`CourseDescription_idCourse`)
    REFERENCES `*SchemaName*`.`CourseDescription` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`LearningMethods_has_CourseDescription`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`LearningMethods_has_CourseDescription` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`LearningMethods_has_CourseDescription` (
  `LearningMethods_idLearningMethods` INT NOT NULL,
  `CourseDescription_idCourse` INT NOT NULL,
  PRIMARY KEY (`LearningMethods_idLearningMethods`, `CourseDescription_idCourse`),
  INDEX `fk_LearningMethods_has_CourseDescription_CourseDescription1_idx` (`CourseDescription_idCourse` ASC),
  INDEX `fk_LearningMethods_has_CourseDescription_LearningMethods1_idx` (`LearningMethods_idLearningMethods` ASC),
  CONSTRAINT `fk_LearningMethods_has_CourseDescription_LearningMethods1`
    FOREIGN KEY (`LearningMethods_idLearningMethods`)
    REFERENCES `*SchemaName*`.`LearningMethods` (`idLearningMethods`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_LearningMethods_has_CourseDescription_CourseDescription1`
    FOREIGN KEY (`CourseDescription_idCourse`)
    REFERENCES `*SchemaName*`.`CourseDescription` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`WorkRequirements_has_CourseDescription`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`WorkRequirements_has_CourseDescription` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`WorkRequirements_has_CourseDescription` (
  `WorkRequirements_idWorkRequirements` INT NOT NULL,
  `CourseDescription_idCourse` INT NOT NULL,
  PRIMARY KEY (`WorkRequirements_idWorkRequirements`, `CourseDescription_idCourse`),
  INDEX `fk_WorkRequirements_has_CourseDescription_CourseDescription_idx` (`CourseDescription_idCourse` ASC),
  INDEX `fk_WorkRequirements_has_CourseDescription_WorkRequirements1_idx` (`WorkRequirements_idWorkRequirements` ASC),
  CONSTRAINT `fk_WorkRequirements_has_CourseDescription_WorkRequirements1`
    FOREIGN KEY (`WorkRequirements_idWorkRequirements`)
    REFERENCES `*SchemaName*`.`WorkRequirements` (`idWorkRequirements`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_WorkRequirements_has_CourseDescription_CourseDescription1`
    FOREIGN KEY (`CourseDescription_idCourse`)
    REFERENCES `*SchemaName*`.`CourseDescription` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`CompetenceGoals_has_CourseDescription`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`CompetenceGoals_has_CourseDescription` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`CompetenceGoals_has_CourseDescription` (
  `CompetenceGoals_idCompetenceGoals` INT NOT NULL,
  `CourseDescription_idCourse` INT NOT NULL,
  PRIMARY KEY (`CompetenceGoals_idCompetenceGoals`, `CourseDescription_idCourse`),
  INDEX `fk_CompetenceGoals_has_CourseDescription_CourseDescription1_idx` (`CourseDescription_idCourse` ASC),
  INDEX `fk_CompetenceGoals_has_CourseDescription_CompetenceGoals1_idx` (`CompetenceGoals_idCompetenceGoals` ASC),
  CONSTRAINT `fk_CompetenceGoals_has_CourseDescription_CompetenceGoals1`
    FOREIGN KEY (`CompetenceGoals_idCompetenceGoals`)
    REFERENCES `*SchemaName*`.`CompetenceGoals` (`idCompetenceGoals`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CompetenceGoals_has_CourseDescription_CourseDescription1`
    FOREIGN KEY (`CourseDescription_idCourse`)
    REFERENCES `*SchemaName*`.`CourseDescription` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `*SchemaName*`.`Statistics`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `*SchemaName*`.`Statistics` ;

CREATE TABLE IF NOT EXISTS `*SchemaName*`.`Statistics` (
  `idStatistics` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`idStatistics`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`Position`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`Position` (`idPosition`, `position`) VALUES (DEFAULT, 'Administrator');
INSERT INTO `*SchemaName*`.`Position` (`idPosition`, `position`) VALUES (DEFAULT, 'Dekan');
INSERT INTO `*SchemaName*`.`Position` (`idPosition`, `position`) VALUES (DEFAULT, 'Instituttleder');
INSERT INTO `*SchemaName*`.`Position` (`idPosition`, `position`) VALUES (DEFAULT, 'Professor');
INSERT INTO `*SchemaName*`.`Position` (`idPosition`, `position`) VALUES (DEFAULT, 'Foreleser');

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`Role`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`Role` (`idRole`, `role`, `read`, `write`, `edit`, `delete`, `create`) VALUES (DEFAULT, 'Administrator', 1, 1, 1, 1, 1);
INSERT INTO `*SchemaName*`.`Role` (`idRole`, `role`, `read`, `write`, `edit`, `delete`, `create`) VALUES (DEFAULT, 'Dekan', 1, 1, 1, 1, 1);
INSERT INTO `*SchemaName*`.`Role` (`idRole`, `role`, `read`, `write`, `edit`, `delete`, `create`) VALUES (DEFAULT, 'Instituttleder', 1, 1, 1, 0, 1);
INSERT INTO `*SchemaName*`.`Role` (`idRole`, `role`, `read`, `write`, `edit`, `delete`, `create`) VALUES (DEFAULT, 'Fagkoordinator', 1, 1, 1, 1, 1);
INSERT INTO `*SchemaName*`.`Role` (`idRole`, `role`, `read`, `write`, `edit`, `delete`, `create`) VALUES (DEFAULT, 'Emneansvarlig', 1, 1, 1, 1, 1);
INSERT INTO `*SchemaName*`.`Role` (`idRole`, `role`, `read`, `write`, `edit`, `delete`, `create`) VALUES (DEFAULT, 'Foreleser', 1, 0, 1, 0, 0);
INSERT INTO `*SchemaName*`.`Role` (`idRole`, `role`, `read`, `write`, `edit`, `delete`, `create`) VALUES (DEFAULT, 'Student', 1, 0, 0, 0, 0);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`User`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`User` (`idUser`, `username`, `email`, `password`, `firstName`, `lastName`, `employeeNumber`, `numLogin`, `Position_idPosition`, `Role_idRole`) VALUES (DEFAULT, 'FrontEndFrode', 'fef@hotOstrich.no', '12345', 'Frode', 'Klode Hode', 2, 11, 4, 5);
INSERT INTO `*SchemaName*`.`User` (`idUser`, `username`, `email`, `password`, `firstName`, `lastName`, `employeeNumber`, `numLogin`, `Position_idPosition`, `Role_idRole`) VALUES (DEFAULT, 'BackEndBen', 'beb@hotOstrich.no', '12345', 'Ben', 'Ten', 3, 5, 4, 5);
INSERT INTO `*SchemaName*`.`User` (`idUser`, `username`, `email`, `password`, `firstName`, `lastName`, `employeeNumber`, `numLogin`, `Position_idPosition`, `Role_idRole`) VALUES (DEFAULT, 'SideSwipeSally', 'sss@hotOstrich.no', '12345', 'Sally', 'Stinson', 4, 7, 4, 5);
INSERT INTO `*SchemaName*`.`User` (`idUser`, `username`, `email`, `password`, `firstName`, `lastName`, `employeeNumber`, `numLogin`, `Position_idPosition`, `Role_idRole`) VALUES (DEFAULT, 'UpTownJam', 'utj@hotOstrich.no', '12345', 'Truls', 'Svendsen', 5, 3, 3, 3);
INSERT INTO `*SchemaName*`.`User` (`idUser`, `username`, `email`, `password`, `firstName`, `lastName`, `employeeNumber`, `numLogin`, `Position_idPosition`, `Role_idRole`) VALUES (DEFAULT, 'DownTownDan', 'dtd@hotOstrich.no', '12345', 'Bob', 'Kåre', 6, 22, 1, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`Language`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`Language` (`idLanguage`, `language`) VALUES (DEFAULT, 'Norsk');
INSERT INTO `*SchemaName*`.`Language` (`idLanguage`, `language`) VALUES (DEFAULT, 'English');

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`ExamType`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`ExamType` (`idExamType`, `examType`) VALUES (DEFAULT, 'Skriftlig');
INSERT INTO `*SchemaName*`.`ExamType` (`idExamType`, `examType`) VALUES (DEFAULT, 'Mappe');
INSERT INTO `*SchemaName*`.`ExamType` (`idExamType`, `examType`) VALUES (DEFAULT, 'Prosjekt');

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`GradeScale`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`GradeScale` (`idGradeScale`, `scale`) VALUES (DEFAULT, 'A-F');
INSERT INTO `*SchemaName*`.`GradeScale` (`idGradeScale`, `scale`) VALUES (DEFAULT, 'Godkjent');

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`TeachingLocation`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`TeachingLocation` (`idTeachingLocation`, `narvik`, `tromso`, `alta`, `moIRana`, `bodo`, `webBased`) VALUES (DEFAULT, 1, 1, 0, 0, 0, 1);
INSERT INTO `*SchemaName*`.`TeachingLocation` (`idTeachingLocation`, `narvik`, `tromso`, `alta`, `moIRana`, `bodo`, `webBased`) VALUES (DEFAULT, 1, 1, 0, 0, 0, 0);
INSERT INTO `*SchemaName*`.`TeachingLocation` (`idTeachingLocation`, `narvik`, `tromso`, `alta`, `moIRana`, `bodo`, `webBased`) VALUES (DEFAULT, 1, 0, 1, 1, 1, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`CourseDescription`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`CourseDescription` (`idCourse`, `year`, `dateCreated`, `dateChanged`, `singleCourse`, `continuation`, `semesterFall`, `semesterSpring`, `archived`, `CreatedBy_idUser`, `Language_idLanguage`, `ExamType_idExamType`, `GradeScale_idGradeScale`, `TeachingLocation_idTeachingLocation`, `ArchivedBy_idUser`) VALUES (DEFAULT, 2020, DEFAULT, NULL, 0, 1, 1, 0, 0, 3, 1, 1, 1, 1, NULL);
INSERT INTO `*SchemaName*`.`CourseDescription` (`idCourse`, `year`, `dateCreated`, `dateChanged`, `singleCourse`, `continuation`, `semesterFall`, `semesterSpring`, `archived`, `CreatedBy_idUser`, `Language_idLanguage`, `ExamType_idExamType`, `GradeScale_idGradeScale`, `TeachingLocation_idTeachingLocation`, `ArchivedBy_idUser`) VALUES (DEFAULT, 2021, DEFAULT, NULL, 0, 1, 1, 1, 0, 2, 1, 2, 1, 2, NULL);
INSERT INTO `*SchemaName*`.`CourseDescription` (`idCourse`, `year`, `dateCreated`, `dateChanged`, `singleCourse`, `continuation`, `semesterFall`, `semesterSpring`, `archived`, `CreatedBy_idUser`, `Language_idLanguage`, `ExamType_idExamType`, `GradeScale_idGradeScale`, `TeachingLocation_idTeachingLocation`, `ArchivedBy_idUser`) VALUES (DEFAULT, 2021, DEFAULT, NULL, 1, 1, 0, 1, 1, 2, 2, 2, 2, 3, NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`Degree`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`Degree` (`idDegree`, `degree`) VALUES (DEFAULT, 'Ingen');
INSERT INTO `*SchemaName*`.`Degree` (`idDegree`, `degree`) VALUES (DEFAULT, 'Forkurs');
INSERT INTO `*SchemaName*`.`Degree` (`idDegree`, `degree`) VALUES (DEFAULT, 'Bachelor');
INSERT INTO `*SchemaName*`.`Degree` (`idDegree`, `degree`) VALUES (DEFAULT, 'Master');
INSERT INTO `*SchemaName*`.`Degree` (`idDegree`, `degree`) VALUES (DEFAULT, 'Doktor');

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`StudyPoints`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`StudyPoints` (`idStudyPoints`, `studyPoints`) VALUES (DEFAULT, 0);
INSERT INTO `*SchemaName*`.`StudyPoints` (`idStudyPoints`, `studyPoints`) VALUES (DEFAULT, 5);
INSERT INTO `*SchemaName*`.`StudyPoints` (`idStudyPoints`, `studyPoints`) VALUES (DEFAULT, 10);
INSERT INTO `*SchemaName*`.`StudyPoints` (`idStudyPoints`, `studyPoints`) VALUES (DEFAULT, 15);
INSERT INTO `*SchemaName*`.`StudyPoints` (`idStudyPoints`, `studyPoints`) VALUES (DEFAULT, 20);
INSERT INTO `*SchemaName*`.`StudyPoints` (`idStudyPoints`, `studyPoints`) VALUES (DEFAULT, 25);
INSERT INTO `*SchemaName*`.`StudyPoints` (`idStudyPoints`, `studyPoints`) VALUES (DEFAULT, 30);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`CourseCode`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`CourseCode` (`idCourseCode`, `courseCode`, `name_nb_no`, `name_nb_nn`, `name_en_gb`, `Degree_idDegree`, `StudyPoints_idStudyPoints`) VALUES (DEFAULT, 'AAA-0000', 'Ingen', 'Inkje', 'None', 1, 1);
INSERT INTO `*SchemaName*`.`CourseCode` (`idCourseCode`, `courseCode`, `name_nb_no`, `name_nb_nn`, `name_en_gb`, `Degree_idDegree`, `StudyPoints_idStudyPoints`) VALUES (DEFAULT, 'BBB-1111', 'Litt', 'Noko', 'Some', 2, 4);
INSERT INTO `*SchemaName*`.`CourseCode` (`idCourseCode`, `courseCode`, `name_nb_no`, `name_nb_nn`, `name_en_gb`, `Degree_idDegree`, `StudyPoints_idStudyPoints`) VALUES (DEFAULT, 'CCC-2222', 'Mer', 'Meir', 'More', 3, 5);
INSERT INTO `*SchemaName*`.`CourseCode` (`idCourseCode`, `courseCode`, `name_nb_no`, `name_nb_nn`, `name_en_gb`, `Degree_idDegree`, `StudyPoints_idStudyPoints`) VALUES (DEFAULT, 'DDD-3333', 'Mye', 'Mykje', 'A Lot', 4, 6);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`Prerequisites`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`Prerequisites` (`idPrerequisites`, `required`, `CourseDescription_idCourse`, `CourseCode_idCourseCode`) VALUES (DEFAULT, 0, 1, 2);
INSERT INTO `*SchemaName*`.`Prerequisites` (`idPrerequisites`, `required`, `CourseDescription_idCourse`, `CourseCode_idCourseCode`) VALUES (DEFAULT, 0, 2, 3);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`Comments`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`Comments` (`idComment`, `title`, `content`, `date`, `CourseDescription_idCourse`, `User_idUser`) VALUES (DEFAULT, 'Bra', 'Dette var en bra kommentar.', DEFAULT, 1, 3);
INSERT INTO `*SchemaName*`.`Comments` (`idComment`, `title`, `content`, `date`, `CourseDescription_idCourse`, `User_idUser`) VALUES (DEFAULT, 'Flott', 'Dette er og en kommentar.', DEFAULT, 2, 3);
INSERT INTO `*SchemaName*`.`Comments` (`idComment`, `title`, `content`, `date`, `CourseDescription_idCourse`, `User_idUser`) VALUES (DEFAULT, 'Ekstra', 'Ekstra kommentar, fordi.', DEFAULT, 2, 3);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`OffersOnlineStudents`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`OffersOnlineStudents` (`idOffersOnlineStudents`, `streaming`, `webMeetingLecture`, `webMeetingEvening`, `followUp`, `organizedArrangements`, `other`, `CourseDescription_idCourse`) VALUES (DEFAULT, 1, 1, 1, 0, 0, '', 1);
INSERT INTO `*SchemaName*`.`OffersOnlineStudents` (`idOffersOnlineStudents`, `streaming`, `webMeetingLecture`, `webMeetingEvening`, `followUp`, `organizedArrangements`, `other`, `CourseDescription_idCourse`) VALUES (DEFAULT, 1, 1, 0, 0, 1, '', 3);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`CourseCoordinator`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`CourseCoordinator` (`User_idUser`, `CourseDescription_idCourse`, `CoursePart`) VALUES (4, 1, 'Programming');
INSERT INTO `*SchemaName*`.`CourseCoordinator` (`User_idUser`, `CourseDescription_idCourse`, `CoursePart`) VALUES (3, 2, 'Database');
INSERT INTO `*SchemaName*`.`CourseCoordinator` (`User_idUser`, `CourseDescription_idCourse`, `CoursePart`) VALUES (4, 3, '');

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`CourseLog`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`CourseLog` (`idCourseLog`, `dateChanged`, `User_idUser`, `CourseDescription_idCourse`) VALUES (DEFAULT, DEFAULT, 4, 1);
INSERT INTO `*SchemaName*`.`CourseLog` (`idCourseLog`, `dateChanged`, `User_idUser`, `CourseDescription_idCourse`) VALUES (DEFAULT, DEFAULT, 3, 2);
INSERT INTO `*SchemaName*`.`CourseLog` (`idCourseLog`, `dateChanged`, `User_idUser`, `CourseDescription_idCourse`) VALUES (DEFAULT, DEFAULT, 2, 3);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`Approval`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`Approval` (`idApproval`, `approvalDeadline`, `approved`, `approvedDate`, `approvedCourseCoordinator`, `approvedDateCourseCoordinator`, `approvedInstituteLeader`, `approvedDateInstituteLeader`, `CourseDescription_idCourse`) VALUES (DEFAULT, DEFAULT, 0, NULL, 0, NULL, 0, NULL, 1);
INSERT INTO `*SchemaName*`.`Approval` (`idApproval`, `approvalDeadline`, `approved`, `approvedDate`, `approvedCourseCoordinator`, `approvedDateCourseCoordinator`, `approvedInstituteLeader`, `approvedDateInstituteLeader`, `CourseDescription_idCourse`) VALUES (DEFAULT, DEFAULT, 0, NULL, 0, NULL, 0, NULL, 2);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`AcademicContent`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`AcademicContent` (`idAcademicContent`, `academicContent`, `dateCreated`) VALUES (DEFAULT, 'Jaja, her var det mye!', DEFAULT);
INSERT INTO `*SchemaName*`.`AcademicContent` (`idAcademicContent`, `academicContent`, `dateCreated`) VALUES (DEFAULT, 'Jajamen!', DEFAULT);
INSERT INTO `*SchemaName*`.`AcademicContent` (`idAcademicContent`, `academicContent`, `dateCreated`) VALUES (DEFAULT, 'Hola, mi amigos!', DEFAULT);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`LearningMethods`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`LearningMethods` (`idLearningMethods`, `learningMethods`, `dateCreated`) VALUES (DEFAULT, 'Hoppsann!', DEFAULT);
INSERT INTO `*SchemaName*`.`LearningMethods` (`idLearningMethods`, `learningMethods`, `dateCreated`) VALUES (DEFAULT, 'Hei', DEFAULT);
INSERT INTO `*SchemaName*`.`LearningMethods` (`idLearningMethods`, `learningMethods`, `dateCreated`) VALUES (DEFAULT, 'Brune Hunder er Awesome!', DEFAULT);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`CompetenceGoals`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`CompetenceGoals` (`idCompetenceGoals`, `competenceGoals`, `dateCreated`) VALUES (DEFAULT, 'Blah', DEFAULT);
INSERT INTO `*SchemaName*`.`CompetenceGoals` (`idCompetenceGoals`, `competenceGoals`, `dateCreated`) VALUES (DEFAULT, 'Nei', DEFAULT);
INSERT INTO `*SchemaName*`.`CompetenceGoals` (`idCompetenceGoals`, `competenceGoals`, `dateCreated`) VALUES (DEFAULT, 'Jo', DEFAULT);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`WorkRequirements`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`WorkRequirements` (`idWorkRequirements`, `workRequirements`, `dateCreated`) VALUES (DEFAULT, 'Blå Kaniner Smaker best', DEFAULT);
INSERT INTO `*SchemaName*`.`WorkRequirements` (`idWorkRequirements`, `workRequirements`, `dateCreated`) VALUES (DEFAULT, 'Struts', DEFAULT);
INSERT INTO `*SchemaName*`.`WorkRequirements` (`idWorkRequirements`, `workRequirements`, `dateCreated`) VALUES (DEFAULT, 'PenguinPowerrrrrrrr', DEFAULT);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`CourseLeader`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`CourseLeader` (`idCourseLeader`, `User_idUser`, `CourseCode_idCourseCode`) VALUES (DEFAULT, 3, 1);
INSERT INTO `*SchemaName*`.`CourseLeader` (`idCourseLeader`, `User_idUser`, `CourseCode_idCourseCode`) VALUES (DEFAULT, 2, 2);
INSERT INTO `*SchemaName*`.`CourseLeader` (`idCourseLeader`, `User_idUser`, `CourseCode_idCourseCode`) VALUES (DEFAULT, 2, 3);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`CourseCode_has_CourseDescription`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`CourseCode_has_CourseDescription` (`CourseCode_idCourseCode`, `CourseDescription_idCourse`) VALUES (1, 1);
INSERT INTO `*SchemaName*`.`CourseCode_has_CourseDescription` (`CourseCode_idCourseCode`, `CourseDescription_idCourse`) VALUES (1, 2);
INSERT INTO `*SchemaName*`.`CourseCode_has_CourseDescription` (`CourseCode_idCourseCode`, `CourseDescription_idCourse`) VALUES (3, 3);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`AcademicContent_has_CourseDescription`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`AcademicContent_has_CourseDescription` (`AcademicContent_idAcademicContent`, `CourseDescription_idCourse`) VALUES (1, 1);
INSERT INTO `*SchemaName*`.`AcademicContent_has_CourseDescription` (`AcademicContent_idAcademicContent`, `CourseDescription_idCourse`) VALUES (1, 2);
INSERT INTO `*SchemaName*`.`AcademicContent_has_CourseDescription` (`AcademicContent_idAcademicContent`, `CourseDescription_idCourse`) VALUES (3, 3);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`LearningMethods_has_CourseDescription`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`LearningMethods_has_CourseDescription` (`LearningMethods_idLearningMethods`, `CourseDescription_idCourse`) VALUES (1, 2);
INSERT INTO `*SchemaName*`.`LearningMethods_has_CourseDescription` (`LearningMethods_idLearningMethods`, `CourseDescription_idCourse`) VALUES (2, 2);
INSERT INTO `*SchemaName*`.`LearningMethods_has_CourseDescription` (`LearningMethods_idLearningMethods`, `CourseDescription_idCourse`) VALUES (3, 2);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`WorkRequirements_has_CourseDescription`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`WorkRequirements_has_CourseDescription` (`WorkRequirements_idWorkRequirements`, `CourseDescription_idCourse`) VALUES (2, 2);
INSERT INTO `*SchemaName*`.`WorkRequirements_has_CourseDescription` (`WorkRequirements_idWorkRequirements`, `CourseDescription_idCourse`) VALUES (1, 3);
INSERT INTO `*SchemaName*`.`WorkRequirements_has_CourseDescription` (`WorkRequirements_idWorkRequirements`, `CourseDescription_idCourse`) VALUES (1, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `*SchemaName*`.`CompetenceGoals_has_CourseDescription`
-- -----------------------------------------------------
START TRANSACTION;
USE `*SchemaName*`;
INSERT INTO `*SchemaName*`.`CompetenceGoals_has_CourseDescription` (`CompetenceGoals_idCompetenceGoals`, `CourseDescription_idCourse`) VALUES (2, 1);
INSERT INTO `*SchemaName*`.`CompetenceGoals_has_CourseDescription` (`CompetenceGoals_idCompetenceGoals`, `CourseDescription_idCourse`) VALUES (2, 2);
INSERT INTO `*SchemaName*`.`CompetenceGoals_has_CourseDescription` (`CompetenceGoals_idCompetenceGoals`, `CourseDescription_idCourse`) VALUES (3, 2);

COMMIT;

