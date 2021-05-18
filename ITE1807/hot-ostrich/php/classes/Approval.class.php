<?php

class Approval {

    private int $idApproval;
    private string $approvalDeadline;
    private int $approved;
    private ?string $approvedDate;
    private int $approvedCourseCoordinator;
    private ?string $approvedDateCourseCoordinator;
    private int $approvedInstituteLeader;
    private ?string $approvedDateInstituteLeader;
    private int $CourseDescription_idCourse;

    /**
     * Approval constructor.
     */
    public function __construct(){}

    /**
     * @return int
     */
    public function getIdApproval(): int{
        return $this->idApproval;
    }

    /**
     * @param int $idApproval
     */
    public function setIdApproval(int $idApproval): void{
        $this->idApproval = $idApproval;
    }

    /**
     * @return string
     */
    public function getApprovalDeadline(): string{
        return $this->approvalDeadline;
    }

    /**
     * @param string $approvalDeadline
     */
    public function setApprovalDeadline(string $approvalDeadline): void{
        $this->approvalDeadline = $approvalDeadline;
    }

    /**
     * @return int
     */
    public function getApproved(): int{
        return $this->approved;
    }

    /**
     * @param int $approved
     */
    public function setApproved(int $approved): void{
        $this->approved = $approved;
    }

    /**
     * @return string|null
     */
    public function getApprovedDate(): ?string{
        return $this->approvedDate;
    }

    /**
     * @param string $approvedDate
     */
    public function setApprovedDate(string $approvedDate): void{
        $this->approvedDate = $approvedDate;
    }

    /**
     * @return int
     */
    public function getApprovedCourseCoordinator(): int{
        return $this->approvedCourseCoordinator;
    }

    /**
     * @param int $approvedCourseCoordinator
     */
    public function setApprovedCourseCoordinator(int $approvedCourseCoordinator): void{
        $this->approvedCourseCoordinator = $approvedCourseCoordinator;
    }

    /**
     * @return string|null
     */
    public function getApprovedDateCourseCoordinator(): ?string{
        return $this->approvedDateCourseCoordinator;
    }

    /**
     * @param string $approvedDateCourseCoordinator
     */
    public function setApprovedDateCourseCoordinator(string $approvedDateCourseCoordinator): void{
        $this->approvedDateCourseCoordinator = $approvedDateCourseCoordinator;
    }

    /**
     * @return int
     */
    public function getApprovedInstituteLeader(): int{
        return $this->approvedInstituteLeader;
    }

    /**
     * @param int $approvedInstituteLeader
     */
    public function setApprovedInstituteLeader(int $approvedInstituteLeader): void{
        $this->approvedInstituteLeader = $approvedInstituteLeader;
    }

    /**
     * @return string|null
     */
    public function getApprovedDateInstituteLeader(): ?string{
        return $this->approvedDateInstituteLeader;
    }

    /**
     * @param string $approvedDateInstituteLeader
     */
    public function setApprovedDateInstituteLeader(string $approvedDateInstituteLeader): void{
        $this->approvedDateInstituteLeader = $approvedDateInstituteLeader;
    }

    /**
     * @return int
     */
    public function getCourseDescriptionIdCourse(): int{
        return $this->CourseDescription_idCourse;
    }

    /**
     * @param int $CourseDescription_idCourse
     */
    public function setCourseDescriptionIdCourse(int $CourseDescription_idCourse): void{
        $this->CourseDescription_idCourse = $CourseDescription_idCourse;
    }


}