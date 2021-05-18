<?php
// TODO: Make class "generic" (pass parameter to constructor) to denote which
class CompetenceGoalsService{
    private PDO $db;
    private array $errorMsgs;

    /**
     * CompetenceGoalsService.class constructor.
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
     * Get all available CompetenceGoals-entries.
     * Returns an array with CompetenceGoals-objects of all available CompetenceGoals-entries. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @return array|null Array Containing CompetenceGoals-objects or NULL
     */
    public function getAllEntries() : ?array {
        $arrEntries = array();
        try {
            // No userdata used, no real need to sanitize...
            $stmt = $this->db->prepare("select * from `CompetenceGoals` order by `CompetenceGoals`.`idCompetenceGoals`;");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch Degree-objects from query-results
                while ($entry = $stmt->fetchObject("CompetenceGoals")) {
                    $arrEntries[] = $entry;
                }
                return $arrEntries;
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
     * Get the latest CompetenceGoals-entry for a given CourseDescription-ID
     * Returns the latest CompetenceGoals-object for a given CourseDescription-ID
     * Call GetLastError to get error-message.
     * @param int $idCourseDescription CourseDescription-ID to fetch the latest entry for
     * @return CompetenceGoals|null CompetenceGoals-object for the given CourseDescription-ID
     */
    public function getLastEntryForCourseDescription(int $idCourseDescription) : ?CompetenceGoals {
        $entries = $this->getAllEntriesForCourseDescription($idCourseDescription);
        if(!is_null($entries) && $entries!=false){
            return reset($entries);
        }
        $this->errorMsgs[]="There were no entries for the given CourseDescription-ID";
        return null;
    }

    /**
     * Get all CompetenceGoals-entries for a given CourseDescription-ID
     * Returns an array containing CompetenceGoals-objects for a given CourseDescription-ID
     * Call GetLastError to get error-message.
     * @param int $idCourseDescription CourseDescription-id to fetch entries for
     * @return array|null Array of All relevant CompetenceGoals-objects for the given CourseDescription-ID
     */
    public function getAllEntriesForCourseDescription(int $idCourseDescription) : ?array {
        $arrEntries = array();
        try {
            //Return NULL if $idCourseCode is less than 1
            if(!is_numeric($idCourseDescription) || $idCourseDescription < 1){
                $this->errorMsgs = array("\$idCourseDescription: " . $idCourseDescription . ": invalid number");
                return NULL;
            }
            $stmt = $this->db->prepare("select `CompetenceGoals`.`idCompetenceGoals`, `CompetenceGoals`.`competenceGoals`, `CompetenceGoals`.`dateCreated` 
                                                    from `CompetenceGoals` left join `CompetenceGoals_has_CourseDescription` on `CompetenceGoals_has_CourseDescription`.`CompetenceGoals_idCompetenceGoals` = `CompetenceGoals`.`idCompetenceGoals`
                                                    where `CompetenceGoals_has_CourseDescription`.`CourseDescription_idCourse` = :idCourseDescription 
                                                    order by `CompetenceGoals`.`dateCreated` desc;");

            $stmt->bindParam(":idCourseDescription", $idCourseDescription, PDO::PARAM_INT);
            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch Degree-objects from query-results
                while ($entry = $stmt->fetchObject("CompetenceGoals")) {
                    $arrEntries[] = $entry;
                }
                return $arrEntries;
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
     * Get the latest CompetenceGoals-entry for a given CourseCode-ID
     * Returns the latest CompetenceGoals-object for a given CourseCode-ID
     * Call GetLastError to get error-message.
     * @param int $idCourseCode CourseCode-id to fetch the latest entry for
     * @return CompetenceGoals|null CompetenceGoals-object for the given CourseCode-ID
     */
    public function getLastEntryForCourseCode(int $idCourseCode) : ?CompetenceGoals {
        $entries = $this->getAllEntriesForCourseCode($idCourseCode);
        if(!is_null($entries) && $entries!=false){
            return reset($entries);
        }
        $this->errorMsgs[]="There were no entries for the given CourseDescription-ID";
        return null;
    }

    /**
     * Get all CompetenceGoals-entries for a given CourseCode-ID
     * Returns an array containing CompetenceGoals-objects for a given CourseCode-ID
     * Call GetLastError to get error-message.
     * @param int $idCourseCode CourseCode-id to fetch entries for
     * @return array|null Array of All relevant CompetenceGoals-objects for the given CourseCode-ID
     */
    public function getAllEntriesForCourseCode(int $idCourseCode) : ?array {
        $arrEntries = array();
        try {
            //Return NULL if $idCourseCode is less than 1
            if(!is_numeric($idCourseCode) || $idCourseCode < 1){
                $this->errorMsgs = array("\$idCourseCode: " . $idCourseCode . ": invalid number");
                return NULL;
            }

            $stmt = $this->db->prepare("select `CG`.`idCompetenceGoals`, `CG`.`competenceGoals`, `CG`.`dateCreated`
                                                    from `CompetenceGoals` as `CG`
                                                        left join `CompetenceGoals_has_CourseDescription` as `CG_has_CD`
                                                        on `CG_has_CD`.`CompetenceGoals_idCompetenceGoals` = `CG`.`idCompetenceGoals`
                                                    where `CG_has_CD`.`CourseDescription_idCourse`
                                                    in (
                                                        select `CC_has_CD`.`CourseDescription_idCourse`
                                                        from `CourseCode_has_CourseDescription` as `CC_has_CD`
                                                        where `CC_has_CD`.`CourseCode_idCourseCode` = :idCourseCode
                                                    )
                                                    group by `CG`.`idCompetenceGoals`, `CG`.`competenceGoals`, `CG`.`dateCreated`
                                                    order by `CG`.`dateCreated` desc;");

            $stmt->bindValue(":idCourseCode", $idCourseCode, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch Degree-objects from query-results
                while ($entry = $stmt->fetchObject("CompetenceGoals")) {
                    $arrEntries[] = $entry;
                }
                return $arrEntries;
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
     * Get data for the latest CompetenceGoals-entry.
     * Returns a CompetenceGoals-object on success, NULL on failure.
     * Call GetLastError to get error-message.
     * @return CompetenceGoals|null Object of type CompetenceGoals containing all the data for for the latest entry
     */
    public function getLastEntry() : ?CompetenceGoals {
        try{
            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `CompetenceGoals` order by `CompetenceGoals`.`dateCreated` desc");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("CompetenceGoals");
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
     * Get data for a specific CompetenceGoals-entry given its' ID.
     * Returns a CompetenceGoals-object on success, NULL on failure.
     * Call GetLastError to get error-message.
     * @param int $idCompetenceGoals CompetenceGoals-ID of which to get data for
     * @return CompetenceGoals|null Object of type CompetenceGoals containing all the data for a given ID or NULL
     */
    public function getEntry(int $idCompetenceGoals) : ?CompetenceGoals {
        try{
            //Return NULL if $idCourseCode is less than 1
            if(!is_numeric($idCompetenceGoals) || $idCompetenceGoals < 1){
                $this->errorMsgs = array("\$idCompetenceGoals: " . $idCompetenceGoals . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `CompetenceGoals` where `idCompetenceGoals` = :idCompetenceGoals");
            $stmt->bindParam(":idCompetenceGoals", $idCompetenceGoals, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("CompetenceGoals");
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
     * Add an CompetenceGoals-entry and associated entry in connecting table
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idCourseDescription Id of course-description the entry belongs to
     * @param string $competenceGoals string-entry to insert
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function addEntry(int $idCourseDescription, string $competenceGoals) : bool {
        try{
            if(!is_numeric($idCourseDescription) || $idCourseDescription < 1){
                $this->errorMsgs = array("\$idCourseDescription: " . $idCourseDescription . ": invalid number");
                return false;
            }

            if(!is_null($competenceGoals) || !empty($competenceGoals)){
                $competenceGoals = substr($competenceGoals, 0, 6000) ;
            } else {
                $this->errorMsgs[] = "\$competenceGoals cannot be empty or null!";
                return false;
            }

            $this->db->beginTransaction();
            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $degree already exists in the table
            $stmt = $this->db->prepare("INSERT INTO `CompetenceGoals` (`idCompetenceGoals`, `competenceGoals`, `dateCreated` ) VALUES (DEFAULT, :competenceGoals, DEFAULT);");

            $stmt->bindParam(":competenceGoals", $competenceGoals, PDO::PARAM_STR);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $idCompetenceGoals = $this->db->lastInsertId();
                //close current query before proceeding to the next
                $stmt->closeCursor();

                $stmtConnectingTable = $this->db->prepare("INSERT INTO `CompetenceGoals_has_CourseDescription` (`CompetenceGoals_idCompetenceGoals`, `CourseDescription_idCourse`) VALUES (:CompetenceGoals_idCompetenceGoals, :CourseDescription_idCourse);");
                $stmtConnectingTable->bindParam(":CompetenceGoals_idCompetenceGoals", $idCompetenceGoals, PDO::PARAM_INT);
                $stmtConnectingTable->bindParam(":CourseDescription_idCourse", $idCourseDescription, PDO::PARAM_INT);

                //Execute query, and set return-status + any potential error-message
                if($stmtConnectingTable->execute()){
                    $this->db->commit();
                    return true;
                } else {
                    $this->errorMsgs = $stmtConnectingTable->errorInfo();
                    $this->errorMsgs[] = $this->db->rollBack()?"Database-write failed. Database rollback succeeded.":"Fatal error! Could not rollback failed database-write!";
                    return false;
                }

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
     * Delete a CompetenceGoals-entry and all entries associated with it in the connecting table.
     * You probably shouldn't use this...
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idCompetenceGoals ID of CompetenceGoals-entry to delete
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function deleteEntry(int $idCompetenceGoals) : bool {
        try{

            //Return NULL if $idCompetenceGoals is less than 1
            if(!is_numeric($idCompetenceGoals) || $idCompetenceGoals < 1){
                $this->errorMsgs = array("\$idCompetenceGoals: " . $idCompetenceGoals . ": invalid number");
                return false;
            }

            $this->db->beginTransaction();

            //Prepare query and bind parameters
            // TODO: Check if there are any entry to delete before trying to delete, as an "empty" delete will still succeed
            $stmtConnectingTable = $this->db->prepare( "DELETE FROM `CompetenceGoals_has_CourseDescription` WHERE `CompetenceGoals_has_CourseDescription`.`CompetenceGoals_idCompetenceGoals` = :idCompetenceGoals;");
            $stmtConnectingTable->bindParam(":idCompetenceGoals", $idCompetenceGoals, PDO::PARAM_INT);

            if($stmtConnectingTable->execute()){
                //close current query,
                $stmtConnectingTable->closeCursor();

                $stmt = $this->db->prepare("DELETE FROM `CompetenceGoals` WHERE `CompetenceGoals`.`idCompetenceGoals` = :idCompetenceGoals;");
                $stmt->bindParam(":idCompetenceGoals", $idCompetenceGoals, PDO::PARAM_INT);

                //Execute query, and set return-status + any potential error-message
                if($stmt->execute()){
                    $this->db->commit();
                    return true;
                } else {
                    $this->errorMsgs = $stmt->errorInfo();
                    $this->errorMsgs[] = $this->db->rollBack()?"Database-write failed. Database rollback succeeded.":"Fatal error! Could not rollback failed database-write!";
                    return false;
                }
            } else {
                $this->errorMsgs = $stmtConnectingTable->errorInfo();
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