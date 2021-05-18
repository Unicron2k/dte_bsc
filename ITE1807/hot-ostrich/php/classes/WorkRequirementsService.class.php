<?php
// TODO: Make class "generic" (pass parameter to constructor) to denote which
class WorkRequirementsService{
    private PDO $db;
    private array $errorMsgs;

    /**
     * WorkRequirementsService.class constructor.
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
     * Get all available WorkRequirements-entries.
     * Returns an array with WorkRequirements-objects of all available WorkRequirements-entries. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @return array|null Array Containing WorkRequirements-objects or NULL
     */
    public function getAllEntries() : ?array {
        $arrEntries = array();
        try {
            // No userdata used, no real need to sanitize...
            $stmt = $this->db->prepare("select * from `WorkRequirements` order by `WorkRequirements`.`idWorkRequirements`;");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch Degree-objects from query-results
                while ($entry = $stmt->fetchObject("WorkRequirements")) {
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
     * Get the latest WorkRequirements-entry for a given CourseDescription-ID
     * Returns the latest WorkRequirements-object for a given CourseDescription-ID
     * Call GetLastError to get error-message.
     * @param int $idCourseDescription CourseDescription-ID to fetch the latest entry for
     * @return WorkRequirements|null WorkRequirements-object for the given CourseDescription-ID
     */
    public function getLastEntryForCourseDescription(int $idCourseDescription) : ?WorkRequirements {
        $entries = $this->getAllEntriesForCourseDescription($idCourseDescription);
        if(!is_null($entries) && $entries!=false){
            return reset($entries);
        }
        $this->errorMsgs[]="There were no entries for the given CourseDescription-ID";
        return null;
    }

    /**
     * Get all WorkRequirements-entries for a given CourseDescription-ID
     * Returns an array containing WorkRequirements-objects for a given CourseDescription-ID
     * Call GetLastError to get error-message.
     * @param int $idCourseDescription CourseDescription-id to fetch entries for
     * @return array|null Array of All relevant WorkRequirements-objects for the given CourseDescription-ID
     */
    public function getAllEntriesForCourseDescription(int $idCourseDescription) : ?array {
        $arrEntries = array();
        try {
            //Return NULL if $idCourseCode is less than 1
            if(!is_numeric($idCourseDescription) || $idCourseDescription < 1){
                $this->errorMsgs = array("\$idCourseDescription: " . $idCourseDescription . ": invalid number");
                return NULL;
            }
            $stmt = $this->db->prepare("select `WorkRequirements`.`idWorkRequirements`, `WorkRequirements`.`workRequirements`, `WorkRequirements`.`dateCreated` 
                                                    from `WorkRequirements` left join `WorkRequirements_has_CourseDescription` on `WorkRequirements_has_CourseDescription`.`WorkRequirements_idWorkRequirements` = `WorkRequirements`.`idWorkRequirements`
                                                    where `WorkRequirements_has_CourseDescription`.`CourseDescription_idCourse` = :idCourseDescription 
                                                    order by `WorkRequirements`.`dateCreated` desc;");

            $stmt->bindParam(":idCourseDescription", $idCourseDescription, PDO::PARAM_INT);
            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch Degree-objects from query-results
                while ($entry = $stmt->fetchObject("WorkRequirements")) {
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
     * Get the latest WorkRequirements-entry for a given CourseCode-ID
     * Returns the latest WorkRequirements-object for a given CourseCode-ID
     * Call GetLastError to get error-message.
     * @param int $idCourseCode CourseCode-id to fetch the latest entry for
     * @return WorkRequirements|null WorkRequirements-object for the given CourseCode-ID
     */
    public function getLastEntryForCourseCode(int $idCourseCode) : ?WorkRequirements {
        $entries = $this->getAllEntriesForCourseCode($idCourseCode);
        if(!is_null($entries) && $entries!=false){
            return reset($entries);
        }
        $this->errorMsgs[]="There were no entries for the given CourseDescription-ID";
        return null;
    }

    /**
     * Get all WorkRequirements-entries for a given CourseCode-ID
     * Returns an array containing WorkRequirements-objects for a given CourseCode-ID
     * Call GetLastError to get error-message.
     * @param int $idCourseCode CourseCode-id to fetch entries for
     * @return array|null Array of All relevant WorkRequirements-objects for the given CourseCode-ID
     */
    public function getAllEntriesForCourseCode(int $idCourseCode) : ?array {
        $arrEntries = array();
        try {
            //Return NULL if $idCourseCode is less than 1
            if(!is_numeric($idCourseCode) || $idCourseCode < 1){
                $this->errorMsgs = array("\$idCourseCode: " . $idCourseCode . ": invalid number");
                return NULL;
            }

            $stmt = $this->db->prepare("select `WR`.`idWorkRequirements`, `WR`.`workRequirements`, `WR`.`dateCreated`
                                                    from `WorkRequirements` as `WR`
                                                        left join `WorkRequirements_has_CourseDescription` as `WR_has_CD`
                                                        on `WR_has_CD`.`WorkRequirements_idWorkRequirements` = `WR`.`idWorkRequirements`
                                                    where `WR_has_CD`.`CourseDescription_idCourse`
                                                    in (
                                                        select `CC_has_CD`.`CourseDescription_idCourse`
                                                        from `CourseCode_has_CourseDescription` as `CC_has_CD`
                                                        where `CC_has_CD`.`CourseCode_idCourseCode` = :idCourseCode
                                                    )
                                                    group by `WR`.`idWorkRequirements`, `WR`.`workRequirements`, `WR`.`dateCreated`
                                                    order by `WR`.`dateCreated` desc;");

            $stmt->bindValue(":idCourseCode", $idCourseCode, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch Degree-objects from query-results
                while ($entry = $stmt->fetchObject("WorkRequirements")) {
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
     * Get data for the latest WorkRequirements-entry.
     * Returns a WorkRequirements-object on success, NULL on failure.
     * Call GetLastError to get error-message.
     * @return WorkRequirements|null Object of type WorkRequirements containing all the data for for the latest entry
     */
    public function getLastEntry() : ?WorkRequirements {
        try{
            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `WorkRequirements` order by `WorkRequirements`.`dateCreated` desc");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("WorkRequirements");
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
     * Get data for a specific WorkRequirements-entry given its' ID.
     * Returns a WorkRequirements-object on success, NULL on failure.
     * Call GetLastError to get error-message.
     * @param int $idWorkRequirements WorkRequirements-ID of which to get data for
     * @return WorkRequirements|null Object of type WorkRequirements containing all the data for a given ID or NULL
     */
    public function getEntry(int $idWorkRequirements) : ?WorkRequirements {
        try{
            //Return NULL if $idCourseCode is less than 1
            if(!is_numeric($idWorkRequirements) || $idWorkRequirements < 1){
                $this->errorMsgs = array("\$idWorkRequirements: " . $idWorkRequirements . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `WorkRequirements` where `idWorkRequirements` = :idWorkRequirements");
            $stmt->bindParam(":idWorkRequirements", $idWorkRequirements, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("WorkRequirements");
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
     * Add an WorkRequirements-entry and associated entry in connecting table
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idCourseDescription Id of course-description the entry belongs to
     * @param string $workRequirements string-entry to insert
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function addEntry(int $idCourseDescription, string $workRequirements) : bool {
        try{
            if(!is_numeric($idCourseDescription) || $idCourseDescription < 1){
                $this->errorMsgs = array("\$idCourseDescription: " . $idCourseDescription . ": invalid number");
                return false;
            }

            if(!is_null($workRequirements) || !empty($workRequirements)){
                $workRequirements = substr($workRequirements, 0, 3000) ;
            } else {
                $this->errorMsgs[] = "\$workRequirements cannot be empty or null!";
                return false;
            }

            $this->db->beginTransaction();
            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $degree already exists in the table
            $stmt = $this->db->prepare("INSERT INTO `WorkRequirements` (`idWorkRequirements`, `workRequirements`, `dateCreated` ) VALUES (DEFAULT, :workRequirements, DEFAULT);");

            $stmt->bindParam(":workRequirements", $workRequirements, PDO::PARAM_STR);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $idWorkRequirements = $this->db->lastInsertId();
                //close current query before proceeding to the next
                $stmt->closeCursor();

                $stmtConnectingTable = $this->db->prepare("INSERT INTO `WorkRequirements_has_CourseDescription` (`WorkRequirements_idWorkRequirements`, `CourseDescription_idCourse`) VALUES (:WorkRequirements_idWorkRequirements, :CourseDescription_idCourse);");
                $stmtConnectingTable->bindParam(":WorkRequirements_idWorkRequirements", $idWorkRequirements, PDO::PARAM_INT);
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
     * Delete a WorkRequirements-entry and all entries associated with it in the connecting table.
     * You probably shouldn't use this...
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idWorkRequirements ID of WorkRequirements-entry to delete
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function deleteEntry(int $idWorkRequirements) : bool {
        try{

            //Return NULL if $idWorkRequirements is less than 1
            if(!is_numeric($idWorkRequirements) || $idWorkRequirements < 1){
                $this->errorMsgs = array("\$idWorkRequirements: " . $idWorkRequirements . ": invalid number");
                return false;
            }

            $this->db->beginTransaction();

            //Prepare query and bind parameters
            // TODO: Check if there are any entry to delete before trying to delete, as an "empty" delete will still succeed
            $stmtConnectingTable = $this->db->prepare( "DELETE FROM `WorkRequirements_has_CourseDescription` WHERE `WorkRequirements_has_CourseDescription`.`WorkRequirements_idWorkRequirements` = :idWorkRequirements;");
            $stmtConnectingTable->bindParam(":idWorkRequirements", $idWorkRequirements, PDO::PARAM_INT);

            if($stmtConnectingTable->execute()){
                //close current query,
                $stmtConnectingTable->closeCursor();

                $stmt = $this->db->prepare("DELETE FROM `WorkRequirements` WHERE `WorkRequirements`.`idWorkRequirements` = :idWorkRequirements;");
                $stmt->bindParam(":idWorkRequirements", $idWorkRequirements, PDO::PARAM_INT);

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