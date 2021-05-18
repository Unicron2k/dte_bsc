<?php

class PrerequisitesService {
    private PDO $db;
    private array $errorMsgs;

    /**
     * PrerequisitesService.class.class constructor.
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
     * Add a Prerequisites-entry
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param bool $required Is the course required? true if required, false if recommended
     * @param int $CourseDescription_idCourse ID of course-description it applies to
     * @param int $CourseCode_idCourseCode ID of course-code it applies to
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function addPrerequisites(bool $required, int $CourseDescription_idCourse, int $CourseCode_idCourseCode) : bool {
        try{
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return false;
            }
            if(!is_numeric($CourseCode_idCourseCode) || $CourseCode_idCourseCode < 1){
                $this->errorMsgs = array("\$CourseCode_idCourseCode: " . $CourseCode_idCourseCode . ": invalid number");
                return false;
            }

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $studyPoints already exists in the table
            $stmt = $this->db->prepare("INSERT INTO `Prerequisites` (`idPrerequisites`, `required`, `CourseDescription_idCourse`, `CourseCode_idCourseCode`)
                                                  VALUES (DEFAULT, :required, :CourseDescription_idCourse, :CourseCode_idCourseCode) ; COMMIT;");

            $stmt->bindParam(":required", $required, PDO::PARAM_BOOL);
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);
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
     * Update a Prerequisites-entry
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idPrerequisites ID of entry to update
     * @param bool $required Is the course required? true if required, false if recommended
     * @param int $CourseDescription_idCourse ID of course-description it applies to
     * @param int $CourseCode_idCourseCode ID of course-code it applies to
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function updatePrerequisites(int $idPrerequisites, bool $required, int $CourseDescription_idCourse, int $CourseCode_idCourseCode) : bool {
        try{
            if(!is_numeric($idPrerequisites) || $idPrerequisites < 1){
                $this->errorMsgs = array("\$idPrerequisites: " . $idPrerequisites . ": invalid number");
                return false;
            }
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return false;
            }
            if(!is_numeric($CourseCode_idCourseCode) || $CourseCode_idCourseCode < 1){
                $this->errorMsgs = array("\$CourseCode_idCourseCode: " . $CourseCode_idCourseCode . ": invalid number");
                return false;
            }

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $courseCode already exists in the table
            $stmt = $this->db->prepare("UPDATE `Prerequisites`
                                                  SET `required` = :required,
                                                  `CourseDescription_idCourse` = :CourseDescription_idCourse,
                                                  `CourseCode_idCourseCode` = :CourseCode_idCourseCode
                                                  WHERE `Prerequisites`.`idPrerequisites` = :idPrerequisites; COMMIT;");

            $stmt->bindParam(":required", $required, PDO::PARAM_BOOL);
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);
            $stmt->bindParam(":CourseCode_idCourseCode", $CourseCode_idCourseCode, PDO::PARAM_INT);
            $stmt->bindParam(":idPrerequisites", $idPrerequisites, PDO::PARAM_INT);

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
     * Get all prerequisites-entries for a given CourseDescription
     * Returns array with prerequisites-objects if succeeded, NULL if failed.
     * Call GetLastError to get error-message.
     * @param int $CourseDescription_idCourse
     * @return array|null Array containing Prerequisites-objects or NULL. Call GetLastError to get last error-message
     */
    public function getAllPrerequisitesByCourseDescription(int $CourseDescription_idCourse) : ?array {
        $arrPrerequisites = array();
        try{
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `Prerequisites` where `Prerequisites`.`CourseDescription_idCourse` = :CourseDescription_idCourse");
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch StudyPoints-objects from query-results
                while ($Prerequisites = $stmt->fetchObject("Prerequisites")) {
                    $arrPrerequisites[] = $Prerequisites;
                }
                return $arrPrerequisites;
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
     * Get all prerequisites-entries for a given CourseCode
     * Returns array with prerequisites-objects if succeeded, NULL if failed.
     * Call GetLastError to get error-message.
     * @param int $CourseCode_idCourseCode ID of course-code to fetch prerequisites for
     * @return array|null Array containing Prerequisites-objects or NULL. Call GetLastError to get last error-message
     */
    public function getAllPrerequisitesByCourseCode(int $CourseCode_idCourseCode) : ?array {
        $arrPrerequisites = array();
        try{
            if(!is_numeric($CourseCode_idCourseCode) || $CourseCode_idCourseCode < 1){
                $this->errorMsgs = array("\$CourseCode_idCourseCode: " . $CourseCode_idCourseCode . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `Prerequisites` where `Prerequisites`.`CourseCode_idCourseCode` = :CourseCode_idCourseCode");
            $stmt->bindParam(":CourseCode_idCourseCode", $CourseCode_idCourseCode, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch StudyPoints-objects from query-results
                while ($Prerequisites = $stmt->fetchObject("Prerequisites")) {
                    $arrPrerequisites[] = $Prerequisites;
                }
                return $arrPrerequisites;
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
     * Get all prerequisite CourseCode-entries for a given Course-ID.
     * Returns array with prerequisite CourseCode-objects if succeeded, NULL if failed.
     * Call GetLastError to get error-message.
     * @param int $CourseDescription_idCourse ID of course-code to fetch prerequisites for.
     * @param bool $required Whether or not the courses are required. <b>True</b> if they are, <b>false</b> false if they aren't. Default is <b>true</b>.
     * @return array|null Array containing Prerequisites-objects or NULL. Call GetLastError to get last error-message.
     */
    public function getPrerequisiteCourseCodesByIdCourse(int $CourseDescription_idCourse, bool $required=true) : ?array {
        $arrPrerequisites = array();
        try{
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `CourseCode` where `CourseCode`.`idCourseCode` in (
                                                        select `CourseCode_idCourseCode` from `Prerequisites`
                                                        where `Prerequisites`.`CourseDescription_idCourse` = :CourseDescription_idCourse
                                                          and `Prerequisites`.`required` = :required
                                                    );");
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);
            $stmt->bindParam(":required", $required, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch StudyPoints-objects from query-results
                while ($Prerequisites = $stmt->fetchObject("CourseCode")) {
                    $arrPrerequisites[] = $Prerequisites;
                }
                return $arrPrerequisites;
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
     * Get all prerequisites-entries for a given CourseCode
     * Returns array with prerequisites-objects if succeeded, NULL if failed.
     * Call GetLastError to get error-message.
     * @param int $CourseCode_idCourseCode ID of course-code to fetch prerequisites for
     * @return array|null Array containing Prerequisites-objects or NULL. Call GetLastError to get last error-message
     */
    public function getAllPrerequisites() : ?array {
        $arrPrerequisites = array();
        try{
            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `Prerequisites`;");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch StudyPoints-objects from query-results
                while ($Prerequisites = $stmt->fetchObject("Prerequisites")) {
                    $arrPrerequisites[] = $Prerequisites;
                }
                return $arrPrerequisites;
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
     * Get a prerequisites-entry for a given ID
     * Returns a prerequisites-object if successful, NULL if failed.
     * Call GetLastError to get error-message.
     * @param int $idPrerequisites ID of prerequisites ton fetch
     * @return Prerequisites|null Prerequisites-object or NULL. Call GetLastError to get last error-message
     */
    public function getPrerequisitesById(int $idPrerequisites) : ?Prerequisites {
        try{
            if(!is_numeric($idPrerequisites) || $idPrerequisites < 1){
                $this->errorMsgs = array("\$idPrerequisites: " . $idPrerequisites . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `Prerequisites` where `Prerequisites`.`idPrerequisites` = :idPrerequisites");
            $stmt->bindParam(":idPrerequisites", $idPrerequisites, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("Prerequisites");
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
     * Deletes all prerequisites for a given CourseDescription
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $CourseDescription_idCourse CourseDescription-ID to delete entries for
     * @return bool True if successful, false if failed.
     */
    public function deleteAllPrerequisitesByCourseDescription(int $CourseDescription_idCourse) : bool {
        try{
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return false;
            }

            //Prepare query and bind parameters
            // TODO: Check if there are eny studypoints to delete before trying to delete, as an "empty" delete will still succeed
            $stmt = $this->db->prepare("DELETE FROM `Prerequisites` WHERE `Prerequisites`.`CourseDescription_idCourse` = :CourseDescription_idCourse; commit;");
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);

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
     * Deletes all prerequisites with a given CourseCode
     * You probably shouldn't use this
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $CourseCode_idCourseCode CourseCode-ID to delete entries for
     * @return bool True if successful, false if failed.
     */
    public function deleteAllPrerequisitesByCourseCode(int $CourseCode_idCourseCode) : bool {
        try{
            if(!is_numeric($CourseCode_idCourseCode) || $CourseCode_idCourseCode < 1){
                $this->errorMsgs = array("\$CourseCode_idCourseCode: " . $CourseCode_idCourseCode . ": invalid number");
                return false;
            }

            //Prepare query and bind parameters
            // TODO: Check if there are eny studypoints to delete before trying to delete, as an "empty" delete will still succeed
            $stmt = $this->db->prepare("DELETE FROM `Prerequisites` WHERE `Prerequisites`.`CourseCode_idCourseCode` = :CourseCode_idCourseCode; commit;");
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
     * Deletes a prerequisites-entry for a given ID
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idPrerequisites ID of prerequisite-entry to delete
     * @return bool True if successful, false if failed.
     */
    public function deleteById(int $idPrerequisites) : bool {
        try{
            if(!is_numeric($idPrerequisites) || $idPrerequisites < 1){
                $this->errorMsgs = array("\$idPrerequisites: " . $idPrerequisites . ": invalid number");
                return false;
            }

            //Prepare query and bind parameters
            // TODO: Check if there are eny studypoints to delete before trying to delete, as an "empty" delete will still succeed
            $stmt = $this->db->prepare("DELETE FROM `Prerequisites` WHERE `Prerequisites`.`idPrerequisites` = :idPrerequisites; commit;");
            $stmt->bindParam(":idPrerequisites", $idPrerequisites, PDO::PARAM_INT);

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