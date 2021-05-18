<?php

class TeachingLocationService {
    private PDO $db;
    private array $errorMsgs;

    /**
     * TeachingLocationService.class.class constructor.
     * Throws InvalidArgumentException if no valid database-object passed.
     * @param $db Database-object to use for the connection
     */
    public function __construct(?PDO $db)
    {
        //Throw exception if we get a NULL object
        if (is_NULL($db)) {
            throw new InvalidArgumentException("No valid database-object passed: \$db=$db");
        } else {
            $this->db = $db;
        }
    }

    /**
     * Get all available TeachingLocations.
     * Returns an array with TeachingLocation-objects of all available TeachingLocations. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @return array|null Array Containing TeachingLocation-objects or NULL
     */
    public function getAllTeachingLocations() : ?array {
        $arrStudyPoints = array();
        try {
            // No userdata used, no real need to sanitize...
            $stmt = $this->db->prepare("select * from `TeachingLocation` order by `TeachingLocation`.`idTeachingLocation`;");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch StudyPoints-objects from query-results
                while ($StudyPoints = $stmt->fetchObject("TeachingLocation")) {
                    $arrStudyPoints[] = $StudyPoints;
                }
                return $arrStudyPoints;
            } else {
                $this->errorMsgs = $stmt->errorInfo();
                return NULL;
            }

        } catch (Exception $e) {
            //TODO: Move to error.twig or return it in getLastError?
            print $e->getMessage() . PHP_EOL;
        }

        return NULL;
    }

    /**
     * Get data for a specific TeachingLocation given its' ID.
     * Returns a TeachingLocation-object on success, NULL on failure.
     * Call GetLastError to get error-message.
     * @param int $idTeachingLocation Id of TeachingLocation to fetch
     * @return TeachingLocation|null Object of type TeachingLocation containing all the data for a given ID or NULL
     */
    public function getTeachingLocationsById(int $idTeachingLocation) : ?TeachingLocation {
        try{
            //Return NULL if $idTeachingLocation is less than 1
            if(!is_numeric($idTeachingLocation) || $idTeachingLocation < 1){
                $this->errorMsgs = array("\$idTeachingLocation: " . $idTeachingLocation . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `TeachingLocation` where `TeachingLocation`.`idTeachingLocation` = :idTeachingLocation");
            $stmt->bindParam(":idTeachingLocation", $idTeachingLocation, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("TeachingLocation");
                if($result){
                    return $result;
                } else {
                    $this->errorMsgs = $stmt->errorInfo();
                    return NULL;
                }
            } else {
                $this->errorMsgs = $stmt->errorInfo();
                return NULL;
            }

        } catch (InvalidArgumentException $e) {
            //TODO: Move to error.twig or return it in getLastError?
            print $e->getMessage() . PHP_EOL;
        }

        return NULL;
    }

    /**
     * Get data for a specific offer given a TeachingLocation-ID.
     * Returns a TeachingLocation-object on success, NULL on failure.
     * Call GetLastError to get error-message.
     * @param int $CourseDescription_idCourse Id of CourseDescription to fetch TeachingLocations for
     * @return TeachingLocation|null Object of type TeachingLocation containing all the data for a given ID or NULL
     */
    public function getTeachingLocationsByCourseDescription(int $CourseDescription_idCourse) : ?TeachingLocation {
        try{
            //Return NULL if $CourseDescription_idCourse is less than 1
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$idCourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `TeachingLocation` where `TeachingLocation`.`idTeachingLocation` = (select TeachingLocation_idTeachingLocation from `CourseDescription` where `CourseDescription`.`idCourse` = :CourseDescription_idCourse)");
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("TeachingLocation");
                if($result){
                    return $result;
                } else {
                    $this->errorMsgs = $stmt->errorInfo();
                    return NULL;
                }
            } else {
                $this->errorMsgs = $stmt->errorInfo();
                return NULL;
            }

        } catch (InvalidArgumentException $e) {
            //TODO: Move to error.twig or return it in getLastError?
            print $e->getMessage() . PHP_EOL;
        }

        return NULL;
    }

    /**
     * Get data for the latest TeachingLocation-entry.
     * Returns a TeachingLocation-object on success, NULL on failure.
     * Call GetLastError to get error-message.
     * @return TeachingLocation|null Object of type TeachingLocation containing all the data for for the latest entry
     */
    public function getLastEntry() : ?TeachingLocation {
        try{
            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `TeachingLocation` order by `TeachingLocation`.`idTeachingLocation` desc");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("TeachingLocation");
                if($result){
                    return $result;
                } else {
                    $this->errorMsgs = $stmt->errorInfo();
                    return NULL;
                }
            } else {
                $this->errorMsgs = $stmt->errorInfo();
                return NULL;
            }

        } catch (InvalidArgumentException $e) {
            //TODO: Move to error.twig or return it in getLastError?
            print $e->getMessage() . PHP_EOL;
        }

        return NULL;

    }

    /**
     * Add a TeachingLocation entry.
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param bool $narvik Will subject be taught in narvik?
     * @param bool $tromso Will subject be taught in tromsø?
     * @param bool $alta Will subject be taught in alta?
     * @param bool $moIRana Will subject be taught in mo i rana?
     * @param bool $bodo Will subject be taught in bodø?
     * @param bool $webBased Will subject be taught web-based?
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function addTeachingLocationEntry(bool $narvik, bool $tromso, bool $alta, bool $moIRana, bool $bodo, bool $webBased) : bool {
        try{
            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $studyPoints already exists in the table
            $stmt = $this->db->prepare("INSERT INTO `TeachingLocation` (`idTeachingLocation`, `narvik`, `tromso`, `alta`, `moIRana`, `bodo`, `webBased`)
                                                  VALUES (DEFAULT, :narvik, :tromso, :alta, :moIRana, :bodo, :webBased) ; COMMIT;");

            $stmt->bindParam(":narvik", $narvik, PDO::PARAM_BOOL);
            $stmt->bindParam(":tromso", $tromso, PDO::PARAM_BOOL);
            $stmt->bindParam(":alta", $alta, PDO::PARAM_BOOL);
            $stmt->bindParam(":moIRana", $moIRana, PDO::PARAM_BOOL);
            $stmt->bindParam(":bodo", $bodo, PDO::PARAM_BOOL);
            $stmt->bindParam(":webBased", $webBased, PDO::PARAM_BOOL);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                return true;
            } else {
                $this->errorMsgs = $stmt->errorInfo();
                return false;
            }

        } catch (InvalidArgumentException $e) {
            //TODO: Move to error.twig or return it in getLastError?
            print $e->getMessage() . PHP_EOL;
        }

        return false;
    }

    /**
     * Update a TeachingLocation entry.
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idTeachingLocation id of teaching-location to update
     * @param bool $narvik Will subject be taught in narvik?
     * @param bool $tromso Will subject be taught in tromsø?
     * @param bool $alta Will subject be taught in alta?
     * @param bool $moIRana Will subject be taught in mo i rana?
     * @param bool $bodo Will subject be taught in bodø?
     * @param bool $webBased Will subject be taught web-based?
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function updateTeachingLocations(int $idTeachingLocation, bool $narvik, bool $tromso, bool $alta, bool $moIRana, bool $bodo, bool $webBased) : bool {
        try{
            // TODO-RFC: make a new entry with old values, then updating it with new, or just overwrite?
            if(!is_numeric($idTeachingLocation) || $idTeachingLocation < 0){
                $this->errorMsgs = array("\$idTeachingLocation: " . $idTeachingLocation . ": invalid number");
                return false;
            }

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $courseCode already exists in the table
            $stmt = $this->db->prepare("UPDATE `TeachingLocation`
                                                  SET `narvik` = :narvik,
                                                  `tromso` = :tromso,
                                                  `alta` = :alta,
                                                  `moIRana` = :moIRana,
                                                  `bodo` = :bodo,
                                                  `webBased` = :webBased
                                                  WHERE `TeachingLocation`.`idTeachingLocation` = :idTeachingLocation; COMMIT;");

            $stmt->bindParam(":narvik", $narvik, PDO::PARAM_BOOL);
            $stmt->bindParam(":tromso", $tromso, PDO::PARAM_BOOL);
            $stmt->bindParam(":alta", $alta, PDO::PARAM_BOOL);
            $stmt->bindParam(":moIRana", $moIRana, PDO::PARAM_BOOL);
            $stmt->bindParam(":bodo", $bodo, PDO::PARAM_BOOL);
            $stmt->bindParam(":webBased", $webBased, PDO::PARAM_BOOL);
            $stmt->bindParam(":idTeachingLocation", $idTeachingLocation, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                return true;
            } else {
                $this->errorMsgs = $stmt->errorInfo();
                return false;
            }

        } catch (InvalidArgumentException $e) {
            //TODO: Move to error.twig or return it in getLastError?
            print $e->getMessage() . PHP_EOL;
        }

        return false;
    }

    /**
     * Updates a TeachingLocations-entry by object
     * @param TeachingLocation $teachingLocation TeachingLocation-object containing updated TeachingLocation-data
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function updateTeachingLocationsByObject(TeachingLocation $teachingLocation) : bool {

        if(is_null($teachingLocation)){
            $this->errorMsgs = array("\$approval was NULL");
            return false;
        }

        return $this::updateTeachingLocations(
            $teachingLocation->getIdTeachingLocation(),
            $teachingLocation->getNarvik(),
            $teachingLocation->getTromso(),
            $teachingLocation->getAlta(),
            $teachingLocation->getMoIRana(),
            $teachingLocation->getBodo(),
            $teachingLocation->getWebBased());

    }

    /**
     * Delete a TeachingLocation-entry. You probably shouldn't use this...
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idTeachingLocation ID of TeachingLocation to delete
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function deleteTeachingLocationsEntry(int $idTeachingLocation) : bool {
        try{
            if(!is_numeric($idTeachingLocation) || $idTeachingLocation < 1){
                $this->errorMsgs = array("\$idOffersOnlineStudents: " . $idTeachingLocation . ": invalid number");
                return false;
            }

            //Prepare query and bind parameters
            // TODO: Check if there are eny studypoints to delete before trying to delete, as an "empty" delete will still succeed
            $stmt = $this->db->prepare("DELETE FROM `OffersOnlineStudents` WHERE `OffersOnlineStudents`.`idOffersOnlineStudents` = :idOffersOnlineStudents; commit;");
            $stmt->bindParam(":idOffersOnlineStudents", $idTeachingLocation, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                return true;
            } else {
                $this->errorMsgs = $stmt->errorInfo();
                return false;
            }

        } catch (InvalidArgumentException $e) {
            //TODO: Move to error.twig or return it in getLastError?
            print $e->getMessage() . PHP_EOL;
        }

        return false;
    }

    /**
     * Returns array $errorMsgs
     * @return array Array containing the last generated error-message
     */
    public function getLastError() : array {
        // TODO: Format array as a single string
        return $this->errorMsgs;
    }

}