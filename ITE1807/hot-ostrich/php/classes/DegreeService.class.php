<?php
class DegreeService
{
    private PDO $db;
    private array $errorMsgs;

    /**
     * DegreeService.class constructor.
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
     * Get all available degrees.
     * Returns an array with Degree-objects of all available degrees. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @return array|null Array Containing Degree-objects or NULL
     */
    public function getAllDegrees() : ?array {
        $arrDegrees = array();
        try {
            // No userdata used, no real need to sanitize...
            $stmt = $this->db->prepare("select * from `Degree` order by `Degree`.`idDegree`;");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch Degree-objects from query-results
                while ($Degree = $stmt->fetchObject("Degree")) {
                    $arrDegrees[] = $Degree;
                }
                return $arrDegrees;
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
     * Get data for a specific Degree given its' ID.
     * Returns a CourseCode-object on success, NULL on failure.
     * Call GetLastError to get error-message.
     * @param int $idDegree Degree-ID of which to get data for
     * @return Degree Object of type CourseCode containing all the data for a given ID or NULL
     */
    public function getDegree(int $idDegree) : ?Degree {
        try{
            //Return NULL if $idCourseCode is less than 1
            if(!is_numeric($idDegree) || $idDegree < 1){
                $this->errorMsgs = array("\$idDegree: " . $idDegree . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `Degree` where `idDegree` = :idDegree");
            $stmt->bindParam(":idDegree", $idDegree, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("Degree");
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
     * Add a Degree.
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param string $degree Degree to insert
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function addDegree(string $degree) : bool {
        try{
            //Make sure $degree is of a valid length
            $degree = substr($degree, 0, 45);

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $degree already exists in the table
            $stmt = $this->db->prepare("INSERT INTO `Degree` (`idDegree`, `degree`) VALUES (DEFAULT, :degree); COMMIT;");

            $stmt->bindParam(":degree", $degree, PDO::PARAM_STR);

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
     * Delete a degree. You probably shouldn't use this...
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idDegree ID of degree to delete
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function deleteDegree(int $idDegree) : bool {
        try{
            //Return NULL if $idCourseCode is less than 1
            if(!is_numeric($idDegree) || $idDegree < 1){
                $this->errorMsgs = array("\$idCourseCode: " . $idDegree . ": invalid number");
                return false;
            }

            //Prepare query and bind parameters
            // TODO: Check if there are eny studypoints to delete before trying to delete, as an "empty" delete will still succeed
            $stmt = $this->db->prepare("DELETE FROM `Degree` WHERE `Degree`.`idDegree` = :idDegree");
            $stmt->bindParam(":idDegree", $idDegree, PDO::PARAM_INT);

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