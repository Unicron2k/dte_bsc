<?php
// TODO: Make class "generic" (pass parameter to constructor) to denote which
class LearningMethodsService{
    private PDO $db;
    private array $errorMsgs;

    /**
     * LearningMethodsService.class constructor.
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
     * Get all available LearningMethods-entries.
     * Returns an array with LearningMethods-objects of all available LearningMethods-entries. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @return array|null Array Containing LearningMethods-objects or NULL
     */
    public function getAllEntries() : ?array {
        $arrEntries = array();
        try {
            // No userdata used, no real need to sanitize...
            $stmt = $this->db->prepare("select * from `LearningMethods` order by `LearningMethods`.`idLearningMethods`;");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch Degree-objects from query-results
                while ($entry = $stmt->fetchObject("LearningMethods")) {
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
     * Get the latest LearningMethods-entry for a given CourseDescription-ID
     * Returns the latest LearningMethods-object for a given CourseDescription-ID
     * Call GetLastError to get error-message.
     * @param int $idCourseDescription CourseDescription-ID to fetch the latest entry for
     * @return LearningMethods|null LearningMethods-object for the given CourseDescription-ID
     */
    public function getLastEntryForCourseDescription(int $idCourseDescription) : ?LearningMethods {
        $entries = $this->getAllEntriesForCourseDescription($idCourseDescription);
        if(!is_null($entries) && $entries!=false){
            return reset($entries);
        }
        $this->errorMsgs[]="There were no entries for the given CourseDescription-ID";
        return null;
    }

    /**
     * Get all LearningMethods-entries for a given CourseDescription-ID
     * Returns an array containing LearningMethods-objects for a given CourseDescription-ID
     * Call GetLastError to get error-message.
     * @param int $idCourseDescription CourseDescription-id to fetch entries for
     * @return array|null Array of All relevant LearningMethods-objects for the given CourseDescription-ID
     */
    public function getAllEntriesForCourseDescription(int $idCourseDescription) : ?array {
        $arrEntries = array();
        try {
            //Return NULL if $idCourseCode is less than 1
            if(!is_numeric($idCourseDescription) || $idCourseDescription < 1){
                $this->errorMsgs = array("\$idCourseDescription: " . $idCourseDescription . ": invalid number");
                return NULL;
            }
            $stmt = $this->db->prepare("select `LearningMethods`.`idLearningMethods`, `LearningMethods`.`learningMethods`, `LearningMethods`.`dateCreated` 
                                                    from `LearningMethods` left join `LearningMethods_has_CourseDescription` on `LearningMethods_has_CourseDescription`.`LearningMethods_idLearningMethods` = `LearningMethods`.`idLearningMethods`
                                                    where `LearningMethods_has_CourseDescription`.`CourseDescription_idCourse` = :idCourseDescription 
                                                    order by `LearningMethods`.`dateCreated` desc;");

            $stmt->bindParam(":idCourseDescription", $idCourseDescription, PDO::PARAM_INT);
            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch Degree-objects from query-results
                while ($entry = $stmt->fetchObject("LearningMethods")) {
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
     * Get the latest LearningMethods-entry for a given CourseCode-ID
     * Returns the latest LearningMethods-object for a given CourseCode-ID
     * Call GetLastError to get error-message.
     * @param int $idCourseCode CourseCode-id to fetch the latest entry for
     * @return LearningMethods|null LearningMethods-object for the given CourseCode-ID
     */
    public function getLastEntryForCourseCode(int $idCourseCode) : ?LearningMethods {
        $entries = $this->getAllEntriesForCourseCode($idCourseCode);
        if(!is_null($entries) && $entries!=false){
            return reset($entries);
        }
        $this->errorMsgs[]="There were no entries for the given CourseDescription-ID";
        return null;
    }

    /**
     * Get all LearningMethods-entries for a given CourseCode-ID
     * Returns an array containing LearningMethods-objects for a given CourseCode-ID
     * Call GetLastError to get error-message.
     * @param int $idCourseCode CourseCode-id to fetch entries for
     * @return array|null Array of All relevant LearningMethods-objects for the given CourseCode-ID
     */
    public function getAllEntriesForCourseCode(int $idCourseCode) : ?array {
        $arrEntries = array();
        try {
            //Return NULL if $idCourseCode is less than 1
            if(!is_numeric($idCourseCode) || $idCourseCode < 1){
                $this->errorMsgs = array("\$idCourseCode: " . $idCourseCode . ": invalid number");
                return NULL;
            }

            $stmt = $this->db->prepare("select `LM`.`idLearningMethods`, `LM`.`learningMethods`, `LM`.`dateCreated`
                                                    from `LearningMethods` as `LM`
                                                        left join `LearningMethods_has_CourseDescription` as `LM_has_CD`
                                                        on `LM_has_CD`.`LearningMethods_idLearningMethods` = `LM`.`idLearningMethods`
                                                    where `LM_has_CD`.`CourseDescription_idCourse`
                                                    in (
                                                        select `CC_has_CD`.`CourseDescription_idCourse`
                                                        from `CourseCode_has_CourseDescription` as `CC_has_CD`
                                                        where `CC_has_CD`.`CourseCode_idCourseCode` = :idCourseCode
                                                    )
                                                    group by `LM`.`idLearningMethods`, `LM`.`learningMethods`, `LM`.`dateCreated`
                                                    order by `LM`.`dateCreated` desc;");

            $stmt->bindValue(":idCourseCode", $idCourseCode, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch Degree-objects from query-results
                while ($entry = $stmt->fetchObject("LearningMethods")) {
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
     * Get data for the latest LearningMethods-entry.
     * Returns a LearningMethods-object on success, NULL on failure.
     * Call GetLastError to get error-message.
     * @return LearningMethods|null Object of type LearningMethods containing all the data for for the latest entry
     */
    public function getLastEntry() : ?LearningMethods {
        try{
            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `LearningMethods` order by `LearningMethods`.`dateCreated` desc");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("LearningMethods");
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
     * Get data for a specific LearningMethods-entry given its' ID.
     * Returns a LearningMethods-object on success, NULL on failure.
     * Call GetLastError to get error-message.
     * @param int $idLearningMethods LearningMethods-ID of which to get data for
     * @return LearningMethods|null Object of type LearningMethods containing all the data for a given ID or NULL
     */
    public function getEntry(int $idLearningMethods) : ?LearningMethods {
        try{
            //Return NULL if $idCourseCode is less than 1
            if(!is_numeric($idLearningMethods) || $idLearningMethods < 1){
                $this->errorMsgs = array("\$idLearningMethods: " . $idLearningMethods . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `LearningMethods` where `idLearningMethods` = :idLearningMethods");
            $stmt->bindParam(":idLearningMethods", $idLearningMethods, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("LearningMethods");
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
     * Add an LearningMethods-entry and associated entry in connecting table
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idCourseDescription Id of course-description the entry belongs to
     * @param string $learningMethods string-entry to insert
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function addEntry(int $idCourseDescription, string $learningMethods) : bool {
        try{
            if(!is_numeric($idCourseDescription) || $idCourseDescription < 1){
                $this->errorMsgs = array("\$idCourseDescription: " . $idCourseDescription . ": invalid number");
                return false;
            }

            if(!is_null($learningMethods) || !empty($learningMethods)){
                $learningMethods = substr($learningMethods, 0, 900) ;
            } else {
                $this->errorMsgs[] = "\$learningMethods cannot be empty or null!";
                return false;
            }

            $this->db->beginTransaction();
            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $degree already exists in the table
            $stmt = $this->db->prepare("INSERT INTO `LearningMethods` (`idLearningMethods`, `learningMethods`, `dateCreated` ) VALUES (DEFAULT, :learningMethods, DEFAULT);");

            $stmt->bindParam(":learningMethods", $learningMethods, PDO::PARAM_STR);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $idLearningMethods = $this->db->lastInsertId();
                //close current query before proceeding to the next
                $stmt->closeCursor();

                $stmtConnectingTable = $this->db->prepare("INSERT INTO `LearningMethods_has_CourseDescription` (`LearningMethods_idLearningMethods`, `CourseDescription_idCourse`) VALUES (:LearningMethods_idLearningMethods, :CourseDescription_idCourse);");
                $stmtConnectingTable->bindParam(":LearningMethods_idLearningMethods", $idLearningMethods, PDO::PARAM_INT);
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
     * Delete a LearningMethods-entry and all entries associated with it in the connecting table.
     * You probably shouldn't use this...
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idLearningMethods ID of LearningMethods-entry to delete
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function deleteEntry(int $idLearningMethods) : bool {
        try{

            //Return NULL if $idLearningMethods is less than 1
            if(!is_numeric($idLearningMethods) || $idLearningMethods < 1){
                $this->errorMsgs = array("\$idLearningMethods: " . $idLearningMethods . ": invalid number");
                return false;
            }

            $this->db->beginTransaction();

            //Prepare query and bind parameters
            // TODO: Check if there are any entry to delete before trying to delete, as an "empty" delete will still succeed
            $stmtConnectingTable = $this->db->prepare( "DELETE FROM `LearningMethods_has_CourseDescription` WHERE `LearningMethods_has_CourseDescription`.`LearningMethods_idLearningMethods` = :idLearningMethods;");
            $stmtConnectingTable->bindParam(":idLearningMethods", $idLearningMethods, PDO::PARAM_INT);

            if($stmtConnectingTable->execute()){
                //close current query,
                $stmtConnectingTable->closeCursor();

                $stmt = $this->db->prepare("DELETE FROM `LearningMethods` WHERE `LearningMethods`.`idLearningMethods` = :idLearningMethods;");
                $stmt->bindParam(":idLearningMethods", $idLearningMethods, PDO::PARAM_INT);

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