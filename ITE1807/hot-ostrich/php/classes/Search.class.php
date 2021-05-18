<?php

class Search

{
    private PDO $db;
    private array $errorMsgs;

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
     * Get all avaible courses with searchs
     * Returns an array with CourseCode-objects of all available courses. Returns NULL if it fails.
     * Call getLastError to get error-message.
     * @param $search // when a user search after a courseCode or Title
     * @return array|null Array Containing CourseCode-objects or NULL
     */
    public function getAllCourseCodesWithSearch($search): ?array
    {
        $arrCourseCodes = array();
        try {
            $search_var = "%$search%";
            $stmt = $this->db->prepare("select * from CourseCode where courseCode like :search or name_nb_no like :search or name_nb_nn like :search or name_en_gb like :search");
            $stmt->bindParam(':search', $search_var, PDO::PARAM_STR); //bind variable
            if ($stmt->execute()) {
                while ($courseCode = $stmt->fetchObject("CourseCode")) {
                    $arrCourseCodes[] = $courseCode;
                }
                return $arrCourseCodes;
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

}
