<?php

class ApprovalService{

    private PDO $db;
    private array $errorMsgs;

    const APPROVED_ALL = 0;
    const COURSE_COORDINATOR = 1;
    const INSTITUTE_LEADER = 2;


    /**
     * CourseCodeService.class constructor.
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
     * Get all available Approvals.
     * Returns an array with Approval-objects of all approvals. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @return array|null Array Containing CourseCode-objects or NULL
     */
    public function getAllApprovals() : ?array {
        $arrApprovals = array();
        try {
            // No userdata used, no real need to sanitize...
            // TODO: change from * to what is minimally needed to display a course
            $stmt = $this->db->prepare("select * from `Approval` order by `Approval`.`idApproval`;");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch CourseCode-objects from query-results
                while ($approval = $stmt->fetchObject("Approval")) {
                    $arrApprovals[] = $approval;
                }
                return $arrApprovals;
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
     * Get Approvals by course.
     * Returns an array with Approval-objects of approvals for a given course. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @param int $idCourse The id of course to
     * @return array|null Array Containing CourseCode-objects or NULL
     */
    public function getApprovalsByCourse($idCourse) : ?array {
        $arrApprovalsByCourse = array();
        try {
            // No userdata used, no real need to sanitize...
            // TODO: change from * to what is minimally needed to display an approval
            $stmt = $this->db->prepare("select * from `Approval` where `Approval`.`CourseDescription_idCourse` = :idCourse order by `Approval`.`idApproval`;");

            $stmt->bindParam(":idCourse", $idCourse, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch CourseCode-objects from query-results
                while ($approval = $stmt->fetchObject("Approval")) {
                    $arrApprovalsByCourse[] = $approval;
                }
                return $arrApprovalsByCourse;
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
     * Get data for a specific Approval given its' ID.
     * Returns a approval-object on success, NULL on failure.
     * Call GetLastError to get error-message.
     * @param int $idApproval Approval-ID of which to get data for
     * @return Approval|null Object of type CourseCode containing all the data for a given ID or NULL
     */
    public function getApprovalByID(int $idApproval) : ?Approval {
        try{
            //Return NULL if $idCourseCode is less than 1
            if(!is_numeric($idApproval) || $idApproval < 1){
                $this->errorMsgs = array("\$idApproval: " . $idApproval . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            // TODO: change from * to what is minimally needed to display a course
            $stmt = $this->db->prepare("select * from `Approval` where `idApproval` = :idApproval");
            $stmt->bindParam(":idApproval", $idApproval, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result = $stmt->fetchObject("Approval");
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
     * Add a new Approval-entry
     * @param string $approvalDeadline Deadline for the approval
     * @param int $CourseDescription_idCourse Which CourseDescription it applies to
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function addApproval (string $approvalDeadline,  int $CourseDescription_idCourse) : bool {
        try{
            //Check if numeric arguments are valid
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return false;
            }

            //Length of approvalDeadline-string string should never be larger than 19: YYYY-MM-DD HH:MM:SS
            $approvalDeadline = substr($approvalDeadline, 0, 19);
            $zeroApprovalDate = "0000-00-00  00:00:00";

            // TODO: Optimize database by removing boolean approved values for course-coordinator and institute-leader. If the approval-date is set, they are, by definition, approved by them
            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $courseCode already exists in the table
            $stmt = $this->db->prepare("INSERT INTO `Approval` (`idApproval`, `approvalDeadline`, `approved`, `approvedDate`, `approvedCourseCoordinator`, `approvedDateCourseCoordinator`, `approvedInstituteLeader`, `approvedDateInstituteLeader`, `CourseDescription_idCourse`)
                                                  VALUES (DEFAULT, :approvalDeadline, 0, :approvedDate, 0, :approvedDateCourseCoordinator, 0, :approvedDateInstituteLeader, :CourseDescription_idCourse); COMMIT;");

            $stmt->bindParam(":approvalDeadline", $approvalDeadline, PDO::PARAM_STR);
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);
            $stmt->bindParam(":approvedDate", $zeroApprovalDate, PDO::PARAM_STR);
            $stmt->bindParam(":approvedDateCourseCoordinator", $zeroApprovalDate, PDO::PARAM_STR);
            $stmt->bindParam(":approvedDateInstituteLeader", $zeroApprovalDate, PDO::PARAM_STR);

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
     * Update an Approval-entry
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idApproval Approval-entry to update
     * @param string $approvalDeadline Deadline of approval
     * @param int $approved All parties has approved
     * @param string $approvedDate Date of approval
     * @param int $approvedCourseCoordinator Approved by courseCoordinator
     * @param string $approvedDateCourseCoordinator Date of approval by CourseCoordinator
     * @param int $approvedInstituteLeader Approved by InstituteLeader
     * @param string $approvedDateInstituteLeader date of approval by InstituteLeader
     * @param int $CourseDescription_idCourse Which CourseDescription the approval applies to
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function updateApprovalEntry(int $idApproval, string $approvalDeadline, int $approved, ?string $approvedDate, int $approvedCourseCoordinator, ?string $approvedDateCourseCoordinator, int $approvedInstituteLeader, ?string $approvedDateInstituteLeader, int $CourseDescription_idCourse) : bool {
        try{
            //Check if numeric arguments are valid
            $arrNumericalArguments = array(
                "\$approved" => $approved,
                "\$approvedCourseCoordinator" => $approvedCourseCoordinator,
                "\$approvedInstituteLeader" => $approvedInstituteLeader
            );
            foreach ($arrNumericalArguments as $key => $value) {
                if (!is_numeric($value) || $value < 0  || $approved > 1) {
                    $this->errorMsgs = array("$key: " . $value . ": invalid number(must be 0 or 1)");
                    return false;
                }
            }
            if (!is_numeric($idApproval) || $idApproval < 1) {
                $this->errorMsgs = array("\$idApproval: " . $idApproval . ": invalid number");
                return false;
            }
            if (!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1) {
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return false;
            }
            //Length of Date-strings should never be larger than 19: YYYY-MM-DD HH:MM:SS
            $DATESTRING_LENGTH = 19;
            $approvalDeadline = substr($approvalDeadline, 0, $DATESTRING_LENGTH);
            if(!is_null($approvedDate))$approvedDate = substr($approvedDate, 0, $DATESTRING_LENGTH); else $approvedDate = "0000-00-00  00:00:00";
            if(!is_null($approvedDateCourseCoordinator))$approvedDateCourseCoordinator = substr($approvedDateCourseCoordinator, 0, $DATESTRING_LENGTH); else $approvedDateCourseCoordinator = "0000-00-00  00:00:00";
            if(!is_null($approvedDateInstituteLeader))$approvedDateInstituteLeader = substr($approvedDateInstituteLeader, 0, $DATESTRING_LENGTH); else $approvedDateInstituteLeader = "0000-00-00  00:00:00";

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $courseCode already exists in the table
            $stmt = $this->db->prepare("UPDATE `Approval`
                                                  SET `approvalDeadline` = :approvalDeadline,
                                                  `approved` = :approved,
                                                  `approvedDate` = :approvedDate,
                                                  `approvedCourseCoordinator` = :approvedCourseCoordinator,
                                                  `approvedDateCourseCoordinator` = :approvedDateCourseCoordinator,
                                                  `approvedInstituteLeader` = :approvedInstituteLeader,
                                                  `approvedDateInstituteLeader` = :approvedDateInstituteLeader,
                                                  `CourseDescription_idCourse` = :CourseDescription_idCourse
                                                  WHERE `Approval`.`idApproval` = :idApproval; COMMIT;");

            $stmt->bindParam(":idApproval", $idApproval, PDO::PARAM_INT);
            $stmt->bindParam(":approvalDeadline", $approvalDeadline, PDO::PARAM_STR);
            $stmt->bindParam(":approved", $approved, PDO::PARAM_INT);
            $stmt->bindParam(":approvedDate", $approvedDate, PDO::PARAM_STR);
            $stmt->bindParam(":approvedCourseCoordinator", $approvedCourseCoordinator, PDO::PARAM_INT);
            $stmt->bindParam(":approvedDateCourseCoordinator", $approvedDateCourseCoordinator, PDO::PARAM_STR);
            $stmt->bindParam(":approvedInstituteLeader", $approvedInstituteLeader, PDO::PARAM_INT);
            $stmt->bindParam(":approvedDateInstituteLeader", $approvedDateInstituteLeader, PDO::PARAM_STR);
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
     * Updates an approval-entry by object
     * @param Approval $approval Approval-object containing updated approval-data
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function updateApprovalEntryByObject(Approval $approval) : bool {

        if(is_null($approval)){
            $this->errorMsgs = array("\$approval was NULL");
            return false;
        }

        return $this::updateApprovalEntry(
            $approval->getIdApproval(),
            $approval->getApprovalDeadline(),
            $approval->getApproved(),
            $approval->getApprovedDate(),
            $approval->getApprovedCourseCoordinator(),
            $approval->getApprovedDateCourseCoordinator(),
            $approval->getApprovedInstituteLeader(),
            $approval->getApprovedDateInstituteLeader(),
            $approval->getCourseDescriptionIdCourse());

    }

    /**
     * Update the approval-status for a given approval-id
     * @param int $idApproval ID of approval to be approved
     * @param int $approvedBy which approval to be approved(Course Coordinator or Institute Leader)
     * @param bool $approved Approval-status
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function updateApprovalStatus(int $idApproval, int $approvedBy, bool $approved) : bool {
        //Check if numeric arguments are valid
        if(!is_numeric($idApproval) || $idApproval < 1){
            $this->errorMsgs = array("\$idApproval: " . $idApproval . ": invalid number");
            return false;
        }
        if(!is_numeric($approvedBy) || $approvedBy < 1){
            $this->errorMsgs = array("\$approvedBy: " . $approvedBy . ": invalid number");
            return false;
        }

        $approval = self::getApprovalByID($idApproval);
        if(is_null($approval)){
            array_push( $this->errorMsgs, "\$approval was NULL");
            return false;
        }

        switch ($approvedBy){
            case self::COURSE_COORDINATOR:
                $approval->setApprovedCourseCoordinator($approved);
                $approval->setApprovedDateCourseCoordinator(date("Y-m-d H:i:s"));
                break;
            case self::INSTITUTE_LEADER:
                $approval->setApprovedInstituteLeader($approved);
                $approval->setApprovedDateInstituteLeader(date("Y-m-d H:i:s"));
                break;
            case self::APPROVED_ALL:
                //TODO: Should we do anything here?
            default:
                break;
        }

        if ($approval->getApprovedCourseCoordinator() && $approval->getApprovedInstituteLeader()) {
            $approval->setApproved($approved);
            $approval->setApprovedDate(date("Y-m-d H:i:s"));
        }

        return $this->updateApprovalEntryByObject($approval);
    }

    /**
     * Get number of approved courses
     * Returns the number of courses that are approved. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @return int|null Number of approved courses
     */
    public function getNumApproved(): ?int
    {
        try {
            // No userdata used, no real need to sanitize...
            // TODO: change from * to what is minimally needed to display a course
            $stmt = $this->db->prepare("select count(`Approval`.`approved`) from `Approval` where `Approval`.`approved` = true;");

            //Execute query, and set return-status + any potential error-message
            if ($stmt->execute()) {
                //Fetch CourseCode-objects from query-results
                $result = $stmt->fetch();
                return reset($result);
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
     * Get number of non approved courses
     * Returns the number of courses that are approved. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @return int|null Number of approved courses
     */
    public function getNumNonApproved(): ?int
    {
        try {
            // No userdata used, no real need to sanitize...
            // TODO: change from * to what is minimally needed to display a course
            $stmt = $this->db->prepare("select count(`Approval`.`approved`) from `Approval` where `Approval`.`approved` != true;");

            //Execute query, and set return-status + any potential error-message
            if ($stmt->execute()) {
                //Fetch CourseCode-objects from query-results
                $result = $stmt->fetch();
                return reset($result);
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
     * Returns array $errorMsgs
     * @return array Array containing the last generated error-message
     */
    public function getLastError(): array
    {
        // TODO: Format array as a single string
        return $this->errorMsgs;
    }
}