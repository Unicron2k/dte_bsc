<?php
class StudyPointsService
{
    private PDO $db;
    private array $errorMsgs;

    /**
     * StudyPointsService.class.class constructor.
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
     * Get all available studypoints.
     * Returns an array with StudyPoints-objects of all available studypoints. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @return array|null Array Containing StudyPoints-objects or NULL
     */
    public function getAllStudyPoints() : ?array {
        $arrStudyPoints = array();
        try {
            // No userdata used, no real need to sanitize...
            $stmt = $this->db->prepare("select * from `StudyPoints` order by `StudyPoints`.`idStudyPoints`;");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch StudyPoints-objects from query-results
                while ($StudyPoints = $stmt->fetchObject("StudyPoints")) {
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
     * Get data for a specific StudyPoints given its' ID.
     * Returns a StudyPoints-object on success, NULL on failure.
     * Call GetLastError to get error-message.
     * @param int $idStudyPoints StudyPoints-ID of which to get data for
     * @return StudyPoints|null Object of type CourseCode containing all the data for a given ID or NULL
     */
    public function getStudyPoints(int $idStudyPoints) : ?StudyPoints {
        try{
            //Return NULL if $idCourseCode is less than 1
            if(!is_numeric($idStudyPoints) || $idStudyPoints < 1){
                $this->errorMsgs = array("\$idStudyPoints: " . $idStudyPoints . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `StudyPoints` where `idStudyPoints` = :idStudyPoints");
            $stmt->bindParam(":idStudyPoints", $idStudyPoints, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("StudyPoints");
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
     * Add a course code.
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $studyPoints StudyPoints to insert
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function addStudyPoints(int $studyPoints) : bool {
        try{
            //Some courses could, in theory, have 0 study-points associated...
            if(!is_numeric($studyPoints) || $studyPoints < 0){
                $this->errorMsgs = array("\$studyPoints: " . $studyPoints . ": invalid number");
                return false;
            }

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $studyPoints already exists in the table
            $stmt = $this->db->prepare("INSERT INTO `StudyPoints` (`idStudyPoints`, `studyPoints`) VALUES (DEFAULT, :studyPoints); COMMIT;");

            $stmt->bindParam(":studyPoints", $studyPoints, PDO::PARAM_STR);

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
     * Delete a StudyPoint-entry. You probably shouldn't use this...
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idStudyPoints ID of studypoints to delete
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function deleteStudyPoints(int $idStudyPoints) : bool {
        try{
            if(!is_numeric($idStudyPoints) || $idStudyPoints < 1){
                $this->errorMsgs = array("\$idStudyPoints: " . $idStudyPoints . ": invalid number");
                return false;
            }

            //Prepare query and bind parameters
            // TODO: Check if there are eny studypoints to delete before trying to delete, as an "empty" delete will still succeed
            $stmt = $this->db->prepare("DELETE FROM `StudyPoints` WHERE `StudyPoints`.`idStudyPoints` = :idStudyPoints");
            $stmt->bindParam(":idStudyPoints", $idStudyPoints, PDO::PARAM_INT);

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