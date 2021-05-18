<?php

class Statistics
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

    public function getAmountOfCourseDescriptions(): ?int
    {
        try {
            $sth = $this->db->prepare("SELECT COUNT(*) FROM CourseDescription");
            if ($sth->execute()) {
                return $sth->fetchColumn();
            } else {
                $this->errorMsgs = $sth->errorInfo();
                return null;
            }
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
        return null;
    }


    public function getEarliestVersionOfCourseDesc(int $id): ?string
    {
        try {
            $sth = $this->db->prepare("SELECT CourseDescription.dateCreated from CourseDescription 
                                                WHERE CourseDescription.idCourse IN 
                                                (SELECT CourseCode_has_CourseDescription.CourseDescription_idCourse 
                                                FROM CourseCode_has_CourseDescription 
                                                WHERE CourseCode_has_CourseDescription.CourseCode_idCourseCode = :courseCodeId) 
                                                ORDER BY CourseDescription.dateCreated ASC LIMIT 1");
            if ($sth->execute(array(
                'courseCodeId' => $id
            ))) {
                return $sth->fetchColumn();
            } else {
                $this->errorMsgs = $sth->errorInfo();
                return null;
            }
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
        return null;
    }

    public function getCourseDescriptionsPendingApproval(): ?int
    {
        try {
            $sth = $this->db->prepare("select count(*) from Approval where Approval.approved = 0 and (Approval.approvedCourseCoordinator = 1 OR Approval.approvedInstituteLeader = 1);");
            if ($sth->execute()) {
                return $sth->fetchColumn();
            } else {
                $this->errorMsgs = $sth->errorInfo();
                return null;
            }
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
        return null;
    }
}