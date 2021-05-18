<?php

class ExamTypeService{
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
     * Get all available examType-entries
     * Returns an array with examType-objects of all available examType-entries. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @return array|null Array Containing ExamType-objects or NULL
     */
    public function getAllExamTypes() : ?array {
        $arrExamTypes = array();
        try{
            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `ExamType`;");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch StudyPoints-objects from query-results
                while ($ExamType = $stmt->fetchObject("ExamType")) {
                    $arrExamTypes[] = $ExamType;
                }
                return $arrExamTypes;
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
     * Get a examType entry by given ID
     * Returns a examType-object for the given examType-id. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @param int $idExamType
     * @return ExamType|null ExamType-object for given ID or NULL
     */
    public function getExamTypeByID(int $idExamType) : ?ExamType {
        try{
            if(!is_numeric($idExamType) || $idExamType < 1){
                $this->errorMsgs = array("\$idExamType: " . $idExamType . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `ExamType` where `ExamType`.`idExamType` = :idExamType");
            $stmt->bindParam(":idExamType", $idExamType, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("ExamType");
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
     * Get a examType entry by given CourseDescription-ID
     * Returns a examType-object for the given CourseDescription-id. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @param int $CourseDescription_idCourse
     * @return ExamType|null ExamType-object for given CourseDescription-ID or NUL
     */
    public function getExamTypeByCourseDescription(int $CourseDescription_idCourse) : ?ExamType {
        try{
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$CourseCode_idCourseCode: " . $CourseDescription_idCourse . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `ExamType` where `ExamType`.`idExamType` = (select ExamType_idExamType from `CourseDescription` where `CourseDescription`.`idCourse` = :CourseDescription_idCourse);");
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("ExamType");
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
     * Add a examType entry by given ID
     * Returns true if it succeeds, false if it fails
     * Call GetLastError to get error-message.
     * @param string $examType ExamType-string to insert
     * @return bool True if succeeded, false if failed
     */
    public function addExamType(string $examType) : bool {
        try{
            if(!is_null($examType) || !empty($examType)){
                $examType = substr($examType, 0, 45) ;
            } else {
                $this->errorMsgs[] = "\$examType cannot be empty or null!";
                return false;
            }

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful update or manually check if $courseCode already exists in the table
            $stmt = $this->db->prepare("INSERT INTO `ExamType` (`idExamType`, `examType`) VALUES (DEFAULT, :examType) ; COMMIT;");

            $stmt->bindParam(":examType", $examType, PDO::PARAM_STR);

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
     * Delete a examType entry by given ExamType-ID
     * You probably shouldn't use this...
     * Returns true if it succeeds, false if it fails
     * Call GetLastError to get error-message.
     * @param int $idExamType ID of examType to delete
     * @return bool True if succeeded, false if failed
     */
    public function deleteExamTypeById(int $idExamType) : bool {
        try{
            if(!is_numeric($idExamType) || $idExamType < 1){
                $this->errorMsgs = array("\$idExamType: " . $idExamType . ": invalid number");
                return false;
            }

            //Prepare query and bind parameters
            // TODO: Check if there are eny studypoints to delete before trying to delete, as an "empty" delete will still succeed
            $stmt = $this->db->prepare("DELETE FROM `ExamType` WHERE `ExamType`.`idExamType` = :idExamType; commit;");
            $stmt->bindParam(":idExamType", $idExamType, PDO::PARAM_INT);

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
     * Delete a examType entry by given CourseDescription-ID
     * You probably shouldn't use this...
     * Returns true if it succeeds, false if it fails
     * Call GetLastError to get error-message.
     * @param int $CourseDescription_idCourse CourseDescription-ID of examType to delete
     * @return bool True if succeeded, false if failed
     */
    public function deleteExamTypeByCourseCode(int $CourseDescription_idCourse) : bool {
        try{
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return false;
            }

            //Prepare query and bind parameters
            // TODO: Check if there are eny studypoints to delete before trying to delete, as an "empty" delete will still succeed
            $stmt = $this->db->prepare("DELETE FROM `ExamType` WHERE `ExamType`.`idExamType` = (select ExamType_idExamType from `CourseDescription` where `CourseDescription`.`idCourse` = :CourseDescription_idCourse); commit;");
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
     * Update a examType entry by given ID
     * Returns true if it succeeds, false if it fails
     * Call GetLastError to get error-message.
     * @param int $idExamType ID of examType to update
     * @param string $examType ExamType-string to update
     * @return bool True if succeeded, false if failed
     */
    public function updateExamType(int $idExamType, string $examType) : bool {
        try{
            if(!is_numeric($idExamType) || $idExamType < 1){
                $this->errorMsgs = array("\$idExamType: " . $idExamType . ": invalid number");
                return false;
            }

            if(!is_null($examType) || !empty($examType)){
                $examType = substr($examType, 0, 45) ;
            } else {
                $this->errorMsgs[] = "\$examType cannot be empty or null!";
                return false;
            }

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful update or manually check if $courseCode already exists in the table
            $stmt = $this->db->prepare("UPDATE `ExamType`
                                                  SET `examType` = :examType
                                                  WHERE `ExamType`.`idExamType` = :idExamType; COMMIT;");

            $stmt->bindParam(":examType", $examType, PDO::PARAM_STR);
            $stmt->bindParam(":idExamType", $idExamType, PDO::PARAM_INT);

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