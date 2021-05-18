<?php

class LanguageService{
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
     * Get all available language-entries
     * Returns an array with language-objects of all available language-entries. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @return array|null Array Containing Language-objects or NULL
     */
    public function getAllLanguages() : ?array {
        $arrLanguages = array();
        try{
            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `Language`;");

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                //Fetch StudyPoints-objects from query-results
                while ($Language = $stmt->fetchObject("Language")) {
                    $arrLanguages[] = $Language;
                }
                return $arrLanguages;
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
     * Get a language entry by given ID
     * Returns a language-object for the given language-id. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @param int $idLanguage
     * @return Language|null Language-object for given ID or NULL
     */
    public function getLanguageByID(int $idLanguage) : ?Language {
        try{
            if(!is_numeric($idLanguage) || $idLanguage < 1){
                $this->errorMsgs = array("\$idLanguage: " . $idLanguage . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `Language` where `Language`.`idLanguage` = :idLanguage");
            $stmt->bindParam(":idLanguage", $idLanguage, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("Language");
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
     * Get a language entry by given CourseDescription-ID
     * Returns a language-object for the given CourseDescription-id. Returns NULL if it fails.
     * Call GetLastError to get error-message.
     * @param int $CourseDescription_idCourse
     * @return Language|null Language-object for given CourseDescription-ID or NUL
     */
    public function getLanguageByCourseDescription(int $CourseDescription_idCourse) : ?Language {
        try{
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return NULL;
            }

            //Prepare query and bind parameters
            $stmt = $this->db->prepare("select * from `Language` where `Language`.`idLanguage` = (select Language_idLanguage from `CourseDescription` where `CourseDescription`.`idCourse` = :CourseDescription_idCourse);");
            $stmt->bindParam(":CourseDescription_idCourse", $CourseDescription_idCourse, PDO::PARAM_INT);

            //Execute query, and set return-status + any potential error-message
            if($stmt->execute()){
                $result =  $stmt->fetchObject("Language");
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
     * Add a language entry by given ID
     * Returns true if it succeeds, false if it fails
     * Call GetLastError to get error-message.
     * @param string $language Language-string to insert
     * @return bool True if succeeded, false if failed
     */
    public function addLanguage(string $language) : bool {
        try{
            if(!is_null($language) || !empty($language)){
                $language = substr($language, 0, 45) ;
            } else {
                $this->errorMsgs[] = "\$language cannot be empty or null!";
                return false;
            }

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful update or manually check if $courseCode already exists in the table
            $stmt = $this->db->prepare("INSERT INTO `Language` (`idLanguage`, `language`) VALUES (DEFAULT, :language) ; COMMIT;");

            $stmt->bindParam(":language", $language, PDO::PARAM_STR);

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
     * Delete a language entry by given Language-ID
     * You probably shouldn't use this...
     * Returns true if it succeeds, false if it fails
     * Call GetLastError to get error-message.
     * @param int $idLanguage ID of language to delete
     * @return bool True if succeeded, false if failed
     */
    public function deleteLanguageById(int $idLanguage) : bool {
        try{
            if(!is_numeric($idLanguage) || $idLanguage < 1){
                $this->errorMsgs = array("\$idLanguage: " . $idLanguage . ": invalid number");
                return false;
            }

            //Prepare query and bind parameters
            // TODO: Check if there are eny studypoints to delete before trying to delete, as an "empty" delete will still succeed
            $stmt = $this->db->prepare("DELETE FROM `Language` WHERE `Language`.`idLanguage` = :idLanguage; commit;");
            $stmt->bindParam(":idLanguage", $idLanguage, PDO::PARAM_INT);

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
     * Delete a language entry by given CourseDescription-ID
     * You probably shouldn't use this...
     * Returns true if it succeeds, false if it fails
     * Call GetLastError to get error-message.
     * @param int $CourseDescription_idCourse CourseDescription-ID of language to delete
     * @return bool True if succeeded, false if failed
     */
    public function deleteLanguageByCourseCode(int $CourseDescription_idCourse) : bool {
        try{
            if(!is_numeric($CourseDescription_idCourse) || $CourseDescription_idCourse < 1){
                $this->errorMsgs = array("\$CourseDescription_idCourse: " . $CourseDescription_idCourse . ": invalid number");
                return false;
            }

            //Prepare query and bind parameters
            // TODO: Check if there are eny studypoints to delete before trying to delete, as an "empty" delete will still succeed
            $stmt = $this->db->prepare("DELETE FROM `Language` WHERE `Language`.`idLanguage` = (select Language_idLanguage from `CourseDescription` where `CourseDescription`.`idCourse` = :CourseDescription_idCourse); commit;");
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
     * Update a language entry by given ID
     * Returns true if it succeeds, false if it fails
     * Call GetLastError to get error-message.
     * @param int $idLanguage ID of language to update
     * @param string $language Language-string to update
     * @return bool True if succeeded, false if failed
     */
    public function updateLanguage(int $idLanguage, string $language) : bool {
        try{
            if(!is_numeric($idLanguage) || $idLanguage < 1){
                $this->errorMsgs = array("\$idLanguage: " . $idLanguage . ": invalid number");
                return false;
            }

            if(!is_null($language) || !empty($language)){
                $language = substr($language, 0, 45) ;
            } else {
                $this->errorMsgs[] = "\$language cannot be empty or null!";
                return false;
            }

            // TODO: Fix this query to prevent auto-increment of PK on unsuccessful update or manually check if $courseCode already exists in the table
            $stmt = $this->db->prepare("UPDATE `Language`
                                                  SET `language` = :language
                                                  WHERE `Language`.`idLanguage` = :idLanguage; COMMIT;");

            $stmt->bindParam(":language", $language, PDO::PARAM_STR);
            $stmt->bindParam(":idLanguage", $idLanguage, PDO::PARAM_INT);

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