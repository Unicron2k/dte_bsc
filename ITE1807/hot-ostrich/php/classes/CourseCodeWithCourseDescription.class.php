<?php

class CourseCodeWithCourseDescription
{
    private int $idCourseCode;
    private string $CourseCode;
    private int $idCourse;
    private int $year;
    private int $semesterFall;
    private int $semesterSpring;

    /**
     * CourseCode constructor.
     */
    public function __construct()
    {
    }

    public function getIdCourseCode(): int
    {
        return $this->idCourseCode;
    }

    public function setIdCourseCode(int $idCourseCode): void
    {
        $this->idCourseCode = $idCourseCode;
    }

    public function getCourseCode(): string
    {
        return $this->CourseCode;
    }

    public function setCourseCode(string $CourseCode): void
    {
        $this->CourseCode = $CourseCode;
    }

    public function getIdCourse(): int
    {
        return $this->idCourse;
    }

    public function setIdCourse(int $idCourse): void
    {
        $this->idCourse = $idCourse;
    }

    public function getYear(): int
    {
        return $this->year;
    }

    public function setYear(int $year): void
    {
        $this->year = $year;
    }

    public function getSemesterFall(): int
    {
        return $this->semesterFall;
    }

    public function setSemesterFall(int $semesterFall): void
    {
        $this->semesterFall = $semesterFall;
    }

    public function getSemesterSpring(): int
    {
        return $this->semesterSpring;
    }

    public function setSemesterSpring(int $semesterSpring): void
    {
        $this->semesterSpring = $semesterSpring;
    }

    public function __toString()
    {
        $semester = "";
        if ($this->getSemesterFall() == 1 && $this->getSemesterSpring() == 1) {
            $semester = "Høst og vår";
        } else if ($this->getSemesterFall() == 1) {
            $semester = "Høst";
        } else if ($this->getSemesterSpring() == 1) {
            $semester = "Vår";
        } else {
            $semester = "Ingen.";
        }
        return $this->getCourseCode() . " (" . $this->getYear() . " - " . $semester . ")";
    }
}