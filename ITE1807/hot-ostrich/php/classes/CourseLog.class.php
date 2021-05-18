<?php

class CourseLog{

    private int $idCourseLog;
    private string $dateChanged;
    private int $User_idUser;
    private int $CourseDescription_idCourse;

    /**
     * CourseLog constructor.
     */
    public function __construct(){}

    /**
     * @return int
     */
    public function getIdCourseLog(): int {
        return $this->idCourseLog;
    }

    /**
     * @param int $idCourseLog
     */
    public function setIdCourseLog(int $idCourseLog): void {
        $this->idCourseLog = $idCourseLog;
    }

    /**
     * @return string
     */
    public function getDateChanged(): string{
        return $this->dateChanged;
    }

    /**
     * @param string $dateChanged
     */
    public function setDateChanged(string $dateChanged): void {
        $this->dateChanged = $dateChanged;
    }

    /**
     * @return int
     */
    public function getUserIdUser(): int {
        return $this->User_idUser;
    }

    /**
     * @param int $User_idUser
     */
    public function setUserIdUser(int $User_idUser): void {
        $this->User_idUser = $User_idUser;
    }

    /**
     * @return int
     */
    public function getCourseDescriptionIdCourse(): int {
        return $this->CourseDescription_idCourse;
    }

    /**
     * @param int $CourseDescription_idCourse
     */
    public function setCourseDescriptionIdCourse(int $CourseDescription_idCourse): void {
        $this->CourseDescription_idCourse = $CourseDescription_idCourse;
    }


}