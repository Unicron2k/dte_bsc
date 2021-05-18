<?php


class CourseCode
{
    private int $idCourseCode;
    private string $courseCode;
    private string $name_nb_no;
    private string $name_nb_nn;
    private string $name_en_gb;
    private int $Degree_idDegree;
    private int $StudyPoints_idStudyPoints;

    /**
     * CourseCode constructor.
     */
    public function __construct(){}

    /**
     * @return int
     */
    public function getIdCourseCode(): int{
        return $this->idCourseCode;
    }

    /**
     * @param int $idCourseCode
     */
    public function setIdCourseCode(int $idCourseCode): void{
        $this->idCourseCode = $idCourseCode;
    }

    /**
     * @return string
     */
    public function getCourseCode(): string{
        return $this->courseCode;
    }

    /**
     * @param string $courseCode
     */
    public function setCourseCode(string $courseCode): void{
        $this->courseCode = $courseCode;
    }

    /**
     * @return string
     */
    public function getNameNbNo(): string{
        return $this->name_nb_no;
    }

    /**
     * @param string $name_nb_no
     */
    public function setNameNbNo(string $name_nb_no): void{
        $this->name_nb_no = $name_nb_no;
    }

    /**
     * @return string
     */
    public function getNameNbNn(): string{
        return $this->name_nb_nn;
    }

    /**
     * @param string $name_nb_nn
     */
    public function setNameNbNn(string $name_nb_nn): void{
        $this->name_nb_nn = $name_nb_nn;
    }

    /**
     * @return string
     */
    public function getNameEnGb(): string{
        return $this->name_en_gb;
    }

    /**
     * @param string $name_en_gb
     */
    public function setNameEnGb(string $name_en_gb): void{
        $this->name_en_gb = $name_en_gb;
    }

    /**
     * @return int
     */
    public function getDegreeIdDegree(): int{
        return $this->Degree_idDegree;
    }

    /**
     * @param int $Degree_idDegree
     */
    public function setDegreeIdDegree(int $Degree_idDegree): void{
        $this->Degree_idDegree = $Degree_idDegree;
    }

    /**
     * @return int
     */
    public function getStudyPointsIdStudyPoints(): int{
        return $this->StudyPoints_idStudyPoints;
    }

    /**
     * @param int $StudyPoints_idStudyPoints
     */
    public function setStudyPointsIdStudyPoints(int $StudyPoints_idStudyPoints): void{
        $this->StudyPoints_idStudyPoints = $StudyPoints_idStudyPoints;
    }

    public function __toString()
    {
     return $this->getCourseCode();
    }
}