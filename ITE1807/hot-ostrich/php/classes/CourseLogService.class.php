<?php

class CourseLogService{
    private PDO $db;
    private array $errorMsgs;

    /**
     * CourseDescriptionService.class constructor.
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
     * Get a CourseLog by given ID
     * Returns a CourseLog-Object or null
     * Call GetLastError to get error-message.
     * @param int $idCourseLog ID of CourseLog to fetch
     * @return CourseLog|null CourseLog-object or NULL
     */
    public function getCourseLogById(int $idCourseLog) : ?CourseLog {
        try{
            //Return NULL if $idCourseLog is less than 1
            if(!is_numeric($idCourseLog) || $idCourseLog < 1){
                $this->errorMsgs = array("\$idCourseLog: " . $idCourseLog . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            // TODO: change from * to what is minimally needed to display a course
            $stmt = $this->db->prepare("select * from `CourseLog` where `CourseLog`.`idCourseLog` = :idCourseLog");
            $stmt->bindParam(":idCourseLog", $idCourseLog, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("CourseLog");
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
     * Get all CourseLogs
     * Returns an array containing CourseLog-Objects or null
     * Call GetLastError to get error-message.
     * @return array|null Array containing CourseLog-Objects or null
     */
    public function getAllCourseLogs() : ?array {
        $arrCourseLogs = array();
        try {
            // No userdata used, no real need to sanitize...
            // TODO: change from * to what is minimally needed to display a course
            $stmt = $this->db->prepare("select * from `CourseLog` order by `CourseLog`.`idCourseLog`;");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch CourseLog-objects from query-results
                while ($courseLog = $stmt->fetchObject("CourseLog")) {
                    $arrCourseLogs[] = $courseLog;
                }
                return $arrCourseLogs;
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
     * Get all CourseLogs for a given UserID
     * Returns an array containing CourseLog-Objects or null
     * Call GetLastError to get error-message.
     * @param int $User_idUser ID of user to fetch CourseLogs for
     * @return array|null Array containing CourseLog-Objects or null
     */
    public function getAllCourseLogsForUser(int $User_idUser) : ?array {
        $arrCourseLogs = array();
        try {
            if(!is_numeric($User_idUser) || $User_idUser < 1){
                $this->errorMsgs = array("\$User_idUser: " . $User_idUser . ": invalid number");
                return NULL;
            }

            $stmt = $this->db->prepare("select * from `CourseLog` where `CourseLog`.`User_idUser` = :User_idUser;");
            $stmt->bindParam(":User_idUser", $User_idUser, PDO::PARAM_INT);


            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch CourseLog-objects from query-results
                while ($courseLog = $stmt->fetchObject("CourseLog")) {
                    $arrCourseLogs[] = $courseLog;
                }
                return $arrCourseLogs;
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
     * Get all CourseLogs for a given CourseDescription
     * Returns an array containing CourseLog-Objects or null
     * Call GetLastError to get error-message.
     * @param int $CourseDescription_idCourse ID of CourseDescription to fetch CourseLogs for
     * @return array|null Array containing CourseLog-Objects or null
     */
    public function getAllCourseLogsForCourseDescription(int $CourseDescription_idCourse) : ?array {
        $arrCourseLogs = array();
        try {

            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return NULL;
            }

            $stmt = $this->db->prepare("select * from `CourseLog` where `CourseLog`.`CourseDescription_idCourse` = :CourseDescription_idCourse order by `CourseLog`.`dateChanged` desc;");
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch CourseLog-objects from query-results
                while ($courseLog = $stmt->fetchObject("CourseLog")) {
                    $arrCourseLogs[] = $courseLog;
                }
                return $arrCourseLogs;
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
     * Add a new CourseLog
     * Returns true if successful, false if failed
     * Call GetLastError to get error-message.
     * @param int $User_idUser ID of user that made the change
     * @param int $CourseDescription_idCourse ID of the course that is changed
     * @return bool True if successful, false if failed
     */
    public function addCourseLog(int $User_idUser, int $CourseDescription_idCourse) : bool {
        try{
            //Check if numeric arguments are valid

            if(!is_numeric($User_idUser) || $User_idUser < 1){
                $this->errorMsgs = array("\$User_idUser: " . $User_idUser . ": invalid number");
                return false;
            }
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return false;
            }

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $courseCode already exists in the table
            $stmt = $this->db->prepare("INSERT INTO `CourseLog` (`idCourseLog`, `dateChanged`, `User_idUser`, `CourseDescription_idCourse`)
                                                  VALUES (DEFAULT, DEFAULT, :User_idUser, :CourseDescription_idCourse); COMMIT;");

            $stmt->bindParam(":User_idUser", $User_idUser, PDO::PARAM_INT);
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
     * Delete a CourseLog with given ID
     * Returns true if successful, false if failed
     * Call GetLastError to get error-message.
     * @param int $idCourseLog ID of CourseLog to delete
     * @return bool True if successful, false if failed
     */
    public function deleteCourseLogById(int $idCourseLog) : bool {
        try{

            if(!is_numeric($idCourseLog) || $idCourseLog < 1){
                $this->errorMsgs = array("\$idCourseLog: " . $idCourseLog . ": invalid number");
                return false;
            }

            $stmt = $this->db->prepare("DELETE FROM `CourseLog` WHERE `CourseLog`.`idCourseLog` = :idCourseLog;");
            $stmt->bindParam(":idCourseLog", $idCourseLog, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if ($stmt->execute()) {
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