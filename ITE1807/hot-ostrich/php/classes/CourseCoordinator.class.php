<?php

class CourseCoordinator{

    private int $User_idUser;
    private int $CourseDescription_idCourse;
    private string $CoursePart;

    /**
     * CourseCoordinator constructor.
     */
    public function __construct()
    {
    }

    /**
     * @return int
     */
    public function getUserIdUser(): int
    {
        return $this->User_idUser;
    }

    /**
     * @param int $User_idUser
     */
    public function setUserIdUser(int $User_idUser): void
    {
        $this->User_idUser = $User_idUser;
    }

    /**
     * @return int
     */
    public function getCourseDescriptionIdCourse(): int
    {
        return $this->CourseDescription_idCourse;
    }

    /**
     * @param int $CourseDescription_idCourse
     */
    public function setCourseDescriptionIdCourse(int $CourseDescription_idCourse): void
    {
        $this->CourseDescription_idCourse = $CourseDescription_idCourse;
    }

    /**
     * @return string
     */
    public function getCoursePart(): string
    {
        return $this->CoursePart;
    }

    /**
     * @param string $CoursePart
     */
    public function setCoursePart(string $CoursePart): void
    {
        $this->CoursePart = $CoursePart;
    }

}