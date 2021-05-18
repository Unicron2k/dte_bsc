<?php

class CourseCoordinatorService
{
    private PDO $db;
    private array $errorMsgs;

    /**
     * CourseCoordinatorService.class constructor.
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
     * Get all CourseCoordinator-entries
     * Returns an array with CourseCoordinator-Objects or null
     * Call GetLastError to get error-message.
     * @return array|null Array containing CourseCoordinator-objects or NULL
     */
    public function getAllCourseCoordinators(): ?array
    {
        $arrCourseCoordinators = array();
        try {
            // No userdata used, no real need to sanitize...
            // TODO: change from * to what is minimally needed to display a course
            $stmt = $this->db->prepare("select * from `CourseCoordinator` order by `CourseCoordinator`.`User_idUser`, `CourseCoordinator`.`CourseDescription_idCourse`;");

            //Execute query, and set return-status + any potential error-message
            if ($stmt->execute()) {
                //Fetch CourseCode-objects from query-results
                while ($approval = $stmt->fetchObject("CourseCoordinator")) {
                    $arrCourseCoordinators[] = $approval;
                }
                return $arrCourseCoordinators;
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
     * Get all CourseCoordinator-entries for a given UserID
     * Returns an array with CourseCoordinator-Objects or null
     * Call GetLastError to get error-message.
     * @param int $User_idUser ID of user to fetch CourseCoordinator-entries for
     * @return array|null Array containing CourseCoordinator-objects or NULL
     */
    public function getCourseCoordinatorByUser(int $User_idUser): ?array
    {
        $arrCourseCoordinators = array();
        try {
            //Return NULL if $idCourseCode is less than 1
            if (!is_numeric($User_idUser) || $User_idUser < 1) {
                $this->errorMsgs = array("\$User_idUser: " . $User_idUser . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            // TODO: change from * to what is minimally needed to display a course
            $stmt = $this->db->prepare("select * from `CourseCoordinator` where `CourseCoordinator`.`User_idUser` = :User_idUser");
            $stmt->bindParam(":User_idUser", $User_idUser, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if ($stmt->execute()) {
                while ($courseCoordinator = $stmt->fetchObject("CourseCoordinator")) {
                    $arrCourseCoordinators[] = $courseCoordinator;
                }
                return $arrCourseCoordinators;
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
     * Get all CourseCoordinator-entries for a given CourseDescription-ID.
     * Returns an array with CourseCoordinator-Objects or null.
     * Call GetLastError to get error-message.
     * @param int $CourseDescription_idCourse ID of course to fetch CourseCoordinators for.
     * @return array|null Array containing CourseCoordinator-objects or null.
     */
    public function getAllCourseCoordinatorsByCourseDescription(int $CourseDescription_idCourse): ?array
    {
        $arrCourseCoordinators = array();
        try {
            //Return NULL if $idCourseCode is less than 1
            if (!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1) {
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            // TODO: change from * to what is minimally needed to display a course
            $stmt = $this->db->prepare("select * from `CourseCoordinator` where `CourseCoordinator`.`CourseDescription_idCourse` = :CourseDescription_idCourse");
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if ($stmt->execute()) {
                while ($courseCoordinator = $stmt->fetchObject("CourseCoordinator")) {
                    $arrCourseCoordinators[] = $courseCoordinator;
                }
                return $arrCourseCoordinators;
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
     * Get CourseCoordinator-entry for a given CourseDescription-ID
     * Returns a CourseCoordinator-Object or null
     * Call GetLastError to get error-message.
     * @param int $CourseDescription_idCourse ID of course to fetch CourseCoordinator for
     * @return CourseCoordinator|null CourseCoordinator-object or NULL
     */
    public function getCourseCoordinatorByCourseDescription(int $CourseDescription_idCourse): ?CourseCoordinator
    {
        try {
            //Return NULL if $idCourseCode is less than 1
            if (!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1) {
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            // TODO: change from * to what is minimally needed to display a course
            $stmt = $this->db->prepare("select * from `CourseCoordinator` where `CourseCoordinator`.`CourseDescription_idCourse` = :CourseDescription_idCourse");
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if ($stmt->execute()) {
                $result = $stmt->fetchObject("CourseCoordinator");
                if ($result) {
                    return $result;
                } else {
                    $this->errorMsgs = $stmt->errorInfo();
                    if (is_null($result) || empty($result)) {
                        $this->errorMsgs[] = "There were no results that matched the query with \$CourseDescription_idCourse = $CourseDescription_idCourse";
                    }
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
     * Add a CourseCoordinator-entry
     * Returns true if successful, false if failed
     * Call GetLastError to get error-message.
     * @param int $User_idUser ID of user to be course coordinator
     * @param int $CourseDescription_idCourse CourseDescription-ID of the course the User-ID will be coordinator for
     * @param string $CoursePart Description of the part of the course the User will be responsible for
     * @return bool True if successful, false if failed
     */
    public function addCourseCoordinator(int $User_idUser, int $CourseDescription_idCourse, string $CoursePart): bool
    {
        try {
            //Check if numeric arguments are valid

            if (!is_numeric($User_idUser) || $User_idUser < 1) {
                $this->errorMsgs = array("\$User_idUser: " . $User_idUser . ": invalid number");
                return false;
            }
            if (!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1) {
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return false;
            }
            if (!is_null($CoursePart) || !empty($CoursePart)) {
                $CoursePart = substr($CoursePart, 0, 45);
            } else {
                $this->errorMsgs[] = "\$CoursePart cannot be empty or null!";
                return false;
            }

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $courseCode already exists in the table
            $stmt = $this->db->prepare("INSERT INTO `CourseCoordinator` (`User_idUser`, `CourseDescription_idCourse`, `CoursePart`)
                                                  VALUES (:User_idUser, :CourseDescription_idCourse, :CoursePart); COMMIT;");

            $stmt->bindParam(":User_idUser", $User_idUser, PDO::PARAM_INT);
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);
            $stmt->bindParam(":CoursePart", $CoursePart, PDO::PARAM_STR);
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
     * Update a CourseCoordinator-entry by CourseDescription-ID
     * Returns true if successful, false if failed
     * Call GetLastError to get error-message.
     * @param int $User_idUser ID of user to be course coordinator
     * @param int $CourseDescription_idCourse CourseDescription-ID of the course the User-ID will be coordinator for
     * @param string $CoursePart Description of the part of the course the User will be responsible for
     * @return bool True if successful, false if failed
     */
    public function updateCourseCoordinatorByCourseDescription(int $User_idUser, int $CourseDescription_idCourse, string $CoursePart): bool
    {
        try {
            //Check if numeric arguments are valid

            if (!is_numeric($User_idUser) || $User_idUser < 1) {
                $this->errorMsgs = array("\$User_idUser: " . $User_idUser . ": invalid number");
                return false;
            }
            if (!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1) {
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return false;
            }
            if (!is_null($CoursePart) || !empty($CoursePart)) {
                $CoursePart = substr($CoursePart, 0, 45);
            } else {
                $this->errorMsgs[] = "\$CoursePart cannot be empty or null!";
                return false;
            }

            $stmt = $this->db->prepare("Update `CourseCoordinator` set `User_idUser` = :User_idUser, `CoursePart` = :CoursePart where `CourseDescription_idCourse` = :CourseDescription_idCourse; COMMIT;");

            $stmt->bindParam(":User_idUser", $User_idUser, PDO::PARAM_INT);
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);
            $stmt->bindParam(":CoursePart", $CoursePart, PDO::PARAM_STR);
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
     * Delete a CourseCoordinator-entry by CourseDescription-ID
     * Returns true if successful, false if failed
     * Call GetLastError to get error-message.
     * @param int $CourseDescription_idCourse CourseID to delete CourseCoordinator for
     * @return bool True if successful, false if failed
     */
    public function deleteCourseCoordinatorByCourseDescription(int $CourseDescription_idCourse): bool
    {
        try {

            if (!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1) {
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return false;
            }

            $stmt = $this->db->prepare("DELETE FROM `CourseCoordinator` WHERE `CourseCoordinator`.`CourseDescription_idCourse` = :CourseDescription_idCourse;");
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);

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

    public function getCourseCoordinatorsByDescWithUser($courseDescId): ?array
    {
        try {
            $courseCoordinators = array();
            $sth = $this->db->prepare("select CourseDescription_idCourse, CoursePart, idUser, firstName, lastName 
                                                from CourseCoordinator, User 
                                                where User.idUser = CourseCoordinator.User_idUser
                                                and CourseDescription_idCourse = :courseDescId");
            if ($sth->execute(array('courseDescId' => $courseDescId))) {
                while ($result = $sth->fetchObject("CourseCoordinatorWithUser")) {
                    $courseCoordinators[] = $result;
                }
                return $courseCoordinators;
            } else {
                $this->errorMsgs = $sth->errorInfo();
            }
        } catch (Exception $e) {
            $this->errorMsgs[] = $e;
        }
        return null;
    }

    /**
     * Returns array $errorMsgs
     * @return array Array containing the last generated error-message
     */
    public function getLastError(): array
    {
        // TODO: Format array as a single string
        return $this->errorMsgs;
    }
}