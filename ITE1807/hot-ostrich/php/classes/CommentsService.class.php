<?php

class CommentsService{
    private PDO $db;
    private array $errorMsgs;

    /**
     * CommentsService.class constructor.
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
     * Get comment by comment-id
     * Returns a Comments-object with all date for given comment-id. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @param int $idComment Id of comment to get
     * @return Comments|null Comments-objects or NULL
     */
    public function getComment(int $idComment) : ?Comments {
        try {
            //Return NULL if $idComment is less than 1
            if(!is_numeric($idComment) || $idComment < 1){
                $this->errorMsgs = array("\$idComment: " . $idComment . ": invalid number");
                return NULL;
            }
            $stmt = $this->db->prepare("select * from `Comments` where `Comments`.`idComment` = :idComment;");

            $stmt->bindParam(":idComment", $idComment, PDO::PARAM_INT);
            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()) {
                //Fetch Degree-objects from query-results
                $result =$stmt->fetchObject("Comments");
                if (!is_null($result) && $result!=false){
                    return $result;
                }
                $this->errorMsgs[] = "Comment does not exist or has been deleted.";
                return Null;
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
     * Get <u>all</u> available Comments-entries, ordered by date
     * Returns an array with Comments-objects of all available Comments-entries. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @return array|null Array containing all Comments-objects or NULL
     */
    public function getAllComments() : ?array {
        $arrEntries = array();
        try {
            // No userdata used, no real need to sanitize...
            $stmt = $this->db->prepare("select * from `Comments` order by  `Comments`.`date` desc;");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch Degree-objects from query-results
                while ($entry = $stmt->fetchObject("Comments")) {
                    $arrEntries[] = $entry;
                }
                return $arrEntries;
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
     * Get all available Comments-entries made by a specific user, sorted by date.
     * Returns an array with Comments-objects of all available Comments-entries for given user. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @param int $User_idUser Id of user to retrieve entries for
     * @return array|null Array Containing Comments-objects or NULL
     */
    public function getAllCommentsByUserId(int $User_idUser) : ?array {
        $arrEntries = array();
        try {
            //Return NULL if $idComment is less than 1
            if(!is_numeric($User_idUser) || $User_idUser < 1){
                $this->errorMsgs = array("\$User_idUser: " . $User_idUser . ": invalid number");
                return NULL;
            }
            $stmt = $this->db->prepare("select * from `Comments` where `Comments`.`User_idUser` = :User_idUser order by  `Comments`.`date` desc;");

            $stmt->bindParam(":User_idUser", $User_idUser, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch Degree-objects from query-results
                while ($entry = $stmt->fetchObject("Comments")) {
                    $arrEntries[] = $entry;
                }
                return $arrEntries;
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
     * Get all available Comments-entries for a given course-description, sorted by date.
     * Returns an array with Comments-objects of all available Comments-entries. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * Currently limited to 10 comments.
     * TODO: Add paginator for comments on CourseDescription frontend.
     * @param int $CourseDescription_idCourse
     * @return array|null Array containing all Comments-objects or NULL
     */
    public function getAllCommentsByCourseDescription(int $CourseDescription_idCourse) : ?array {
        $arrEntries = array();
        try {
            //Return NULL if $idComment is less than 1
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return NULL;
            }
            $stmt = $this->db->prepare("select * from `Comments` where `Comments`.`CourseDescription_idCourse` = :CourseDescription_idCourse order by  `Comments`.`date` desc LIMIT 10;");

            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch Degree-objects from query-results
                while ($entry = $stmt->fetchObject("Comments")) {
                    $arrEntries[] = $entry;
                }
                return $arrEntries;
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
     * Get all available Comments-entries made by a user for a course-description.
     * Returns an array with Comments-objects of all available Comments-entries made by a user for a course-description. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @param int $User_idUser Id of user to retrieve entries for
     * @param int $CourseDescription_idCourse Id of course-description to retrieve entries for
     * @return array|null Array containing Comments-objects or NULL
     */
    public function getAllCommentsByUserIdForCourseDescription(int $User_idUser, int $CourseDescription_idCourse) : ?array {
        $arrEntries = array();
        try {
            //Return NULL if $idComment is less than 1
            if(!is_numeric($User_idUser) || $User_idUser < 1){
                $this->errorMsgs = array("\$User_idUser: " . $User_idUser . ": invalid number");
                return NULL;
            }
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return NULL;
            }

            $stmt = $this->db->prepare("select * from `Comments` where `Comments`.`CourseDescription_idCourse` = :CourseDescription_idCourse and `Comments`.`User_idUser` = :User_idUser order by  `Comments`.`date` desc;");

            $stmt->bindParam(":User_idUser", $User_idUser, PDO::PARAM_INT);
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch Degree-objects from query-results
                while ($entry = $stmt->fetchObject("Comments")) {
                    $arrEntries[] = $entry;
                }
                return $arrEntries;
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
     * Add a comment.
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $User_idUser Is of user making the entry
     * @param int $CourseDescription_idCourse Id of course-description the comment belongs to
     * @param string $title Title of comment
     * @param string $content Content of comment
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function addComment(int $User_idUser, int $CourseDescription_idCourse, string $title, string $content) : bool {
        try{
            if(!is_numeric($User_idUser) || $User_idUser < 1){
                $this->errorMsgs = array("\$User_idUser: " . $User_idUser . ": invalid number");
                return false;
            }
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return false;
            }

            //Make sure $degree is of a valid length
            $title = substr($title, 0, 45);
            $content = substr($content, 0, 240);

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $degree already exists in the table
            $stmt = $this->db->prepare("INSERT INTO `Comments` (`idComment`, `title`, `content`, `date`, `CourseDescription_idCourse`, `User_idUser`) VALUES (DEFAULT, :title, :content, DEFAULT, :CourseDescription_idCourse, :User_idUser); commit; ");

            $stmt->bindParam(":title", $title, PDO::PARAM_STR);
            $stmt->bindParam(":content", $content, PDO::PARAM_STR);
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);
            $stmt->bindParam(":User_idUser", $User_idUser, PDO::PARAM_INT);

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
     * Delete a comment.
     * You probably shouldn't use this...
     * Returns true if successful, false if failed.
     * Call GetLastError to get error-message.
     * @param int $idComment Id of comment to delete
     * @return bool True if succeeded, false if failed. In case of false, call GetLastError to get last error-message
     */
    public function deleteEntry(int $idComment) : bool {
        try{

            //Return NULL if $idCompetenceGoals is less than 1
            if(!is_numeric($idComment) || $idComment < 1){
                $this->errorMsgs = array("\$idComment: " . $idComment . ": invalid number");
                return false;
            }

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful insert or manually check if $degree already exists in the table
            $stmt = $this->db->prepare("DELETE FROM `Comments` WHERE `Comments`.`idComment` = :idComment; commit; ");

            $stmt->bindParam(":idComment", $idComment, PDO::PARAM_INT);

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