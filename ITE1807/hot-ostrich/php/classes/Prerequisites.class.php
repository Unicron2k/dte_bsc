<?php

class Prerequisites{

    // TODO: Can Boolean replace int, due to database being tinyint?
    private int $idPrerequisites;
    private int $required;
    private int $CourseDescription_idCourse;
    private int $CourseCode_idCourseCode;

    /**
     * Prerequisites constructor.
     */
    public function __construct(){}

    /**
     * @return int
     */
    public function getIdPrerequisites(): int {
        return $this->idPrerequisites;
    }

    /**
     * @param int $idPrerequisites
     */
    public function setIdPrerequisites(int $idPrerequisites): void {
        $this->idPrerequisites = $idPrerequisites;
    }

    /**
     * @return int
     */
    public function getRequired(): int {
        return $this->required;
    }

    /**
     * @param int $required
     */
    public function setRequired(int $required): void {
        $this->required = $required;
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