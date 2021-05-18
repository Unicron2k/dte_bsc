<?php

class OffersOnlineStudentsService {
    private PDO $db;
    private array $errorMsgs;

    /**
     * OffersOnlineStudentsService.class.class constructor.
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
     * Get all available Offers.
     * Returns an array with OffersOnlineStudents-objects of all available offers. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @return array|null Array Containing OffersOnlineStudents-objects or NULL
     */
    public function getAllOffers() : ?array {
        $arrStudyPoints = array();
        try {
            // No userdata used, no real need to sanitize...
            $stmt = $this->db->prepare("select * from `OffersOnlineStudents` order by `OffersOnlineStudents`.`idOffersOnlineStudents`;");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch StudyPoints-objects from query-results
                while ($StudyPoints = $stmt->fetchObject("OffersOnlineStudents")) {
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
     * Get data for a specific offer given its' ID.
     * Returns a OffersOnlineStudents-object on success, NULL on failure.
     * Call GetLastError to get error-message.
     * @param int $idOffersOnlineStudents Id of offer to fetch
     * @return OffersOnlineStudents|null Object of type OffersOnlineStudents containing all the data for a given ID or NULL
     */
    public function getOffersById(int $idOffersOnlineStudents) : ?OffersOnlineStudents {
        try{
            //Return NULL if $CourseDescription_idCourse is less than 1
            if(!is_numeric($idOffersOnlineStudents) || $idOffersOnlineStudents < 1){
                $this->errorMsgs = array("\$idOffersOnlineStudents: " . $idOffersOnlineStudents . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `OffersOnlineStudents` where `OffersOnlineStudents`.`idOffersOnlineStudents` = :idOffersOnlineStudents");
            $stmt->bindParam(":idOffersOnlineStudents", $idOffersOnlineStudents, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("OffersOnlineStudents");
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
     * Get data for a specific offer given a CourseDescription-ID.
     * Returns a OffersOnlineStudents-object on success, NULL on failure.
     * Call GetLastError to get error-message.
     * @param int $CourseDescription_idCourse Id of CourseDescription to fetch
     * @return OffersOnlineStudents|null Object of type OffersOnlineStudents containing all the data for a given ID or NULL
     */
    public function getOffersByCourseDescription(int $CourseDescription_idCourse) : ?OffersOnlineStudents {
        try{
            //Return NULL if $CourseDescription_idCourse is less than 1
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$idCourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `OffersOnlineStudents` where `OffersOnlineStudents`.`CourseDescription_idCourse` = :CourseDescription_idCourse");
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("OffersOnlineStudents");
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
     * Add a OffersOnlineStudents entry.
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $CourseDescription_idCourse CourseDescription the offers applies to to insert
     * @param bool $streaming Will there be streaming of lectures?
     * @param bool $webMeetingLecture Will there be a web-meeting for the lecture?
     * @param bool $webMeetingEvening Will there be a web-meeting during the evenings?
     * @param bool $followUp Will there be follow-up for each student?
     * @param bool $organizedArrangements Will there be any organized arrangements?
     * @param string $other Other notes regarding available offers
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function addOffersForCourseDescriptions(int $CourseDescription_idCourse, bool $streaming, bool $webMeetingLecture, bool $webMeetingEvening, bool $followUp, bool $organizedArrangements, string $other) : bool {
        try{
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 0){
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return false;
            }
            if(!is_null($other)){
                $other = substr($other, 0, 1000) ;
            } else {
                $this->errorMsgs[] = "\$other cannot be empty or null!";
                return false;
            }

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $studyPoints already exists in the table
            $stmt = $this->db->prepare("INSERT INTO `OffersOnlineStudents` (`idOffersOnlineStudents`, `streaming`, `webMeetingLecture`, `webMeetingEvening`, `followUp`, `organizedArrangements`, `other`, `CourseDescription_idCourse`)
                                                  VALUES (DEFAULT, :streaming, :webMeetingLecture, :webMeetingEvening, :followUp, :organizedArrangements, :other, :CourseDescription_idCourse) ; COMMIT;");

            $stmt->bindParam(":streaming", $streaming, PDO::PARAM_BOOL);
            $stmt->bindParam(":webMeetingLecture", $webMeetingLecture, PDO::PARAM_BOOL);
            $stmt->bindParam(":webMeetingEvening", $webMeetingEvening, PDO::PARAM_BOOL);
            $stmt->bindParam(":followUp", $followUp, PDO::PARAM_BOOL);
            $stmt->bindParam(":organizedArrangements", $organizedArrangements, PDO::PARAM_BOOL);
            $stmt->bindParam(":other", $other, PDO::PARAM_STR);
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
     * Updates a OffersOnlineStudents-entry.
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idOffersOnlineStudents CourseDescription to update
     * @param bool $streaming Will there be streaming of lectures?
     * @param bool $webMeetingLecture Will there be a web-meeting for the lecture?
     * @param bool $webMeetingEvening Will there be a web-meeting during the evenings?
     * @param bool $followUp Will there be follow-up for each student?
     * @param bool $organizedArrangements Will there be any organized arrangements?
     * @param string $other Other notes regarding available offers
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function updateOffers(int $idOffersOnlineStudents, bool $streaming, bool $webMeetingLecture, bool $webMeetingEvening, bool $followUp, bool $organizedArrangements, string $other) : bool {
        try{
            // TODO-RFC: make a new entry with old values, then updating it with new, or just overwrite?
            if(!is_numeric($idOffersOnlineStudents) || $idOffersOnlineStudents < 0){
                $this->errorMsgs = array("\$idOffersOnlineStudents: " . $idOffersOnlineStudents . ": invalid number");
                return false;
            }
            if(!is_null($other) || !empty($other)){
                $other = substr($other, 0, 1000) ;
            } else {
                $this->errorMsgs[] = "\$other cannot be empty or null!";
                return false;
            }

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $courseCode already exists in the table
            $stmt = $this->db->prepare("UPDATE `OffersOnlineStudents`
                                                  SET `streaming` = :streaming,
                                                  `webMeetingLecture` = :webMeetingLecture,
                                                  `webMeetingEvening` = :webMeetingEvening,
                                                  `followUp` = :followUp,
                                                  `organizedArrangements` = :organizedArrangements,
                                                  `other` = :other
                                                  WHERE `OffersOnlineStudents`.`idOffersOnlineStudents` = :idOffersOnlineStudents; COMMIT;");

            $stmt->bindParam(":streaming", $streaming, PDO::PARAM_BOOL);
            $stmt->bindParam(":webMeetingLecture", $webMeetingLecture, PDO::PARAM_BOOL);
            $stmt->bindParam(":webMeetingEvening", $webMeetingEvening, PDO::PARAM_BOOL);
            $stmt->bindParam(":followUp", $followUp, PDO::PARAM_BOOL);
            $stmt->bindParam(":organizedArrangements", $organizedArrangements, PDO::PARAM_BOOL);
            $stmt->bindParam(":other", $other, PDO::PARAM_STR);
            $stmt->bindParam(":idOffersOnlineStudents", $idOffersOnlineStudents, PDO::PARAM_INT);

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
     * Updates an OffersOnlineStudents-entry by object
     * @param OffersOnlineStudents $offers OffersOnlineStudents-object containing updated Offers-data
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function updateOffersByObject(OffersOnlineStudents $offers) : bool {

        if(is_null($offers)){
            $this->errorMsgs = array("\$approval was NULL");
            return false;
        }

        return $this::updateOffers(
            $offers->getIdOffersOnlineStudents(),
            $offers->getStreaming(),
            $offers->getWebMeetingLecture(),
            $offers->getWebMeetingEvening(),
            $offers->getFollowUp(),
            $offers->getOrganizedArrangements(),
            $offers->getOther());

    }

    /**
     * Delete a StudyPoint-entry. You probably shouldn't use this...
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idOffersOnlineStudents ID of studypoints to delete
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function deleteOffersOnlineStudentsEntry(int $idOffersOnlineStudents) : bool {
        try{
            if(!is_numeric($idOffersOnlineStudents) || $idOffersOnlineStudents < 1){
                $this->errorMsgs = array("\$idOffersOnlineStudents: " . $idOffersOnlineStudents . ": invalid number");
                return false;
            }

            //Prepare query and bind parameters
            // TODO: Check if there are eny studypoints to delete before trying to delete, as an "empty" delete will still succeed
            $stmt = $this->db->prepare("DELETE FROM `OffersOnlineStudents` WHERE `OffersOnlineStudents`.`idOffersOnlineStudents` = :idOffersOnlineStudents; commit;");
            $stmt->bindParam(":idOffersOnlineStudents", $idOffersOnlineStudents, PDO::PARAM_INT);

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