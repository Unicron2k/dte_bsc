<?php


class CourseLeader {

    private int $idCourseLeader;
    private int $User_idUser;
    private int $CourseCode_idCourseCode;

    /**
     * CourseLeader constructor.
     */
    public function __construct(){}

    /**
     * @return int
     */
    public function getIdCourseLeader(): int {
        return $this->idCourseLeader;
    }

    /**
     * @param int $idCourseLeader
     */
    public function setIdCourseLeader(int $idCourseLeader): void {
        $this->idCourseLeader = $idCourseLeader;
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
    public function getCourseCodeIdCourseCode(): int {
        return $this->CourseCode_idCourseCode;
    }

    /**
     * @param int $CourseCode_idCourseCode
     */
    public function setCourseCodeIdCourseCode(int $CourseCode_idCourseCode): void {
        $this->CourseCode_idCourseCode = $CourseCode_idCourseCode;
    }




}