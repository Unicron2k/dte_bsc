<?php

class GradeScaleService{
    private PDO $db;
    private array $errorMsgs;

    /**
     * GradeScaleService.class constructor.
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
     * Get all available gradeScale-entries
     * Returns an array with gradeScale-objects of all available gradeScale-entries. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @return array|null Array Containing GradeScale-objects or NULL
     */
    public function getAllGradeScales() : ?array {
        $arrGradeScales = array();
        try{
            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `GradeScale`;");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch StudyPoints-objects from query-results
                while ($GradeScale = $stmt->fetchObject("GradeScale")) {
                    $arrGradeScales[] = $GradeScale;
                }
                return $arrGradeScales;
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
     * Get a gradeScale entry by given ID
     * Returns a gradeScale-object for the given gradeScale-id. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @param int $idGradeScale
     * @return GradeScale|null GradeScale-object for given ID or NULL
     */
    public function getGradeScaleByID(int $idGradeScale) : ?GradeScale {
        try{
            if(!is_numeric($idGradeScale) || $idGradeScale < 1){
                $this->errorMsgs = array("\$idGradeScale: " . $idGradeScale . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `GradeScale` where `GradeScale`.`idGradeScale` = :idGradeScale");
            $stmt->bindParam(":idGradeScale", $idGradeScale, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("GradeScale");
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
     * Get a gradeScale entry by given CourseDescription-ID
     * Returns a gradeScale-object for the given CourseDescription-id. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @param int $CourseDescription_idCourse
     * @return GradeScale|null GradeScale-object for given CourseDescription-ID or NUL
     */
    public function getGradeScaleByCourseDescription(int $CourseDescription_idCourse) : ?GradeScale {
        try{
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `GradeScale` where `GradeScale`.`idGradeScale` = (select GradeScale_idGradeScale from `CourseDescription` where `CourseDescription`.`idCourse` = :CourseDescription_idCourse);");
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("GradeScale");
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
     * Add a gradeScale entry by given ID
     * Returns true if it succeeds, false if it fails
     * Call GetLastError to get error-message.
     * @param string $gradeScale GradeScale-string to insert
     * @return bool True if succeeded, false if failed
     */
    public function addGradeScale(string $gradeScale) : bool {
        try{
            if(!is_null($gradeScale) || !empty($gradeScale)){
                $gradeScale = substr($gradeScale, 0, 20) ;
            } else {
                $this->errorMsgs[] = "\$gradeScale cannot be empty or null!";
                return false;
            }

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful update or manually check if $courseCode already exists in the table
            $stmt = $this->db->prepare("INSERT INTO `GradeScale` (`idGradeScale`, `scale`) VALUES (DEFAULT, :scale) ; COMMIT;");

            $stmt->bindParam(":scale", $gradeScale, PDO::PARAM_STR);

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
     * Delete a gradeScale entry by given GradeScale-ID
     * You probably shouldn't use this...
     * Returns true if it succeeds, false if it fails
     * Call GetLastError to get error-message.
     * @param int $idGradeScale ID of gradeScale to delete
     * @return bool True if succeeded, false if failed
     */
    public function deleteGradeScaleById(int $idGradeScale) : bool {
        try{
            if(!is_numeric($idGradeScale) || $idGradeScale < 1){
                $this->errorMsgs = array("\$idGradeScale: " . $idGradeScale . ": invalid number");
                return false;
            }

            //Prepare query and bind parameters
            // TODO: Check if there are eny studypoints to delete before trying to delete, as an "empty" delete will still succeed
            $stmt = $this->db->prepare("DELETE FROM `GradeScale` WHERE `GradeScale`.`idGradeScale` = :idGradeScale; commit;");
            $stmt->bindParam(":idGradeScale", $idGradeScale, PDO::PARAM_INT);

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
     * Delete a gradeScale entry by given CourseDescription-ID
     * You probably shouldn't use this...
     * Returns true if it succeeds, false if it fails
     * Call GetLastError to get error-message.
     * @param int $CourseDescription_idCourse CourseDescription-ID of gradeScale to delete
     * @return bool True if succeeded, false if failed
     */
    public function deleteGradeScaleByCourseCode(int $CourseDescription_idCourse) : bool {
        try{
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return false;
            }

            //Prepare query and bind parameters
            // TODO: Check if there are eny studypoints to delete before trying to delete, as an "empty" delete will still succeed
            $stmt = $this->db->prepare("DELETE FROM `GradeScale` WHERE `GradeScale`.`idGradeScale` = (select GradeScale_idGradeScale from `CourseDescription` where `CourseDescription`.`idCourse` = :CourseDescription_idCourse); commit;");
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
     * Update a gradeScale entry by given ID
     * Returns true if it succeeds, false if it fails
     * Call GetLastError to get error-message.
     * @param int $idGradeScale ID of gradeScale to update
     * @param string $gradeScale GradeScale-string to update
     * @return bool True if succeeded, false if failed
     */
    public function updateGradeScale(int $idGradeScale, string $gradeScale) : bool {
        try{
            if(!is_numeric($idGradeScale) || $idGradeScale < 1){
                $this->errorMsgs = array("\$idGradeScale: " . $idGradeScale . ": invalid number");
                return false;
            }

            if(!is_null($gradeScale) || !empty($gradeScale)){
                $gradeScale = substr($gradeScale, 0, 20) ;
            } else {
                $this->errorMsgs[] = "\$gradeScale cannot be empty or null!";
                return false;
            }

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful update or manually check if $courseCode already exists in the table
            $stmt = $this->db->prepare("UPDATE `GradeScale`
                                                  SET `scale` = :scale
                                                  WHERE `GradeScale`.`idGradeScale` = :idGradeScale; COMMIT;");

            $stmt->bindParam(":scale", $gradeScale, PDO::PARAM_STR);
            $stmt->bindParam(":idGradeScale", $idGradeScale, PDO::PARAM_INT);

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