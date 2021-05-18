<?php
class CourseLeaderService {

    private PDO $db;
    private array $errorMsgs;

    /**
     * CourseLeaderService.class constructor.
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
     * Get all available courseleaders.
     * Returns an array with CourseLeader-objects of all available courseleaders. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @return array|null Array Containing CourseLeader-objects or NULL
     */
    public function getAllCourseLeaders() : ?array {
        $arrCourseLeaders = array();
        try {
            // No userdata used, no real need to sanitize...
            // TODO: change from * to what is minimally needed to display a course
            $stmt = $this->db->prepare("select * from `CourseLeader` order by `CourseLeader`.`idCourseLeader`;");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch CourseLeader-objects from query-results
                while ($courseLeader = $stmt->fetchObject("CourseLeader")) {
                    $arrCourseLeaders[] = $courseLeader;
                }
                return $arrCourseLeaders;
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
     * Get data for a specific courseleader given its' ID.
     * Returns a CourseLeader-object on success, NULL on failure.
     * Call GetLastError to get error-message.
     * @param int $idCourseLeader CourseLeader-ID of which to get data for
     * @return CourseLeader|null Object of type CourseLeader containing all the data for a given ID or NULL
     */
    public function getCourseLeader(int $idCourseLeader) : ?CourseLeader {
        try{
            //Return NULL if $idCourseLeader is less than 1
            if(!is_numeric($idCourseLeader) || $idCourseLeader < 1){
                $this->errorMsgs = array("\$idCourseLeader: " . $idCourseLeader . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            // TODO: change from * to what is minimally needed to display a course
            $stmt = $this->db->prepare("select * from `CourseLeader` where `CourseLeader`.`idCourseLeader` = :idCourseLeader");
            $stmt->bindParam(":idCourseLeader", $idCourseLeader, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result = $stmt->fetchObject("CourseLeader");
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
     * Get data for a specific courseleader given its' ID.
     * Returns a CourseLeader-object on success, NULL on failure.
     * Call GetLastError to get error-message.
     * @param int $idCourseCode CourseLeader-ID of which to get data for
     * @return CourseLeader|null Object of type CourseLeader containing all the data for a given ID or NULL
     */
    public function getCourseLeaderByCourseCode(int $idCourseCode) : ?CourseLeader {
        try{
            //Return NULL if $idCourseLeader is less than 1
            if(!is_numeric($idCourseCode) || $idCourseCode < 1){
                $this->errorMsgs = array("\$idCourseCode: " . $idCourseCode . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            // TODO: change from * to what is minimally needed to display a course
            $stmt = $this->db->prepare("select * from `CourseLeader` where `CourseLeader`.`CourseCode_idCourseCode` = :idCourseCode");
            $stmt->bindParam(":idCourseCode", $idCourseCode, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result = $stmt->fetchObject("CourseLeader");
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
     * Add a CourseLeader-entry code.
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $User_idUser Foreign-key referencing User
     * @param int $CourseCode_idCourseCode Foreign-key referencing CourseCode
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function addCourseLeader(int $User_idUser, int $CourseCode_idCourseCode) : bool {
        try{
            //Check if numeric arguments are valid
            if(!is_numeric($User_idUser) || $User_idUser < 1){
                $this->errorMsgs = array("\$User_idUser: " . $User_idUser . ": invalid number");
                return false;
            }
            if(!is_numeric($CourseCode_idCourseCode) || $CourseCode_idCourseCode < 1){
                $this->errorMsgs = array("\$CourseCode_idCourseCode: " . $CourseCode_idCourseCode . ": invalid number");
                return false;
            }

            // TODO: Fix database-bug: No unique check for CourseCode_idCourseCode! (Each coursecode should have only one User_idUser associated)
            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $courseCode already exists in the table
            $stmt = $this->db->prepare("INSERT INTO `CourseLeader` (`idCourseLeader`, `User_idUser`, `CourseCode_idCourseCode`)
                                                  VALUES (DEFAULT, :User_idUser, :CourseCode_idCourseCode); COMMIT;");

            $stmt->bindParam(":User_idUser", $User_idUser, PDO::PARAM_INT);
            $stmt->bindParam(":CourseCode_idCourseCode", $CourseCode_idCourseCode, PDO::PARAM_INT);

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
     * Delete a CourseLeader-entry. You probably shouldn't use this...
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idCourseLeader ID of CourseLeader to delete
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function deleteCourseLeader(int $idCourseLeader) : bool {
        try{
            //Return NULL if $idCourseCode is less than 1
            if(!is_numeric($idCourseLeader) || $idCourseLeader < 1){
                $this->errorMsgs = array("\$idCourseLeader: " . $idCourseLeader . ": invalid number");
                return false;
            }

            //Prepare query and bind parameters
            // TODO: Check if there are eny studypoints to delete before trying to delete, as an "empty" delete will still succeed
            $stmt = $this->db->prepare("DELETE FROM `CourseLeader` WHERE `CourseLeader`.`idCourseLeader` = :idCourseCode");
            $stmt->bindParam(":idCourseCode", $idCourseLeader, PDO::PARAM_INT);

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
     * Update a CourseLeader-entry to change
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idCourseLeader CourseLeader-entry to update
     * @param int $User_idUser ID of the new user who will be set as the new course-leader
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function updateCourseLeader(int $idCourseLeader, int $User_idUser) : bool {
        try{
            //Check if numeric arguments are valid
            if(!is_numeric($idCourseLeader) || $idCourseLeader < 1){
                $this->errorMsgs = array("\$idCourseLeader: " . $idCourseLeader . ": invalid number");
                return false;
            }
            if(!is_numeric($User_idUser) || $User_idUser < 1){
                $this->errorMsgs = array("\$User_idUser: " . $User_idUser . ": invalid number");
                return false;
            }

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $courseCode already exists in the table
            $stmt = $this->db->prepare("UPDATE `CourseLeader` SET `User_idUser` = :User_idUser WHERE `CourseLeader`.`idCourseLeader` = :idCourseLeader; COMMIT;");

            $stmt->bindParam(":User_idUser", $User_idUser, PDO::PARAM_INT);
            $stmt->bindParam(":idCourseLeader", $idCourseLeader, PDO::PARAM_INT);

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